package application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

/**
 * Der ClientHandler ist zum empfangen und Interpretieren der Nachrichten des
 * Clients da.
 * 
 * @author Friedemann Runte, Diyar Omar
 *
 */
public class ClientHandler implements Runnable {
	Socket client;
	Server myServer;

	public ClientHandler(Socket myClient, Server myS) {
		client = myClient;
		myServer = myS;
	}

	/**
	 * Die Run-Methode interpretiert die Nachrichten die vom Client Kommen wie
	 * es das Protokoll vorsieht: "P" Signalisiert eine Anzahl an eintreffenden
	 * Nachrichten "W" Signalisiert eine Anfrage nach Nachrichten die nach einem
	 * bestimmten Zeitpunkt geschrieben wurden "T" Signalisiert eine Anfrage
	 * nach Nachrichten mit einem bestimmten Thema "L" Signalisiert eine Anfrage
	 * nach Nachrichten mit den Themen die übergeben wurden "X" Beendet die
	 * Verbindung
	 */
	@Override
	public void run() {

		try {
			PrintWriter out = new PrintWriter(client.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			while (true) {
				try {
					String line = in.readLine();
					System.out.println(line);
					if (line != null) {
						String first = line.substring(0, 1);
						if (first.equals("P")) {
							try {
								StringTokenizer myS = new StringTokenizer(line, " ");
								myS.nextToken();
								int number = Integer.parseInt(myS.nextToken());
								for (int j = 0; j < number; j++) {
									line = in.readLine();
									System.out.println(line);
									String nachricht = "";

									int n = Integer.parseInt(line);
									String timeTheme = in.readLine();
									StringTokenizer myST = new StringTokenizer(timeTheme, " ");
									long time = Long.parseLong(myST.nextToken());
									String theme = "";
									while (myST.hasMoreTokens()) {
										theme = theme + " " + myST.nextToken();
									}
									nachricht = in.readLine();
									for (int i = 1; i < n - 1; i++) {
										System.out.println(i + " " + nachricht);
										nachricht = nachricht + "\n" + in.readLine();
									}
									myServer.writeMessage(time, nachricht, theme, n);
									client.setKeepAlive(true);
								}

							} catch (NoSuchElementException e1) {

								out.println("E Wrong Format");
							} catch (NumberFormatException e2) {
								out.println("E Wrong Format");
							}
						} else {
							if (first.equals("W")) {
								if (line.substring(1, 2).equals(" ")) {
									line = line.substring(2);
									myServer.dispatchMessage(myServer.myN.getByTime(Long.parseLong(line)));
								} else {
									out.println("E FORMAT ERROR");
								}
							} else {
								if (first.equals("T")) {
									if (line.substring(1, 2).equals(" ")) {
										line = line.substring(2);
										myServer.dispatchMessage(myServer.myN.getByTheme(line));
									} else {
										out.println("E FORMAT ERROR");
									}

								} else {
									if (first.equals("L")) {
										if (line.substring(1, 2).equals(" ")) {
											StringTokenizer myST = new StringTokenizer(line.substring(2), " ");
											ArrayList<String> myList = new ArrayList<String>();
											while (myST.hasMoreTokens()) {
												String currentTheme = myST.nextToken();
												System.out.println(currentTheme);
												myList.add(currentTheme);
											}
											myServer.dispatchMessage(myServer.myN.getMultiThemes(myList));
										} else {
											out.println("E FORMAT ERROR");
										}
									} else {
										if (first.equals("X")) {
											out.println("E Verbindung wurde getrennt");

											out.close();
											client.close();

										} else {
											out.println("E FORMAT ERROR");
										}

									}
								}
							}
						}

					}
				} catch (SocketException e) {
				}
			}
		} catch (

		IOException e) {
			e.printStackTrace();
		}
	}

}
