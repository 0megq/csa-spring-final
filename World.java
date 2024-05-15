import java.awt.event.MouseEvent;
import java.util.*;

import javax.sound.midi.Soundbank;

public class World {
	private static int MAX_SWEEPS_PER_FRAME = 10;
	private AABB ballAABB;
	private Vector2 ballLaunchPos; // Position of ball when it is launched. We use this in case the physics simulation fails and we need to reset.
	private Vector2 ballVelocity;
	private double ballBounceFactor; // TODO: Implement collisions and bouncing
	private double ballGravity;
	private double ballFriction; // TODO: Implement friction when ball is on ground. Also figure out how to do ground check
	private double ballLaunchMultiplier; // Multiplied by distance between ball and mouse to get magnitude of launch velocity
	private ArrayList<AABB> terrain;
	private AABB hole;
	private boolean isMouseDown; // TODO: Use this variable
	private boolean mouseJustPressed;
	private boolean mouseJustReleased;
	private Vector2 mousePos;
	private boolean waitingForInput;
	private boolean aiming; // When mouse is held down and mouse is moving around. If mouse goes off screen this turns to false
	private int turns; // Amount of turns the player has taken. This should be set when the ball stops.

	public World() {
		ballVelocity = new Vector2(30.0, 15.0);
		ballAABB = new AABB(150, 150, 5, 5);
		ballLaunchPos = ballAABB.getPos().duplicate();
		ballBounceFactor = 0.5;
		ballGravity = 10;
		terrain = new ArrayList<>();
		hole = new AABB();
		isMouseDown = false;
		mouseJustPressed = false;
		mouseJustReleased = false;
		mousePos = new Vector2();
		waitingForInput = true;
		ballLaunchMultiplier = 3;
		turns = 0;

		// terrain.add(new AABB(0, 380, 600, 20)); // floor
		terrain.add(new AABB(100, 300, 160, 10)); // wall
	}

	public void update(final double DELTA) { // we don't want to accidentally change DELTA so it's final

		if (!waitingForInput) { // replace with while to check for collision later
			// if (false) { // If ground collider colliding and vel.y < 1
			// 	ballVelocity.setY(0);
			// 	// Sign of vel x * (abs (vel x) - friction)
			// 	ballVelocity.setX(Math.signum(ballVelocity.getX()) * (Math.abs(ballVelocity.getX()) - ballFriction * DELTA));
			// 	simulateBall(DELTA);
			// } else {
			// 	ballVelocity.setY(ballVelocity.getY() + ballGravity * DELTA);
			// }
			simulateBall(DELTA); // if not successful then reset ball to launch position

			// ballVelocity.setY(ballVelocity.getY() + ballGravity); // apply gravity
			// // Move ball by increments and check for collisions
			// for (int i = 0; i < MAX_MOVE_ITERATIONS; i++) {
			// 	integrateBallPos(DELTA / MAX_MOVE_ITERATIONS); // Go 1/10th and then check collision if collision then
			// 	AABB collider = getBallCollidingAABB();
			// 	if (collider != null) {
			// 		integrateBallPos(-DELTA / MAX_MOVE_ITERATIONS);
			// 		i--;
			// 		if (ballAABB.getCollisionDirection(collider).getX() != 0) {
			// 			ballVelocity.setX(-ballVelocity.getX() * ballBounceFactor);
			// 			System.out.println("x flipped");
			// 		} else {
			// 			ballVelocity.setY(-ballVelocity.getY() * ballBounceFactor);
			// 			System.out.println("y flipped");
			// 		}
			// 	}
			// }

			if (ballVelocity.equals(Vector2.ZERO)) { // stop moving ball if velocity = 0
				waitingForInput = true;
			}
		} else if (aiming) {
			// If mouse released set aiming to false and waiting for input to true and set ball velocity.
			if (mouseJustReleased) {
				aiming = false;
				waitingForInput = false;
				Vector2 mouseToBall = ballAABB.getCenter().subtract(mousePos);
				Vector2 newBallVel = mouseToBall.normalize().multiply(mouseToBall.getLength() * ballLaunchMultiplier);
				ballVelocity.copy(newBallVel);
				ballLaunchPos.copy(ballAABB.getPos());
			}
			if (mousePos.getX() < -1 || mousePos.getY() < -1 || mousePos.getX() > Game.WIDTH + 1
					|| mousePos.getY() > Game.HEIGHT + 1) {
				aiming = false;
			}
		
			// System.out.println("aiming");

		} else if (mouseJustPressed) {
			aiming = true;
		}

		// These should be at end of the update function
		mouseJustPressed = false;
		mouseJustReleased = false;
	}

	// The boolean value indicates if the simulation was sucessful or not
	private boolean simulateBall(double delta) {
		// Sweep AABB collision will return delta of the collision value
		AABB.Collision collision = getSweepingBallCollision(delta);
		if (collision == null) {
			// If no collision integrate ball and end
			integrateBallPos(delta);
			return true;
		}
		System.out.println("yo " + collision.DELTA + " " + collision.NORMAL);
		int sweeps = 0;
		// while there is remaining delta then there is a collision and less than MAX iterations
		while (collision != null && sweeps < MAX_SWEEPS_PER_FRAME) {
			// integrate ball
			integrateBallPos(collision.DELTA);
			// bounce in correct direction
			if (collision.NORMAL.getX() != 0) {
				ballVelocity.setX(-ballVelocity.getX() * ballBounceFactor);
			} else if (collision.NORMAL.getY() != 0) {
				System.out.print("before: " + ballVelocity); // Ball is colliding twice
				ballVelocity.setY(-ballVelocity.getY() * ballBounceFactor);
				System.out.println(" after: " + ballVelocity);
			}
			// Sweep AABB collision with remaining delta and return moved delta
			collision = getSweepingBallCollision(delta);
			sweeps++;
		}
		// return iterations < MAX iterations
		return sweeps < MAX_SWEEPS_PER_FRAME;
	}

	// Moves the ball by its velocity for delta seconds
	private void integrateBallPos(double delta) {
		Vector2 ballPos = ballAABB.getPos();
		ballPos.copy(ballPos.add(ballVelocity.multiply(delta)));
	}

	private AABB.Collision getSweepingBallCollision(double delta) {
		AABB.Collision collision = null;
		for (AABB terrainAABB : terrain) {
			AABB.Collision currentCollision = ballAABB.sweepAABB(terrainAABB, delta, ballVelocity);
			// Find the collision with the smallest delta
			// System.out.println(terrainAABB);
			if (currentCollision != null)
				System.out.println(currentCollision.DELTA + " " + currentCollision.NORMAL + " " + delta);
			if (currentCollision != null && currentCollision.DELTA <= delta) {
				System.out.println("yo");
				collision = currentCollision;
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