import java.awt.event.MouseEvent;
import java.util.*;

public class World {
	private AABB ballAABB;
	private Vector2 ballVelocity;
	private double ballBounceFactor; // TODO: Implement collisions and bouncing
	private double ballGravity;
	private double ballLaunchMultiplier; // Multiplied by distance between ball and mouse to get magnitude of launch velocity
	private ArrayList<AABB> terrain;
	private AABB hole;
	private boolean isMouseDown; // TODO: Use this variable
	private boolean mouseJustPressed;
	private boolean mouseJustReleased;
	private Vector2 mousePos;
	private boolean waitingForInput;
	private boolean aiming; // When mouse is held down and mouse is moving around. If mouse goes off screen this turns to false

	public World() {
		ballVelocity = new Vector2(30.0, 15.0);
		ballAABB = new AABB(150, 150, 5, 5);
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

		terrain.add(new AABB(0, 380, 600, 20)); // ground
		terrain.add(new AABB(300, 200, 10, 80)); // ground
	}

	public void update(double delta) {


		if (!waitingForInput) { // replace with while to check for collision later
			ballVelocity.setY(ballVelocity.getY() + ballGravity); // apply gravity
			integrateBallPos(delta); // move ball based on velocity
			if (isBallColliding() != null) {
				ballVelocity.copy(Vector2.ZERO); // temporary solution for collision
			}
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
			}
			if (mousePos.getX() < -1 || mousePos.getY() < -1 || mousePos.getX() > Game.WIDTH + 1 || mousePos.getY() > Game.HEIGHT + 1) {
				aiming = false;
			}
			// draw the line
			System.out.println("aiming");

		} else if (mouseJustPressed) {
			aiming = true;
		}

		// These should be at end of the update function
		mouseJustPressed = false;
		mouseJustReleased = false;
	}

	// Moves the ball by its velocity for delta seconds
	private void integrateBallPos(double delta) {
		Vector2 ballPos = ballAABB.getPos();
		ballPos.copy(ballPos.add(ballVelocity.multiply(delta)));
	}

	private AABB isBallColliding() {
		for (AABB terrainAABB : terrain) {
			if (ballAABB.isCollding(terrainAABB)) {
				return terrainAABB;
			}
		}
		return null;
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