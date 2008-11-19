package dei.es2008;
import java.util.*;

public class Jogo {

   public Peca peca;
   public int rotacaoInicialPeca;
   public Peca pecaSeguinte;
   public int rotacaoInicialPecaSeguinte;
   public Vector <Percurso>tabela;


   
   Jogo(Peca peca,int rotacaoInicialPeca, Peca pecaSeguinte,int rotacaoInicialPecaSeguinte) {

      
      this.peca=peca;
      this.pecaSeguinte=pecaSeguinte;
      this.rotacaoInicialPeca=rotacaoInicialPeca;
      this.rotacaoInicialPecaSeguinte=rotacaoInicialPecaSeguinte;
      tabela = new Vector();
      
   }
   
   public boolean guardarInstante(long temp , int tipo,int nivel) {
      try{


			tabela.add(new Percurso(temp,  tipo, nivel));
	}catch(Exception e){
        return false;
    }

		return true;

      
   }
   

    

   public  class Percurso {
		long  temp;
		int tipo;
        int nivel;

		public Percurso (long temp, int tipo,int nivel) {
			this.temp = temp;
			this.tipo = tipo;
            this.nivel=nivel;
            
		}

		public long getTempo() { return temp; }

		public int getTipo() { return tipo; }

        public int getNivel() { return nivel; }
	}

}