package dei.es2008;

import java.util.*;
import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 * Classe responsavel pela manipulacao do ranking
 */

public class Ranking implements ListModel {

	private int numPontucoes = 10;
	private List pontuacoes;

	public Ranking() {
		this(10);
	}

	public Ranking (int numPontucoes) {
		this.numPontucoes = numPontucoes;
		pontuacoes = new ArrayList();
	}

	public boolean inserePontuacao (int pontuacao, String Nome) {
		if (pontuacoes.size() == 0) {
			pontuacoes.add(new Pontuacao(pontuacao, new Date(), Nome));
			notifyListeners();
			return true;
		}

		for (int i=0; i<pontuacoes.size(); i++) {
			Pontuacao sc = (Pontuacao) pontuacoes.get(i);
			if (pontuacao > sc.getPontuacao()) {
				pontuacoes.add(i, new Pontuacao(pontuacao, new Date(), Nome));
				if (pontuacoes.size() > numPontucoes)
					pontuacoes.remove(pontuacoes.size()-1);
				
				notifyListeners();
				return true;
			}
		}
		return false;
	}

	private void notifyListeners() {
		Iterator iter = listeners.iterator();
		while (iter.hasNext()) {
			((ListDataListener) iter.next()).contentsChanged(
				new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0, numPontucoes-1));
		}
	}

	public String getSerializedPontuacoes() {
		StringBuffer sb = new StringBuffer(500);
		sb.append(numPontucoes);
		for (int i=0; i<pontuacoes.size(); i++) {
			Pontuacao sc = (Pontuacao) pontuacoes.get(i);
			Date dt = sc.getData();
			sb.append(",").append(sc.getPontuacao());
			sb.append(",").append(dt.getYear());
			sb.append(",").append(dt.getMonth());
			sb.append(",").append(dt.getDate());
            sb.append(",").append(sc.getNome());
		}
		return sb.toString();
	}

	public void setSerializedPontuacoes (String serializedPontuacoes) {
		String[] parts = serializedPontuacoes.split(",");
		pontuacoes.clear();
		numPontucoes = Integer.parseInt(parts[0]);
		for (int i=1; i<parts.length; i+=5) {
			pontuacoes.add(new Pontuacao(Integer.parseInt(parts[i]),
						new Date(Integer.parseInt(parts[i+1]),
								Integer.parseInt(parts[i+2]),
								Integer.parseInt(parts[i+3])),
                           parts[i+4]));
		}
	}

	public Object getElementAt (int index) {
		if (index < pontuacoes.size()) {
			Pontuacao sc = (Pontuacao) pontuacoes.get(index);
			Date dt = sc.getData();
			String str = ""+sc.getPontuacao();
                        str=str+" "+sc.getNome();
			while (str.length() < 5)
				str = " "+str;

			int year = dt.getYear() - 100;
			int month = dt.getMonth() + 1;
			int day = dt.getDate();

			return str+"   "
					+ (year<10 ? "0" : "") + year + "/"
					+ (month<10 ? "0" : "") + month + "/"
					+ (day<10 ? "0" : "") + day;
                        
		} else {
			return "";
		}
	}

	public int getSize() { return numPontucoes; }

	private Set listeners = new HashSet();

	public void addListDataListener (ListDataListener l) { listeners.add(l); }

	public void removeListDataListener (ListDataListener l) { listeners.remove(l); }

	/**
	 * Esta classe representa o contrutor de uma pontuacao.
         * Esta guarda o valor da pontuacao e associa a data a mesma.
	 */
	private class Pontuacao {
		int pontuacao;
		Date data;
        String Nome;

		public Pontuacao (int pontuacao, Date data, String Nome) {
			this.pontuacao = pontuacao;
			this.data = data;
            this.Nome = Nome;
		}

		public int getPontuacao() { return pontuacao; }

		public Date getData() { return data; }
                
        public String getNome() { return Nome; }
	}
}

