public class Ball {
	private Vector2 velocity; // direction and speed of ball
	private AABB boundingBox; // The size and position of the ball
	private double bounceFactor; // From 0 to 1 how much velocity is conserved when bouncing of an object
	private double gravity; // This is an acceleration

	public Ball(Vector2 velocity, AABB boundingBox, double bounceFactor, double gravity) {
		this.velocity = velocity;
		this.boundingBox = boundingBox;
		this.bounceFactor = bounceFactor;
		this.gravity = gravity;
	}

	public void setVelocity(Vector2 velocity) {
		this.velocity.copy(velocity); // We don't want to copy the memory reference so we use copy
	}

	public void setBoundingBox(AABB boundingBox) {
		this.boundingBox.copy(boundingBox); // We don't want copy the memory reference so we use copy
	}

	public Vector2 getVelocity() {
		return velocity;
	}

	public AABB getBoundingBox() {
		return boundingBox;
	}

	public double getBounceFactor() {
		return bounceFactor;
	}

	public double getGravity() {
		return gravity;
	}
}