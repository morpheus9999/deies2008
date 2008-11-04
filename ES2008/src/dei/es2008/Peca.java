/***********************************************************************
 * Module:  Peca.java
 * Author:  Filipe
 * Purpose: Defines the Class Peca
 ***********************************************************************/
package dei.es2008;

import java.awt.Point;
import java.util.*;

/** @pdOid 2d5cdd22-f6e0-4a7e-a82d-b5e138dc265e */
public class Peca {

    /** @pdOid 80247ce6-c2ea-4844-836b-2755baa1cfc0 */
    private int tipo;
    private int numeroRotacao;
    /** @pdOid 6d38fb3a-ec66-47ac-8a66-1f0e6090dffd */
    private Point[] coordenadasPecas;
    /** @pdOid 81ae8e7e-f513-461e-ac9d-e88e9ede1c21 */
    private Point[][] ajustesPecasTipo1;
    private Point[][] ajustesPecasTipo2;
    private Point[][] ajustesPecasTipo3;
    private Point[][] ajustesPecasTipo4;
    private Point[][] ajustesPecasTipo5;
    private Point[][] ajustesPecasTipo6;
    private Mundo mundo;

    public Peca(Mundo mundo) {
        this.mundo = mundo;
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
    public void rodarPeca() {

        if (tipo == 1) {
            switch (numeroRotacao) {
                case 0:
                    if (mundo.verificaPosicaoPeca()) {
                        for (int i = 0; i < coordenadasPecas.length; i++) {
                            coordenadasPecas[i].x = coordenadasPecas[i].x + ajustesPecasTipo1[0][i].x;
                            coordenadasPecas[i].y = coordenadasPecas[i].y + ajustesPecasTipo1[0][i].y;
                        }
                        ;
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
