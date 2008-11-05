/***********************************************************************
 * Module:  ControladorDeJogo.java
 * Author:  Filipe
 * Purpose: Defines the Class ControladorDeJogo
 ***********************************************************************/
package dei.es2008;

/** @pdOid cc38ef14-8c8c-48d3-8264-92a18d09f86e */
public class ControladorDeJogo {

    /** @pdOid 83dbf15f-1951-45d5-b706-21d5a2b8cff4
    @pdRoleInfo migr=yes name=Mundo assc=association10 */
    private Mundo association10;
    /** @pdOid 1d63d1cc-c8ca-4fcc-9846-a18bc215ba78 */
    private Ficheiros ficheiro;
    /** @pdOid 890685d1-5de0-406e-8591-570a9a61f876
    @pdRoleInfo migr=yes name=Ranking assc=association11 */
    private Ranking ranking;
    /** @pdOid 3090189f-68ae-4827-81c5-347827b5bde8
    @pdRoleInfo migr=yes name=Jogo assc=kj mult=1..1 */
    private Jogo kj;
    /** @pdRoleInfo migr=no name=Mundo assc=association12 mult=0..1 */
    public Mundo mundo;
    /** @pdRoleInfo migr=no name=Jogo assc=association15 mult=0..1 */
    public Jogo jogo;
    /** @pdRoleInfo migr=no name=Ficheiros assc=association16 mult=0..1 */
    public Ficheiros ficheiros;

    public void ControladorDeJogo() {
        // TODO: implement
    }
    
    /** @pdOid 8a29e47a-dd73-4a0d-a56f-9206850587c4 */
    
    public void iniciarJogo() {
        this.mundo=new Mundo();
    }

    /** @pdOid c7ee8da6-ce9f-4a5d-ae80-c7b0adee3768 */
    public void sairJogo() {
       System.exit(0);
    }

    /** @pdOid c5ff1938-3ac8-433f-8755-61a2a68c8d56 */
    public void verRanking() {
      ficheiro.carregarRanking();
      //assumindo que ele na classe ficheiros carrega e imprime
    }

    /** @pdOid ebe7fd04-e4b6-4bc0-94aa-63b452b25c22 */
    public void gravarJogo() {
        ficheiro.gravarJogo(jogo);
    }

    /** @pdOid 26d35a9f-f9c4-4ec1-9ff5-2f939826e711 */
    public void carregarJogo() {
        ficheiro.carregarJogo();
    }

    /** @pdOid af659177-3570-4a70-a061-13d418c57cc9 */
    public void inserirCodigo() {
        // TODO: implement
    }

    /** @param estado
     * @pdOid aa36fe23-641f-4d92-98dd-a68c2ccd9555 */
    public void actualizaEstadoPeca(int estado) {
        
        
    }

    /** @pdOid 9a6201c4-bc46-487c-ba67-a4249f8ded95 */
    public void actualizaMundo() {
        jogo.carregarInstante();
    }

    /** @pdOid f17cdf90-d33a-4800-9d47-7079d34a3a1d */
    public void pausarjogo() {
        /*thread.setPaused(true);
        board.setMessage("Paused");
        component.button.setLabel("Resume");*/
    }
}