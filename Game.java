import javax.swing.*;
import java.awt.*;

public class Game {

	public static final int WIDTH = 600;
	public static final int HEIGHT = 400;

	// Set up canvas
	public void play() {
		//creating instance of JFrame
		JFrame f = new JFrame();

		// Set up game canvas. Game canvas will call its own updates to keep the game loop running
		GameCanvas currentGameCanvas = new GameCanvas();
		currentGameCanvas.setPreferredSize(new Dimension(WIDTH, HEIGHT));

		f.add(currentGameCanvas);
		f.pack();

		//Toolkit tk = Toolkit.getDefaultToolkit();
		//Cursor c = tk.createCustomCursor(MALLET, new Point(7,31), "Mallet");
		//f.setCursor(c);

		f.setVisible(true);

	}
}
