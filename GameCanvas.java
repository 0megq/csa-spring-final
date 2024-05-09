import javax.swing.*;
import java.awt.*;
import java.utils.*;
public class GameCanvas extends JComponent {
    
    private ArrayList<AABBDrawer> drawers = new ArrayList<AABBDrawer>();
    
    public void addDrawer(AABB box) {
        drawers.add(box);
    }
    
    public void paintComponent(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        for (AABBDrawer drawer : drawers) {
            if (drawer.getShouldDraw())
                drawer.draw(g2);
        }
    }
}