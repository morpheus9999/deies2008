package dei.es2008;
import java.awt.Color;
import java.awt.Point;

public class Peca {

    public static final int QUADRADO = 1;
    public static final int LINHA = 2;
    public static final int S = 3;
    public static final int S_INVERTIDO = 4;
    public static final int L = 5;
    public static final int L_INVERTIDO = 6;
    public static final int T = 7;

    private Mundo mundo = null;
    private Point[] coordenadasPecas ;
    
    private int xPos = 0;
    private int yPos = 0;
    private int fallen = 0;

    private int numeroRotacao = 0;
    private int maxNumeroRotacoes = 4;

    private Color color = Color.white;

    public Peca(int tipo) throws IllegalArgumentException {
        coordenadasPecas = new Point[4];
        criaPeca(tipo);
    }
    
    /**
     * as diversas pecas que podem ser criadas sao:
     * QUADRADO
     * LINHA
     * S
     * S_INVERTIDO
     * L
     * L_INVERTIDO
     * T   
     */
    private void criaPeca(int tipo) throws IllegalArgumentException {
                
        mundo = null;
        xPos = 0;
        yPos = 0;
        numeroRotacao = 0;

        switch (tipo) {
                        
        case QUADRADO:
            maxNumeroRotacoes = 1;
            color = new Color(Integer.parseInt("ffd8b1", 16));
            coordenadasPecas[0] = new Point (-1, 0);
            coordenadasPecas[1] = new Point (0, 0);
            coordenadasPecas[2] = new Point (-1, 1);
            coordenadasPecas[3] = new Point (0, 1);          
            break;
            
        case LINHA:
            maxNumeroRotacoes = 2;
            color = new Color(Integer.parseInt("ffb4b4", 16));
            coordenadasPecas[0] = new Point (-2, 0);
            coordenadasPecas[1] = new Point (-1, 0);
            coordenadasPecas[2] = new Point (0, 0);
            coordenadasPecas[3] = new Point (1, 0);                      
            break;
            
        case S:
            maxNumeroRotacoes = 2;
            color = new Color(Integer.parseInt("a3d5ee", 16));
            coordenadasPecas[0] = new Point (0, 0);
            coordenadasPecas[1] = new Point (1, 0);
            coordenadasPecas[2] = new Point (-1, 1);
            coordenadasPecas[3] = new Point (0, 1);                        
            break;
            
        case S_INVERTIDO:
            maxNumeroRotacoes = 2;
            color = new Color(Integer.parseInt("f4adff", 16));
            coordenadasPecas[0] = new Point (-1, 0);
            coordenadasPecas[1] = new Point (0, 0);
            coordenadasPecas[2] = new Point (0, 1);
            coordenadasPecas[3] = new Point (1, 1);                      
            break;
            
        case L:
            maxNumeroRotacoes = 4;
            color = new Color(Integer.parseInt("c0b6fa", 16));
            coordenadasPecas[0] = new Point (-1, 0);
            coordenadasPecas[1] = new Point (0, 0);
            coordenadasPecas[2] = new Point (1, 0);
            coordenadasPecas[3] = new Point (1, 1);            
            break;
            
        case L_INVERTIDO:
            maxNumeroRotacoes = 4;
            color = new Color(Integer.parseInt("f5f4a7", 16));
            coordenadasPecas[0] = new Point (-1, 0);
            coordenadasPecas[1] = new Point (0, 0);
            coordenadasPecas[2] = new Point (1, 0);
            coordenadasPecas[3] = new Point (-1, 1);            
            break;
            
        case T:
            maxNumeroRotacoes = 4;
            color = new Color(Integer.parseInt("a4d9b6", 16));
            coordenadasPecas[0] = new Point (-1, 0);
            coordenadasPecas[1] = new Point (0, 0);
            coordenadasPecas[2] = new Point (1, 0);
            coordenadasPecas[3] = new Point (0, 1);            
            break;
            
        default :
            throw new IllegalArgumentException("No figure constant: " + tipo);
        }
    }

    /**
     * @devolve true se a peca ja esta agarrada ao mundo
     */
    public boolean isAttached() {
        return mundo != null;
    }
    /**
     * @devolve o numero de linhas que a peca fez cair     
     */
    public int numRowsFallen() {
        return fallen;
    }

    /**
     * metodo responsavel por juntar as pecas ao mundo.
     * @devolve true se a peca pode ser juntada ao mundo, caso contrario devolve false.
     */
    public boolean attach(Mundo mundo, boolean center) {
        int  newX;
        int  newY;
        int  i;

        // verifica se ja houve uma juncao de uma peca ao mundo.
        if (isAttached()) {
            detach();
        }

        xPos = 0;
        yPos = 0;

        newX = mundo.getLarguraTabuleiro() / 2;
        if (center) {
            newY = mundo.getAlturaTabuleiro() / 2;
        } else {
            newY = 0;
            for (i = 0; i < coordenadasPecas.length; i++) {
                if (getRelativeY(i, numeroRotacao) - newY > 0) {
                    newY = -getRelativeY(i, numeroRotacao);
                }
            }
        }
     
        this.mundo = mundo;
        if (!verificaPosicaoPeca(newX, newY, numeroRotacao)) {
            this.mundo = null;
            return false;
        }

        xPos = newX;
        yPos = newY;
        paint(color);
        mundo.actualizaMundo();

        return true;
    }
    
    public void detach() {
        mundo = null;
    }

    /**
     * Verifica se a peca esta totalmente visivel no mundo.
     * Se a peca nao se encontrar juntada ao mundo, devolve false.
     * 
     * @devolve true se a peca esta totalmente visivel, caos contrario devolve false.
     */
    public boolean isAllVisible() {
        if (!isAttached()) {
            return false;
        }
         for (int i = 0; i < coordenadasPecas.length; i++) {
            if (yPos + getRelativeY(i, numeroRotacao) < 0) {
                return false;
            }
        }
        return true;
    }

    

    /**
     * Verifica se a peca ja chegou ao fundo do tabuleiro de jogo ou se encaixou em pecas que ja estejam no mundo.  
     * @devolve true se a peca chegou ao fundo do tabuleiro ou se assentou em pecas ja existentes, caso
     *  contrario devolve false.
     */
    public boolean hasLanded() {
        return !isAttached() || !verificaPosicaoPeca(xPos, yPos + 1, numeroRotacao);
    }

        /**
     * metodo responsavel por efectuar os movimentos da peça para a direita, esquerda e para baixo
     * o metodo recebe um valor inteiro que define que tipo de movimento é:
     *      direccao = -1: movimento para a esquerda
     *      direccao = 0: movimento para a baixo
     *      direccao = 1: movimento para a direita
     */
    public void deslocarPeca(int direccao){
        
        if(direccao == -1){
            if (isAttached() && verificaPosicaoPeca(xPos - 1, yPos, numeroRotacao)) {
                paint(null);
                xPos--;
                paint(color);
                mundo.actualizaMundo();
                }                        
        }
        
        if(direccao == 1){
            if (isAttached() && verificaPosicaoPeca(xPos + 1, yPos, numeroRotacao)) {
                paint(null);
                xPos++;
                paint(color);
                mundo.actualizaMundo();
            }                        
        }
        
        if(direccao == 0){
            fallen = 0;
            if (isAttached() && verificaPosicaoPeca(xPos, yPos + 1, numeroRotacao)) {
                paint(null);
                yPos++;
                paint(color);
                mundo.actualizaMundo();
            }            
        }        
    }

    /**
     * Devolve o valor actual da rotacao de modo a sabermos em que posicao se encontra peca.
     */
    public int getRotacao() {
        return numeroRotacao;
    }
    
    /**
     * Metodo responsavel por efectuar as rotacoes das pecas. Este verifica se é possivel
     * efectuar uma rotacao consoante a peca em causa, consoante a sua posicao no tabuleiro
     * e a sua orientacao momentanea. Se nao é possivel efectuar a rotacao, nada é alterado.         
     */
    public void setRotacao(int rotacao) {
        int novoNumeroRotacoes;

        // actualiza a variavel que representa a orientacao momentanea
        novoNumeroRotacoes = rotacao % maxNumeroRotacoes;

        // verifica a posicao da peca
        if (!isAttached()) {
            numeroRotacao = novoNumeroRotacoes;
        } else if (verificaPosicaoPeca(xPos, yPos, novoNumeroRotacoes)) {
            paint(null);
            numeroRotacao = novoNumeroRotacoes;
            paint(color);
            mundo.actualizaMundo();
        }
    }

    /**
     * Efectua uma rotacao aleatoria nas pecas para estas nao serem lancadas no mundo
     * sempre nas mesmas posicoes.
     */
    public void rotacaoRandom() {
        setRotacao((int) (Math.random() * 4.0) % maxNumeroRotacoes);
    }

    /**
     * Efectua a rotacao da peca em causa no sentido dos ponteiros do relogio.
     */
    public void rotacao() {
        if (maxNumeroRotacoes == 1) {
            return;
        } else {
            setRotacao((numeroRotacao + 1) % maxNumeroRotacoes);
        }
    }

    /**
     * Verifica se as coordenadas de um bloco se encontram numa peca.
     * @devolve true as coordenadas se encontram na peca, caso contrario devolve false.
     */
    private boolean isInside(int x, int y) {
        for (int i = 0; i < coordenadasPecas.length; i++) {
            if (x == xPos + getRelativeX(i, numeroRotacao)
             && y == yPos + getRelativeY(i, numeroRotacao)) {

                return true;
            }
        }
        return false;
    }

    /**
     * Verifica se uma peca pode ser movida para uma nova posicao. É verificado
     * se existem colisoes. Se houver colisoes, o metodo devolve false, caso contrario devolve true.
     */
    private boolean verificaPosicaoPeca(int newX, int newY, int novoNumeroRotacoes) {
        int  x;
        int  y;

        for (int i = 0; i < 4; i++) {
            x = newX + getRelativeX(i, novoNumeroRotacoes);
            y = newY + getRelativeY(i, novoNumeroRotacoes);
            if (!isInside(x, y) && !mundo.isBlocoVazio(x, y)) {
                return false;
            }
        }
        return true;
    }

    /**
     * devolve a posicao relativa horizontal de um bloco especifico.
     * o bloco sofrera rotacao de acordo com a orientacao especificada.
     * @devolve a posicao depois de sofrida a rotacao relativa horizontal.
     */
    private int getRelativeX(int square, int numeroRotacoes) {
        switch (numeroRotacoes % 4) {
        case 0 :
            return coordenadasPecas[square].x;
        case 1 :
            return -coordenadasPecas[square].y;
        case 2 :
            return -coordenadasPecas[square].x;
        case 3 :
            return coordenadasPecas[square].y;
        default:
            return 0;
        }
    }

    /**
     * devolve a posicao relativa vertical de um bloco especifico.
     * o bloco sofrera rotacao de acordo com a orientacao especificada.
     * @devolve a posicao depois de sofrida a rotacao relativa vertical.
     */
    private int getRelativeY(int bloco, int numeroRotacoes) {
        switch (numeroRotacoes % 4) {
        case 0 :
            return coordenadasPecas[bloco].y;
        case 1 :
            return coordenadasPecas[bloco].x;
        case 2 :
            return -coordenadasPecas[bloco].y;
        case 3 :
            return -coordenadasPecas[bloco].x;
        default:
            return 0;
        }
    }
    
    /**
     * Pinta a peca no mundo com uma cor especifica.             
     */
    private void paint(Color color) {
        int x, y;

        for (int i = 0; i < coordenadasPecas.length; i++) {
            x = xPos + getRelativeX(i, numeroRotacao);
            y = yPos + getRelativeY(i, numeroRotacao);
            mundo.setCorBloco(x, y, color);
        }
    }
}
