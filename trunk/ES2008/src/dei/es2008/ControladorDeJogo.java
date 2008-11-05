package dei.es2008;

public class ControladorDeJogo {

    private Mundo association10;
    private Ficheiros ficheiro;
    private Ranking ranking;
    private Jogo kj;
    public Mundo mundo;
    public Jogo jogo;
    public Ficheiros ficheiros;

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
        ficheiro.gravarJogo(jogo);
    }

    public void carregarJogo() {
        ficheiro.carregarJogo();
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

    public void actualizaEstadoPeca(int estado) {        
        
    }

    public void actualizaMundo() {
        jogo.carregarInstante();
    }

    public void pausarjogo() {
        /*thread.setPaused(true);
        board.setMessage("Paused");
        component.button.setLabel("Resume");*/
    }
}