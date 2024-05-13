import java.awt.*;

public class Vector2 {
	public static final double EPSILON = 0.001;
	public static final Vector2 ZERO = new Vector2();
	public static final Vector2 UP = new Vector2(0, -1);
	public static final Vector2 DOWN = new Vector2(0, 1);
	public static final Vector2 LEFT = new Vector2(-1, 0);
	public static final Vector2 RIGHT = new Vector2(1, 0);
	private double x;
	private double y;

	public Vector2() {
		x = 0;
		y = 0;
	}

	public Vector2(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public Vector2(Point point) {
		this.x = point.getX();
		this.y = point.getY();
	}

	public Vector2 duplicate() {
		return new Vector2(x, y);
	}

	public void copy(Vector2 other) {
		x = other.x;
		y = other.y;
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public Vector2 add(Vector2 other) {
		return new Vector2(x + other.x, y + other.y);
	}

	public Vector2 add(double x, double y) {
		return new Vector2(this.x + x, this.y + y);
	}

	public Vector2 subtract(Vector2 other) {
		return new Vector2(x - other.x, y - other.y);
	}

	public Vector2 negate() {
		return new Vector2(-x, -y);
	}

	public Vector2 multiply(double multiplier) {
		return new Vector2(x * multiplier, y * multiplier);
	}

	public Vector2 normalize() {
		return new Vector2(x / getLength(), y / getLength());
	}

	public double getAngle() {
		return Math.atan2(y, x);
	}

	public double distanceTo(Vector2 other) {
		return Math.sqrt(Math.pow(x - other.x, 2) + Math.pow(y - other.y, 2));
	}

	public double getLength() {
		return Math.sqrt(x * x + y * y);
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public Point toPoint() {
		return new Point((int) x, (int) y);
	}

	public String toString() {
		return "(" + x + "," + y + ")";
	}

	public boolean equals(Vector2 other) {
		return Math.abs(x - other.x) < EPSILON && Math.abs(y - other.y) < EPSILON;
	}

	public static Vector2 fromPolar(double r, double theta) {
		return new Vector2(r * Math.sin(theta), r * Math.cos(theta));
	}
}