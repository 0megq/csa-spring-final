import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.awt.event.*;

public class GameCanvas extends JComponent {
	enum Menu {
		NONE,
		MAIN,
		PAUSE,
		LEVEL_SELECT,
		RESULTS,
		CREDITS,
	}

	public static final int MSPT = 16; // ms per tick, also our tick speed
	public static final double DELTA = MSPT / 1000.0; // delta time (s per tick)
	public static final double FPS = 1 / DELTA;
	private static final float aimLineWidth = 2;

	private ArrayList<AABBDrawer> worldDrawers = new ArrayList<AABBDrawer>();
	private ArrayList<AABBDrawer> otherDrawers = new ArrayList<AABBDrawer>();
	private ButtonDrawer[] mainButtonDrawers;
	private ButtonDrawer[] pauseButtonDrawers;
	private int elapsedTicks;
	private World world;
	private int currentLevel;
	private boolean leftMousePressed;
	private boolean leftMouseJustPressed;
	private boolean leftMouseJustReleased;
	private boolean rightMousePressed;
	private boolean rightMouseJustPressed;
	private boolean rightMouseJustReleased;
	private Vector2 mousePos;
	private Menu currentMenu;
	private boolean mainTutorialTextVisible;

	public GameCanvas() {
		leftMousePressed = false;
		leftMouseJustPressed = false;
		leftMouseJustReleased = false;
		rightMousePressed = false;
		rightMouseJustPressed = false;
		rightMouseJustReleased = false;
		mousePos = new Vector2();
		currentLevel = 0;
		elapsedTicks = 0;
		currentMenu = Menu.PAUSE;

		// Create world
		world = initializeWorld(Level.LEVELS[currentLevel]);

		// Menus
		// Main menu
		Button mainPlayButton = new Button(250, 100, 100, 50, "Play!",
				new AABBDrawSettings(true, DrawType.FILL, new Color(200, 200, 200)),
				new AABBDrawSettings(true, DrawType.FILL, new Color(150, 150, 150)),
				new AABBDrawSettings(true, DrawType.FILL, new Color(100, 100, 100)));
		// Button mainLevelSelectButton = new Button(250, 100, 100, 50, "Play!",
		// 		new AABBDrawSettings(true, DrawType.FILL, new Color(200, 200, 200)),
		// 		new AABBDrawSettings(true, DrawType.FILL, new Color(150, 150, 150)),
		// 		new AABBDrawSettings(true, DrawType.FILL, new Color(100, 100, 100)));
		mainTutorialTextVisible = false;
		Button mainTutorialButton = new Button(250, 170, 100, 50, "Tutorial",
				new AABBDrawSettings(true, DrawType.FILL, new Color(200, 200, 200)),
				new AABBDrawSettings(true, DrawType.FILL, new Color(150, 150, 150)),
				new AABBDrawSettings(true, DrawType.FILL, new Color(100, 100, 100)));
		Button mainQuitButton = new Button(250, 240, 100, 50, "Quit",
				new AABBDrawSettings(true, DrawType.FILL, new Color(200, 200, 200)),
				new AABBDrawSettings(true, DrawType.FILL, new Color(150, 150, 150)),
				new AABBDrawSettings(true, DrawType.FILL, new Color(100, 100, 100)));

		mainButtonDrawers = new ButtonDrawer[] {new ButtonDrawer(mainPlayButton), new ButtonDrawer(mainQuitButton), new ButtonDrawer(mainTutorialButton)};
		
		Button pauseResumeButton = new Button(230, 100, 120, 50, "Resume",
				new AABBDrawSettings(true, DrawType.FILL, new Color(200, 200, 200)),
				new AABBDrawSettings(true, DrawType.FILL, new Color(150, 150, 150)),
				new AABBDrawSettings(true, DrawType.FILL, new Color(100, 100, 100)));
		Button pauseMainMenuButton = new Button(230, 170, 120, 50, "Back to Main Menu",
				new AABBDrawSettings(true, DrawType.FILL, new Color(200, 200, 200)),
				new AABBDrawSettings(true, DrawType.FILL, new Color(150, 150, 150)),
				new AABBDrawSettings(true, DrawType.FILL, new Color(100, 100, 100)));
		Button pauseQuitButton = new Button(230, 240, 120, 50, "Quit",
				new AABBDrawSettings(true, DrawType.FILL, new Color(200, 200, 200)),
				new AABBDrawSettings(true, DrawType.FILL, new Color(150, 150, 150)),
				new AABBDrawSettings(true, DrawType.FILL, new Color(100, 100, 100)));

		pauseButtonDrawers = new ButtonDrawer[] {new ButtonDrawer(pauseResumeButton), new ButtonDrawer(pauseMainMenuButton), new ButtonDrawer(pauseQuitButton)};

		AABB cursorFollow = new AABB(0, 0, 15, 15);

		otherDrawers.add(
				new AABBDrawer(cursorFollow, new AABBDrawSettings(true, DrawType.FILL, new Color(0, 255, 0, 120))));

		// Setup game update cycle
		Timer updateTimer = new Timer(MSPT, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// if (world.getWaitingForInput() && world.isBallInHole()) {
				// 	currentLevel++;
				// 	if (currentLevel >= Level.LEVELS.length) // if no more levels then reset to first level
				// 		currentLevel = 0;
				// 	world = initializeWorld(Level.LEVELS[currentLevel]);
				// }
				// Get the mouse position if the component is showing. Prevents crash when launching game
				if (isShowing()) {
					Vector2 screenMouseLoc = new Vector2(MouseInfo.getPointerInfo().getLocation());
					Vector2 componentLoc = new Vector2(getLocationOnScreen());
					mousePos = screenMouseLoc.subtract(componentLoc);
					cursorFollow.setCenter(mousePos);
				}

				switch (currentMenu) {
					case NONE:
						// Update world
						world.updateInput(mousePos, leftMousePressed, leftMouseJustPressed, leftMouseJustReleased, rightMousePressed, rightMouseJustPressed, rightMouseJustReleased);
						world.update(DELTA);
						break;
					case MAIN:
						mainPlayButton.update(mousePos, leftMousePressed, leftMouseJustPressed, leftMouseJustReleased);
						mainTutorialButton.update(mousePos, leftMousePressed, leftMouseJustPressed, leftMouseJustReleased);
						mainQuitButton.update(mousePos, leftMousePressed, leftMouseJustPressed, leftMouseJustReleased);
						if (mainTutorialButton.getStatus() == Button.Status.PRESSED)
							mainTutorialTextVisible = !mainTutorialTextVisible;
						if (mainPlayButton.getStatus() == Button.Status.RELEASED)
							world = initializeWorld(Level.LEVELS[currentLevel]);
							currentMenu = Menu.NONE;
						break;
					case PAUSE:
						pauseResumeButton.update(mousePos, leftMousePressed, leftMouseJustPressed, leftMouseJustReleased);
						pauseMainMenuButton.update(mousePos, leftMousePressed, leftMouseJustPressed, leftMouseJustReleased);
						pauseQuitButton.update(mousePos, leftMousePressed, leftMouseJustPressed, leftMouseJustReleased);
						break;
					case LEVEL_SELECT:
						break;
					case RESULTS:
						break;
					case CREDITS:
						break;
				}

				// Draw call
				repaint();
				// Increase ticks
				elapsedTicks += 1;

				// These should be at end of the update function
				leftMouseJustPressed = false;
				leftMouseJustReleased = false;
				rightMouseJustPressed = false;
				rightMouseJustReleased = false;
			}
		});
		// Start game update cycle
		updateTimer.start();

		// Get mouse clicks and send clicks to world object
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					leftMouseJustPressed = true;
					rightMousePressed = true;
				}
				if (e.getButton() == MouseEvent.BUTTON3) {
					rightMouseJustPressed = true;
					rightMousePressed = true;
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					leftMouseJustReleased = true;
					leftMousePressed = false;
				}
				if (e.getButton() == MouseEvent.BUTTON3) {
					rightMouseJustReleased = true;
					rightMousePressed = false;
				}
			}
		});
	}

	public void changeMenu(Menu newMenu) {
		// this is here if i need it. i dont need now, but maybe i will later
	}

	// TODO implement this
	public Level getLevel() {
		return null;
	}

	// Creates a new world and connects it to the graphics
	public World initializeWorld(Level level) {
		world = new World(level);

		worldDrawers = new ArrayList<AABBDrawer>();

		AABB[] terrain = world.TERRAIN;
		AABBDrawSettings terrainDrawSettings = new AABBDrawSettings(true, DrawType.BOTH, Color.GREEN,
				new boolean[] { false, false, false, true }, Color.BLACK, 4);
		for (AABB aabb : terrain) {
			worldDrawers.add(new AABBDrawer(aabb, terrainDrawSettings));
		}
		AABB hole = world.HOLE;
		int poleLength = 30;
		AABB flagPole = new AABB(hole.getCenter().getX(), hole.getEnd().getY() - poleLength, 2, poleLength);
		AABB flag = new AABB(flagPole.getPos().getX() - 7, flagPole.getPos().getY(),
				7, 5);
		AABBDrawSettings flagPoleDrawSettings = new AABBDrawSettings(true, DrawType.FILL, Color.BLACK);
		AABBDrawSettings flagDrawSettings = new AABBDrawSettings(true, DrawType.FILL, Color.RED);
		worldDrawers.add(new AABBDrawer(flagPole, flagPoleDrawSettings));
		worldDrawers.add(new AABBDrawer(flag, flagDrawSettings));

		// Get ball aabb from world
		AABB ballAABB = world.getBallAABB();
		// Create draw settings for ball
		AABBDrawSettings ballDrawSettings = new AABBDrawSettings(true, DrawType.FILL, Color.WHITE);
		// Create ballDrawer to draw ball
		AABBDrawer ballDrawer = new AABBDrawer(ballAABB, ballDrawSettings);
		worldDrawers.add(ballDrawer);

		return world;
	}

	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

		switch (currentMenu) {
			case NONE:
				g2.setColor(new Color(152, 234, 250));
				g2.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);

				for (AABBDrawer drawer : worldDrawers) {
					if (drawer.getShouldDraw()) {
						drawer.draw(g2);
					}
				}

				// aiming line
				if (world.isAiming()) {
					g2.setColor(Color.BLACK);
					g2.setStroke(new BasicStroke(aimLineWidth));
					Vector2 ballPos = world.getBallAABB().getCenter();
					Vector2 startPos = world.getAimStartPos();
					Vector2 mousePos = world.getMousePos();
					Vector2 directionPos = ballPos.add(startPos.subtract(mousePos));
					g2.drawLine((int) ballPos.getX(), (int) ballPos.getY(), (int) directionPos.getX(),
							(int) directionPos.getY());
					g2.drawLine((int) startPos.getX(), (int) startPos.getY(), (int) mousePos.getX(), (int) mousePos.getY());
				}

				g2.drawString("Par: " + world.PAR, 10, 20);
				g2.drawString("Stroke: " + world.getStrokes(), 530, 20);
				if (world.isAiming()) {
					g2.drawString("Right Click to Cancel", 245, 20);
				} else if (world.getWaitingForInput()) {
					g2.drawString("Ready for input", 255, 20);
				}
				break;
			case MAIN:
				FontMetrics fontMetrics = g2.getFontMetrics();
				for (ButtonDrawer drawer : mainButtonDrawers) {
					if (drawer.getShouldDraw()) {
						drawer.draw(g2, fontMetrics);
					}
				}
				if (mainTutorialTextVisible)
					g2.drawString("This is a tutorial\n       Hello!", 360, 180);
				break;
			case PAUSE:
				fontMetrics = g2.getFontMetrics();
				for (ButtonDrawer drawer : pauseButtonDrawers) {
					if (drawer.getShouldDraw()) {
						drawer.draw(g2, fontMetrics);
					}
				}
				break;
			case LEVEL_SELECT:
				break;
			case RESULTS:
				break;
			case CREDITS:
				break;	
		}

		// other drawers are drawn over all the world stuff
		for (AABBDrawer drawer : otherDrawers) {
			if (drawer.getShouldDraw()) {
				drawer.draw(g2);
			}
		}

		g2.setColor(Color.BLACK);

		// Draw border around screen
		g2.setColor(Color.GRAY);
		g2.setStroke(new BasicStroke(3));
		g2.drawRect(1, 1, Game.WIDTH - 3, Game.HEIGHT - 3);
	}
}