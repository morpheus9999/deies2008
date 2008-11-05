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
import java.util.*;

/** @pdOid 57eceda6-9a90-4a28-a307-5068d2611aec */
public class Mundo {
   /** @pdOid 1930c0ed-cfa2-4350-838b-b0cc41bb0106 */
   private Color[][] mundo;
   /** @pdOid 82e42fd5-a340-4160-adda-750ebefe7409 */
   private int pontuacao;
   /** @pdOid 5d5a055b-5faf-41be-aaaf-01bc4decee64 */
   private int nivel;
   /** @pdOid 51b5257f-2a17-4dcf-a86b-8a1495c78586 */
   private int tempoDecorrido;
   /** @pdOid 9fdd7696-a5e7-4e3e-8bba-23852a85bdb6
       @pdRoleInfo migr=yes name=Peca assc=association13 */
   private Peca peca=new Peca(this);
   
   /** @pdOid 062af6de-82ba-4a79-b5c3-52f9cf8d65cd */
   private int height;
   private int width;
   private String  message = null;
   private int  removedLines = 0;
   
   /**
     * The graphical sqare board component. This graphical 
     * representation is created upon the first call to getComponent().
     */
    private SquareBoardComponent  component = null;
   public Mundo(int width, int height) {
        
        this.mundo= new Color [height][width];
        this.pontuacao=0;
        
    }
   public Mundo() {
        //ver valores que meti
        this.mundo= new Color [10][30];
        this.pontuacao=0;
        
    }

    
         /**
     * Clears the board, i.e. removes all the colored squares. As 
     * side-effects, the number of removed lines will be reset to 
     * zero, and the component will be repainted immediately.
     */
    public void clear() {
        removedLines = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                this.mundo[y][x] = null;
            }
        }
        if (component != null) {
            component.redrawAll();
        }
    }
    
    /**
     * Checks if a specified square is empty, i.e. if it is not 
     * marked with a color. If the square is outside the board, 
     * false will be returned in all cases except when the square is 
     * directly above the board.
     *
     * @param x         the horizontal position (0 <= x < width)
     * @param y         the vertical position (0 <= y < height)
     * 
     * @return true if the square is emtpy, or false otherwise
     */
    public boolean isSquareEmpty(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return x >= 0 && x < width && y < 0;
        } else {
            return mundo[y][x] == null;
        }
    }
    
    
    
    /**
     * Changes the color of an individual square on the board. The 
     * square will be marked as in need of a repaint, but the 
     * graphical component will NOT be repainted until the update() method is called.
     *
     * @param x         the horizontal position (0 <= x < width)
     * @param y         the vertical position (0 <= y < height)
     * @param color     the new square color, or null for empty
     */
    public void setSquareColor(int x, int y, Color color) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return;
        }
        mundo[y][x] = color;
        if (component != null) {
            component.invalidateSquare(x, y);
        }
    }
    /**
     * Updates the graphical component. Any squares previously changed 
     * will be repainted by this method.
     */
    public void update() {
        component.redraw();
    }
     /**
     * Sets a message to display on the square board. This is supposed 
     * to be used when the board is not being used for active drawing, 
     * as it slows down the drawing considerably.
     *
     * @param message  a message to display, or null to remove a
     *                 previous message
     */
    public void setMessage(String message) {
        this.message = message;
        if (component != null) {
            component.redrawAll();
        }
    }

    void adicionarNivel(int i) {
        this.nivel=this.nivel+i;
    }

    int getNivel() {
        return this.nivel;
    }
   private void fundePeca() {
      // TODO: implement
       
   }
   
   /** @pdOid 9f0aea09-9310-488a-adff-645bb653980b */
   private Boolean verificaLinha() {
      // TODO: implement
      return null;
   }
   
   /** @pdOid 01d5a05a-ff10-46e9-b85b-b987ff70d7e9 */
   private Boolean apagaLinha() {
      // TODO: implement
      return null;
   }
   
   /** @pdOid be259b16-d8a2-4172-8fe1-018e95856dd6 */
   public Boolean adicionarPeca() {
       
      // TODO: implement
      return true;
   }
   
   /** @pdOid a76d8a35-d893-4c83-951a-ac9c822316ee */
   public boolean verificaPosicaoPeca() {
      // TODO: implement
       return true;
   }
   
   /** @pdOid 165c287f-14f4-4b2d-ab8d-722e596edfc5 */
   public void rodarPeca() {
      // TODO: implement
   }
   
   /** @param direccao
    * @pdOid 096d4194-4019-41f8-821c-576aa0ae5326 */
   public void deslocarPeca(int direccao) {
      // TODO: implement
   }
   public Component getComponent() {
        if (component == null) {
            component = new SquareBoardComponent();
        }
        return component;
    }
   
   public int getBoardHeight() {
        return height;
    }
   
   public int getBoardWidth() {
        return width;
    }
   private class SquareBoardComponent extends Component {

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
        private Dimension  squareSize = new Dimension(0, 0);

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
        private Color  messageColor = Color.white;

        /**
         * A lookup table containing lighter versions of the colors.
         * This table is used to avoid calculating the lighter 
         * versions of the colors for each and every square drawn.
         */
        private Map  lighterColors = new HashMap();

        /**
         * A lookup table containing darker versions of the colors.
         * This table is used to avoid calculating the darker
         * versions of the colors for each and every square drawn.
         */
        private Map  darkerColors = new HashMap();

        /**
         * A flag set when the component has been updated.
         */
        private boolean  updated = true;

        /**
         * A bounding box of the squares to update. The coordinates 
         * used in the rectangle refers to the square matrix.
         */
        private Rectangle  updateRect = new Rectangle();

        /**
         * Creates a new square board component.
         */
        public SquareBoardComponent() {
            setBackground(new Color(Integer.parseInt("000000", 16)));
            messageColor = new Color(Integer.parseInt("ffffff", 16));
        }

        /**
         * Adds a square to the set of squares in need of redrawing.
         *
         * @param x     the horizontal position (0 <= x < width)
         * @param y     the vertical position (0 <= y < height)
         */
        public void invalidateSquare(int x, int y) {
            if (updated) {
                updated = false;
                updateRect.x = x;
                updateRect.y = y;
                updateRect.width = 0;
                updateRect.height = 0;
            } else {
                if (x < updateRect.x) {
                    updateRect.width += updateRect.x - x;
                    updateRect.x = x;
                } else if (x > updateRect.x + updateRect.width) {
                    updateRect.width = x - updateRect.x;
                }
                if (y < updateRect.y) {
                    updateRect.height += updateRect.y - y;
                    updateRect.y = y;
                } else if (y > updateRect.y + updateRect.height) {
                    updateRect.height = y - updateRect.y;
                }
            }
        }


        /**
         * Redraws all the invalidated squares. If no squares have 
         * been marked as in need of redrawing, no redrawing will occur.
         */
        public void redraw() {
            Graphics  g;

            if (!updated) {
                updated = true;
                g = getGraphics();
                g.setClip(insets.left + updateRect.x * squareSize.width,
                          insets.top + updateRect.y * squareSize.height,
                          (updateRect.width + 1) * squareSize.width,
                          (updateRect.height + 1) * squareSize.height);
                paint(g);
            }
        }

        /**
         * Redraws the whole component.
         */
        public void redrawAll() {
            Graphics  g;

            updated = true;
            g = getGraphics();
            g.setClip(insets.left, 
                      insets.top, 
                      width * squareSize.width, 
                      height * squareSize.height);
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
            return new Dimension(width * 20, height * 20);
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
         * will be calculated and added to the lookup table for later reference.
         * 
         * @param c     the base color
         * 
         * @return the lighter version of the color
         */
        private Color getLighterColor(Color c) {
            Color  lighter;
            
            lighter = (Color) lighterColors.get(c);
            if (lighter == null) {
                lighter = c.brighter().brighter();
                lighterColors.put(c, lighter);
            }
            return lighter;
        }

        /**
         * Returns a darker version of the specified color. The 
         * darker color will looked up in a hashtable, making this
         * method fast. If the color is not found, the darker color 
         * will be calculated and added to the lookup table for later reference.
         * 
         * @param c     the base color
         * 
         * @return the darker version of the color
         */
        private Color getDarkerColor(Color c) {
            Color  darker;
            
            darker = (Color) darkerColors.get(c);
            if (darker == null) {
                darker = c.darker().darker();
                darkerColors.put(c, darker);
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
                squareSize.width = size.width / width;
                squareSize.height = size.height / height;
                if (squareSize.width <= squareSize.height) {
                    squareSize.height = squareSize.width;
                } else {
                    squareSize.width = squareSize.height;
                }
                insets.left = (size.width - width * squareSize.width) / 2;
                insets.right = insets.left;
                insets.top = 0;
                insets.bottom = size.height - height * squareSize.height;
                bufferImage = createImage(width * squareSize.width, height * squareSize.height);
            }

            // Paint component in buffer image
            rect = g.getClipBounds();
            bufferGraphics = bufferImage.getGraphics();
            bufferGraphics.setClip(rect.x - insets.left, rect.y - insets.top, rect.width, rect.height);
            paintComponent(bufferGraphics);

            // Paint image buffer
            g.drawImage(bufferImage, insets.left, insets.top, getBackground(), null);
        }

        /**
         * Paints this component directly. All the squares on the 
         * board will be painted directly to the specified graphics context.
         * 
         * @param g     the graphics context to use
         */
        private void paintComponent(Graphics g) {

            // Paint background
            g.setColor(getBackground());
            g.fillRect(0, 0, width * squareSize.width, height * squareSize.height);
            
            // Paint squares
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (mundo[y][x] != null) {
                        paintSquare(g, x, y);
                    }
                }
            }

            // Paint message
            if (message != null) {
                paintMessage(g, message);
            }
        }

        /**
         * Paints a single board square. The specified position must contain a color object.
         *
         * @param g     the graphics context to use
         * @param x     the horizontal position (0 <= x < width)
         * @param y     the vertical position (0 <= y < height)
         */
        private void paintSquare(Graphics g, int x, int y) {
            Color  color = mundo[y][x];
            int    xMin = x * squareSize.width;
            int    yMin = y * squareSize.height;
            int    xMax = xMin + squareSize.width - 1;
            int    yMax = yMin + squareSize.height - 1;
            int    i;

            // Skip drawing if not visible
            bufferRect.x = xMin;
            bufferRect.y = yMin;
            bufferRect.width = squareSize.width;
            bufferRect.height = squareSize.height;
            if (!bufferRect.intersects(g.getClipBounds())) {
                return;
            }

            // Fill with base color
            g.setColor(color);
            g.fillRect(xMin, yMin, squareSize.width, squareSize.height);

            // Draw brighter lines
            g.setColor(getLighterColor(color));
            for (i = 0; i < squareSize.width / 10; i++) {
                g.drawLine(xMin + i, yMin + i, xMax - i, yMin + i);
                g.drawLine(xMin + i, yMin + i, xMin + i, yMax - i);
            }

            // Draw darker lines
            g.setColor(getDarkerColor(color));
            for (i = 0; i < squareSize.width / 10; i++) {
                g.drawLine(xMax - i, yMin + i, xMax - i, yMax - i);
                g.drawLine(xMin + i, yMax - i, xMax - i, yMax - i);
            }
        }

        /**
         * Paints a board message. The message will be drawn at the center of the component.
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
            g.setFont(new Font("SansSerif", Font.BOLD, squareSize.width + 4));
            fontWidth = g.getFontMetrics().stringWidth(msg);

            // Find centered position
            x = (width * squareSize.width - fontWidth) / 2;
            y = height * squareSize.height / 2;

            // Draw black version of the string
            offset = squareSize.width / 10;
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
            g.setColor(messageColor);
            g.drawString(msg, x, y);
        }
    }

}