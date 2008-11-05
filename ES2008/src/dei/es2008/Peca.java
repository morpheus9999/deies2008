/***********************************************************************
 * Module:  Peca.java
 * Author:  Filipe
 * Purpose: Defines the Class Peca
 ***********************************************************************/
package dei.es2008;

import java.awt.Color;
import java.awt.Point;
import java.util.*;

/** @pdOid 2d5cdd22-f6e0-4a7e-a82d-b5e138dc265e */
public class Peca {

    /** @pdOid 80247ce6-c2ea-4844-836b-2755baa1cfc0 */
    private int tipo;
    private int numeroRotacao;
    /** @pdOid 6d38fb3a-ec66-47ac-8a66-1f0e6090dffd */
    private Point[] coordenadasPecas;
    private int maxOrientation = 4;
    /** @pdOid 81ae8e7e-f513-461e-ac9d-e88e9ede1c21 */
    private Point[][] ajustesPecasTipo1;
    private Point[][] ajustesPecasTipo2;
    private Point[][] ajustesPecasTipo3;
    private Point[][] ajustesPecasTipo4;
    private Point[][] ajustesPecasTipo5;
    private Point[][] ajustesPecasTipo6;
    private Mundo mundo;
    private Point p;
    /**
     * The figure color.
     */
    private Color color = Color.white;

    public Peca(Mundo mundo, int num) {
        this.mundo = mundo;
        this.tipo=num;
        //falta criar o array coordenadasPecas
    }

    /** @metodo responsavel pelas rotações das peças
        peça do tipo 1 corresponde ao 'L'
     *  peça do tipo 2 corresponde ao 'L invertido'
     *  peça do tipo 3 corresponde ao 'T'
     *  peça do tipo 4 corresponde ao 'S'
     *  peça do tipo 5 corresponde ao 'S invertido'
     *  peça do tipo 6 corresponde a 'barra'
     *  peça do tipo 7 corresponde ao 'quadrado'
     */
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
    public boolean isAttached() {
        return mundo != null;
    }
    public void detach() {
        mundo = null;
    }

    public boolean attach(Mundo mundo, boolean center) {
        int  newX;
        int  newY;
        int  i;

        // Check for previous attachment
        if (isAttached()) {
            detach();
        }

        // Reset position (for correct controls)
        p.x = 0;
        p.y = 0;

        // Calculate position
        newX = mundo.getBoardWidth() / 2;
        if (center) {
            newY = mundo.getBoardHeight() / 2;
        } else {
            newY = 0;
            for (i = 0; i < coordenadasPecas.length; i++) {
                if(tipo==1){
                    if (getRelativeY(i, numeroRotacao) - newY > 0) {
                    newY = -getRelativeY(i, numeroRotacao);
                }
                }
                
            }
        }

        // Check position        
        this.mundo = mundo;
        if (!canMoveTo(newX, newY, numeroRotacao)) {
            this.mundo = null;
            return false;
        }

        // Draw figure
        p.x = newX;
        p.y = newY;
        paint(color);
        mundo.update();

        return true;
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
            if (!isInside(x, y) && !mundo.isSquareEmpty(x, y)) {
                return false;
            }
        }
        return true;
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
            if (x == p.x + getRelativeX(i, numeroRotacao)
             && y == p.y + getRelativeY(i, numeroRotacao)) {

                return true;
            }
        }
        return false;
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
     * Paints the figure on the board with the specified color.
     *
     * @param color     the color to paint with, or null for clearing
     */
    private void paint(Color color) {
        int x, y;

        for (int i = 0; i < coordenadasPecas.length; i++) {
            x = p.x + getRelativeX(i, numeroRotacao);
            y = p.y + getRelativeY(i, numeroRotacao);
            mundo.setSquareColor(x, y, color);
        }
    }
    
    
    public void setRotation(int rotation) {
        int newOrientation;

        // Set new orientation
        newOrientation = rotation % maxOrientation;

        // Check new position
        if (!isAttached()) {
             numeroRotacao = newOrientation;
        } else if (canMoveTo(p.x, p.y, newOrientation)) {
            paint(null);
            numeroRotacao = newOrientation;
            paint(color);
            mundo.update();
        }
    }
     public int getRotation() {
        return numeroRotacao;
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
    public void rodarPeca() {

        if (tipo == 1) {
            switch (numeroRotacao) {
                case 0:
                    if (mundo.verificaPosicaoPeca()) {
                        for (int i = 0; i < coordenadasPecas.length; i++) {
                            coordenadasPecas[i].x = coordenadasPecas[i].x + ajustesPecasTipo1[0][i].x;
                            coordenadasPecas[i].y = coordenadasPecas[i].y + ajustesPecasTipo1[0][i].y;
                        }
                        
                        numeroRotacao += 1;
                    }
                    break;

                case 1:
                    if (mundo.verificaPosicaoPeca()) {
                        for (int i = 0; i < coordenadasPecas.length; i++) {
                            coordenadasPecas[i].x = coordenadasPecas[i].x + ajustesPecasTipo1[1][i].x;
                            coordenadasPecas[i].y = coordenadasPecas[i].y + ajustesPecasTipo1[1][i].y;
                        }
                        numeroRotacao += 1;
                    }
                    break;

                case 2:
                    if (mundo.verificaPosicaoPeca()) {
                        for (int i = 0; i < coordenadasPecas.length; i++) {
                            coordenadasPecas[i].x = coordenadasPecas[i].x + ajustesPecasTipo1[2][i].x;
                            coordenadasPecas[i].y = coordenadasPecas[i].y + ajustesPecasTipo1[2][i].y;
                        }
                        numeroRotacao += 1;
                    }
                    break;

                case 3:
                    if (mundo.verificaPosicaoPeca()) {
                        for (int i = 0; i < coordenadasPecas.length; i++) {
                            coordenadasPecas[i].x = coordenadasPecas[i].x + ajustesPecasTipo1[3][i].x;
                            coordenadasPecas[i].y = coordenadasPecas[i].y + ajustesPecasTipo1[3][i].y;
                        }
                        numeroRotacao = 0;
                    }
            }

        }

        if (tipo == 2) {
            switch (numeroRotacao) {
                case 0:
                    if (mundo.verificaPosicaoPeca()) {
                        for (int i = 0; i < coordenadasPecas.length; i++) {
                            coordenadasPecas[i].x = coordenadasPecas[i].x + ajustesPecasTipo2[0][i].x;
                            coordenadasPecas[i].y = coordenadasPecas[i].y + ajustesPecasTipo2[0][i].y;
                        }
                        ;
                        numeroRotacao += 1;
                    }
                    break;

                case 1:
                    if (mundo.verificaPosicaoPeca()) {
                        for (int i = 0; i < coordenadasPecas.length; i++) {
                            coordenadasPecas[i].x = coordenadasPecas[i].x + ajustesPecasTipo2[1][i].x;
                            coordenadasPecas[i].y = coordenadasPecas[i].y + ajustesPecasTipo2[1][i].y;
                        }
                        numeroRotacao += 1;
                    }
                    break;

                case 2:
                    if (mundo.verificaPosicaoPeca()) {
                        for (int i = 0; i < coordenadasPecas.length; i++) {
                            coordenadasPecas[i].x = coordenadasPecas[i].x + ajustesPecasTipo2[2][i].x;
                            coordenadasPecas[i].y = coordenadasPecas[i].y + ajustesPecasTipo2[2][i].y;
                        }
                        numeroRotacao += 1;
                    }
                    break;

                case 3:
                    if (mundo.verificaPosicaoPeca()) {
                        for (int i = 0; i < coordenadasPecas.length; i++) {
                            coordenadasPecas[i].x = coordenadasPecas[i].x + ajustesPecasTipo2[3][i].x;
                            coordenadasPecas[i].y = coordenadasPecas[i].y + ajustesPecasTipo2[3][i].y;
                        }
                        numeroRotacao = 0;
                    }
            }


        }
        if (tipo == 3) {
            switch (numeroRotacao) {
                case 0:
                    if (mundo.verificaPosicaoPeca()) {
                        for (int i = 0; i < coordenadasPecas.length; i++) {
                            coordenadasPecas[i].x = coordenadasPecas[i].x + ajustesPecasTipo3[0][i].x;
                            coordenadasPecas[i].y = coordenadasPecas[i].y + ajustesPecasTipo3[0][i].y;
                        }
                        numeroRotacao += 1;
                    }
                    break;

                case 1:
                    if (mundo.verificaPosicaoPeca()) {
                        for (int i = 0; i < coordenadasPecas.length; i++) {
                            coordenadasPecas[i].x = coordenadasPecas[i].x + ajustesPecasTipo3[1][i].x;
                            coordenadasPecas[i].y = coordenadasPecas[i].y + ajustesPecasTipo3[1][i].y;
                        }
                        numeroRotacao += 1;
                    }
                    break;

                case 2:
                    if (mundo.verificaPosicaoPeca()) {
                        for (int i = 0; i < coordenadasPecas.length; i++) {
                            coordenadasPecas[i].x = coordenadasPecas[i].x + ajustesPecasTipo3[2][i].x;
                            coordenadasPecas[i].y = coordenadasPecas[i].y + ajustesPecasTipo3[2][i].y;
                        }
                        numeroRotacao += 1;
                    }
                    break;

                case 3:
                    if (mundo.verificaPosicaoPeca()) {
                        for (int i = 0; i < coordenadasPecas.length; i++) {
                            coordenadasPecas[i].x = coordenadasPecas[i].x + ajustesPecasTipo3[3][i].x;
                            coordenadasPecas[i].y = coordenadasPecas[i].y + ajustesPecasTipo3[3][i].y;
                        }
                        numeroRotacao = 0;
                    }
            }


        }
        if (tipo == 4) {
            switch (numeroRotacao) {
                case 0:
                    if (mundo.verificaPosicaoPeca()) {
                        for (int i = 0; i < coordenadasPecas.length; i++) {
                            coordenadasPecas[i].x = coordenadasPecas[i].x + ajustesPecasTipo4[0][i].x;
                            coordenadasPecas[i].y = coordenadasPecas[i].y + ajustesPecasTipo4[0][i].y;
                        }
                        ;
                        numeroRotacao += 1;
                    }
                    break;

                case 1:
                    if (mundo.verificaPosicaoPeca()) {
                        for (int i = 0; i < coordenadasPecas.length; i++) {
                            coordenadasPecas[i].x = coordenadasPecas[i].x + ajustesPecasTipo4[1][i].x;
                            coordenadasPecas[i].y = coordenadasPecas[i].y + ajustesPecasTipo4[1][i].y;
                        }
                        numeroRotacao += 1;
                    }
                    break;

                case 2:
                    if (mundo.verificaPosicaoPeca()) {
                        for (int i = 0; i < coordenadasPecas.length; i++) {
                            coordenadasPecas[i].x = coordenadasPecas[i].x + ajustesPecasTipo4[2][i].x;
                            coordenadasPecas[i].y = coordenadasPecas[i].y + ajustesPecasTipo4[2][i].y;
                        }
                        numeroRotacao = 0;
                    }
                    break;

            }

        }
        if (tipo == 5) {
            switch (numeroRotacao) {
                case 0:
                    if (mundo.verificaPosicaoPeca()) {
                        for (int i = 0; i < coordenadasPecas.length; i++) {
                            coordenadasPecas[i].x = coordenadasPecas[i].x + ajustesPecasTipo5[0][i].x;
                            coordenadasPecas[i].y = coordenadasPecas[i].y + ajustesPecasTipo5[0][i].y;
                        }
                        ;
                        numeroRotacao += 1;
                    }
                    break;

                case 1:
                    if (mundo.verificaPosicaoPeca()) {
                        for (int i = 0; i < coordenadasPecas.length; i++) {
                            coordenadasPecas[i].x = coordenadasPecas[i].x + ajustesPecasTipo5[1][i].x;
                            coordenadasPecas[i].y = coordenadasPecas[i].y + ajustesPecasTipo5[1][i].y;
                        }
                        numeroRotacao += 1;
                    }
                    break;

                case 2:
                    if (mundo.verificaPosicaoPeca()) {
                        for (int i = 0; i < coordenadasPecas.length; i++) {
                            coordenadasPecas[i].x = coordenadasPecas[i].x + ajustesPecasTipo5[2][i].x;
                            coordenadasPecas[i].y = coordenadasPecas[i].y + ajustesPecasTipo5[2][i].y;
                        }
                        numeroRotacao = 0;
                    }
                    break;

            }

        }
        if (tipo == 6) {
            switch (numeroRotacao) {
                case 0:
                    if (mundo.verificaPosicaoPeca()) {
                        for (int i = 0; i < coordenadasPecas.length; i++) {
                            coordenadasPecas[i].x = coordenadasPecas[i].x + ajustesPecasTipo6[0][i].x;
                            coordenadasPecas[i].y = coordenadasPecas[i].y + ajustesPecasTipo6[0][i].y;
                        }
                        ;
                        numeroRotacao += 1;
                    }
                    break;

                case 1:
                    if (mundo.verificaPosicaoPeca()) {
                        for (int i = 0; i < coordenadasPecas.length; i++) {
                            coordenadasPecas[i].x = coordenadasPecas[i].x + ajustesPecasTipo6[1][i].x;
                            coordenadasPecas[i].y = coordenadasPecas[i].y + ajustesPecasTipo6[1][i].y;
                        }
                        numeroRotacao = 0;
                    }
                    break;
            }

        }

    }

    /** @param direccao
     * metodo responsável pelo deslocamento das peças para a direita, esquerda e para baixo
       direccao = -1 corresponde a um deslocamento para a direita
     * direccao = 1 corresponde a um deslocamento para a esquerda
     * direccao = 0 corresponde a um deslocamento para baixo
     */
    /**
     * Checks if the figure has landed. If this method returns true,
     * the moveDown() or the moveAllWayDown() methods should have no 
     * effect. If no square board is attached, this method will return true.
     *
     * @return true if the figure has landed, or false otherwise
     */
    public boolean hasLanded() {
        return !isAttached() || !canMoveTo(p.x, p.y + 1, numeroRotacao);
    }
    
    public void deslocarPeca(int direccao) {
        if (direccao == -1) {
            if(mundo.verificaPosicaoPeca()){
                for (int i = 0; i < coordenadasPecas.length; i++) {
                    coordenadasPecas[i].x -= 1;
                }
            }            
        }
        
        if (direccao == 1) {
            if(mundo.verificaPosicaoPeca()){
                for (int i = 0; i < coordenadasPecas.length; i++) {
                    coordenadasPecas[i].x += 1;
                }
            }            
        }        
        
        if (direccao == 0) {
            if(mundo.verificaPosicaoPeca()){
                for (int i = 0; i < coordenadasPecas.length; i++) {
                    coordenadasPecas[i].y += 1;
                }
            }            
        }        
    }
}
