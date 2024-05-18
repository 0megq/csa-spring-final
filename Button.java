public class Button extends AABB {
	enum Status {
		NORMAL,
		HOVERED,
		DOWN,
		RELEASED,
	}

	private final AABBDrawSettings NORMAL_DRAW_SETTINGS;
	private final AABBDrawSettings HOVER_DRAW_SETTINGS;
	private final AABBDrawSettings DOWN_DRAW_SETTINGS;
	private String text;
	private AABBDrawSettings currentDrawSettings;
	private Status latestStatus;

	public Button(int x, int y, int w, int h, String text, AABBDrawSettings normalDrawSettings,
			AABBDrawSettings hoverDrawSettings, AABBDrawSettings downDrawSettings) {
		super(x, y, w, h);
		this.NORMAL_DRAW_SETTINGS = normalDrawSettings;
		this.HOVER_DRAW_SETTINGS = hoverDrawSettings;
		this.DOWN_DRAW_SETTINGS = downDrawSettings;
		this.text = text;
		this.latestStatus = Status.NORMAL;
		this.currentDrawSettings = NORMAL_DRAW_SETTINGS;
	}

	public void update(Vector2 mousePos, boolean leftClickDown) {
		if (isColliding(mousePos)) {
			if (!leftClickDown && latestStatus == Status.DOWN) {
				currentDrawSettings.copy(NORMAL_DRAW_SETTINGS); // NORMAL and RELEASED status should share the same visuals
				latestStatus = Status.RELEASED;
			} else if (leftClickDown) {
				currentDrawSettings.copy(DOWN_DRAW_SETTINGS);
				latestStatus = Status.DOWN;
			} else {
				currentDrawSettings.copy(HOVER_DRAW_SETTINGS);
				latestStatus = Status.HOVERED;
			}
		} else {
			currentDrawSettings.copy(NORMAL_DRAW_SETTINGS);
			latestStatus = Status.NORMAL;
		}
	}

	public void setText(String text) {
		this.text = text;
	}

	public AABBDrawSettings getDrawSettings() {
		return currentDrawSettings;
	}

	public String getText() {
		return text;
	}

	public Status getLatestStatus() {
		return latestStatus;
	}

	public String toString() {
		return "Button, Text: " + text + "Status: " + latestStatus + " " + super.toString();
	}
}