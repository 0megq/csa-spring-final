public class Button extends AABB {
	enum Status {
		NORMAL,
		HOVERED,
		PRESSED,
		DOWN,
		RELEASED,
	}

	private final AABBDrawSettings NORMAL_DRAW_SETTINGS;
	private final AABBDrawSettings HOVER_DRAW_SETTINGS;
	private final AABBDrawSettings DOWN_DRAW_SETTINGS;
	private String text;
	private AABBDrawSettings currentDrawSettings;
	private Status status;

	public Button(int x, int y, int w, int h, String text, AABBDrawSettings normalDrawSettings,
			AABBDrawSettings hoverDrawSettings, AABBDrawSettings downDrawSettings) {
		super(x, y, w, h);
		this.NORMAL_DRAW_SETTINGS = normalDrawSettings;
		this.HOVER_DRAW_SETTINGS = hoverDrawSettings;
		this.DOWN_DRAW_SETTINGS = downDrawSettings;
		this.text = text;
		this.status = Status.NORMAL;
		this.currentDrawSettings = NORMAL_DRAW_SETTINGS.duplicate();
	}

	public void update(Vector2 mousePos, boolean leftMousePressed, boolean leftMouseJustPressed,
			boolean leftMouseJustReleased) {
		if (isColliding(mousePos)) {
			if (leftMouseJustReleased) {
				status = Status.RELEASED;
			} else if (leftMouseJustPressed) {
				currentDrawSettings.copy(DOWN_DRAW_SETTINGS);
				status = Status.PRESSED;
			} else if (status == Status.DOWN || status == Status.PRESSED) {
				currentDrawSettings.copy(DOWN_DRAW_SETTINGS);
				status = Status.DOWN;
			} else {
				currentDrawSettings.copy(HOVER_DRAW_SETTINGS);
				status = Status.HOVERED;
			}
		} else {
			currentDrawSettings.copy(NORMAL_DRAW_SETTINGS);
			status = Status.NORMAL;
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

	public Status getStatus() {
		return status;
	}

	public String toString() {
		return "Button, Text: " + text + "Status: " + status + " " + super.toString();
	}
}