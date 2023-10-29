package application;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Queue;

/**
 * Die Klasse Nachrichtenhandler ist das NachrichtenBoard Sie verwaltet
 * Nachrichten und kann verschiedene Operation auf diese ausführen
 * 
 * @author Friedemann Runte/Diyar Omar
 *
 */
public class NachrichtenHandler {
	Queue<Nachricht> newMessages;
	ArrayList<Nachricht> messages;

	public NachrichtenHandler() {
		messages = new ArrayList<Nachricht>();
		newMessages = new ArrayDeque<Nachricht>();
	}

	/**
	 * addMessage fügt eine Nachricht zum Board hinzu und erwartet dazu alle
	 * Parameter einer Nachricht
	 * 
	 * @param pTime
	 * @param inhalt
	 * @param theme
	 * @param pLength
	 */
	public synchronized void addMessage(long pTime, String inhalt, String theme, int pLength) {
		System.out.println("NACHRICHT ERHALTEN: " + pTime + " " + inhalt + " " + theme);
		messages.add(new Nachricht(pTime, inhalt, theme, pLength));
		newMessages.add(new Nachricht(pTime, inhalt, theme, pLength));
	}

	/**
	 * Diese Methode holt alle Nachrichten vom Board die nach dem übergebenen
	 * Zeitpunkt auf das Board geschrieben wurden
	 * 
	 * @param time
	 * @return
	 */
	public ArrayList<Nachricht> getByTime(long time) {
		ArrayList<Nachricht> myN = new ArrayList<Nachricht>();
		for (int i = 0; i < messages.size(); i++) {
			if (messages.get(i).getTimestamp() >= time) {
				myN.add(messages.get(i));
			}
		}
		return myN;
	}

	/**
	 * Holt alle Nachrichten eines Themas vom Board
	 * 
	 * @param theme
	 * @return
	 */
	public ArrayList<Nachricht> getByTheme(String theme) {
		ArrayList<Nachricht> myN = new ArrayList<Nachricht>();
		for (int i = 0; i < messages.size(); i++) {
			if (messages.get(i).getTheme().equals(theme)) {
				System.out.println("Theme message: " + messages.get(i).getInhalt());

				myN.add(messages.get(i));
			}
		}
		if (myN.size() == 0) {
			myN.add(new Nachricht(0, "Keine Nachricht mit dem Thema" + theme + "  gefunden", "ERROR", 1));
		}
		Collections.sort(myN);
		return myN;
	}

	/**
	 * Holt alle Nachrichten der übergebenen Themen vom Board
	 * @param theme
	 * @return
	 */
	public ArrayList<Nachricht> getMultiThemes(ArrayList<String> theme) {
		ArrayList<Nachricht> myAll = new ArrayList<Nachricht>();
		for (int k = 0; k < theme.size(); k++) {
			ArrayList<Nachricht> myN = getByTheme(theme.get(k));

			myAll.addAll(myN);
		}
		return myAll;
	}
	// public ArrayList<Nachricht>

	public ArrayList<Nachricht> getNew() {
		ArrayList<Nachricht> myN = new ArrayList<Nachricht>();
		for (int i = 0; i < newMessages.size(); i++) {
			myN.add(newMessages.poll());
		}
		return myN;
	}

	public void SortMessages() {
		Collections.sort(messages);
	}
}
