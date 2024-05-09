import java.awt.*;

enum DrawType {
	FILL,
	OUTLINE,
	BOTH,
}

enum OutlineSide {
	BOTTOM,
	LEFT,
	RIGHT,
	TOP
}

public class AABBDrawSettings {
	private boolean visible;
	private DrawType drawType;
	private Color fillColor;
	private final boolean[] OUTLINES_TO_DRAW; // Whether to draw an outline on a side. Indexed by OutlineSide enum
	private Color outlineColor;
	private int outlineWidth;

	public AABBDrawSettings() {
		OUTLINES_TO_DRAW = new boolean[4];
	}

	public AABBDrawSettings(boolean visible, DrawType drawType, Color fillColor) {
		this.visible = visible;
		this.drawType = drawType;
		this.fillColor = fillColor;
		// Initialize rest to defaults
		this.OUTLINES_TO_DRAW = new boolean[4];
		this.outlineColor = null;
		this.outlineWidth = 0;
	}

	public AABBDrawSettings(boolean visible, DrawType drawType, Color fillColor, boolean[] outlinesToDraw,
			Color outlineColor, int outlineWidth) {
		this.visible = visible;
		this.drawType = drawType;
		this.fillColor = fillColor;
		this.OUTLINES_TO_DRAW = new boolean[4];
		this.OUTLINES_TO_DRAW[0] = outlinesToDraw[0];
		this.OUTLINES_TO_DRAW[1] = outlinesToDraw[1];
		this.OUTLINES_TO_DRAW[2] = outlinesToDraw[2];
		this.OUTLINES_TO_DRAW[3] = outlinesToDraw[3];
		this.outlineColor = outlineColor;
		this.outlineWidth = outlineWidth;
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
		outlineWidth = other.outlineWidth;
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

	public int getOutlineWidth() {
		return outlineWidth;
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

	public void setOutlineWidth(int outlineWidth) {
		this.outlineWidth = outlineWidth;
	}
}