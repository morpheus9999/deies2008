/***********************************************************************
 * Module:  Gui.java
 * Author:  Filipe
 * Purpose: Defines the Class Gui
 ***********************************************************************/

package dei.es2008;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.*;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListModel;

/** @pdOid 91d1597c-405b-464d-92c1-2f6649512fa8 */
public class Gui {
   
   
   /** @pdRoleInfo migr=no name=ControladorDeJogo assc=association2 mult=1..1 */
   public ControladorDeJogo controladorDeJogo;
   
   /**
     * The graphical game component. This component is created on the first call to getComponent().
     */
    private GamePanel component = null;
   
   public Mundo mundo;
   
   
   public void Gui(){
        
        //Meter a seguir qualquer coisa para iniciar
        iniciarJogo();

   }
   /** @pdOid fd504a66-e0c1-43d1-9111-0b1ed1c688cb */
   public void actualizaMundo() {
      // TODO: implement
   }
   
   /** @pdOid c85ed12d-5f24-4a11-ba8a-dac8f3df35ed */
   public int actualizaEstadoPeca() {
      // TODO: implement
      return 0;
   }
   
   /** @pdOid 56a6d77d-2453-48cb-8068-3cf006fbf286 */
   public void iniciarJogo() {
       new ControladorDeJogo();
      // TODO: implement
       
   }
   
   /** @pdOid b4bd1c29-0751-4022-9fcd-27928c023a63 */
   public void sairJogo() {
      // TODO: implement
   }
   
   /** @pdOid 3e250627-0c1f-49b1-abbb-141af507973a */
   public void verRanking() {
      // TODO: implement
   }
   
   /** @pdOid dcd1dd19-3b77-45a2-85de-169cbb5dc105 */
   public void gravarJogo() {
      // TODO: implement
   }
   
   /** @pdOid 5a55ec66-7e7d-490c-b8a5-e226177277af */
   public void carregarJogo() {
      // TODO: implement
   }
   
   /** @pdOid 58082346-d6b2-4ec1-8f20-6b5f7c85d0cd */
   public void inserirCodigo() {
      // TODO: implement
   }
   
   /** @pdOid bfcc7152-43d6-4caa-ba85-234bd21665c0 */
   public void pausarJogo() {
      // TODO: implement
   }
   
   
   /**
     * Handles a timer event. This will normally move the figure down
     * one step, but when a figure has landed or isn't ready other 
     * events will be launched. This method is synchronized to avoid 
     * race conditions with other asynchronous events (keyboard and mouse).
     */
    private synchronized void handleTimer() {
        if (figure == null) {
            handleFigureStart();
        } else if (figure.hasLanded()) {
            handleFigureLanded();
        } else {
            figure.moveDown();
        }
    }
   /**
     * Handles a button press event. This will launch different events
     * depending on the state of the game, as the button semantics
     * change as the game changes. This method is synchronized to 
     * avoid race conditions with other asynchronous events (timer and keyboard).
     */
    private synchronized void handleButtonPressed() {
        if (nextFigure == null) {
            handleStart();
        } else if (thread.isPaused()) {
            handleResume();
        } else {
            handlePause();
        }
    }
   
   
   /**
     * Handles a keyboard event. This will result in different actions
     * being taken, depending on the key pressed. In some cases, other
     * events will be launched. This method is synchronized to avoid 
     * race conditions with other asynchronous events (timer and mouse).
     * 
     * @param e         the key event
     */
    private synchronized void handleKeyEvent(KeyEvent e) {

        // Handle start, pause and resume
        if (e.getKeyCode() == KeyEvent.VK_P) {
            handleButtonPressed();
            return;
        }

        // Don't proceed if stopped or paused
        if (figure == null || moveLock || thread.isPaused()) {
            return;
        }

        // Handle remaining key events
        switch (e.getKeyCode()) {

        case KeyEvent.VK_LEFT:
            figure.moveLeft();
            break;

        case KeyEvent.VK_RIGHT:
            figure.moveRight();
            break;

        case KeyEvent.VK_SPACE:
            figure.moveAllWayDown();
            moveLock = true;
            break;

        case KeyEvent.VK_UP:
			figure.rotateClockwise();
			break;

        case KeyEvent.VK_DOWN:
			figure.rotateCounterClockwise();
            break;

        case KeyEvent.VK_S:
            if (level < 9) {
                level++;
                handleLevelModification();
            }
            break;

        case KeyEvent.VK_N:
            showPreview = !showPreview;
            if (showPreview && figure != nextFigure) {
                nextFigure.attach(previewBoard, true);
                nextFigure.detach(); 
            } else {
                previewBoard.clear();
            }
            break;
        }
    }
   
   
   
   /**
     * A game panel component. Contains all the game components.
     */
    private class GamePanel extends JPanel {
        
        /**
         * The component size. If the component has been resized, that 
         * will be detected when the paint method executes. If this 
         * value is set to null, the component dimensions are unknown.
         */
        private Dimension  size = null;

        /**
         * The score label.
         */
        private JLabel scoreLabel = new JLabel("Score: 0");

        /**
         * The high score label.
         */
        private JLabel highScoreLabel = new JLabel("High Scores");

        /**
         * The list of high scores.
         */
        private JList highScoreList;

        /**
         * The level label.
         */
        private JLabel levelLabel = new JLabel("Level: 1");

        /**
         * The generic button.
         */
        private JButton button = new JButton("Start");

        /**
         * Creates a new game panel. All the components will also be added to the panel.
         */
        public GamePanel (ListModel lm) {
            super();
			highScoreList = new JList(lm);
            initComponents();
        }

        /**
         * Paints the game component. This method is overridden from 
         * the default implementation in order to set the correct background color.
         * 
         * @param g     the graphics context to use
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

        /**
         * Initializes all the components, and places them in the panel.
         */
        private void initComponents() {
            GridBagConstraints  c;

            // Set layout manager and background
            setLayout(new GridBagLayout());
            setBackground(Color.white);

            // Add game board
            c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = 0;
            c.gridheight = 6;
            c.weightx = 1.0;
            c.weighty = 1.0;
            c.fill = GridBagConstraints.BOTH;
            this.add(board.getComponent(), c);

            // Add next figure board
            c = new GridBagConstraints();
            c.gridx = 1;
            c.gridy = 0;
            c.weightx = 0.2;
            c.weighty = 0.18;
            c.fill = GridBagConstraints.BOTH;
            c.insets = new Insets(15, 15, 0, 15);
            this.add(previewBoard.getComponent(), c);

            // Add score label
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

            // Add level label
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

            // Add generic button
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

            // Add high score label
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

            // Add high score list
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

            // Add event handling            
            enableEvents(KeyEvent.KEY_EVENT_MASK);
            this.addKeyListener(new KeyAdapter() {
                public void keyPressed(KeyEvent e) {
                    handleKeyEvent(e);
                }
            });
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    handleButtonPressed();
                    component.requestFocus();
                }
            });
        }
        
        /**
         * Resizes all the static components, and invalidates the current layout.
         */
        private void resizeComponents() {
            Dimension  size = scoreLabel.getSize();
            Font       font;
            int        unitSize;
            
            // Calculate the unit size
            size = board.getComponent().getSize();
            size.width /= board.getBoardWidth();
            size.height /= board.getBoardHeight();
            if (size.width > size.height) {
                unitSize = size.height;
            } else {
                unitSize = size.width;
            }

            // Adjust font sizes
            font = new Font("SansSerif", Font.BOLD, 3 + (int) (unitSize / 1.8));
            scoreLabel.setFont(font);
            highScoreLabel.setFont(font);
            levelLabel.setFont(font);
            font = new Font("SansSerif", Font.PLAIN, 2 + unitSize / 2);
            button.setFont(font);
            
            // Invalidate layout
            scoreLabel.invalidate();
            highScoreLabel.invalidate();
            levelLabel.invalidate();
            button.invalidate();
        }
    }

}