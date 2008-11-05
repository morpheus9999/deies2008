package dei.es2008;
import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;

/**
 * É a classe main do jogo.
 */
public class Arranque extends Applet {

    private Gui game = null;
    
    public static void main (String[] args) {
        JFrame frame = new JFrame("Tetris");
        Gui game = new Gui(false);
        frame.add(game.getComponent());
        frame.pack();
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
		center(frame);
        frame.show();
	}
	
	 static private void center (JFrame frame) {
	    //center window
	    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	    Dimension frameSize = frame.getSize();

	    if (frameSize.height > screenSize.height) {
	        frameSize.height = screenSize.height;
	    }

	    if (frameSize.width > screenSize.width) {
	        frameSize.width = screenSize.width;
	    }

	    frame.setLocation((screenSize.width - frameSize.width) / 2,
	        (screenSize.height - frameSize.height) / 2);
	}

    /**
     * Inicia a applet do jogo.
     */
    public void init() {
        
        game = new Gui(true);
		game.setAppletContext(getAppletContext());
      
        setLayout(new BorderLayout());
        add(game.getComponent(), "Center");
    }

    public void stop() {
        game.quit();
    }
    
}
