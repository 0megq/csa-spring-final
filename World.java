public class World {
	private static final double HEIGHT_LIMIT = Game.HEIGHT * 40;
	private static final AABB BOUNDS = new AABB(0, -HEIGHT_LIMIT, Game.WIDTH, Game.HEIGHT + HEIGHT_LIMIT);
	private static final int MAX_SWEEPS_PER_FRAME = 10;
	private static final double MAX_GROUNDED_VELOCITY = 6.0; // the y velocity at which the ball is considered to be grounded
	private static final int BALL_SIZE = 5;
	private static final double BALL_BOUNCE_FACTOR = 0.4;
	private static final double BALL_GRAVITY = 700.0;
	private static final double BALL_FRICTION = 300.0;
	private static final double BALL_LAUNCH_MULTIPLIER = 4.0; // Multiplied by distance between ball and mouse to get magnitude of launch velocity
	public final AABB[] TERRAIN;
	public final AABB HOLE;
	public final int PAR;
	private AABB ballAABB;
	private Vector2 ballLaunchPos; // Position of ball when it is launched. We use this in case the physics simulation fails and we need to reset.
	private Vector2 ballVelocity;
	private boolean leftMousePressed;
	private boolean leftMouseJustPressed;
	private boolean leftMouseJustReleased;
	private boolean rightMousePressed;
	private boolean rightMouseJustPressed;
	private boolean rightMouseJustReleased;
	private Vector2 mousePos;
	private boolean waitingForInput;
	private Vector2 aimStartPos;
	private boolean aiming; // When mouse is held down and mouse is moving around. If mouse goes off screen this turns to false
	private int strokes; // Amount of turns the player has taken. Doesn't count failed ball simulations

	// Debug stuff
	// private Vector2 lastLaunchVelocity;

	public World(Level level) {
		// Load level data into instance fields
		ballAABB = new AABB(level.BALL_START, Vector2.ONE.multiply(BALL_SIZE));
		TERRAIN = level.TERRAIN;
		HOLE = level.HOLE;
		PAR = level.PAR;
		// Other instance fields
		strokes = 0;
		ballVelocity = new Vector2();
		ballLaunchPos = ballAABB.getPos().duplicate();
		// Input
		aimStartPos = new Vector2();
		aiming = false;
		leftMousePressed = false;
		leftMouseJustPressed = false;
		leftMouseJustReleased = false;
		rightMousePressed = false;
		rightMouseJustPressed = false;
		rightMouseJustReleased = false;
		waitingForInput = false;
		mousePos = new Vector2();

		// Debug
		// lastLaunchVelocity = new Vector2();
	}

	public void updateInput(Vector2 mousePos, boolean lmbPressed, boolean lmbJustPressed, boolean lmbJustReleased,
			boolean rmbPressed, boolean rmbJustPressed, boolean rmbJustReleased) {
		this.mousePos = mousePos;
		this.leftMousePressed = lmbPressed;
		this.leftMouseJustPressed = lmbJustPressed;
		this.leftMouseJustReleased = lmbJustReleased;
		this.rightMousePressed = rmbPressed;
		this.rightMouseJustPressed = rmbJustPressed;
		this.rightMouseJustReleased = rmbJustReleased;
	}

	public void update(final double DELTA) { // we don't want to accidentally change DELTA so it's final
		if (!waitingForInput) {
			Vector2 groundAabbSize = ballAABB.getSize().add(0, MAX_GROUNDED_VELOCITY * DELTA);
			AABB groundAabb = new AABB();
			groundAabb.setSize(groundAabbSize);
			groundAabb.setPos(ballAABB.getPos());
			AABB groundCollider = groundAabb.isColliding(TERRAIN);
			// If ball is near the ground and y velocity is low enough
			if (groundCollider != null && Math.abs(ballVelocity.getY()) <= MAX_GROUNDED_VELOCITY) {
				ballVelocity.setY(0);
				// This is the new x speed (direction not included) of the ball after friction
				double newXSpeed = Math.abs(ballVelocity.getX()) - BALL_FRICTION * DELTA;
				if (newXSpeed < 0) // If speed is negative then the friction was greater than the velocity, so we snap it to 0
					ballVelocity.setX(0);
				else // Otherwise apply friction normally
					ballVelocity.setX(
							Math.signum(ballVelocity.getX()) * newXSpeed);
				// Snap ball y position to floor
				ballAABB.getPos().setY(groundCollider.getPos().getY() - ballAABB.getSize().getY());

				if (ballVelocity.equals(Vector2.ZERO)) { // stop moving ball if velocity = 0 and on ground
					waitingForInput = true;
				}
			} else {
				ballVelocity.setY(ballVelocity.getY() + BALL_GRAVITY * DELTA); // apply gravity
			}

			// If failed to simulate ball
			if (!simulateBall(DELTA)) {
				System.out.println("Ball simulation failed. Resetting to launch position");
				ballAABB.setPos(ballLaunchPos);
				ballVelocity.copy(Vector2.ZERO);
				strokes--; // Remove a stroke if ball simulation failed
			}

			// If ball goes beyond bounds then reset ball and don't remove a stroke
			if (!ballAABB.isColliding(BOUNDS)) {
				ballAABB.setPos(ballLaunchPos);
				ballVelocity.copy(Vector2.ZERO);
			}
		} else if (aiming) {
			// If mouse released set aiming to false and waiting for input to true and launch ball.
			if (leftMouseJustReleased) {
				aiming = false;
				waitingForInput = false;
				Vector2 mouseToStart = aimStartPos.subtract(mousePos);
				Vector2 newBallVel = mouseToStart.normalize()
						.multiply(mouseToStart.getLength() * BALL_LAUNCH_MULTIPLIER);
				ballVelocity.copy(newBallVel);
				ballLaunchPos.copy(ballAABB.getPos());
				strokes++;
				// System.out.println("Stroke " + strokes + " taken");

				// lastLaunchVelocity.copy(ballVelocity);
			}
			if (mousePos.getX() < -1 || mousePos.getY() < -1 || mousePos.getX() > Game.WIDTH + 1
					|| mousePos.getY() > Game.HEIGHT + 1 || rightMouseJustPressed) {
				aiming = false;
			}

		} else if (leftMouseJustPressed) {
			aimStartPos.copy(mousePos);
			aiming = true;
		}
		// else if (rightMouseJustPressed) {
		// 	waitingForInput = false;
		// 	ballVelocity.copy(lastLaunchVelocity);
		// 	System.out.println("Launched ball with previous velocity");
		// }
	}

	// get collision
	// if collision null
	// integrate ball remaining delta
	// return
	// integrate ball by col delta
	// remove col delta from delta
	// go back to get collision

	// The boolean value indicates if the simulation was sucessful or not
	private boolean simulateBall(double delta) {
		for (int sweeps = 0; delta > 0 && sweeps < MAX_SWEEPS_PER_FRAME; sweeps++) {
			// Sweep AABB collision will return delta of the collision value
			AABB.Collision collision = getSweepingBallCollision(delta);
			if (collision == null) {
				// If no collision integrate ball and end
				integrateBallPos(delta);
				return true;
			}
			integrateBallPos(collision.DELTA); // move by delta
			delta -= collision.DELTA;
			// Flip ball by normal if collision
			if (collision.NORMAL.getX() != 0) {
				ballVelocity.setX(-ballVelocity.getX() * BALL_BOUNCE_FACTOR);
			} else if (collision.NORMAL.getY() != 0) {
				ballVelocity.setY(-ballVelocity.getY() * BALL_BOUNCE_FACTOR);
			}
		}
		return false;
	}

	// Moves the ball by its velocity for delta seconds
	private void integrateBallPos(double delta) {
		ballAABB.setPos(ballAABB.getPos().add(ballVelocity.multiply(delta)));
	}

	public boolean isBallInHole() {
		return ballAABB.isColliding(HOLE);
	}

	private AABB.Collision getSweepingBallCollision(double delta) {
		// if ((ballVelocity.getY() * delta + ballAABB.getPos().getY()) > 275) {
		// 	System.out.println("Going into ground with Velocity * Delta: " + delta * ballVelocity.getY() + " Pos: "
		// 			+ ballAABB.getPos());
		// }
		AABB.Collision collision = null;
		for (AABB terrainAABB : TERRAIN) {
			AABB.Collision currentCollision = ballAABB.sweepAABB(terrainAABB, ballVelocity);
			// Find the collision with the smallest delta
			if (currentCollision != null && currentCollision.DELTA <= delta && currentCollision.DELTA >= 0) {
				if (collision == null || collision.DELTA > currentCollision.DELTA) {
					collision = currentCollision;
					// System.out.println("Collision detected");
				}
			}
		}
		return collision;
	}

	public int getStrokes() {
		return strokes;
	}

	public Vector2 getAimStartPos() {
		return aimStartPos;
	}

	public AABB getBallAABB() {
		return ballAABB;
	}

	public boolean getWaitingForInput() {
		return waitingForInput;
	}

	public boolean isAiming() {
		return aiming;
	}

	public void cancelAiming() {
		aiming = false;
		waitingForInput = true;
	}

	public Vector2 getMousePos() {
		return mousePos;
	}
}