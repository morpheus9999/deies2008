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
     * Devolve uma componente grárica para desenhar o tabuleiro. Esta componente
     * irá ser automaticamente actualizada quando são feitas mudanças ao
     * tabuleiro. Multiplas chamadas a este método vao devolver a mesma 
     * componente, visto que só existe um mundo.
     * 
     * @return Devolve uma componente gráfica que desenha este tabuleiro
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
     * Devolve a altura do tabuleiro (em quadrados). Este método devolve o
     * número de quadrados verticais que cabem no tabuleiro.
     * 
     * @return a altura do tabuleiro em quadrados
     */
    public int getAlturaTabuleiro() {
        return altura;
    }

    /**
     * Devolve a largura do tabuleiro (em quadrados). Este método devolve o
     * número de quadrados na horizontal que cabem no tabuleiro.
     * 
     * @return a largura do tabuleiro em quadrados.
     */
    public int getLarguraTabuleiro() {
        return largura;
    }

    /**
     * Devolve o número de linhas removidas desde o último clear().
     * 
     * @return o número de linhas removidas desde a última chamada do clear()
     */
    public int getLinhasRemovidas() {
        return LinhasRemovidas;
    }

    /**
     * Devolve a cor de um quadrado específico do tabuleiro. Se o quadrado
     * estiver vazio ou fora do tabuleiro retorna null.
     *
     * @param x         a posição horizontal (0 <= x < width)
     * @param y         a posição vertical (0 <= y < height)
     * 
     * @return a cor do quadrado, ou null se não a tiver.
     */
    public Color getCorBloco(int x, int y) {
        if (x < 0 || x >= largura || y < 0 || y >= altura) {
            return null;
        } else {
            return mundo[y][x];
        }
    }

    /**      
     * Muda a cor de um quadrado específico do tabuleiro. O quadrado vai ser
     * marcado para ser repintado, mas a componente gráfica não o vai repintar
     * até o método update() ser chamado.
     *
     * @param x         a posição horizontal (0 <= x < width)
     * @param y         a posição vertical (0 <= y < height)
     * @param color     a nova posição do quadrado, null se estiver vazio
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
     * Define uma mensagem a ser mostrada no tabuleiro.`É suposto ser usado
     * apenas quando o tabuleiro não estiver a ser utilizado para desenhos, 
     * uma vez que atrasa o processo de desenho consideravelmente.
     * 
     * @param message  uma mensagem a ser imprimida, null se se pretender 
     * remover
     */
    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
        if (componente != null) {
            componente.reDesenhaTudo();
        }
    }

    /**
     * Limpa o tabuleiro, ou seja, remove todos os quadrados coloridos.
     * Também faz reset ao número de linhas completas, e a componente vai ser
     * repintada imediatamente.
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
     * Remove todas as linhas cheias. Todas as linhas acima vão ser movidas 
     * para baixo um quadrado, e uma nova linha vazia vai ser adicionada no
     * topo. Após remover todas as linhas cheias a componente vai ser repintada
     * 
     * @ver #hasFullLines
     */
    public void removeLinhasCompletas() {
        boolean repinta = false;

        // Remove as linhas cheias
        for (int y = altura - 1; y >= 0; y--) {
            if (isLinhaCompleta(y)) {
                removeLinha(y);
                LinhasRemovidas++;
                repinta = true;
                y++;
            }
        }

        // Repinta se necessário
        if (repinta && componente != null) {
            componente.reDesenhaTudo();
        }
    }

    /**
     * Remove uma única linha, todas as linhas acima são movidas um passo abaixo
     * e uma nova linha vazia é adicionada ao topo. Não vai haver repinturas
     * após remover a linha.
     * 
     * @param y         a posição vertical (0 <= y < height)
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
     * Actualiza a componente gráfica. Qualquer quadrado previamente mudado
     * irá ser repintado por este método.
     */
    public void actualizaMundo() {
        componente.reDesenha();
    }


    /**
     * A componente gráfica que pinta o tabuleiro. Isto é implementado
     * como uma classe interior de maneira a facilitar a abstracção da
     * informação detalhada que tem que ser enviada entre o tabuleiro e a
     * sua representação gráfica.
     */
    private class interfaceTabuleiroJogo extends Component {

        /**
         * A componente tamanho. Se esta componente for redimencionada, isso
         * irá ser detectado quando o método de pintura for executado.
         * Se este valor for designado como null as dimensões serão
         * desconhecidas.
         */
        private Dimension  size = null;

        /**
         * A componente insets. Os valores de inset são usados para
         * criar uma fronteira á volta do tabuleiro de maneira a compensar
         * o aspecto desproporcional . Se o componente for redimencionado, 
         * os valores inset vao ser recalculados quando o método de pintura
         * for executado.
         */
        private Insets  insets = new Insets(0, 0, 0, 0);

        /**
         * O tamanho dos quadrados em pixels. Este valor é actualizado quando
         * o componente tamanho e alterado.
         */
        private Dimension  tamanhoBloco = new Dimension(0, 0);

        /**
         * Uma imagem usada para buffering duplo. O tabuleiro e primeiro
         * pintado para esta imagem, depois essa imagem é enttão pintada na
         * superfície "real" de maneira a evitar mostrar este processo vísivel
         * ao utilzador. Esta imagem será então recreada cada vez que a 
         * componente tamanho (size) muda.
         */
        private Image  bufferImage = null;

        /**
         * A clip boundary buffer rectangle. This rectangle is used 
         * when calculating the clip boundaries, in order to avoid 
         * allocating a new clip rectangle for each board square.
         */
        private Rectangle  bufferRect = new Rectangle();

        /**
         * A cor da mensagem
         */
        private Color  corMensagem = Color.white;

        /**
         * Uma tabela de pesquisa que contém vesões mais claras das cores.
         * Esta tabela é usada para evitar calcular as versões mais claras
         * das cores para cada quadrado desenhado.
         */
        private Hashtable  coresClaras = new Hashtable();

        /**
         * Uma tabela de pesquisa que contém vesões mais escuras das cores.
         * Esta tabela é usada para evitar calcular as versões mais escuras
         * das cores para cada quadrado desenhado.
         */
        private Hashtable  coresEscuras = new Hashtable();

        /**
         * Uma flag definida para quando o componente for actualizado.
         */
        private boolean  actualizado = true;

        /**
         * Uma caixa delimitadora dos quadrados para actualizar. As coordenadas
         * usadas no rectangulo referem-se ao quadrado matriz.
         */
        private Rectangle  actualizaRect = new Rectangle();

        /**
         * Cria uma nova componente tabuleiro
         */
        public interfaceTabuleiroJogo() {
            setBackground(Configuration.getColor("board.background", 
                                                 "#000000"));
            corMensagem = Configuration.getColor("board.message", 
                                                  "#ffffff");
        }

        /**
         * Adiciona um quadrado ao conjunto de quadrados designados para
         * redesenho.
         *
         * @param x     a posição horizontal (0 <= x < width)
         * @param y     a posição vertical (0 <= y < height)
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
         * Redesenha todos os quadrados inúteis. Se nenhum quadrado estiver
         * marcado como em espera para redesenho então nenhum será feito.
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
         * Redesenha toda a componente.
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
         * Retorna true uma vez que o componente é buffered duplamente.
         * 
         * @return true uma vez que o componente é buffered duplamente
         */
        public boolean isDoubleBuffered() {
            return true;
        }
        
        /**
         * Retorna o tamanho preferido deste componente.
         * 
         * @return o tamanho preferido deste componente
         */
        public Dimension getPreferredSize() {
            return new Dimension(largura * 20, altura * 20);
        }

        /**
         * Retorna o tamanho mínimo deste componente.
         * 
         * @return o tamanho mínimo deste componente
         */
        public Dimension getMinimumSize() {
            return getPreferredSize();
        }

        /**
         * Retorna o tamanho máximo deste componente.
         * 
         * @return o tamanho máximo deste componente
         */
        public Dimension getMaximumSize() {
            return getPreferredSize();
        }

        /**
         * Retorna uma versão mais clara da cor especificada. A cor mais clara
         * ira ser procurada na hashtable, tornando este método rápido. Se a cor
         * não for encontrada, então a cor mais clara irá ser calculada e
         * adicionada à tabela de pesquisa para referência futura.
         * 
         * @param c     a cor base
         * 
         * @return a versão mais clara da cor
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
         * Retorna uma versão mais escura da cor especificada. A cor mais clara
         * ira ser procurada na hashtable, tornando este método rápido. Se a cor
         * não for encontrada, então a cor mais escura irá ser calculada e
         * adicionada à tabela de pesquisa para referência futura.
         * 
         * @param c     a cor base
         * 
         * @return a versão mais escura da cor
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
         * Pinta esta componente indirectamente. A pintura é primeiro feita
         * a uma imagem buffer, é então pintada directamente para o contexto
         * gráfico especificado.
         * 
         * @param g     o contexto gráfico a usar
         */
        public synchronized void paint(Graphics g) {
            Graphics   bufferGraphics;
            Rectangle  rect;

            // Gere a mudança da componente size
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

            // Pinta a componente na imagem buffer
            rect = g.getClipBounds();
            bufferGraphics = bufferImage.getGraphics();
            bufferGraphics.setClip(rect.x - insets.left, 
                                   rect.y - insets.top, 
                                   rect.width, 
                                   rect.height);
            paintComponent(bufferGraphics);

            // Pinta a imagem buffer
            g.drawImage(bufferImage, 
                        insets.left,
                        insets.top, 
                        getBackground(), 
                        null);
        }

        /**
         * Pinta esta componente directamente. Todos os quadrados no tabuleiro
         * vão ser pintados directamente para o contexto gráfico designado.
         * 
         * @param g     o contexto gráfico a usar
         */
        private void paintComponent(Graphics g) {

            // Pinta o fundo
            g.setColor(getBackground());
            g.fillRect(0, 
                       0, 
                       largura * tamanhoBloco.width, 
                       altura * tamanhoBloco.height);
            
            // Pinta quadrados
            for (int y = 0; y < altura; y++) {
                for (int x = 0; x < largura; x++) {
                    if (mundo[y][x] != null) {
                        paintSquare(g, x, y);
                    }
                }
            }

            // Pinta mensagem
            if (mensagem != null) {
                paintMessage(g, mensagem);
            }
        }

        /**
         * Pinta um único quadrado do tabuleiro. A posição especificada
         * tem que conter um objecto de cor.
         *
         * @param g     o contexto gráfico a usar
         * @param x     a posição horizontal (0 <= x < width)
         * @param y     a posição vertical (0 <= y < height)
         */
        private void paintSquare(Graphics g, int x, int y) {
            Color  color = mundo[y][x];
            int    xMin = x * tamanhoBloco.width;
            int    yMin = y * tamanhoBloco.height;
            int    xMax = xMin + tamanhoBloco.width - 1;
            int    yMax = yMin + tamanhoBloco.height - 1;
            int    i;

            // Evita desenhar se não for visivel
            bufferRect.x = xMin;
            bufferRect.y = yMin;
            bufferRect.width = tamanhoBloco.width;
            bufferRect.height = tamanhoBloco.height;
            if (!bufferRect.intersects(g.getClipBounds())) {
                return;
            }

            // Enche com a cor base
            g.setColor(color);
            g.fillRect(xMin, yMin, tamanhoBloco.width, tamanhoBloco.height);

            // Desenha linhas mais claras
            g.setColor(getCorClara(color));
            for (i = 0; i < tamanhoBloco.width / 10; i++) {
                g.drawLine(xMin + i, yMin + i, xMax - i, yMin + i);
                g.drawLine(xMin + i, yMin + i, xMin + i, yMax - i);
            }

            // Desenha linhas mais escuras
            g.setColor(getCorEscura(color));
            for (i = 0; i < tamanhoBloco.width / 10; i++) {
                g.drawLine(xMax - i, yMin + i, xMax - i, yMax - i);
                g.drawLine(xMin + i, yMax - i, xMax - i, yMax - i);
            }
        }

        /**
         * Pinta a mensagem no tabuleiro. Vai aparecer no centro
         *
         * @param g     o contexto gráfico a usar
         * @param msg   a mensagem em string
         */
        private void paintMessage(Graphics g, String msg) {
            int  fontWidth;
            int  offset;
            int  x;
            int  y;

            // Encontra a largura da fonte da string
            g.setFont(new Font("SansSerif", Font.BOLD, tamanhoBloco.width + 4));
            fontWidth = g.getFontMetrics().stringWidth(msg);

            // Encontra a posição centrada
            x = (largura * tamanhoBloco.width - fontWidth) / 2;
            y = altura * tamanhoBloco.height / 2;

            // Desenha uma versão preta da string
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

            // Desenha uma versão branca da string
            g.setColor(corMensagem);
            g.drawString(msg, x, y);
        }
    }
}
