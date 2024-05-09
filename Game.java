import javax.swing.*;
import java.awt.*;
public class Game {

    public static final int WIDTH = 600; 
    public static final int HEIGHT = 400;
    public static final int MSPT = 16; // ms per tick, also our tick speed
    public static final double DELTA = MSPT / 1000.0; // delta time (s per tick)
    public static final double FPS = 1 / DELTA;
    
    // Set up canvas
    public void play() {
        //creating instance of JFrame
        JFrame f = new JFrame();

        // Set up game canvas. Game canvas will call its own updates to keep the game loop running
        GameCanvas currentGameCanvas = new GameCanvas();
        currentGameCanvas.setPreferredSize(new Dimension(WIDTH,HEIGHT)); 
        
        f.add(currentGameCanvas);
        f.pack();
        
        //Toolkit tk = Toolkit.getDefaultToolkit();
        //Cursor c = tk.createCustomCursor(MALLET, new Point(7,31), "Mallet");
        //f.setCursor(c);
        
        f.setVisible(true);

    }    
}

