import java.awt.event.MouseEvent;
import java.util.*;

public class World {
	private static int MAX_SWEEPS_PER_FRAME = 10;
	private double maxGroundedYVelocity; // the y velocity at which the ball is considered to be grounded
	private AABB ballAABB;
	private Vector2 ballLaunchPos; // Position of ball when it is launched. We use this in case the physics simulation fails and we need to reset.
	private Vector2 ballVelocity;
	private double ballBounceFactor;
	private double ballGravity;
	private double ballFriction;
	private double ballLaunchMultiplier; // Multiplied by distance between ball and mouse to get magnitude of launch velocity
	private ArrayList<AABB> terrain;
	private AABB hole;
	private boolean isMouseDown;
	private boolean mouseJustPressed;
	private boolean mouseJustReleased;
	private Vector2 mousePos;
	private boolean waitingForInput;
	private boolean aiming; // When mouse is held down and mouse is moving around. If mouse goes off screen this turns to false
	private int strokes; // Amount of turns the player has taken. Doesn't count failed ball simulations

	public World() {
		maxGroundedYVelocity = 4;
		ballVelocity = new Vector2(30.0, 15.0);
		ballAABB = new AABB(150, 195, 5, 5);
		ballLaunchPos = ballAABB.getPos().duplicate();
		ballBounceFactor = 0.4;
		ballGravity = 300;
		ballFriction = 100;
		terrain = new ArrayList<AABB>();
		hole = new AABB();
		isMouseDown = false;
		mouseJustPressed = false;
		mouseJustReleased = false;
		mousePos = new Vector2();
		waitingForInput = true;
		ballLaunchMultiplier = 4.0;
		strokes = 0;

		terrain.add(new AABB(0, 200, 174, 20));
		terrain.add(new AABB(0, 100, 176, 20));
		terrain.add(new AABB(181, 200, 200, 20));
		terrain.add(new AABB(0, 100, 100, 300));
		terrain.add(new AABB(200, 100, 100, 300));
	}

	public void update(final double DELTA) { // we don't want to accidentally change DELTA so it's final

		if (!waitingForInput) {
			Vector2 groundAabbSize = ballAABB.getSize().add(0, maxGroundedYVelocity * DELTA);
			AABB groundAabb = new AABB();
			groundAabb.setSize(groundAabbSize);
			groundAabb.setPos(ballAABB.getPos());
			AABB groundCollider = groundAabb.isColliding(terrain);
			// If ball is near the ground and y velocity is low enough
			if (groundCollider != null && ballVelocity.getY() <= maxGroundedYVelocity) {
				ballVelocity.setY(0);
				// This is the new x speed (direction not included) of the ball after friction
				double newXSpeed = Math.abs(ballVelocity.getX()) - ballFriction * DELTA;
				if (newXSpeed < 0) // If speed is negative then the friction was greater than the velocity, so we snap it to 0
					ballVelocity.setX(0);
				else // Otherwise apply friction normally
					ballVelocity.setX(
							Math.signum(ballVelocity.getX()) * newXSpeed);
				// Snap ball y position to floor
				ballAABB.getPos().setY(groundCollider.getPos().getY() - ballAABB.getSize().getY());
			} else {
				ballVelocity.setY(ballVelocity.getY() + ballGravity * DELTA); // apply gravity
			}

			// If failed to simulate ball
			if (!simulateBall(DELTA)) {
				System.out.println("Ball simulation failed. Resetting to launch position");
				ballAABB.setPos(ballLaunchPos);
				ballVelocity.copy(Vector2.ZERO);
				strokes--; // Remove a stroke if ball simulation failed
			}

			if (ballVelocity.equals(Vector2.ZERO)) { // stop moving ball if velocity = 0
				waitingForInput = true;
			}
		} else if (aiming) {
			// If mouse released set aiming to false and waiting for input to true and launch ball.
			if (mouseJustReleased) {
				aiming = false;
				waitingForInput = false;
				Vector2 mouseToBall = ballAABB.getCenter().subtract(mousePos);
				Vector2 newBallVel = mouseToBall.normalize().multiply(mouseToBall.getLength() * ballLaunchMultiplier);
				strokes++;
				ballVelocity.copy(newBallVel);
				ballLaunchPos.copy(ballAABB.getPos());
				ballAABB.getPos().setY(ballAABB.getPos().getY() - maxGroundedYVelocity * DELTA); // Bug fix for ball glitching through floor
			}
			if (mousePos.getX() < -1 || mousePos.getY() < -1 || mousePos.getX() > Game.WIDTH + 1
					|| mousePos.getY() > Game.HEIGHT + 1) {
				aiming = false;
			}

		} else if (mouseJustPressed) {
			aiming = true;
		}

		// These should be at end of the update function
		mouseJustPressed = false;
		mouseJustReleased = false;
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
				ballVelocity.setX(-ballVelocity.getX() * ballBounceFactor);
			} else if (collision.NORMAL.getY() != 0) {
				ballVelocity.setY(-ballVelocity.getY() * ballBounceFactor);
			}
		}
		return false;
	}

	// Moves the ball by its velocity for delta seconds
	private void integrateBallPos(double delta) {
		ballAABB.setPos(ballAABB.getPos().add(ballVelocity.multiply(delta)));
	}

	private boolean isBallInHole() {
		return ballAABB.isColliding(hole);
	}

	private AABB.Collision getSweepingBallCollision(double delta) {
		AABB.Collision collision = null;
		for (AABB terrainAABB : terrain) {
			AABB.Collision currentCollision = ballAABB.sweepAABB(terrainAABB, ballVelocity);
			// Find the collision with the smallest delta
			if (currentCollision != null && currentCollision.DELTA <= delta && currentCollision.DELTA > 0) {
				if (collision == null || collision.DELTA > currentCollision.DELTA) {
					collision = currentCollision;
				}
			}
		}
		return collision;
	}

	public void mousePressed(MouseEvent e) {
		mouseJustPressed = true;
		isMouseDown = true;
	}

	public void mouseReleased(MouseEvent e) {
		mouseJustReleased = true;
		isMouseDown = false;
	}

	public void setMousePosition(Vector2 pos) {
		mousePos.copy(pos);
	}

	public void setBallAABB(AABB aabb) {
		this.ballAABB = aabb;
	}

	public void setTerrain(ArrayList<AABB> terrain) {
		this.terrain = terrain;
	}

	public void setHole(AABB hole) {
		this.hole = hole;
	}

	public void addTerrain(AABB box) {
		terrain.add(box);
	}

	public AABB getBallAABB() {
		return ballAABB;
	}

	public boolean isAiming() {
		return aiming;
	}

	public Vector2 getMousePos() {
		return mousePos;
	}

	public ArrayList<AABB> getTerrain() {
		return terrain;
	}

	public AABB getHole() {
		return hole;
	}
}