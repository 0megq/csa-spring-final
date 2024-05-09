import java.awt.event.MouseEvent;
import java.util.*;

public class World {
    private Ball ball;
    private ArrayList<AABB> terrain;
    private AABB hole;
    private boolean isMouseDown;
    private boolean mouseJustPressed;
    private boolean mouseJustReleased;
    private Vector2 mousePos;

    public World() {
        ball = new Ball(new Vector2(), new AABB(30, 30, 10, 10), 0.5, 10);
        terrain = new ArrayList<>();
        hole = new AABB();
        isMouseDown = false;
        mouseJustPressed = false;
        mouseJustReleased = false;
        mousePos = new Vector2();
    }

    public void update(double delta) {

        // These should be at end of the update function
        mouseJustPressed = false;
        mouseJustReleased = false;
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

    public void setBall(Ball ball) {
        this.ball = ball;
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

    public Ball getBall() {
        return ball;
    }
   
    public ArrayList<AABB> getTerrain() {
        return terrain;
    }
   
    public AABB getHole() {
        return hole;
    }
}