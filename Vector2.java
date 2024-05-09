public class Vector2 {
    private double x;
    private double y;
    
    public static void main(String[] args) {
        Vector2 v = new Vector2(2, 2);
        System.out.println(v.getAngle());
    }
    
    public Vector2() {
        x = 0;
        y = 0;
    }
    
    public Vector2(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    public Vector2 add(Vector2 other) {
        return new Vector2(x + other.x, y + other.y);
    }
    
    public Vector2 negate() {
        return new Vector2(-x, -y);
    }
    
    public Vector2 multiply(double multiplier) {
        return new Vector2(x * multiplier, y * multiplier);
    }
    
    public Vector2 normalize() {
        return new Vector2();
    }
    
    public double getAngle() {
        return Math.atan2(y, x);
    }
    
    public double distanceTo(Vector2 other) {
        return Math.sqrt(Math.pow(x - other.x, 2), Math.pow(y - other.y, 2));
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
}