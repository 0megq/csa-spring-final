public class AABB {
	private Vector2 pos;
	private Vector2 size;

	public AABB() {
		pos = new Vector2();
		size = new Vector2();
	}

	public AABB(Vector2 pos, Vector2 size) {
		this.pos = pos;
		this.size = size;
	}

	public AABB(double x, double y, double w, double h) {
		this.pos = new Vector2(x, y);
		this.size = new Vector2(w, h);
	}

	public boolean isCollding(AABB other) {
		return pos.getX() < other.pos.getX() + other.size.getX() && other.pos.getX() < pos.getX() + size.getX()
				&& pos.getY() < other.pos.getY() + other.size.getY() && other.pos.getY() < pos.getY() + size.getY();
	}

	// Sweep the current aabb to the other AABB with the given velocity and delta. The delta at which the collision occurred is returned.
	// Will return null if aabbs are inside each other
	public Collision sweepAABB(AABB other, Vector2 velocity) {
		Vector2 segmentStart = pos.duplicate(); // So we don't accidentally change the position
		AABB paddedAabb = new AABB(other.pos.duplicate(), size.add(other.size)); // New aabb with pos of other and size of both combined

		// Variables to see which side the segment is on
		boolean top = segmentStart.getY() < paddedAabb.pos.getY();
		boolean bottom = segmentStart.getY() > paddedAabb.getEnd().getY();
		boolean left = segmentStart.getX() < paddedAabb.pos.getX();
		boolean right = segmentStart.getX() > paddedAabb.getEnd().getX();
		// System.out.println(top + " " + bottom + " " + right + " " + left);

		Collision collision = null;

		if (top) {
			// Get y position. See where the ray is at that y position
			double deltaTo = (other.pos.getY() - pos.getY()) / velocity.getY();
			double xAtDelta = pos.getX() + velocity.getX() * deltaTo;
			// System.out.print("on top side");
			// System.out.print(" " + xAtDelta);
			// System.out.println(" " + deltaTo);
			// Check if the x position after being integrated by deltaTo * velocity is within the bounds of the padded AABB
			if (xAtDelta > paddedAabb.pos.getX() && xAtDelta < paddedAabb.getEnd().getX()) {
				// Potential collision
				// DeltaTo is the delta at which the collision occurred. This will be returned
				// The smaller deltaTo indicates collision happened earlier so we will get minimum between the current col delta and delta to
				// so that the smallest delta to is returned
				if (collision == null || deltaTo < collision.DELTA) {
					collision = new Collision(deltaTo, Vector2.UP);
				}
				
			}
		}
		if (bottom) {
			// Get y position. See where the ray is at that y position
			double deltaTo = (other.getEnd().getY() - pos.getY()) / velocity.getY();
			double xAtDelta = velocity.getX() * deltaTo;
			// Check if the x position after being integrated by deltaTo * velocity is within the bounds of the padded AABB
			if (xAtDelta > paddedAabb.pos.getX() && xAtDelta < paddedAabb.getEnd().getX()) {
				// Potential collision
				// DeltaTo is the delta at which the collision occurred. This will be returned
				// The smaller deltaTo indicates collision happened earlier so we will get minimum between the current col delta and delta to
				// so that the smallest delta to is returned
				if (collision == null || deltaTo < collision.DELTA) {
					collision = new Collision(deltaTo, Vector2.DOWN);
				}
				
			}
		}
		if (left) {
			// Get x position of left side. Get delta value to left side. get the y value at that delta
			double deltaTo = (other.pos.getX() - pos.getX()) / velocity.getX();
			double yAtDelta = velocity.getY() * deltaTo;
			// Check if the y position after being integrated by deltaTo * velocity is within the bounds of the padded AABB
			if (yAtDelta > paddedAabb.pos.getY() && yAtDelta < paddedAabb.getEnd().getY()) {
				// Potential collision
				// DeltaTo is the delta at which the collision occurred. This will be returned
				// The smaller deltaTo indicates collision happened earlier so we will get minimum between the current col delta and delta to
				// so that the smallest delta to is returned
				if (collision == null || deltaTo < collision.DELTA) {
					collision = new Collision(deltaTo, Vector2.LEFT);
				}
			}
		}
		if (right) {
			// Get x position of right side. See where the ray is at that x position
			double deltaTo = (other.getEnd().getX() - pos.getX()) / velocity.getX();
			double yAtDelta = velocity.getY() * deltaTo;
			// Check if the x position after being integrated by deltaTo * velocity is within the bounds of the padded AABB
			if (yAtDelta > paddedAabb.pos.getY() && yAtDelta < paddedAabb.getEnd().getY()) {
				// Potential collision
				// DeltaTo is the delta at which the collision occurred. This will be returned
				// The smaller deltaTo indicates collision happened earlier so we will get minimum between the current col delta and delta to
				// so that the smallest delta to is returned
				if (collision == null || deltaTo < collision.DELTA) {
					collision = new Collision(deltaTo, Vector2.RIGHT);
				}
				
			}
		}
		return collision;
	}

	public class Collision {
		public final double DELTA; // the delta at which the collision occurred. 
		// if delta is negative then the AABB are already inside each other
		public final Vector2 NORMAL; // If the an aabb hits the other aabb from the top
		public Collision(double delta, Vector2 normal) {
			this.DELTA = delta;
			this.NORMAL = normal;
		}
	}

	public void copy(AABB other) {
		this.pos.copy(other.pos);
		this.size.copy(other.size);
	}

	public void setPos(Vector2 pos) {
		this.pos = pos;
	}

	public void setSize(Vector2 size) {
		this.size = size;
	}

	public void setCenter(Vector2 center) {
		pos.copy(center.subtract(size.multiply(0.5)));
	}

	public Vector2 getPos() {
		return pos;
	}

	public Vector2 getSize() {
		return size;
	}

	public Vector2 getEnd() {
		return pos.add(size);
	}

	public Vector2 getCenter() {
		return pos.add(size.multiply(0.5));
	}

	public String toString() {
		return "P: " + pos + ", S: " + size;
	}
}