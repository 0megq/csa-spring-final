import java.awt.event.MouseEvent;
import java.util.*;

public class World {
	private AABB ballAABB;
	private Vector2 ballVelocity;
	private double ballBounceFactor;
	private double ballGravity;
	private ArrayList<AABB> terrain;
	private AABB hole;
	private boolean isMouseDown;
	private boolean mouseJustPressed;
	private boolean mouseJustReleased;
	private Vector2 mousePos;

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

		terrain.add(new AABB(0, 380, 600, 20)); // ground
		terrain.add(new AABB(300, 200, 10, 80)); // ground
	}

	public void update(double delta) {


		if (!ballVelocity.equals(Vector2.ZERO)) { // replace with while to check for collision later
			integrateBallPos(delta);
		}

		// These should be at end of the update function
		mouseJustPressed = false;
		mouseJustReleased = false;
	}

	// Moves the ball by its velocity for delta seconds
	private void integrateBallPos(double delta) {
		Vector2 ballPos = ballAABB.getPos();
		ballPos.setX(ballPos.getX() + ballVelocity.getX() * delta);
		ballPos.setY(ballPos.getY() + ballVelocity.getY() * delta);
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

	public ArrayList<AABB> getTerrain() {
		return terrain;
	}

	public AABB getHole() {
		return hole;
	}
}