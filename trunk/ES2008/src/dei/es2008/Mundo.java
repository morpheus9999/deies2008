/***********************************************************************
 * Module:  Mundo.java
 * Author:  Filipe
 * Purpose: Defines the Class Mundo
 ***********************************************************************/

package dei.es2008;

import java.awt.Color;
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
   private Peca peca;
   
   /** @pdOid 062af6de-82ba-4a79-b5c3-52f9cf8d65cd */
   
   public Mundo(int width, int height) {
        
        this.mundo= new Color [height][width];
        this.pontuacao=0;
        
    }
   public Mundo() {
        //ver valores que meti
        this.mundo= new Color [10][30];
        this.pontuacao=0;
        
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
      return null;
   }
   
   /** @pdOid a76d8a35-d893-4c83-951a-ac9c822316ee */
   public void verificaPosicaoPeca() {
      // TODO: implement
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

}