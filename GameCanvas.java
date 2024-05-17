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
	private int elapsedTicks;
	private World world;

	public GameCanvas() {
		elapsedTicks = 0;
		// Create world
		world = initializeWorld(Level.LEVEL1);

		AABB cursorFollow = new AABB(0, 0, 15, 15);

		otherDrawers.add(
				new AABBDrawer(cursorFollow, new AABBDrawSettings(true, DrawType.FILL, new Color(0, 255, 0, 120))));

		// Setup game update cycle
		Timer updateTimer = new Timer(MSPT, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Get the mouse position if the component is showing. Prevents crash when launching game
				if (isShowing()) {
					Vector2 screenMouseLoc = new Vector2(MouseInfo.getPointerInfo().getLocation());
					Vector2 componentLoc = new Vector2(getLocationOnScreen());
					Vector2 mousePos = screenMouseLoc.subtract(componentLoc);
					world.setMousePosition(mousePos);
					cursorFollow.setCenter(mousePos);
				}

				// Update world
				world.update(DELTA);
				// Draw call
				repaint();
				// Increase ticks
				elapsedTicks += 1;
			}
		});
		// Start game update cycle
		updateTimer.start();

		// Get mouse clicks and send clicks to world object
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				world.mousePressed(e);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				world.mouseReleased(e);
			}
		});
	}

	// TODO implement this
	public Level getLevel() {
		return null;
	}

	// Creates a new world and connects it to the graphics
	public World initializeWorld(Level level) {
		world = new World(level);

		worldDrawers = new ArrayList<AABBDrawer>();

		// Get ball aabb from world
		AABB ballAABB = world.getBallAABB();
		// Create draw settings for ball
		AABBDrawSettings ballDrawSettings = new AABBDrawSettings(true, DrawType.FILL, Color.RED);
		// Create ballDrawer to draw ball
		AABBDrawer ballDrawer = new AABBDrawer(ballAABB, ballDrawSettings);
		worldDrawers.add(ballDrawer);

		AABB[] terrain = world.TERRAIN;
		AABBDrawSettings terrainDrawSettings = new AABBDrawSettings(true, DrawType.BOTH, Color.GREEN,
				new boolean[] { false, false, false, true }, Color.BLACK, 4);
		for (AABB aabb : terrain) {
			worldDrawers.add(new AABBDrawer(aabb, terrainDrawSettings));
		}
		AABB hole = world.HOLE;
		AABB flag = new AABB(hole.getPos().getX() + 3.0, hole.getPos().getY(),
				hole.getCenter().getX() - hole.getPos().getX() - 3, 300);
		AABB flagPole = new AABB(hole.getCenter().getX(), hole.getPos().getY() + 20, 2, hole.getPos().getY() - 2);
		AABBDrawSettings holeDrawSettings = new AABBDrawSettings(true, DrawType.FILL, new Color(0, 0, 255, 120));
		AABBDrawSettings flagPoleDrawSettings = new AABBDrawSettings(true, DrawType.FILL, Color.BLACK);
		AABBDrawSettings flagDrawSettings = new AABBDrawSettings(true, DrawType.FILL, Color.RED);
		// worldDrawers.add(new AABBDrawer(flagPole, flagPoleDrawSettings));

		worldDrawers.add(new AABBDrawer(flag, flagDrawSettings));
		return world;
	}

	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

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

		// other drawers are drawn over all the world stuff
		for (AABBDrawer drawer : otherDrawers) {
			if (drawer.getShouldDraw()) {
				drawer.draw(g2);
			}
		}

		g2.setColor(Color.BLACK);
		g2.drawString("Par: " + world.PAR, 10, 10);
		g2.drawString("Stroke: " + world.getStrokes(), 530, 10);
		if (world.isAiming()) {
			g2.drawString("Right Click to Cancel", 175, 10);
		} else if (world.getWaitingForInput()) {
			g2.drawString("Ready for input", 180, 10);
		}
	}
}