import java.awt.*;

public class AABBDrawer {
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
		g.fillRect((int) aabb.getPos().getX(), (int) aabb.getPos().getY(), (int) aabb.getSize().getX(),
				(int) aabb.getSize().getY());
	}

	private void outline(Graphics2D g) {
		g.setColor(drawSettings.getOutlineColor());

		Vector2 pos = aabb.getPos();
		int x = (int) pos.getX();
		int y = (int) pos.getY();

		Vector2 size = aabb.getSize();
		int w = (int) size.getX();
		int h = (int) size.getY();

		boolean[] outlinesToDraw = drawSettings.getOutlinesToDraw();
		int outlineWidth = drawSettings.getOutlineWidth();

		// Draw the outline for each side. Outline is drawn inside the aabb
		// If a transparent color is used, then the corners will be darker, due to overlap
		if (outlinesToDraw[OutlineSide.LEFT.ordinal()])
			g.fillRect(x, y, outlineWidth, h);
		if (outlinesToDraw[OutlineSide.RIGHT.ordinal()])
			g.fillRect(x + w - outlineWidth, y, outlineWidth, h);
		if (outlinesToDraw[OutlineSide.TOP.ordinal()])
			g.fillRect(x, y, w, outlineWidth);
		if (outlinesToDraw[OutlineSide.BOTTOM.ordinal()])
			g.fillRect(x, y + h - outlineWidth, w, outlineWidth);
	}
}