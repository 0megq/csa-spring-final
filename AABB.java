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

	public Vector2 getCollisionDirection(AABB other) {
		Vector2 normal = new Vector2();
		double dx = other.pos.getX() - pos.getX(); // X distance
		double px = (other.size.getX() + size.getX()) - Math.abs(dx); // Add size and subtract distance
		if (px <= 0) { // If px is less than zero then no collision that means the size combined is less than the distance between them.
			return normal;
		}

		double dy = other.pos.getX() - pos.getY(); // Y distance
		double py = (other.size.getY() + size.getY()) - Math.abs(dy);
		if (py <= 0) {
			return normal;
		}

		if (px < py) { // px and py is amount the boxes are inside each other in each direction. If there is more y then the collision is from the side
			double sx = Math.signum(dx);
			normal.setX(sx);
		} else {
			double sy = Math.signum(dy);
			normal.setY(sy);
		}

		return normal;
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