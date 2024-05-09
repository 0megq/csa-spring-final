import java.awt.*;
enum DrawType {
    FILL,
    OUTLINE,
}

enum OutlineType {
    CENTER, // Start outline from center
    OUTER, // Start outline from 1 px out and go out
    INNER, // Outline starts from border px and goes in
}

public class AABBDrawSettings {
    private boolean visible;
    private DrawType type;
    private Color fillColor;
    private final boolean[] OUTLINES_TO_DRAW;
    private Color outlineColor;
    private OutlineType outlineType;
    
    public AABBDrawSettings() {
        OUTLINES_TO_DRAW = new boolean[4];
    }
    
    public boolean getVisible() {
        return visible;
    }
    
    public DrawType getDrawType() {
        return type;
    }
    
    public Color getFillColor() {
        return fillColor;
    }
    
    public boolean[] getOutlinesToDraw() {
        return OUTLINES_TO_DRAW;
    }
    
    public Color getOutlineColor() {
        return outlineColor;
    }
    
    public boolean getOutlineType() {
        return outlineType;
    }
    
    public void setVisible(boolean visible) {
        this.visible = visible;
    }
    
    public void setDrawType(DrawType drawType) {
        this.drawType = drawType;
    }
    
    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
    }
    
    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}