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
     * as diversas pecas possiveis sao:
     * @see #QUADRADO
     * @see #LINHA
     * @see #S
     * @see #S_INVERTIDO
     * @see #L
     * @see #L_INVERTIDO
     * @see #T   
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
     
     * @return true se a peca ja esta agarrada ao mundo
     */
    public boolean isAttached() {
        return mundo != null;
    }
    /**
     * @return o numero de linhas que a peca fez cair     
     */
    public int numRowsFallen() {
        return fallen;
    }

    /**
     * 
     * 
     * Attaches the figure to a specified square board. The figure 
     * will be drawn either at the absolute top of the board, with 
     * only the bottom line visible, or centered onto the board. In 
     * both cases, the squares on the new board are checked for 
     * collisions. If the squares are already occupied, this method
     * returns false and no attachment is made.<p>
     *
     * The horizontal and vertical coordinates will be reset for the 
     * figure, when centering the figure on the new board. The figure
     * orientation (rotation) will be kept, however. If the figure was
     * previously attached to another board, it will be detached from
     * that board before attaching to the new board.
     *
     * @param board     the square board to attach to
     * @param center    the centered position flag
     * 
     * @return true if the figure could be attached, or false otherwise
     */
    public boolean attach(Mundo mundo, boolean center) {
        int  newX;
        int  newY;
        int  i;

        // Check for previous attachment
        if (isAttached()) {
            detach();
        }

        xPos = 0;
        yPos = 0;

        newX = mundo.getBoardWidth() / 2;
        if (center) {
            newY = mundo.getBoardHeight() / 2;
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
        mundo.update();

        return true;
    }
    
    /**
     * Detaches this figure from its square board. The figure will not be removed
	 * from the board by this operation, resulting in the figure being left intact.
     */
    public void detach() {
        mundo = null;
    }

    /**
     * Checks if the figure is fully visible on the square board. If
     * the figure isn't attached to a board, false will be returned.
     * 
     * @return true if the figure is fully visible, or false otherwise
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
     * Checks if the figure has landed. If this method returns true,
     * the moveDown() or the moveAllWayDown() methods should have no 
     * effect. If no square board is attached, this method will return true.
     *
     * @return true if the figure has landed, or false otherwise
     */
    public boolean hasLanded() {
        return !isAttached() || !verificaPosicaoPeca(xPos, yPos + 1, numeroRotacao);
    }

    // metodo responsavel por efectuar os movimentos da peça para a direita, esquerda e para baixo
    //o metodo recebe um valor inteiro que define que tipo de movimento é:
    //direccao = -1: movimento para a esquerda
    //direccao = 0: movimento para a baixo
    //direccao = 1: movimento para a direita
    public void deslocarPeca(int direccao){
        
        if(direccao == -1){
            if (isAttached() && verificaPosicaoPeca(xPos - 1, yPos, numeroRotacao)) {
                paint(null);
                xPos--;
                paint(color);
                mundo.update();
                }                        
        }
        
        if(direccao == 1){
            if (isAttached() && verificaPosicaoPeca(xPos + 1, yPos, numeroRotacao)) {
                paint(null);
                xPos++;
                paint(color);
                mundo.update();
            }                        
        }
        
        if(direccao == 0){
            fallen = 0;
            if (isAttached() && verificaPosicaoPeca(xPos, yPos + 1, numeroRotacao)) {
                paint(null);
                yPos++;
                paint(color);
                mundo.update();
            }            
        }        
    }

    /**
     * Devolve o valor actual da rotacao.
     */
    public int getRotation() {
        return numeroRotacao;
    }
    
    /**
     * Sets the figure rotation (orientation). If the desired rotation 
     * is not possible with respect to the square board, nothing is 
     * done. The square board will be changed as the figure moves,
     * clearing the previous cells. If no square board is attached, 
     * the rotation is performed directly.
     * 
     * @param rotation  the new figure orientation
     */
    public void setRotacao(int rotacao) {
        int novoNumeroRotacoes;

        // Set new orientation
        novoNumeroRotacoes = rotacao % maxNumeroRotacoes;

        // Check new position
        if (!isAttached()) {
            numeroRotacao = novoNumeroRotacoes;
        } else if (verificaPosicaoPeca(xPos, yPos, novoNumeroRotacoes)) {
            paint(null);
            numeroRotacao = novoNumeroRotacoes;
            paint(color);
            mundo.update();
        }
    }

    /**
     * Rotates the figure randomly. If such a rotation is not
     * possible with respect to the square board, nothing is done.
     * The square board will be changed as the figure moves,
     * clearing the previous cells. If no square board is attached, 
     * the rotation is performed directly.
     */
    public void rotacaoRandom() {
        setRotacao((int) (Math.random() * 4.0) % maxNumeroRotacoes);
    }

    /**
     * Rotates the figure clockwise. If such a rotation is not
     * possible with respect to the square board, nothing is done.
     * The square board will be changed as the figure moves,
     * clearing the previous cells. If no square board is attached, 
     * the rotation is performed directly.
     */
    public void rotacao() {
        if (maxNumeroRotacoes == 1) {
            return;
        } else {
            setRotacao((numeroRotacao + 1) % maxNumeroRotacoes);
        }
    }

    /**
     * Checks if a specified pair of (square) coordinates are inside the figure, or not.
     *
     * @param x         the horizontal position
     * @param y         the vertical position
     * 
     * @return true if the coordinates are inside the figure, or false otherwise
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
     * Checks if the figure can move to a new position. The current 
     * figure position is taken into account when checking for 
     * collisions. If a collision is detected, this method will return false.
     *
     * @param newX            the new horizontal position
     * @param newY            the new vertical position
     * @param newOrientation  the new orientation (rotation)
     * 
     * @return true if the figure can be moved, or false otherwise
     */
    private boolean verificaPosicaoPeca(int newX, int newY, int novoNumeroRotacoes) {
        int  x;
        int  y;

        for (int i = 0; i < 4; i++) {
            x = newX + getRelativeX(i, novoNumeroRotacoes);
            y = newY + getRelativeY(i, novoNumeroRotacoes);
            if (!isInside(x, y) && !mundo.isSquareEmpty(x, y)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns the relative horizontal position of a specified square.
     * The square will be rotated according to the specified orientation.
     *
     * @param square       the square to rotate (0-3)
     * @param orientation  the orientation to use (0-3)
     * 
     * @return the rotated relative horizontal position
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
            return 0; // Should never occur
        }
    }

    /**
     * Rotates the relative vertical position of a specified square. 
     * The square will be rotated according to the specified orientation.
     *
     * @param square       the square to rotate (0-3)
     * @param orientation  the orientation to use (0-3)
     * 
     * @return the rotated relative vertical position
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
            return 0; // Should never occur
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
            mundo.setSquareColor(x, y, color);
        }
    }
}
