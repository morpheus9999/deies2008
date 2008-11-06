package dei.es2008;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * É a classe main do jogo.
 */
public class Arranque extends Applet {

    private static ControladorDeJogo game;

    public static void main(String[] args) {

        try {
            // Set System L&F
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("Ocorreu um erro ao mudar o look and feel");
        }


        JFrame frame = new JFrame("Tetris");
        game = new ControladorDeJogo(false);
        frame.add(game.getComponente());
        frame.pack();
        frame.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        centrarJanela(frame);
        frame.show();
    }

    static private void centrarJanela(JFrame frame) {
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

        game = new ControladorDeJogo(true);
        game.setAppletContext(getAppletContext());

        setLayout(new BorderLayout());
        add(game.getComponente(), "Center");
    }

    public void stop() {
        game.sair();
    }
}
