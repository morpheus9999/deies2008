package dei.es2008;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;

public class Ficheiros {

   public Vector<Jogo> carregarJogo() throws IOException {
      // TODO: implement
      FileInputStream f_in;
		try {
			f_in = new
			FileInputStream("saveJogo.data");
			ObjectInputStream obj_in =
				new ObjectInputStream (f_in);
			Object obj = obj_in.readObject();
			if (obj instanceof Vector)
			{
				Vector v = (Vector) obj;
				//System.out.println(v);
				//System.out.println("--- ---");

				try{
					return (Vector) v;
				}
				catch(NullPointerException NPE){
					System.out.println("WOPS nullpointerexcepton");
                    return null;

				}
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
            return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
            return null;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
            return null;
		}
      return null;
   }

   public Boolean gravarJogo(Vector<Jogo> jogo) {
      FileOutputStream f_out;
      try {
			f_out = new FileOutputStream("saveJogo.data");
			ObjectOutputStream obj_out = new ObjectOutputStream (f_out);
			obj_out.writeObject ( jogo );
 
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
            return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
            return false;
		}

      return true;
   }

   public Boolean gravarRanking(Ranking ranking) {
      // TODO: implement
      return null;
   }

   public Ranking carregarRanking() {
      // TODO: implement
      return null;
   }

}