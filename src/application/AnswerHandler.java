package application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
/**
 * Die Klasse AnswerHandler ist eine Testklasse die die Antworten vom Server ausgibt die der Client bekommt
 * 
 * @author Friedemann Runte/Diyar Omar
 *
 */
public class AnswerHandler implements Runnable {
	Socket serverConnection;
	BufferedReader myI = null;

	public AnswerHandler(Socket connection) {
		serverConnection = connection;

	}

	/**
	 * Es wird mit einem BufferedReader ein InputStreamReader erstellt der alle Nachrichten die vom Server kommen ausgibt
	 */
	@Override
	public void run() {
		try {
			myI = new BufferedReader(new InputStreamReader(serverConnection.getInputStream()));
		} catch (IOException e) {
			System.out.println("IO EXCEPTION");
		}
		while (true) {
			String line;
			try {
				line = myI.readLine();

				if (myI != null) {
					System.out.println(line);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
