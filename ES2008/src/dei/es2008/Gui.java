package dei.es2008;

import java.applet.AppletContext;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.prefs.Preferences;
import javax.swing.*;



    /**
     * Painel de componentes do jogo.
     */
class Gui extends JPanel {
        private ControladorDeJogo controlo;
        private Dimension  size = null;
        public JLabel scoreLabel = new JLabel("Pontuação: 0");
        private JLabel highScoreLabel = new JLabel("");
        private JList highScoreList;
        public JLabel levelLabel = new JLabel("Nível: 1");
        public JButton button = new JButton("Iniciar");
        
        public Gui (ListModel lm,ControladorDeJogo controlo) {
            super();
			highScoreList = new JList(lm);
                        this.controlo=controlo;
            initComponents();
        }

        /**
         * Metodo que pinta as componentes do jogo.
         */
        public void paintComponent (Graphics g) {
            Rectangle rect = g.getClipBounds();

            if (size == null || !size.equals(getSize())) {
                size = getSize();
                resizeComponents();
            }
            g.setColor(getBackground());
            g.fillRect(rect.x, rect.y, rect.width, rect.height);
        }

        private void initComponents() {
            GridBagConstraints  c;

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
                    controlo.handleKeyEvent(e);
                }
            });
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    controlo.handleButtonPressed();
                    controlo.componente.requestFocus();
                }
            });
        }
        
        /**
         * Redefine as componentes estaticas e invalida o layout actual.
         */
        private void resizeComponents() {
            Dimension  size = scoreLabel.getSize();
            Font       font;
            int        unitSize;
            
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

