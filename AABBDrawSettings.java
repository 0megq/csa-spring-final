import java.awt.*;
enum DrawType {
    FILL,
    OUTLINE,
    BOTH,
}

enum OutlineType {
    CENTER, // Start outline from center
    OUTER, // Start outline from 1 px out and go out
    INNER, // Outline starts from border px and goes in
}

public class AABBDrawSettings {
    private boolean visible;
    private DrawType drawType;
    private Color fillColor;
    private final boolean[] OUTLINES_TO_DRAW;
    private Color outlineColor;
    private OutlineType outlineType;
    
    public AABBDrawSettings() {
        OUTLINES_TO_DRAW = new boolean[4];
    }

    public AABBDrawSettings(boolean visible, DrawType drawType, Color fillColor, boolean[] outlinesToDraw, Color outlineColor, OutlineType outlineType) {
        this.visible = visible;
        this.drawType = drawType;
        this.fillColor = fillColor;
        this.OUTLINES_TO_DRAW = new boolean[4];
        this.OUTLINES_TO_DRAW[0] = outlinesToDraw[0];
        this.OUTLINES_TO_DRAW[1] = outlinesToDraw[1];
        this.OUTLINES_TO_DRAW[2] = outlinesToDraw[2];
        this.OUTLINES_TO_DRAW[3] = outlinesToDraw[3];
        this.outlineColor = outlineColor;
        this.outlineType = outlineType;
    }

    public void copy(AABBDrawSettings other) {
        visible = other.visible;
        drawType = other.drawType;
        fillColor = other.fillColor;
        OUTLINES_TO_DRAW[0] = other.OUTLINES_TO_DRAW[0];
        OUTLINES_TO_DRAW[1] = other.OUTLINES_TO_DRAW[1];
        OUTLINES_TO_DRAW[2] = other.OUTLINES_TO_DRAW[2];
        OUTLINES_TO_DRAW[3] = other.OUTLINES_TO_DRAW[3];
        outlineColor = other.outlineColor;
        outlineType = other.outlineType;
    }

    public AABBDrawSettings duplicate() {
        AABBDrawSettings newSettings = new AABBDrawSettings();
        newSettings.copy(this);
        return newSettings;
    }
    
    public boolean getVisible() {
        return visible;
    }
    
    public DrawType getDrawType() {
        return drawType;
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
    
    public OutlineType getOutlineType() {
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
    
    public void setOutlineColor(Color outlineColor) {
        this.outlineColor = outlineColor;
    }
}