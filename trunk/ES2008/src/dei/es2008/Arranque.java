package dei.es2008;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
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
    
    int x=0;

    public static void main(String[] args) {
        try {
            // Set System L&F
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("Ocorreu um erro ao mudar o look and feel");
        }
        new Arranque();
    //if(true){



    }
    private ControladorDeJogo game;

    public Arranque() {
        Menu xpto = new Menu(this);
        xpto.f.setBounds(10, 10, 500, 400);
        xpto.f.getContentPane().setLayout(new GridLayout());
        xpto.f.getContentPane().add(xpto);
        xpto.f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        xpto.f.setVisible(true);
        xpto.h.setVisible(false);
        centrarJanela(xpto.f);
        
        Menu p2 = new Menu(this);
        
        try{
                p2.getFileImage("ducky.jpg");
            }
            catch (Exception ex) {
                System.out.println("Erro ao carregar imagem");
            }

        p2.h.setBounds(10, 10, 500, 400);
        p2.h.getContentPane().setLayout(new GridLayout());
        p2.h.getContentPane().add(p2);
        p2.h.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        p2.bJogar.hide();
        p2.bScore.hide();
        p2.bCheats.hide();
        p2.bSair.hide();
        p2.bMenu.show();
        p2.labelNome.show();
        p2.labelN1.show();
        p2.labelN2.show();
        p2.labelN3.show();
        p2.labelN4.show();
        p2.labelN5.show();
        p2.labelPontos.show();
        p2.labelP1.show();
        p2.labelP2.show();
        p2.labelP3.show();
        p2.labelP4.show();
        p2.labelP5.show();
        p2.label1.show();
        p2.label2.show();
        p2.label3.show();
        p2.label4.show();
        p2.label5.show();
        
        centrarJanela(p2.h);
    }
    
    public void InitHighsc(){
        
        
        
       
    }

    public void iniciarJogo() {

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
