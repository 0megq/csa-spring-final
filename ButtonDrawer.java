import java.awt.*;
import java.awt.geom.Rectangle2D;

public class ButtonDrawer {
	private static final int TEXT_Y_ADJUST = 3; // pixels to adjust the text y position by. This helps it to look more centered
	private boolean shouldDraw;
	private Button button;
	private AABBDrawSettings drawSettings;

	public ButtonDrawer(Button button) {
		this.button = button;
		this.shouldDraw = true;
		this.drawSettings = button.getDrawSettings();
	}

	public void setShouldDraw(boolean shouldDraw) {
		this.shouldDraw = shouldDraw;
	}

	public boolean getShouldDraw() {
		return shouldDraw;
	}

	// I made fontMetrics an argument so we don't need to call g.getFontMetrics for every single call to this function
	public void draw(Graphics2D g, FontMetrics fontMetrics) {
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
		String text = button.getText();
		g.setColor(Color.BLACK); // All buttons will have black text and same font size

		Rectangle2D textRect = fontMetrics.getStringBounds(text, g);

		Vector2 butPos = button.getPos();
		Vector2 butSize = button.getSize();

		double textX = butPos.getX() + (butSize.getX() - textRect.getWidth()) / 2;
		double textY = button.getEnd().getY() - (butSize.getY() - textRect.getHeight()) / 2 - TEXT_Y_ADJUST;
		// g.drawRect((int) textX, (int) (textY - textRect.getWidth()), (int) textRect.getWidth(),
		// 		(int) textRect.getHeight());
		g.drawString(text, (int) textX, (int) textY);
	}

	private void fill(Graphics2D g) {
		g.setColor(drawSettings.getFillColor());
		g.fillRect((int) button.getPos().getX(), (int) button.getPos().getY(), (int) button.getSize().getX(),
				(int) button.getSize().getY());
	}

	private void outline(Graphics2D g) {
		g.setColor(drawSettings.getOutlineColor());

		Vector2 pos = button.getPos();
		int x = (int) pos.getX();
		int y = (int) pos.getY();

		Vector2 size = button.getSize();
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