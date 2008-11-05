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

    private Mundo board = null;
    private Point[] coordenadasPecas ;
    
    private int xPos = 0;
    private int yPos = 0;
    private int fallen = 0;

    private int orientation = 0;
    private int maxOrientation = 4;

    private Color color = Color.white;

    public Peca(int tipo) throws IllegalArgumentException {
        coordenadasPecas = new Point[4];
        initialize(tipo);
    }
    
    /**
     * Initializes the instance variables for a specified figure type.
     * 
     * @param type      the figure type (one of the figure constants)
     * 
     * @see #QUADRADO
     * @see #LINHA
     * @see #S
     * @see #S_INVERTIDO
     * @see #l
     * @see #L_INVERTIDO
     * @see #T
     * 
     * @throws IllegalArgumentException if the figure type specified is not recognized
     */
    private void initialize(int tipo) throws IllegalArgumentException {
        
        // Initialize default variables
        board = null;
        xPos = 0;
        yPos = 0;
        orientation = 0;


        // Initialize figure type variables
        switch (tipo) {
            
            
        case QUADRADO:
            maxOrientation = 1;
            color = new Color(Integer.parseInt("ffd8b1", 16));

            coordenadasPecas[0] = new Point (-1, 0);
            coordenadasPecas[1] = new Point (0, 0);
            coordenadasPecas[2] = new Point (-1, 1);
            coordenadasPecas[3] = new Point (0, 1);
                      
           
                      
            break;
            
        case LINHA:
            maxOrientation = 2;
            color = new Color(Integer.parseInt("ffb4b4", 16));
            coordenadasPecas[0] = new Point (-2, 0);
            coordenadasPecas[1] = new Point (-1, 0);
            coordenadasPecas[2] = new Point (0, 0);
            coordenadasPecas[3] = new Point (1, 0);
                      
            break;
            
        case S:
            maxOrientation = 2;
            color = new Color(Integer.parseInt("a3d5ee", 16));
            coordenadasPecas[0] = new Point (0, 0);
            coordenadasPecas[1] = new Point (1, 0);
            coordenadasPecas[2] = new Point (-1, 1);
            coordenadasPecas[3] = new Point (0, 1);            
            
            break;
            
        case S_INVERTIDO:
            maxOrientation = 2;
            color = new Color(Integer.parseInt("f4adff", 16));
            coordenadasPecas[0] = new Point (-1, 0);
            coordenadasPecas[1] = new Point (0, 0);
            coordenadasPecas[2] = new Point (0, 1);
            coordenadasPecas[3] = new Point (1, 1);
                      
            break;
            
        case L:
            maxOrientation = 4;
            color = new Color(Integer.parseInt("c0b6fa", 16));
            coordenadasPecas[0] = new Point (-1, 0);
            coordenadasPecas[1] = new Point (0, 0);
            coordenadasPecas[2] = new Point (1, 0);
            coordenadasPecas[3] = new Point (1, 1);
            
            break;
            
        case L_INVERTIDO:
            maxOrientation = 4;
            color = new Color(Integer.parseInt("f5f4a7", 16));
            coordenadasPecas[0] = new Point (-1, 0);
            coordenadasPecas[1] = new Point (0, 0);
            coordenadasPecas[2] = new Point (1, 0);
            coordenadasPecas[3] = new Point (-1, 1);
            
            break;
            
        case T:
            maxOrientation = 4;
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
     * Checks if this figure is attached to a square board.
     * 
     * @return true if the figure is already attached, or false otherwise
     */
    public boolean isAttached() {
        return board != null;
    }
/**
     * @return the number of rows this figure has fallen
     */
    public int numRowsFallen() {
        return fallen;
    }

    /**
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
    public boolean attach(Mundo board, boolean center) {
        int  newX;
        int  newY;
        int  i;

        // Check for previous attachment
        if (isAttached()) {
            detach();
        }

        // Reset position (for correct controls)
        xPos = 0;
        yPos = 0;

        // Calculate position
        newX = board.getBoardWidth() / 2;
        if (center) {
            newY = board.getBoardHeight() / 2;
        } else {
            newY = 0;
            for (i = 0; i < coordenadasPecas.length; i++) {
                if (getRelativeY(i, orientation) - newY > 0) {
                    newY = -getRelativeY(i, orientation);
                }
            }
        }

        // Check position        
        this.board = board;
        if (!canMoveTo(newX, newY, orientation)) {
            this.board = null;
            return false;
        }

        // Draw figure
        xPos = newX;
        yPos = newY;
        paint(color);
        board.update();

        return true;
    }
    
    /**
     * Detaches this figure from its square board. The figure will not be removed
	 * from the board by this operation, resulting in the figure being left intact.
     */
    public void detach() {
        board = null;
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
            if (yPos + getRelativeY(i, orientation) < 0) {
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
        return !isAttached() || !canMoveTo(xPos, yPos + 1, orientation);
    }

    // metodo responsavel por efectuar os movimentos da peça para a direita, esquerda e para baixo
    //o metodo recebe um valor inteiro que define que tipo de movimento é:
    //direccao = -1: movimento para a esquerda
    //direccao = 0: movimento para a baixo
    //direccao = 1: movimento para a direita
    public void deslocarPeca(int direccao){
        
        if(direccao == -1){
            if (isAttached() && canMoveTo(xPos - 1, yPos, orientation)) {
                paint(null);
                xPos--;
                paint(color);
                board.update();
                }                        
        }
        
        if(direccao == 1){
            if (isAttached() && canMoveTo(xPos + 1, yPos, orientation)) {
                paint(null);
                xPos++;
                paint(color);
                board.update();
            }                        
        }
        
        if(direccao == 0){
            fallen = 0;
            if (isAttached() && canMoveTo(xPos, yPos + 1, orientation)) {
                paint(null);
                yPos++;
                paint(color);
                board.update();
            }            
        }        
    }


    /**
     * Moves the figure all the way down. The limits of the move are 
     * either the square board bottom, or squares not being empty. If 
     * no move is possible with respect to the square board, nothing 
     * is done. The square board will be changed as the figure moves, 
     * clearing the previous cells. If no square board is attached, nothing is done.
	 *
	 * @return the number of rows the figure was moved down
     */
//    public void moveAllWayDown() {
//		fallen = 0;
//        int y = yPos;
//
//        // Check for board
//        if (!isAttached()) {
//            return;
//        }
//
//        // Find lowest position
//        while (canMoveTo(xPos, y + 1, orientation)) {
//            y++;
//			fallen++;
//        }
//
//        // Update
//        if (y != yPos) {
//            paint(null);
//            yPos = y;
//            paint(color);
//            board.update();
//		}
//    }

    /**
     * Returns the current figure rotation (orientation).
     * 
     * @return the current figure rotation
     */
    public int getRotation() {
        return orientation;
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
    public void setRotation(int rotation) {
        int newOrientation;

        // Set new orientation
        newOrientation = rotation % maxOrientation;

        // Check new position
        if (!isAttached()) {
            orientation = newOrientation;
        } else if (canMoveTo(xPos, yPos, newOrientation)) {
            paint(null);
            orientation = newOrientation;
            paint(color);
            board.update();
        }
    }

    /**
     * Rotates the figure randomly. If such a rotation is not
     * possible with respect to the square board, nothing is done.
     * The square board will be changed as the figure moves,
     * clearing the previous cells. If no square board is attached, 
     * the rotation is performed directly.
     */
    public void rotateRandom() {
        setRotation((int) (Math.random() * 4.0) % maxOrientation);
    }

    /**
     * Rotates the figure clockwise. If such a rotation is not
     * possible with respect to the square board, nothing is done.
     * The square board will be changed as the figure moves,
     * clearing the previous cells. If no square board is attached, 
     * the rotation is performed directly.
     */
    public void rotateClockwise() {
        if (maxOrientation == 1) {
            return;
        } else {
            setRotation((orientation + 1) % maxOrientation);
        }
    }

    /**
     * Rotates the figure counter-clockwise. If such a rotation
     * is not possible with respect to the square board, nothing
     * is done. The square board will be changed as the figure
     * moves, clearing the previous cells. If no square board is 
     * attached, the rotation is performed directly.
     */
    public void rotateCounterClockwise() {
        if (maxOrientation == 1) {
            return;
        } else {
            setRotation((orientation + 3) % 4);
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
            if (x == xPos + getRelativeX(i, orientation)
             && y == yPos + getRelativeY(i, orientation)) {

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
    private boolean canMoveTo(int newX, int newY, int newOrientation) {
        int  x;
        int  y;

        for (int i = 0; i < 4; i++) {
            x = newX + getRelativeX(i, newOrientation);
            y = newY + getRelativeY(i, newOrientation);
            if (!isInside(x, y) && !board.isSquareEmpty(x, y)) {
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
    private int getRelativeX(int square, int orientation) {
        switch (orientation % 4) {
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
    private int getRelativeY(int square, int orientation) {
        switch (orientation % 4) {
        case 0 :
            return coordenadasPecas[square].y;
        case 1 :
            return coordenadasPecas[square].x;
        case 2 :
            return -coordenadasPecas[square].y;
        case 3 :
            return -coordenadasPecas[square].x;
        default:
            return 0; // Should never occur
        }
    }
    
    /**
     * Paints the figure on the board with the specified color.
     *
     * @param color     the color to paint with, or null for clearing
     */
    private void paint(Color color) {
        int x, y;

        for (int i = 0; i < coordenadasPecas.length; i++) {
            x = xPos + getRelativeX(i, orientation);
            y = yPos + getRelativeY(i, orientation);
            board.setSquareColor(x, y, color);
        }
    }
}
