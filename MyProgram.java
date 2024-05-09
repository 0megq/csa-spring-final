import javax.swing.*;
import java.awt.*;
public class MyProgram {
    
    public static final int WIDTH = 600; 
    public static final int HEIGHT = 400;
    
    public static void main(String[] args) {
        //creating instance of JFrame
        JFrame f = new JFrame();

        GameCanvas currentGameCanvas = new GameCanvas();
        currentGameCanvas.setPreferredSize(new Dimension(WIDTH,HEIGHT)); 
        
        f.add(currentGameCanvas);
        f.pack();
        
        Toolkit tk = Toolkit.getDefaultToolkit();
        //Cursor c = tk.createCustomCursor(MALLET, new Point(7,31), "Mallet");
        //f.setCursor(c);
        
        
        f.setVisible(true);
    }
}