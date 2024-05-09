import java.awt.*;

public class AABBDrawer {
    // 
    private boolean shouldDraw;
    private AABB aabb;
    private AABBDrawSettings drawSettings;
    
    public AABBDrawer(AABB aabb, AABBDrawSettings drawSettings) {
        this.aabb = aabb;
        this.drawSettings = drawSettings;
        this.shouldDraw = true;
    }
    
    public void setShouldDraw(boolean shouldDraw) {
        this.shouldDraw = shouldDraw;
    }
    
    public boolean getShouldDraw() {
        return shouldDraw;
    }
    
    public void draw(Graphics2D g) {
        if (!drawSettings.getVisible())
            return;
        switch (drawSettings.getDrawType()) {
            case FILL:
                fill(g); 
                break;
            case OUTLINE:
                outline(g);
                break;
            case BOTH:
                fill(g);
                outline(g);
                break;
        }
    }
    
    private void fill(Graphics2D g) {
        g.setColor(drawSettings.getFillColor());
        g.fillRect((int)aabb.getPos().getX(), (int)aabb.getPos().getY(), (int)aabb.getSize().getX(), (int)aabb.getSize().getY());
    }

    private void outline(Graphics2D g) {
        g.setColor(drawSettings.getOutlineColor());
        g.drawRect((int)aabb.getPos().getX(), (int)aabb.getPos().getY(), (int)aabb.getSize().getX(), (int)aabb.getSize().getY());
    }
}