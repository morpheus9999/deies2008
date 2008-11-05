package dei.es2008;

import java.io.*;
import java.util.*;
import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 * This class represents the high score list. It implements a Swing ListModel for
 * display purposes, and knows how convert a high score list to and from a string
 * (which is suitable for storing with the Preferences API or applet streams).
 *
 * @author   Ulf Dittmer
 */

public class Ranking implements ListModel {

	// the number of scores to keep track of;
	// note that the 'scores' list can be shorter, if there aren't that many scores yet
	private int numScores = 10;

	// a list of 'Score' objects, sorted by score in descending order
	private List scores;

	public Ranking() {
		this(10);
	}

	public Ranking (int numScores) {
		this.numScores = numScores;
		scores = new ArrayList();
	}

	public boolean putScore (int score) {
		if (scores.size() == 0) {
			scores.add(new Score(score, new Date()));
			notifyListeners();
			return true;
		}

		for (int i=0; i<scores.size(); i++) {
			Score sc = (Score) scores.get(i);
			if (score > sc.getScore()) {
				scores.add(i, new Score(score, new Date()));
				if (scores.size() > numScores)
					scores.remove(scores.size()-1);
				
				notifyListeners();
				return true;
			}
		}
		return false;
	}

	// notify listeners; there generally is only a single one
	private void notifyListeners() {
		Iterator iter = listeners.iterator();
		while (iter.hasNext()) {
			((ListDataListener) iter.next()).contentsChanged(
				new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0, numScores-1));
			// yeah, we could be more specific about which elements have been updated
		}
	}

	public String getSerializedScores() {
		StringBuffer sb = new StringBuffer(500);
		sb.append(numScores);
		for (int i=0; i<scores.size(); i++) {
			Score sc = (Score) scores.get(i);
			Date dt = sc.getDate();
			sb.append(",").append(sc.getScore());
			sb.append(",").append(dt.getYear());
			sb.append(",").append(dt.getMonth());
			sb.append(",").append(dt.getDate());
		}
		return sb.toString();
	}

	public void setSerializedScores (String serializedScores) {
		String[] parts = serializedScores.split(",");
		scores.clear();
		numScores = Integer.parseInt(parts[0]);
		for (int i=1; i<parts.length; i+=4) {
			scores.add(new Score(Integer.parseInt(parts[i]),
						new Date(Integer.parseInt(parts[i+1]),
								Integer.parseInt(parts[i+2]),
								Integer.parseInt(parts[i+3]))));
		}
	}

	// the following methods implement the ListModel interface
	public Object getElementAt (int index) {
		if (index < scores.size()) {
			Score sc = (Score) scores.get(index);
			Date dt = sc.getDate();
			String str = ""+sc.getScore();
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

	public int getSize() { return numScores; }

	private Set listeners = new HashSet();

	public void addListDataListener (ListDataListener l) { listeners.add(l); }

	public void removeListDataListener (ListDataListener l) { listeners.remove(l); }

	/**
	 * This class represents a single score and the date on which it was achieved.
	 */
	private class Score {
		int score;
		Date date;

		public Score (int score, Date date) {
			this.score = score;
			this.date = date;
		}

		public int getScore() { return score; }

		public Date getDate() { return date; }
	}
}

