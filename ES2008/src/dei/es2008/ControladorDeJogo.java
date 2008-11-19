package dei.es2008;

import java.applet.AppletContext;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.Vector;

import java.util.prefs.Preferences;

public class ControladorDeJogo {


    public Vector<Jogo> jogo=new Vector();
    public boolean replayJogo=false;

    public Mundo board;
    public int i = 0;
    public int k = 0;
    public Menu menu;
    public Mundo previewBoard = new Mundo(5, 5);
    public Gui componente;

    private Peca[] figures = {
        new Peca(Peca.QUADRADO),
        new Peca(Peca.LINHA),
        new Peca(Peca.S),
        new Peca(Peca.S_INVERTIDO),
        new Peca(Peca.L),
        new Peca(Peca.L_INVERTIDO),
        new Peca(Peca.T)
    };
    private Ficheiros ficheiro;
    private Ranking ranking;
    //public Mundo mundo;


    public Ficheiros ficheiros;
    private Preferences prefRoot;
    private AppletContext appContext;
    private GameThread thread;
    private int nivel = 1;
    private int pontuacao = 0;
    private Peca peca;
    private Peca pecaSeguinte;
    private int rotacaoSeguinte = 0;
    private boolean mostraPrevisaoPeca = true;
    private boolean bloqueiaMovimento = false;
    private boolean isApplet;
    private boolean verifica;



    /**
     * Cria um novo jogo de Tetris. O tabuleiro tera por defaultum tamanho de 10x20.
     */
    public ControladorDeJogo(boolean isApplet, Menu principal) {
        this(12, 25);
        this.isApplet = isApplet;
        menu = principal;
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
            String hs = prefRoot.get("Ranking", null);
            if (hs != null) {
                ranking.setSerializedPontuacoes(hs);
            }
            mostraPrevisaoPeca = prefRoot.getBoolean("ver peça seguinte", true);
        }

    }

    public ControladorDeJogo(int width, int height) {
        board = new Mundo(width, height);
        board.setMensagem("Iniciar");
        thread = new GameThread();


    }

    public void setAppletContext(AppletContext ac) {
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
            componente = new Gui(ranking, this);
        }
        return componente;
    }

    /**
     * Inicializa as variaveis do jogo antes de iniciar um jogo
     */
    public void trataArranque() {
        if(replayJogo==false){
            k=0;
            i=0;
            nivel = 1;
            pontuacao = 0;
            peca = null;
            
            pecaSeguinte = randomFigure();
            pecaSeguinte.rotacaoRandom();
            rotacaoSeguinte = pecaSeguinte.getRotacao();
            jogo.addElement(new Jogo(null,0,pecaSeguinte,rotacaoSeguinte));
            board.setMensagem(null);
            board.limpa();
            previewBoard.limpa();
            trataMudarNivel();
            trataScore();
            componente.button.setLabel("Pausa");

            thread.reset();
        }else{
            i=0;
            k=0;
            nivel= 1;
            pontuacao = 0;
            peca=null;
            pecaSeguinte=jogo.elementAt(i).pecaSeguinte;
            pecaSeguinte.setRotacao(jogo.elementAt(i).rotacaoInicialPecaSeguinte);
            rotacaoSeguinte=pecaSeguinte.getRotacao();
            board.setMensagem(null);
            board.limpa();
            previewBoard.limpa();
            trataMudarNivel();
            trataScore();
            componente.button.setLabel("Pausa");
            
            thread.reset();
        }
    }

    /**
     * Para a thread do jogo, faz reset as pecas e escreve uma mensagem de fim de jogo.
     */
    public void trataFimJogo() {

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
        replayJogo=true;
        System.out.println("TABELA :"+jogo.elementAt(0).tabela.size());
        componente.button.setLabel("Iniciar");

        trataRankingMod();

        if (!isApplet) {
            try {
                prefRoot.putBoolean("ver peÃ§a seguinte", mostraPrevisaoPeca);
                prefRoot.flush();
            } catch (Exception ex) {
            }
        }
    }

    /**
     * Faz pausa no jogo e escreve uma mensagem de pausa no ecra.
     */
    public void trataPausa() {
        thread.setPaused(true);

        board.setMensagem("Pausado");
        componente.button.setLabel("Retomar");
    }

    /**
     * retoma o jogo depois de uma pausa. repoe a thread e remove a mensagem de pausa do ecra.
     */
    public void trataRetomarJogo() {
        board.setMensagem(null);
        componente.button.setLabel("Pausar");
        thread.setPaused(false);
    }

    /**
     * Modifica o nivel e ajusta a velocidade da thread.
     */
    public void trataMudarNivel() {
        componente.levelLabel.setText("Nível: " + nivel);
        thread.adjustSpeed();
    }

    /**
     * modifica a label da pontuacao.
     */
    public void trataScore() {
        componente.scoreLabel.setText("Pontos: " + pontuacao);
    }

    /**
     * Modifica o ranking se necessari
     */
    public void trataRankingMod() {

        String nome = menu.nomeTemp;
        System.out.println("noooooooooome " + nome);
        if (ranking.inserePontuacao(pontuacao, nome)) {
            String hs = ranking.getSerializedPontuacoes();
            try {
                if (isApplet) {
                    InputStream bais = new ByteArrayInputStream(hs.getBytes());
                    appContext.setStream("Ranking", bais);
                } else {
                    prefRoot.put("Ranking", hs);
                    prefRoot.flush();
                }
            } catch (Exception ex) {
                System.err.println("Imposs√≠vel guardar a pontua√ß√£o: " + ex.getMessage());
            }
        }
    }

    /**
     * Metodo que move a "peca seguinte" para a posicao da "peca actual" e 
     * cria uma nova "peca seguinte".
     */
    public void adicionaPeca() {
        int rotation;
        if (replayJogo == false) {
            peca = pecaSeguinte;
            i++;
            k = 0;
            bloqueiaMovimento = false;
            rotation = rotacaoSeguinte;
            pecaSeguinte = randomFigure();
            pecaSeguinte.rotacaoRandom();
            rotacaoSeguinte = pecaSeguinte.getRotacao();
            jogo.addElement(new Jogo(peca, rotation, pecaSeguinte, rotacaoSeguinte));
        if(replayJogo==false){
        peca = pecaSeguinte;
        i++;
        k=0;
        bloqueiaMovimento = false;
        rotation = rotacaoSeguinte;
        pecaSeguinte = randomFigure();
        pecaSeguinte.rotacaoRandom(); 
        rotacaoSeguinte = pecaSeguinte.getRotacao(); 
        jogo.addElement(new Jogo(peca,rotation,pecaSeguinte,rotacaoSeguinte));
        

            if (mostraPrevisaoPeca) {
                previewBoard.limpa();
                pecaSeguinte.fundePeca(previewBoard, true);
                pecaSeguinte.detach();
            }

            peca.setRotacao(rotation);
            if (!peca.fundePeca(board, false)) {
                previewBoard.limpa();
                peca.fundePeca(previewBoard, true);
                peca.detach();
                trataFimJogo();
            }

        } else {

            i++;
            k = 0;
            peca = jogo.elementAt(i).peca;
            bloqueiaMovimento = false;
            rotation = jogo.elementAt(i).rotacaoInicialPeca;
            pecaSeguinte = jogo.elementAt(i).pecaSeguinte;
            pecaSeguinte.setRotacao(jogo.elementAt(i).rotacaoInicialPecaSeguinte);
            rotacaoSeguinte = pecaSeguinte.getRotacao();

            if (mostraPrevisaoPeca) {
                previewBoard.limpa();
                pecaSeguinte.fundePeca(previewBoard, true);
                pecaSeguinte.detach();
            }

            peca.setRotacao(rotation);
            if (!peca.fundePeca(board, false)) {
                previewBoard.limpa();
                peca.fundePeca(previewBoard, true);
                peca.detach();
                trataFimJogo();

            }

        }

        }else{

        i++;
        k=0;
        peca = jogo.elementAt(i).peca;
        bloqueiaMovimento = false;
        rotation = jogo.elementAt(i).rotacaoInicialPeca;
        pecaSeguinte = jogo.elementAt(i).pecaSeguinte;
        pecaSeguinte.setRotacao(jogo.elementAt(i).rotacaoInicialPecaSeguinte);
        rotacaoSeguinte = pecaSeguinte.getRotacao();

        if (mostraPrevisaoPeca) {
            previewBoard.limpa();
            pecaSeguinte.fundePeca(previewBoard, true);
            pecaSeguinte.detach();
        }

        peca.setRotacao(rotation);
        if (!peca.fundePeca(board, false)) {
            previewBoard.limpa();
            peca.fundePeca(previewBoard, true);
            peca.detach();
            trataFimJogo();

        }
        
        }
    }

    /**
     * Verifica se uma peca esta totalmente visivel, se nao estiver lanca o evento
     * de "game over".
     */
    public void trataFigAterrou() {


        if (peca.estaVisivel()) {
            if (peca.numLinhasOff() > 0) {
                pontuacao += nivel * 10;
            }
            pontuacao += nivel * peca.numLinhasOff();
        } else {
            trataFimJogo();
            return;
        }
        peca.detach();
        peca = null;
        if (!mostraPrevisaoPeca) {
            pontuacao += nivel;
        }
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
                trataMudarNivel();
            }
        } else {
            adicionaPeca();
        }

        trataScore();
    }

    /**
     * Metodo que faz descer a peca no tabuleiro ao longo do tempo.
     * Verifica a cada instante se a peca assentou no tabuleiro.
     */
    public synchronized void temporizador() {
        if (peca == null) {
            adicionaPeca();
        } else if (peca.aterrou()) {
            trataFigAterrou();
        } else {
            
                jogo.elementAt(i).guardarInstante( System.currentTimeMillis(),0, nivel);
            k++;
            peca.deslocarPeca(0);
        }
    }

    public synchronized void temporizadorReplay() {
        verifica=true;
        if (peca == null) {
            adicionaPeca();
        }  else {
            
            
        while(verifica==true){
            
            System.out.println("i:"+i+"k:"+k);
            
            if(jogo.elementAt(i).tabela.elementAt(k).tipo!=2){
                try {
                    

                    if(i== 1 && k==0){

                    }else{
                        if(k>0)
                            wait(jogo.elementAt(i).tabela.elementAt(k).temp-jogo.elementAt(i).tabela.elementAt(k-1).temp);
                        else if(k==0){
                            wait(jogo.elementAt(i).tabela.elementAt(k).temp-jogo.elementAt(i-1).tabela.elementAt(jogo.elementAt(i-1).tabela.size()-1).temp);
                        }

                    }
                    //wait(100);
                } catch (InterruptedException ex) {
                    System.out.println("merda");
                }
                peca.deslocarPeca(jogo.elementAt(i).tabela.get(k).tipo);

            }else{
                try {
                    //wait(100);
                    if(i== 1 && k==0){

                    }else{
                        if(k>0)
                            wait(jogo.elementAt(i).tabela.elementAt(k).temp-jogo.elementAt(i).tabela.elementAt(k-1).temp);
                        else if(k==0){
                            wait(jogo.elementAt(i).tabela.elementAt(k).temp-jogo.elementAt(i-1).tabela.elementAt(jogo.elementAt(i-1).tabela.size()-1).temp);
                        }

                    }
                } catch (InterruptedException ex) {
                    System.out.println("merda");
                }
                peca.rotacao();

            }
            k++;
            if(k>jogo.elementAt(i).tabela.size()-1){
                        verifica=false;
                        trataFigAterrou();
                
                k=0;
                }

                }
        }
    }

    public synchronized void trataBotao() {
        if (pecaSeguinte == null) {
            trataArranque();
        } else if (thread.isPaused()) {
            trataRetomarJogo();
        } else {
            trataPausa();
        }

    }

    public synchronized void trataTeclaPremida(KeyEvent e) {

        if(replayJogo==true)
            return;
        if (e.getKeyCode() == KeyEvent.VK_P) {
            trataBotao();
            return;
        }

        if (peca == null || bloqueiaMovimento || thread.isPaused()) {
            return;
        }

        switch (e.getKeyCode()) {

        case KeyEvent.VK_LEFT:
            
                jogo.elementAt(i).guardarInstante( System.currentTimeMillis(),-1, nivel);
            k++;
            peca.deslocarPeca(-1);
            break;

        case KeyEvent.VK_RIGHT:
            
                jogo.elementAt(i).guardarInstante( System.currentTimeMillis(),1, nivel);
            k++;
            peca.deslocarPeca(1);
            break;
            
        case KeyEvent.VK_SPACE:
            
                jogo.elementAt(i).guardarInstante( System.currentTimeMillis(),2, nivel);
            k++;
            peca.rotacao();
            break;

        case KeyEvent.VK_UP:
            
                jogo.elementAt(i).guardarInstante( System.currentTimeMillis(),2, nivel);
            
            k++;
			peca.rotacao();
			break;

        case KeyEvent.VK_DOWN:
            
                jogo.elementAt(i).guardarInstante( System.currentTimeMillis(),0, nivel);
            
                k++;
			peca.deslocarPeca(0);
            break;


            case KeyEvent.VK_S:
                if (nivel < 9) {
                    nivel++;
                    trataMudarNivel();
                }
                break;

            case KeyEvent.VK_N:
                mostraPrevisaoPeca = !mostraPrevisaoPeca;
                if (mostraPrevisaoPeca && peca != pecaSeguinte) {
                    pecaSeguinte.fundePeca(previewBoard, true);
                    pecaSeguinte.detach();
                } else {
                    previewBoard.limpa();
                }
                break;
        }
        System.out.println("2 PECA "+i+" exec "+k+" no instante :"+jogo.elementAt(i).tabela.elementAt(k-1).temp);
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
            while (thread == this && replayJogo==false) {
                temporizador();

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
            while(thread==this && replayJogo==true){
//                try {
                    
                temporizadorReplay();
                //Thread.sleep(sleepTime);
//                } catch (InterruptedException ignore) {
//                }
                while (paused && thread == this) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ignore) {
                    }
                }
                
            }
        }
    }

    public void ControladorDeJogo() {
    // TODO: implement
    }

//    public void iniciarJogo() {
//        this.mundo=new Mundo();
//    }
    public void sairJogo() {
        System.exit(0);
    }

    public void verRanking() {
        ficheiro.carregarRanking();
    //assumindo que ele na classe ficheiros carrega e imprime
    }

    public void gravarJogo() {
        //ficheiro.gravarJogo(jogo);
    }

    public void carregarJogo() {
       // ficheiro.carregarJogo();
    }

    public void inserirCodigo(int n) {
        switch (n) {
            case 2: /*meter a dificuldade a 2*/
                ;
            case 3:
                ;
            //etc...
        }
    }

    public void actualizaMundo() {
        //jogo.carregarInstante();
    }
}
