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

	public Vector2 getPos() {
		return pos;
	}

	public Vector2 getSize() {
		return size;
	}

	public String toString() {
		return "P: " + pos + ", S: " + size;
	}
}