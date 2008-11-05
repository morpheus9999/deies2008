public class Gui {

    
    private Mundo board = null;
    private Mundo previewBoard = new Mundo(5, 5);

    private Peca[] figures = {
        new Peca(Peca.QUADRADO),
        new Peca(Peca.LINHA),
        new Peca(Peca.S),
        new Peca(Peca.S_INVERTIDO),
        new Peca(Peca.L),
        new Peca(Peca.L_INVERTIDO),
        new Peca(Peca.T)
    };

    private Preferences prefRoot = null;
    private AppletContext appContext = null;
    private GamePanel componente = null;
    private GameThread thread = null;
    private int nivel = 1;
    private int pontuacao = 0;
    private Ranking ranking;
    private Peca peca = null;
    private Peca pecaSeguinte = null;
    private int rotacaoSeguinte = 0;
    private boolean mostraPrevisaoPeca = true;
    private boolean bloqueiaMovimento = false;
    private boolean isApplet;

    /**
     * Cria um novo jogo de Tetris. O tabuleiro tera por defaultum tamanho de 10x20.
     */
    public Gui (boolean isApplet) {
        this(12, 25);
		this.isApplet = isApplet;

		ranking = new Ranking();

		if (isApplet) {
			try {
				InputStream is = appContext.getStream("Ranking");
				BufferedReader br = new BufferedReader(new InputStreamReader(is));
				ranking.setSerializedPontuacoes(br.readLine());
				br.close();
				is.close();
			} catch (Exception ex) {
				System.err.println("Impossivel ler o ranking: " + ex.getMessage());
			}
		} else {
			prefRoot = Preferences.userNodeForPackage(Gui.class);
			String hs = prefRoot.get("", null);
			if (hs != null)
				ranking.setSerializedPontuacoes(hs);
			mostraPrevisaoPeca = prefRoot.getBoolean("ver peça seguinte", true);
		}
    }

    public Gui (int width, int height) {
        board = new Mundo(width, height);
        board.setMensagem("Iniciar");
        thread = new GameThread();
    }

	public void setAppletContext (AppletContext ac) {
		appContext = ac;
	}

    /**
     * mata as threads que estao a correr no jogo e faz a limpeza.
     */
    public void sair() {
        thread = null;
    }

    public Component getComponente() {
        if (componente == null) {
            componente = new GamePanel(ranking);
        }
        return componente;
    }

    /**
     * Inicializa as variaveis do jogo antes de iniar um jogo
     */
    private void handleStart() {

        nivel = 1;
        pontuacao = 0;
        peca = null;
        pecaSeguinte = randomFigure();
        pecaSeguinte.rotacaoRandom();
        rotacaoSeguinte = pecaSeguinte.getRotacao();  

        board.setMensagem(null);
        board.limpa();
        previewBoard.limpa();
        handleLevelModification();
        handleScoreModification();
        componente.button.setLabel("Pausa");

        thread.reset();
    }

    /**
     * Para a thread do jogo, faz reset as pecas e escreve uma mensagem de fim de jogo.
     */
    private void handleGameOver() {

        thread.setPaused(true);
        
        if (peca != null) {
            peca.detach();
        }
        peca = null;
        if (pecaSeguinte != null) {
            pecaSeguinte.detach();
        }
        pecaSeguinte = null;

        board.setMensagem("Game Over");
        componente.button.setLabel("Iniciar");

		handleHighScoreModification();

		if (! isApplet) try {
			prefRoot.putBoolean("ver peça seguinte", mostraPrevisaoPeca);
			prefRoot.flush();
		} catch (Exception ex) { }
    }

    /**
     * Faz pausa no jogo e escreve uma mensagem de pausa no ecra.
     */
    private void handlePause() {
        thread.setPaused(true);
        
        board.setMensagem("Pausado");
        componente.button.setLabel("Retomar");
    }

    /**
     * retoma o jogo depois de uma pausa. repoe a thread e remove a mensagem de pausa do ecra.
     */
    private void handleResume() {
        board.setMensagem(null);
        componente.button.setLabel("Pausar");
        thread.setPaused(false);
    }

    /**
     * Modifica o nivel e ajusta a velocidade da thread.
     */
    private void handleLevelModification() {
        componente.levelLabel.setText("Nível: " + nivel);
        thread.adjustSpeed();
    }
    
    /**
     * modifica a label da pontuacao.
     */
    private void handleScoreModification() {
        componente.scoreLabel.setText("Pontuação: " + pontuacao);
    }
 
    /**
     * Modifica o ranking se necessari
     */
    private void handleHighScoreModification() {
		if (ranking.inserePontuacao(pontuacao)) {
			String hs = ranking.getSerializedPontuacoes();
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
     * Metodo que move a "peca seguinte" para a posicao da "peca actual" e 
     * cria uma nova "peca seguinte".
     */
    private void handleFigureStart() {
        int  rotation;

        peca = pecaSeguinte;
        bloqueiaMovimento = false;
        rotation = rotacaoSeguinte;
        pecaSeguinte = randomFigure();
        pecaSeguinte.rotacaoRandom(); 
        rotacaoSeguinte = pecaSeguinte.getRotacao(); 

        if (mostraPrevisaoPeca) {
            previewBoard.limpa(); 
            pecaSeguinte.attach(previewBoard, true);
            pecaSeguinte.detach();
        }

        peca.setRotacao(rotation);
        if (!peca.attach(board, false)) {
            previewBoard.limpa();
            peca.attach(previewBoard, true);
            peca.detach();
            handleGameOver();
        }
    }

    /**
     * Verifica se uma peca esta totalmente visivel, se nao estiver lanca o evento
     * de "game over".
     */

    private void handleFigureLanded() {


        if (peca.isAllVisible()) {
			if (peca.numRowsFallen() > 0)
				pontuacao += nivel * 10;
            pontuacao += nivel * peca.numRowsFallen();
        } else {
            handleGameOver();
            return;
        }
        peca.detach();
        peca = null;
		if (!mostraPrevisaoPeca)
			pontuacao += nivel;
		int fullLines = board.getLinhasCompletas();
        if (fullLines > 0) {
            board.removeLinhasCompletas();
			switch (fullLines) {
				case 1:
					pontuacao += nivel * 10;
					break;
				case 2:
					pontuacao += nivel * 30;
					break;
				case 3:
					pontuacao += nivel * 60;
					break;
				case 4:
					pontuacao += nivel * 100;
					break;
			}
            if (nivel < 9 && board.getLinhasRemovidas() / 20 > nivel) {
                nivel = board.getLinhasRemovidas() / 20;
                handleLevelModification();
            }
        } else {
            handleFigureStart();
        }

		handleScoreModification();
    }

    /**
     * Metodo que faz descer a peca no tabuleiro ao longo do tempo.
     * Verifica a cada instante se a peca assentou no tabuleiro.
     */
    private synchronized void handleTimer() {
        if (peca == null) {
            handleFigureStart();
        } else if (peca.hasLanded()) {
            handleFigureLanded();
        } else {
            peca.deslocarPeca(0);
        }
    }

    private synchronized void handleButtonPressed() {
        if (pecaSeguinte == null) {
            handleStart();
        } else if (thread.isPaused()) {
            handleResume();
        } else {
            handlePause();
        }
    }

    private synchronized void handleKeyEvent(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_P) {
            handleButtonPressed();
            return;
        }

        if (peca == null || bloqueiaMovimento || thread.isPaused()) {
            return;
        }

        switch (e.getKeyCode()) {

        case KeyEvent.VK_LEFT:
            peca.deslocarPeca(-1);
            break;

        case KeyEvent.VK_RIGHT:
            peca.deslocarPeca(1);
            break;
            
        case KeyEvent.VK_SPACE:
            peca.rotacao();
            break;

        case KeyEvent.VK_UP:
			peca.rotacao();
			break;

        case KeyEvent.VK_DOWN:
			peca.deslocarPeca(0);
            break;

        case KeyEvent.VK_S:
            if (nivel < 9) {
                nivel++;
                handleLevelModification();
            }
            break;

        case KeyEvent.VK_N:
            mostraPrevisaoPeca = !mostraPrevisaoPeca;
            if (mostraPrevisaoPeca && peca != pecaSeguinte) {
                pecaSeguinte.attach(previewBoard, true);
                pecaSeguinte.detach(); 
            } else {
                previewBoard.limpa();
            }
            break;
        }
    }

    /**
     * Devolve uma peca criada aleatoriamente
     */
    private Peca randomFigure() {
        return figures[(int) (Math.random() * figures.length)];
    }


    /**
     * A thread de tempo de jogo
     */
    private class GameThread extends Thread {

 
        private boolean paused = true;
        private int sleepTime = 500;
        public GameThread() {
        }

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
            sleepTime = 4500 / (nivel + 5) - 250;
            if (sleepTime < 50) {
                sleepTime = 50;
            }
        }

        public void run() {
            while (thread == this) {
                handleTimer();

                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException ignore) {
                }

                while (paused && thread == this) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ignore) {
                    }
                }
            }
        }
    }