/***********************************************************************
 * Module:  Mundo.java
 * Author:  Filipe
 * Purpose: Defines the Class Mundo
 ***********************************************************************/

package dei.es2008;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.util.Hashtable;

/**
 * Esta classe é o mundo, onde cria o tabuleiro de jogo e tem uma grelha com
 * os quadrados coloridos.O tabuleiro está limitado em baixo e nos lados
 * , apenas fica solto em cima onde a peça aparece faseadamente antes de entrar
 * na totalidade no tabuleiro de jogo.
 */
public class Mundo extends Object {

    /**
     * A largura do tabuleiro(em quadrados).
     */
    private int  largura = 0;

    /**
     * A altura do tabuleiro (em quadrados).
     */
    private int  altura = 0;

    /**
     * A matriz colorida do tabuleiro de jogo. Esta matriz contem uma entrada
     * de cor para cada quadrado do tabuleiro. A matriz é indexada pela
     * cordenada vertical e horizontal.
     */
    private Color[][]  mundo = null;

    /**
     * Mensagam opcional do tabuleiro. Esta mensagem pode ser accionada a
     * qualquer altura, imprimindo o texto a meio do tabuleiro
     */
    private String  mensagem = null;

    /**
     * Contador para o número de linhas que o jogador conseguiu completar, é
     * incrementado cada vez que o player completa uma linha.
     */
    private int  LinhasRemovidas = 0;

    /**
     * Representação gráfica do tabuleiro de jogo. Esta componente gráfica
     * é criada quando a chamada getComponent() é realizada.
     */
    private interfaceTabuleiroJogo  componente = null;

    /**
     * Cria um novo tabuleiro de jogo com o tamanho designado. O tabuleiro vai
     * começar vazio.
     *
     * @param width     a largura do tabuleiro
     * @param height    a altura do tabuleiro
     */
    public Mundo(int largura, int altura) {
        this.largura = largura;
        this.altura = altura;
        this.mundo = new Color[altura][largura];
        limpa();
    }

    /**
     * Verifica se o tabuleiro contém alguma linha cheia.
     * @return the number of full lines
     */
    public int getLinhasCompletas() {
		int linhasCompletas = 0;
        for (int y = altura - 1; y >= 0; y--) {
            if (isLinhaCompleta(y)) {
				linhasCompletas++;
            }
        }
        return linhasCompletas;
    }
    /**
     * Verifica se um quadrado específico não tem cor, ou seja se tiver fora 
     * do tabuleiro de jogo não tem cor e indica isso retornando false (excepto
     * quando a peça se encontra acima do tabuleiro de jogo).
     * 
     * @param x         a posição horizontal (0 <= x < width)
     * @param y         a posição vertical (0 <= y < height)
     * 
     * @return true se o quadrado for vazio (emtpy), falso em caso contrário
     */
    public boolean isBlocoVazio(int x, int y) {
        if (x < 0 || x >= largura || y < 0 || y >= altura) {
            return x >= 0 && x < largura && y < 0;
        } else {
            return mundo[y][x] == null;
        }
    }



    /**
     * Verifica se uma determinada linha está xeia, se a linha estiver fora do
     * tabuleiro de jogo retorna true.
     *
     * @param y         a posição vertical (0 <= y < height)
     * 
     * @return true se toda a linha estiver cheia, falso em caso contrário.
     */
    public boolean isLinhaCompleta(int y) {
        if (y < 0 || y >= altura) {
            return true;
        }
        for (int x = 0; x < largura; x++) {
            if (mundo[y][x] == null) {
                return false;
            }
        }
        return true;
    }


    /**
     * Returns a graphical component to draw the board. The component 
     * returned will automatically be updated when changes are made to
     * this board. Multiple calls to this method will return the same
     * component, as a square board can only have a single graphical
     * representation.
     * 
     * @return a graphical component that draws this board
     */
    public Component getComponente() {
        if (componente == null) {
            componente = new interfaceTabuleiroJogo();
        }
        return componente;
    }

    /**
     * Returns the board height (in squares). This method returns, 
     * i.e, the number of vertical squares that fit on the board.
     * 
     * @return the board height in squares
     */
    public int getAlturaTabuleiro() {
        return altura;
    }

    /**
     * Returns the board width (in squares). This method returns, i.e,
     * the number of horizontal squares that fit on the board.
     * 
     * @return the board width in squares
     */
    public int getLarguraTabuleiro() {
        return largura;
    }

    /**
     * Returns the number of lines removed since the last clear().
     * 
     * @return the number of lines removed since the last clear call
     */
    public int getLinhasRemovidas() {
        return LinhasRemovidas;
    }

    /**
     * Returns the color of an individual square on the board. If the 
     * square is empty or outside the board, null will be returned.
     *
     * @param x         the horizontal position (0 <= x < width)
     * @param y         the vertical position (0 <= y < height)
     * 
     * @return the square color, or null for none
     */
    public Color getCorBloco(int x, int y) {
        if (x < 0 || x >= largura || y < 0 || y >= altura) {
            return null;
        } else {
            return mundo[y][x];
        }
    }

    /**
     * Changes the color of an individual square on the board. The 
     * square will be marked as in need of a repaint, but the 
     * graphical component will NOT be repainted until the update() 
     * method is called.
     *
     * @param x         the horizontal position (0 <= x < width)
     * @param y         the vertical position (0 <= y < height)
     * @param color     the new square color, or null for empty
     */
    public void setCorBloco(int x, int y, Color cor) {
        if (x < 0 || x >= largura || y < 0 || y >= altura) {
            return;
        }
        mundo[y][x] = cor;
        if (componente != null) {
            componente.invalidaBloco(x, y);
        }
    }

    /**
     * Sets a message to display on the square board. This is supposed 
     * to be used when the board is not being used for active drawing, 
     * as it slows down the drawing considerably.
     *
     * @param message  a message to display, or null to remove a
     *                 previous message
     */
    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
        if (componente != null) {
            componente.reDesenhaTudo();
        }
    }

    /**
     * Clears the board, i.e. removes all the colored squares. As 
     * side-effects, the number of removed lines will be reset to 
     * zero, and the component will be repainted immediately.
     */
    public void limpa() {
        LinhasRemovidas = 0;
        for (int y = 0; y < altura; y++) {
            for (int x = 0; x < largura; x++) {
                this.mundo[y][x] = null;
            }
        }
        if (componente != null) {
            componente.reDesenhaTudo();
        }
    }

    /**
     * Removes all full lines. All lines above a removed line will be 
     * moved downward one step, and a new empty line will be added at 
     * the top. After removing all full lines, the component will be 
     * repainted.
     * 
     * @see #hasFullLines
     */
    public void removeLinhasCompletas() {
        boolean repinta = false;

        // Remove full lines
        for (int y = altura - 1; y >= 0; y--) {
            if (isLinhaCompleta(y)) {
                removeLinha(y);
                LinhasRemovidas++;
                repinta = true;
                y++;
            }
        }

        // Repaint if necessary
        if (repinta && componente != null) {
            componente.reDesenhaTudo();
        }
    }

    /**
     * Removes a single line. All lines above are moved down one step, 
     * and a new empty line is added at the top. No repainting will be 
     * done after removing the line.
     *
     * @param y         the vertical position (0 <= y < height)
     */
    private void removeLinha(int y) {
        if (y < 0 || y >= altura) {
            return;
        }
        for (; y > 0; y--) {
            for (int x = 0; x < largura; x++) {
                mundo[y][x] = mundo[y - 1][x];
            }
        }
        for (int x = 0; x < largura; x++) {
            mundo[0][x] = null;
        }
    }

    /**
     * Updates the graphical component. Any squares previously changed 
     * will be repainted by this method.
     */
    public void actualizaMundo() {
        componente.reDesenha();
    }


    /**
     * The graphical component that paints the square board. This is
     * implemented as an inner class in order to better abstract the 
     * detailed information that must be sent between the square board
     * and its graphical representation.
     */
    private class interfaceTabuleiroJogo extends Component {

        /**
         * The component size. If the component has been resized, that 
         * will be detected when the paint method executes. If this 
         * value is set to null, the component dimensions are unknown.
         */
        private Dimension  size = null;

        /**
         * The component insets. The inset values are used to create a 
         * border around the board to compensate for a skewed aspect 
         * ratio. If the component has been resized, the insets values 
         * will be recalculated when the paint method executes.
         */
        private Insets  insets = new Insets(0, 0, 0, 0);

        /**
         * The square size in pixels. This value is updated when the 
         * component size is changed, i.e. when the <code>size</code> 
         * variable is modified.
         */
        private Dimension  tamanhoBloco = new Dimension(0, 0);

        /**
         * An image used for double buffering. The board is first
         * painted onto this image, and that image is then painted 
         * onto the real surface in order to avoid making the drawing
         * process visible to the user. This image is recreated each
         * time the component size changes.
         */
        private Image  bufferImage = null;

        /**
         * A clip boundary buffer rectangle. This rectangle is used 
         * when calculating the clip boundaries, in order to avoid 
         * allocating a new clip rectangle for each board square.
         */
        private Rectangle  bufferRect = new Rectangle();

        /**
         * The board message color.
         */
        private Color  corMensagem = Color.white;

        /**
         * A lookup table containing lighter versions of the colors.
         * This table is used to avoid calculating the lighter 
         * versions of the colors for each and every square drawn.
         */
        private Hashtable  coresClaras = new Hashtable();

        /**
         * A lookup table containing darker versions of the colors.
         * This table is used to avoid calculating the darker
         * versions of the colors for each and every square drawn.
         */
        private Hashtable  coresEscuras = new Hashtable();

        /**
         * A flag set when the component has been updated.
         */
        private boolean  actualizado = true;

        /**
         * A bounding box of the squares to update. The coordinates 
         * used in the rectangle refers to the square matrix.
         */
        private Rectangle  actualizaRect = new Rectangle();

        /**
         * Creates a new square board component.
         */
        public interfaceTabuleiroJogo() {
            setBackground(Configuration.getColor("board.background", 
                                                 "#000000"));
            corMensagem = Configuration.getColor("board.message", 
                                                  "#ffffff");
        }

        /**
         * Adds a square to the set of squares in need of redrawing.
         *
         * @param x     the horizontal position (0 <= x < width)
         * @param y     the vertical position (0 <= y < height)
         */
        public void invalidaBloco(int x, int y) {
            if (actualizado) {
                actualizado = false;
                actualizaRect.x = x;
                actualizaRect.y = y;
                actualizaRect.width = 0;
                actualizaRect.height = 0;
            } else {
                if (x < actualizaRect.x) {
                    actualizaRect.width += actualizaRect.x - x;
                    actualizaRect.x = x;
                } else if (x > actualizaRect.x + actualizaRect.width) {
                    actualizaRect.width = x - actualizaRect.x;
                }
                if (y < actualizaRect.y) {
                    actualizaRect.height += actualizaRect.y - y;
                    actualizaRect.y = y;
                } else if (y > actualizaRect.y + actualizaRect.height) {
                    actualizaRect.height = y - actualizaRect.y;
                }
            }
        }


        /**
         * Redraws all the invalidated squares. If no squares have 
         * been marked as in need of redrawing, no redrawing will 
         * occur.
         */
        public void reDesenha() {
            Graphics  g;

            if (!actualizado) {
                actualizado = true;
                g = getGraphics();
                g.setClip(insets.left + actualizaRect.x * tamanhoBloco.width,
                          insets.top + actualizaRect.y * tamanhoBloco.height,
                          (actualizaRect.width + 1) * tamanhoBloco.width,
                          (actualizaRect.height + 1) * tamanhoBloco.height);
                paint(g);
            }
        }

        /**
         * Redraws the whole component.
         */
        public void reDesenhaTudo() {
            Graphics  g;

            actualizado = true;
            g = getGraphics();
            g.setClip(insets.left, 
                      insets.top, 
                      largura * tamanhoBloco.width, 
                      altura * tamanhoBloco.height);
            paint(g);
        }

        /**
         * Returns true as this component is double buffered.
         * 
         * @return true as this component is double buffered
         */
        public boolean isDoubleBuffered() {
            return true;
        }
        
        /**
         * Returns the preferred size of this component.
         * 
         * @return the preferred component size
         */
        public Dimension getPreferredSize() {
            return new Dimension(largura * 20, altura * 20);
        }

        /**
         * Returns the minimum size of this component.
         * 
         * @return the minimum component size
         */
        public Dimension getMinimumSize() {
            return getPreferredSize();
        }

        /**
         * Returns the maximum size of this component.
         * 
         * @return the maximum component size
         */
        public Dimension getMaximumSize() {
            return getPreferredSize();
        }

        /**
         * Returns a lighter version of the specified color. The 
         * lighter color will looked up in a hashtable, making this
         * method fast. If the color is not found, the ligher color 
         * will be calculated and added to the lookup table for later
         * reference.
         * 
         * @param c     the base color
         * 
         * @return the lighter version of the color
         */
        private Color getCorClara(Color c) {
            Color  lighter;
            
            lighter = (Color) coresClaras.get(c);
            if (lighter == null) {
                lighter = c.brighter().brighter();
                coresClaras.put(c, lighter);
            }
            return lighter;
        }

        /**
         * Returns a darker version of the specified color. The 
         * darker color will looked up in a hashtable, making this
         * method fast. If the color is not found, the darker color 
         * will be calculated and added to the lookup table for later
         * reference.
         * 
         * @param c     the base color
         * 
         * @return the darker version of the color
         */
        private Color getCorEscura(Color c) {
            Color  darker;
            
            darker = (Color) coresEscuras.get(c);
            if (darker == null) {
                darker = c.darker().darker();
                coresEscuras.put(c, darker);
            }
            return darker;
        }

        /**
         * Paints this component indirectly. The painting is first 
         * done to a buffer image, that is then painted directly to 
         * the specified graphics context.
         * 
         * @param g     the graphics context to use
         */
        public synchronized void paint(Graphics g) {
            Graphics   bufferGraphics;
            Rectangle  rect;

            // Handle component size change
            if (size == null || !size.equals(getSize())) {
                size = getSize();
                tamanhoBloco.width = size.width / largura;
                tamanhoBloco.height = size.height / altura;
                if (tamanhoBloco.width <= tamanhoBloco.height) {
                    tamanhoBloco.height = tamanhoBloco.width;
                } else {
                    tamanhoBloco.width = tamanhoBloco.height;
                }
                insets.left = (size.width - largura * tamanhoBloco.width) / 2;
                insets.right = insets.left;
                insets.top = 0;
                insets.bottom = size.height - altura * tamanhoBloco.height;
                bufferImage = createImage(largura * tamanhoBloco.width, 
                                          altura * tamanhoBloco.height);
            }

            // Paint component in buffer image
            rect = g.getClipBounds();
            bufferGraphics = bufferImage.getGraphics();
            bufferGraphics.setClip(rect.x - insets.left, 
                                   rect.y - insets.top, 
                                   rect.width, 
                                   rect.height);
            paintComponent(bufferGraphics);

            // Paint image buffer
            g.drawImage(bufferImage, 
                        insets.left,
                        insets.top, 
                        getBackground(), 
                        null);
        }

        /**
         * Paints this component directly. All the squares on the 
         * board will be painted directly to the specified graphics
         * context.
         * 
         * @param g     the graphics context to use
         */
        private void paintComponent(Graphics g) {

            // Paint background
            g.setColor(getBackground());
            g.fillRect(0, 
                       0, 
                       largura * tamanhoBloco.width, 
                       altura * tamanhoBloco.height);
            
            // Paint squares
            for (int y = 0; y < altura; y++) {
                for (int x = 0; x < largura; x++) {
                    if (mundo[y][x] != null) {
                        paintSquare(g, x, y);
                    }
                }
            }

            // Paint message
            if (mensagem != null) {
                paintMessage(g, mensagem);
            }
        }

        /**
         * Paints a single board square. The specified position must 
         * contain a color object.
         *
         * @param g     the graphics context to use
         * @param x     the horizontal position (0 <= x < width)
         * @param y     the vertical position (0 <= y < height)
         */
        private void paintSquare(Graphics g, int x, int y) {
            Color  color = mundo[y][x];
            int    xMin = x * tamanhoBloco.width;
            int    yMin = y * tamanhoBloco.height;
            int    xMax = xMin + tamanhoBloco.width - 1;
            int    yMax = yMin + tamanhoBloco.height - 1;
            int    i;

            // Skip drawing if not visible
            bufferRect.x = xMin;
            bufferRect.y = yMin;
            bufferRect.width = tamanhoBloco.width;
            bufferRect.height = tamanhoBloco.height;
            if (!bufferRect.intersects(g.getClipBounds())) {
                return;
            }

            // Fill with base color
            g.setColor(color);
            g.fillRect(xMin, yMin, tamanhoBloco.width, tamanhoBloco.height);

            // Draw brighter lines
            g.setColor(getCorClara(color));
            for (i = 0; i < tamanhoBloco.width / 10; i++) {
                g.drawLine(xMin + i, yMin + i, xMax - i, yMin + i);
                g.drawLine(xMin + i, yMin + i, xMin + i, yMax - i);
            }

            // Draw darker lines
            g.setColor(getCorEscura(color));
            for (i = 0; i < tamanhoBloco.width / 10; i++) {
                g.drawLine(xMax - i, yMin + i, xMax - i, yMax - i);
                g.drawLine(xMin + i, yMax - i, xMax - i, yMax - i);
            }
        }

        /**
         * Paints a board message. The message will be drawn at the
         * center of the component.
         *
         * @param g     the graphics context to use
         * @param msg   the string message
         */
        private void paintMessage(Graphics g, String msg) {
            int  fontWidth;
            int  offset;
            int  x;
            int  y;

            // Find string font width
            g.setFont(new Font("SansSerif", Font.BOLD, tamanhoBloco.width + 4));
            fontWidth = g.getFontMetrics().stringWidth(msg);

            // Find centered position
            x = (largura * tamanhoBloco.width - fontWidth) / 2;
            y = altura * tamanhoBloco.height / 2;

            // Draw black version of the string
            offset = tamanhoBloco.width / 10;
            g.setColor(Color.black);
            g.drawString(msg, x - offset, y - offset);
            g.drawString(msg, x - offset, y);
            g.drawString(msg, x - offset, y - offset);
            g.drawString(msg, x, y - offset);
            g.drawString(msg, x, y + offset);
            g.drawString(msg, x + offset, y - offset);
            g.drawString(msg, x + offset, y);
            g.drawString(msg, x + offset, y + offset);

            // Draw white version of the string
            g.setColor(corMensagem);
            g.drawString(msg, x, y);
        }
    }
}
