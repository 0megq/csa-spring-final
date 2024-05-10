import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.awt.event.*;

public class GameCanvas extends JComponent {

	public static final int MSPT = 16; // ms per tick, also our tick speed
	public static final double DELTA = MSPT / 1000.0; // delta time (s per tick)
	public static final double FPS = 1 / DELTA;

	private ArrayList<AABBDrawer> drawers = new ArrayList<AABBDrawer>();
	private int elapsedTicks;
	private World world;

	public GameCanvas() {
		elapsedTicks = 0;
		// Create world
		world = new World();
		// Get ball aabb from world
		AABB ballAABB = world.getBallAABB();
		// Create draw settings for ball
		AABBDrawSettings ballDrawSettings = new AABBDrawSettings(true, DrawType.FILL, Color.RED);
		// Create ballDrawer to draw ball
		AABBDrawer ballDrawer = new AABBDrawer(ballAABB, ballDrawSettings);
		drawers.add(ballDrawer);

		ArrayList<AABB> terrain = world.getTerrain();
		AABBDrawSettings terrainDrawSettings = new AABBDrawSettings(true, DrawType.FILL, Color.BLACK);
		for (AABB aabb : terrain) {
			drawers.add(new AABBDrawer(aabb, terrainDrawSettings));
		}

		AABB cursorFollow = new AABB(0, 0, 15, 15);

		drawers.add(new AABBDrawer(cursorFollow, new AABBDrawSettings(true, DrawType.FILL, new Color(0, 255, 0, 120))));

		class UpdateListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				// Get the mouse position
				Vector2 screenMouseLoc = new Vector2(MouseInfo.getPointerInfo().getLocation());
				Vector2 componentLoc = new Vector2(getLocationOnScreen());
				Vector2 mousePos = screenMouseLoc.subtract(componentLoc);
				world.setMousePosition(mousePos);
				cursorFollow.setPos(mousePos);

				// Update world
				world.update(DELTA);
				// Draw call
				repaint();
				// Increase ticks
				elapsedTicks += 1;
			}
		}

		// Begin the game update cycle
		Timer t = new Timer(MSPT, new UpdateListener());
		t.start();

		// Get mouse clicks and send clicks to world
		class MouseInput extends MouseAdapter {
			public void mousePressed(MouseEvent e) {
				world.mousePressed(e);
			}

			public void mouseReleased(MouseEvent e) {
				world.mouseReleased(e);
			}
		}
		addMouseListener(new MouseInput());
	}

	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		for (AABBDrawer drawer : drawers) {
			if (drawer.getShouldDraw())
				drawer.draw(g2);
		}

	}
}