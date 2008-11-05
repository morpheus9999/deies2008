package dei.es2008;

import java.applet.AppletContext;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.prefs.Preferences;
import javax.swing.*;


public class Gui {

    /**
     * Cria objecto da class Mundo para uso
     */
    private Mundo board = null;

    /**
     * The preview square board. This board is used to display a preview of the figures.
     */
    private Mundo previewBoard = new Mundo(5, 5);

    /**
     * Cria as objectos da class Peca que podem ser reutilizados
     */
    private Peca[] figures = {
        new Peca(Peca.QUADRADO),
        new Peca(Peca.LINHA),
        new Peca(Peca.S),
        new Peca(Peca.S_INVERTIDO),
        new Peca(Peca.L),
        new Peca(Peca.L_INVERTIDO),
        new Peca(Peca.T)
    };

	/**
	 * Used for storing the high score in application mode.
	 */
	private Preferences prefRoot = null;

	/**
	 * Used for storing the high score in applet mode.
	 */
	private AppletContext appContext = null;

    /**
     * The graphical game component. This component is created on the first call to getComponent().
     */
    private GamePanel component = null;

    /**
     * The thread that runs the game. When this variable is set to 
     * null, the game thread will terminate.
     */
    private GameThread thread = null;

    /**
     * The game level. The level will be increased for every 20 lines removed from the square board.
     */
    private int level = 1;

    /**
     * The current score. The score is increased for every figure that
     * is possible to place on the main board.
     */
    private int score = 0;

    /**
     * The current high score list. This is saved between application runs.
     */
    private Ranking highScore;

    /**
     * The current figure. The figure will be updated when 
     */
    private Peca figure = null;

    /**
     * The next figure.
     */
    private Peca nextFigure = null;
    
    /**
     * The rotation of the next figure.
     */
    private int nextRotation = 0;

    /**
     * The figure preview flag. If this flag is set, the figure
     * will be shown in the figure preview board.
     */
    private boolean showPreview = true;

    /**
     * The move lock flag. If this flag is set, the current figure
     * cannot be moved. This flag is set when a figure is moved all 
     * the way down, and reset when a new figure is displayed.
     */
    private boolean moveLock = false;

    /**
     * Whether or not the code runs as an applet.
	 * As an applet, it can't use java.util.prefs to store the high score.
     */
    private boolean isApplet;

    /**
     * Creates a new Tetris game. The square board will be given the default size of 10x20.
     */
    public Gui (boolean isApplet) {
        this(12, 25);
		this.isApplet = isApplet;

		highScore = new Ranking();

		if (isApplet) {
			try {
				InputStream is = appContext.getStream("Ranking");
				BufferedReader br = new BufferedReader(new InputStreamReader(is));
				highScore.setSerializedPontuacoes(br.readLine());
				br.close();
				is.close();
			} catch (Exception ex) {
				System.err.println("can't load high score: " + ex.getMessage());
			}
		} else {
			prefRoot = Preferences.userNodeForPackage(Gui.class);
			String hs = prefRoot.get("", null);
			if (hs != null)
				highScore.setSerializedPontuacoes(hs);
			showPreview = prefRoot.getBoolean("ver peça seguinte", true);
		}
    }

    /**
     * Creates a new Tetris game. The square board will be given the specified size.
     *
     * @param width     the width of the square board (in positions)
     * @param height    the height of the square board (in positions)
     */
    public Gui (int width, int height) {
        board = new Mundo(width, height);
        board.setMensagem("Iniciar");
        thread = new GameThread();
    }

	/**
	 * For pasing the AppletContext when running in applet mode.
	 */
	public void setAppletContext (AppletContext ac) {
		appContext = ac;
	}

    /**
     * Kills the game running thread and makes necessary clean-up.
     * After calling this method, no further methods in this class
     * should be called. Neither should the component returned earlier be trusted upon.
     */
    public void quit() {
        thread = null;
    }

    /**
     * Returns a new component that draws the game.
     * 
     * @return the component that draws the game
     */
    public Component getComponent() {
        if (component == null) {
            component = new GamePanel(highScore);
        }
        return component;
    }

    /**
     * Handles a game start event. Both the main and preview square
     * boards will be reset, and all other game parameters will be
     * reset. Finally the game thread will be launched.
     */
    private void handleStart() {

        // Reset score and figures
        level = 1;
        score = 0;
        figure = null;
        nextFigure = randomFigure();
        nextFigure.rotacaoRandom();
        nextRotation = nextFigure.getRotation();  

        // Reset components
        board.setMensagem(null);
        board.limpa();
        previewBoard.limpa();
        handleLevelModification();
        handleScoreModification();
        component.button.setLabel("Pausa");

        // Start game thread
        thread.reset();
    }

    /**
     * Handles a game over event. This will stop the game thread,
     * reset all figures and print a game over message.
     */
    private void handleGameOver() {

        // Stop game thred
        thread.setPaused(true);

        // Reset figures
        if (figure != null) {
            figure.detach();
        }
        figure = null;
        if (nextFigure != null) {
            nextFigure.detach();
        }
        nextFigure = null;

        // Handle components
        board.setMensagem("Game Over");
        component.button.setLabel("Iniciar");

		handleHighScoreModification();

		if (! isApplet) try {
			prefRoot.putBoolean("ver peça seguinte", showPreview);
			prefRoot.flush();
		} catch (Exception ex) { }
    }

    /**
     * Handles a game pause event. This will pause the game thread and
     * print a pause message on the game board.
     */
    private void handlePause() {
        thread.setPaused(true);
        
        board.setMensagem("Pausado");
        component.button.setLabel("Retomar");
    }

    /**
     * Handles a game resume event. This will resume the game thread 
     * and remove any messages on the game board.
     */
    private void handleResume() {
        board.setMensagem(null);
        component.button.setLabel("Pausar");
        thread.setPaused(false);
    }

    /**
     * Handles a level modification event. This will modify the level
     * label and adjust the thread speed.
     */
    private void handleLevelModification() {
        component.levelLabel.setText("Nível: " + level);
        thread.adjustSpeed();
    }
    
    /**
     * Handle a score modification event. This will modify the score label.
     */
    private void handleScoreModification() {
        component.scoreLabel.setText("Pontuação: " + score);
    }
 
    /**
     * Handle a high score modification event. This will modify the high score list if necessary.
     */
    private void handleHighScoreModification() {
		if (highScore.inserePontuacao(score)) {
			String hs = highScore.getSerializedPontuacoes();
			try {
				if (isApplet) {
					InputStream bais = new ByteArrayInputStream(hs.getBytes());
					appContext.setStream("", bais);
				} else {
					prefRoot.put("", hs);
					prefRoot.flush();
				}
			} catch (Exception ex) {
				System.err.println("Impossível guardar a pontuação: "+ex.getMessage());
			}
		}
    }


    /**
     * Handles a figure start event. This will move the next figure
     * to the current figure position, while also creating a new 
     * preview figure. If the figure cannot be introduced onto the
     * game board, a game over event will be launched.
     */
    private void handleFigureStart() {
        int  rotation;

        // Move next figure to current
        figure = nextFigure;
        moveLock = false;
        rotation = nextRotation;
        nextFigure = randomFigure();
        nextFigure.rotacaoRandom(); 
        nextRotation = nextFigure.getRotation(); 

        // Handle figure preview
        if (showPreview) {
            previewBoard.limpa(); 
            nextFigure.attach(previewBoard, true);
            nextFigure.detach();
        }

        // Attach figure to game board
        figure.setRotacao(rotation);
        if (!figure.attach(board, false)) {
            previewBoard.limpa();
            figure.attach(previewBoard, true);
            figure.detach();
            handleGameOver();
        }
    }

    /**
     * Handles a figure landed event. This will check that the figure
     * is completely visible, or a game over event will be launched.
     * After this control, any full lines will be removed. If no full
     * lines could be removed, a figure start event is launched directly.
     */

    private void handleFigureLanded() {

        // Check and detach figure
        if (figure.isAllVisible()) {
			// 10 points per level if a figure has been dropped
			if (figure.numRowsFallen() > 0)
				score += level * 10;
			// ... and some extra points for each row of height
            score += level * figure.numRowsFallen();
        } else {
            handleGameOver();
            return;
        }
        figure.detach();
        figure = null;

		// a few extra points if the preview is turned off
		if (!showPreview)
			score += level;

        // Check for full lines or create new figure
		int fullLines = board.getLinhasCompletas();
        if (fullLines > 0) {
            board.removeLinhasCompletas();
			// adjust score: removing a full lines nets 10 points for a single line,
			// 20 more for the second, 30 more for the third, and 40 more for the fourth
			switch (fullLines) {
				case 1:
					score += level * 10;
					break;
				case 2:
					score += level * 30;
					break;
				case 3:
					score += level * 60;
					break;
				case 4:
					score += level * 100;
					break;
			}
            if (level < 9 && board.getLinhasRemovidas() / 20 > level) {
                level = board.getLinhasRemovidas() / 20;
                handleLevelModification();
            }
        } else {
            handleFigureStart();
        }

		handleScoreModification();
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
            figure.deslocarPeca(0);
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
            figure.deslocarPeca(-1);
            break;

        case KeyEvent.VK_RIGHT:
            figure.deslocarPeca(1);
            break;
            
        case KeyEvent.VK_SPACE:
            figure.rotacao();
            //figure.moveAllWayDown();
            //moveLock = true;
            break;

        case KeyEvent.VK_UP:
			figure.rotacao();
			break;

        case KeyEvent.VK_DOWN:
			figure.deslocarPeca(0);
                        //figure.rotateCounterClockwise();
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
                previewBoard.limpa();
            }
            break;
        }
    }

    /**
     * Returns a random figure. The figures come from the figures
     * array, and will not be initialized.
     * 
     * @return a random figure
     */
    private Peca randomFigure() {
        return figures[(int) (Math.random() * figures.length)];
    }


    /**
     * The game time thread. This thread makes sure that the timer
     * events are launched appropriately, making the current figure 
     * fall. This thread can be reused across games, but should be set
     * to paused state when no game is running.
     */
    private class GameThread extends Thread {

        /**
         * The game pause flag. This flag is set to true while the game should pause.
         */
        private boolean paused = true;

        /**
         * The number of milliseconds to sleep before each automatic
         * move. This number will be lowered as the game progresses.
         */
        private int sleepTime = 500;

        /**
         * Creates a new game thread with default values.
         */
        public GameThread() {
        }

        /**
         * Resets the game thread. This will adjust the speed and 
         * start the game thread if not previously started.
         */
        public void reset() {
            adjustSpeed();
            setPaused(false);
            if (!isAlive()) {
                this.start();
            }
        }

        
        public boolean isPaused() {
            return paused;
        }

        public void setPaused(boolean paused) {
            this.paused = paused;
        }

        
        public void adjustSpeed() {
            sleepTime = 4500 / (level + 5) - 250;
            if (sleepTime < 50) {
                sleepTime = 50;
            }
        }

        /**
         * Runs the game.
         */
        public void run() {
            while (thread == this) {
                // Make the time step
                handleTimer();

                // Sleep for some time
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException ignore) {
                    // Do nothing
                }

                // Sleep if paused
                while (paused && thread == this) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ignore) {
                        // Do nothing
                    }
                }
            }
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
        private JLabel scoreLabel = new JLabel("Pontuação: 0");

        /**
         * The high score label.
         */
        private JLabel highScoreLabel = new JLabel("");

        /**
         * The list of high scores.
         */
        private JList highScoreList;

        /**
         * The level label.
         */
        private JLabel levelLabel = new JLabel("Nível: 1");

        /**
         * The generic button.
         */
        private JButton button = new JButton("Iniciar");

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
            this.add(board.getComponente(), c);

            // Add next figure board
            c = new GridBagConstraints();
            c.gridx = 1;
            c.gridy = 0;
            c.weightx = 0.2;
            c.weighty = 0.18;
            c.fill = GridBagConstraints.BOTH;
            c.insets = new Insets(15, 15, 0, 15);
            this.add(previewBoard.getComponente(), c);

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
            size = board.getComponente().getSize();
            size.width /= board.getLarguraTabuleiro();
            size.height /= board.getAlturaTabuleiro();
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
