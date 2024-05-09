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
        this.shouldDraw = shouldraw;
    }
    
    public boolean getShouldDraw() {
        return shouldDraw;
    }
    
    public void draw(Graphics2D g) {
        //implement this later
        //g.draw
    }
}