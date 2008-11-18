package dei.es2008;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.awt.image.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.*;
import java.awt.event.*;

/**
 * Painel de componentes do jogo.
 */
class Gui extends JPanel {

    private ControladorDeJogo controlo;
    private Dimension size = null;
    public JLabel scoreLabel = new JLabel("Pontos: 0");
    private JLabel highScoreLabel = new JLabel("");
    private JList highScoreList;
    public JLabel levelLabel = new JLabel("Nível: 1");
    public JButton button = new JButton("Iniciar");

    public Gui(ListModel lm, ControladorDeJogo controlo) {
        super();
        highScoreList = new JList(lm);
        this.controlo = controlo;
        initComponents();
    }

    /**
     * Metodo que pinta as componentes do jogo.
     */
    public void paintComponent(Graphics g) {
        Rectangle rect = g.getClipBounds();

        if (size == null || !size.equals(getSize())) {
            size = getSize();
            redimensionaComponentes();
        }
        g.setColor(getBackground());
        g.fillRect(rect.x, rect.y, rect.width, rect.height);
    }

    private void initComponents() {

        GridBagConstraints c;

        setLayout(new GridBagLayout());
        setBackground(Color.white);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.gridheight = 6;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.fill = GridBagConstraints.BOTH;
        this.add(controlo.board.getComponente(), c);

        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 0.2;
        c.weighty = 0.18;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(15, 15, 0, 15);
        this.add(controlo.previewBoard.getComponente(), c);

        scoreLabel.setBackground(Color.white);
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 1;
        c.weightx = 0.3;
        c.weighty = 0.05;
        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(0, 15, 0, 15);
        this.add(scoreLabel, c);

        levelLabel.setBackground(Color.white);
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 2;
        c.weightx = 0.3;
        c.weighty = 0.05;
        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(0, 15, 0, 15);
        this.add(levelLabel, c);

        button.setBackground(Color.white);
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 3;
        c.weightx = 0.3;
        c.weighty = 0.5;
        c.anchor = GridBagConstraints.NORTH;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(15, 15, 15, 15);
        this.add(button, c);

        highScoreLabel.setBackground(Color.white);
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 4;
        c.weightx = 0.3;
        c.weighty = 0.05;
        c.anchor = GridBagConstraints.NORTH;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(0, 15, 0, 15);
        this.add(highScoreLabel, c);

        highScoreList.setBackground(Color.white);
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 5;
        c.weightx = 0.3;
        c.weighty = 1.0;
        c.anchor = GridBagConstraints.NORTH;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(0, 15, 0, 15);
        this.add(highScoreList, c);

        enableEvents(KeyEvent.KEY_EVENT_MASK);
        this.addKeyListener(new KeyAdapter() {

            public void keyPressed(KeyEvent e) {
                controlo.trataTeclaPremida(e);
            }
            });
        button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                controlo.trataBotao();
                controlo.componente.requestFocus();
            }
            });
    }

    /**
     * Redefine as componentes estaticas e invalida o layout actual.
     */
    private void redimensionaComponentes() {
        Dimension size = scoreLabel.getSize();
        Font font;
        int unitSize;

        size = controlo.board.getComponente().getSize();
        size.width /= controlo.board.getLarguraTabuleiro();
        size.height /= controlo.board.getAlturaTabuleiro();
        if (size.width > size.height) {
            unitSize = size.height;
        } else {
            unitSize = size.width;
        }

        font = new Font("SansSerif", Font.BOLD, 3 + (int) (unitSize / 1.8));
        scoreLabel.setFont(font);
        highScoreLabel.setFont(font);
        levelLabel.setFont(font);
        font = new Font("SansSerif", Font.PLAIN, 2 + unitSize / 2);
        button.setFont(font);

        scoreLabel.invalidate();
        highScoreLabel.invalidate();
        levelLabel.invalidate();
        button.invalidate();
    }
    }

class Menu extends JPanel {

    Image bgImage = null;
    //criacao dos butoes
    JButton bJogar = new JButton("Jogar");
    JButton bScore = new JButton("Highscores");
    JButton bCheats = new JButton("Xites");
    JButton bSair = new JButton("Sair");
    JButton bMenu = new JButton("Menu");
    JLabel label1 = new JLabel("texto");
    JLabel label2 = new JLabel("texto");
    JLabel label3 = new JLabel("texto");
    JLabel label4 = new JLabel("texto");
    JLabel label5 = new JLabel("texto");
    JLabel labelNome = new JLabel("texto");
    JLabel labelN1 = new JLabel("texto");
    JLabel labelN2 = new JLabel("texto");
    JLabel labelN3 = new JLabel("texto");
    JLabel labelN4 = new JLabel("texto");
    JLabel labelN5 = new JLabel("texto");
    JLabel labelPontos = new JLabel("texto");
    JLabel labelP1 = new JLabel("texto");
    JLabel labelP2 = new JLabel("texto");
    JLabel labelP3 = new JLabel("texto");
    JLabel labelP4 = new JLabel("texto");
    JLabel labelP5 = new JLabel("texto");
    //criacao dos icones
    Icon v1 = new ImageIcon("v1.gif");
    Icon v2 = new ImageIcon("v2.gif");
    Icon a1 = new ImageIcon("a1.gif");
    Icon a2 = new ImageIcon("a2.gif");
    Icon b1 = new ImageIcon("b1.gif");
    Icon b2 = new ImageIcon("b2.gif");
    Icon c1 = new ImageIcon("c1.gif");
    Icon c2 = new ImageIcon("c2.gif");
    static JFrame f = new JFrame("Tetris Jojo e gay");
    static JFrame h = new JFrame("Highscores");
    //variaveis dos nomes para os highscores
    String n1 = "---//---";
    String n2 = "---//---";
    String n3 = "---//---";
    String n4 = "---//---";
    String n5 = "---//---";
    //variaveis dos pontos para os highscores
    int p1 = 0;
    int p2 = 0;
    int p3 = 0;
    int p4 = 0;
    int p5 = 0;
    String message = "";
    private Arranque pai;

    public Menu(final Arranque pai) {
        super(null);
        

        this.pai = pai;
        try {
            //define os limites dos butoes i mete os icones em cada butao
            getFileImage("tetris.jpg");
            bJogar.setBounds(175, 150, 150, 40);
            add(bJogar);
            bJogar.setIcon(a1);
            bScore.setBounds(175, 200, 150, 40);
            add(bScore);
            bScore.setIcon(b1);
            bCheats.setBounds(175, 250, 150, 40);
            add(bCheats);
            bCheats.setIcon(c1);
            bSair.setBounds(175, 300, 150, 40);
            add(bSair);
            bSair.setIcon(v1);
            bMenu.setBounds(175, 300, 150, 40);
            add(bMenu);
            bMenu.setIcon(v1);
            bMenu.hide();

            labelNome.setText("Nome");
            labelNome.setFont(new Font("Serif", Font.BOLD, 20));
            labelNome.setBounds(150, 15, 100, 100);
            add(labelNome);
            labelNome.hide();

            //nomes dos Highscorers
            labelN1.setText(n1);
            labelN1.setFont(new Font("Serif", Font.PLAIN, 20));
            labelN1.setBounds(150, 70, 100, 100);
            add(labelN1);
            labelN1.hide();

            labelN2.setText(n2);
            labelN2.setFont(new Font("Serif", Font.PLAIN, 20));
            labelN2.setBounds(150, 100, 100, 100);
            add(labelN2);
            labelN2.hide();

            labelN3.setText(n3);
            labelN3.setFont(new Font("Serif", Font.PLAIN, 20));
            labelN3.setBounds(150, 130, 100, 100);
            add(labelN3);
            labelN3.hide();

            labelN4.setText(n4);
            labelN4.setFont(new Font("Serif", Font.PLAIN, 20));
            labelN4.setBounds(150, 160, 100, 100);
            add(labelN4);
            labelN4.hide();

            labelN5.setText(n5);
            labelN5.setFont(new Font("Serif", Font.PLAIN, 20));
            labelN5.setBounds(150, 190, 100, 100);
            add(labelN5);
            labelN5.hide();

            //Pontos      
            labelPontos.setText("Pontos");
            labelPontos.setFont(new Font("Serif", Font.BOLD, 20));
            labelPontos.setBounds(320, 15, 100, 100);
            add(labelPontos);
            labelPontos.hide();

            //Pontos dos jogadores no highscores

            labelP1.setFont(new Font("Serif", Font.PLAIN, 20));
            labelP1.setBounds(320, 70, 100, 100);
            add(labelP1);
            labelP1.hide();

            labelP2.setFont(new Font("Serif", Font.PLAIN, 20));
            labelP2.setBounds(320, 100, 100, 100);
            add(labelP2);
            labelP2.hide();

            labelP3.setFont(new Font("Serif", Font.PLAIN, 20));
            labelP3.setBounds(320, 130, 100, 100);
            add(labelP3);
            labelP3.hide();

            labelP4.setFont(new Font("Serif", Font.PLAIN, 20));
            labelP4.setBounds(320, 160, 100, 100);
            add(labelP4);
            labelP4.hide();

            labelP5.setFont(new Font("Serif", Font.PLAIN, 20));
            labelP5.setBounds(320, 190, 100, 100);
            add(labelP5);
            labelP5.hide();

            //posicoes dos jogadores nos highscores

            label1.setText("1º");
            label1.setFont(new Font("Serif", Font.BOLD, 20));
            label1.setBounds(50, 70, 100, 100);
            add(label1);
            label1.hide();

            label2.setText("2º");
            label2.setFont(new Font("Serif", Font.BOLD, 20));
            label2.setBounds(50, 100, 100, 100);
            add(label2);
            label2.hide();

            label3.setText("3º");
            label3.setFont(new Font("Serif", Font.BOLD, 20));
            label3.setBounds(50, 130, 100, 100);
            add(label3);
            label3.hide();

            label4.setText("4º");
            label4.setFont(new Font("Serif", Font.BOLD, 20));
            label4.setBounds(50, 160, 100, 100);
            add(label4);
            label4.hide();

            label5.setText("5º");
            label5.setFont(new Font("Serif", Font.BOLD, 20));
            label5.setBounds(50, 190, 100, 100);
            add(label5);
            label5.hide();


        } catch (Exception ex) {
            message = "File load failed: " + ex.getMessage();
        }

        //eventos para o jogar
        bJogar.addMouseListener(new MouseAdapter() {

            public void mouseEntered(MouseEvent e) {
                bJogar.setIcon(a2);
            }

            public void mouseExited(MouseEvent e) {
                bJogar.setIcon(a1);
            }

            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    f.setVisible(false);
                    pai.iniciarJogo();
                }
            }
        });

        //eventos para o Highscores
        bScore.addMouseListener(new MouseAdapter() {

            public void mouseEntered(MouseEvent e) {
                bScore.setIcon(b2);
            }

            public void mouseExited(MouseEvent e) {
                bScore.setIcon(b1);
            }

            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    f.setVisible(false);
                    h.setVisible(true);
                    pai.InitHighsc();
                }
            }
        });

        //eventos para o Cheats
        bCheats.addMouseListener(new MouseAdapter() {

            public void mouseEntered(MouseEvent e) {
                bCheats.setIcon(c2);
            }

            public void mouseExited(MouseEvent e) {
                bCheats.setIcon(c1);
            }

            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                }
            }
        });

        //eventos para o Sair
        bSair.addMouseListener(new MouseAdapter() {

            public void mouseEntered(MouseEvent e) {
                bSair.setIcon(v2);
            }

            public void mouseExited(MouseEvent e) {
                bSair.setIcon(v1);
            }

            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    System.exit(0);
                }
            }
        });
        //eventos para o Voltar ao menu
        bMenu.addMouseListener(new MouseAdapter() {

            public void mouseEntered(MouseEvent e) {
                bMenu.setIcon(v2);
            }

            public void mouseExited(MouseEvent e) {
                bMenu.setIcon(v1);
            }

            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    h.setVisible(false);
                    f.setVisible(true);


                }
            }
        });


    }

    public void paintComponent(Graphics g) {
        if (bgImage != null) {
            g.drawImage(bgImage, 0, 0, this);
        } else {
            g.drawString(message, 40, 40);
        }
    }

    public void getFileImage(String filePath) throws InterruptedException, IOException {
        FileInputStream in = new FileInputStream(filePath);
        byte[] b = new byte[in.available()];
        in.read(b);
        in.close();
        bgImage = Toolkit.getDefaultToolkit().createImage(b);
        MediaTracker mt = new MediaTracker(this);
        mt.addImage(bgImage, 0);
        mt.waitForAll();
    }
   
}
