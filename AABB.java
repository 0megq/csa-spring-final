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

	// implement this 
	public Vector2 getCollisionDirection() {
	// 	const dx = box.pos.x - this.pos.x;
    // const px = (box.half.x + this.half.x) - abs(dx);
    // if (px <= 0) {
    //   return null;
    // }

    // const dy = box.pos.y - this.pos.y;
    // const py = (box.half.y + this.half.y) - abs(dy);
    // if (py <= 0) {
    //   return null;
    // }

    // const hit = new Hit(this);
    // if (px < py) {
    //   const sx = sign(dx);
    //   hit.delta.x = px * sx;
    //   hit.normal.x = sx;
    //   hit.pos.x = this.pos.x + (this.half.x * sx);
    //   hit.pos.y = box.pos.y;
    // } else {
    //   const sy = sign(dy);
    //   hit.delta.y = py * sy;
    //   hit.normal.y = sy;
    //   hit.pos.x = box.pos.x;
    //   hit.pos.y = this.pos.y + (this.half.y * sy);
    // }
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