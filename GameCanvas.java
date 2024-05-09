import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.awt.event.*;
import java.awt.MouseInfo;
public class GameCanvas extends JComponent {
    
    public static final int MSPT = 16; // ms per tick, also our tick speed
    public static final double DELTA = MSPT / 1000.0; // delta time (s per tick)
    public static final double FPS = 1 / DELTA;
    
    private ArrayList<AABBDrawer> drawers = new ArrayList<AABBDrawer>();
    private int elapsedTicks;
    private World world;
    
    public GameCanvas() {
        elapsedTicks = 0;
        world = new World();
        Ball ball = world.getBall();
        AABBDrawSettings ballDrawSettings = new AABBDrawSettings(true, DrawType.BOTH, Color.RED, new boolean[]{false, false, false, false}, Color.GREEN, null);
        AABBDrawer ballDrawer = new AABBDrawer(ball.getBoundingBox(), ballDrawSettings);
        drawers.add(ballDrawer);

        class UpdateListener implements ActionListener{
            public void actionPerformed(ActionEvent e){
                // Get the mouse position
                Vector2 screenMouseLoc = new Vector2(MouseInfo.getPointerInfo().getLocation());
                Vector2 componentLoc = new Vector2(getLocationOnScreen());
                world.setMousePosition(screenMouseLoc.subtract(componentLoc));

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
        class MouseInput extends MouseAdapter{
            public void mousePressed(MouseEvent e){world.mousePressed(e);}
            public void mouseReleased(MouseEvent e){world.mouseReleased(e);}
        }
        addMouseListener(new MouseInput());
    }
    
    public void paintComponent(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        for (AABBDrawer drawer : drawers) {
            if (drawer.getShouldDraw())
                drawer.draw(g2);
        }
    }
}