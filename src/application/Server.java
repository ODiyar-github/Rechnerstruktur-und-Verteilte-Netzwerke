package application;

import java.io.IOException;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Server implements Runnable {
	private ServerSocket server;
	private InetSocketAddress address;
	private ArrayList<Socket> myClients;
	NachrichtenHandler myN = new NachrichtenHandler();
	public Server(String pAddress, int port)
	{ 
		
		address = new InetSocketAddress(pAddress,port);
		System.out.println("Verbunden an: " + address.toString());
	
	}
	/**
	 * Standard Run Methode fuer den Server:
	 * Diese wird von der Main-Methode aufgerufen und ist verantwortlich fuer das annehmen von Clients
	 * Wenn ein Client verbunden wird, wird ein neuer Client-Handler fuer ihn erstellt.
	 * Dieser ist verantwortlich fuer das interpretieren der Nachrichten des Clients
	 * 
	 */
	@Override
	public void run() {
		System.out.println("Server Gestartet");
		myClients = new ArrayList<Socket>();
		server = null;
		try {
			server = new ServerSocket();
			server.bind(address);
		} catch (IOException e1) {
			System.out.println("Das ServerSocket konnte nicht mit den angegebenen Parametern erstellt werden. Überprüfen sie ob ihre Eingaben gültig sind und ob die Addresse nicht bereits durch eine andere Anwendung belegt wurde");
		}
		while (true) {
			try {
				Socket myClient = server.accept();
				myClients.add(myClient);
				System.out.println("Client Connected");
				Thread myThread = new Thread(new ClientHandler(myClient,this), myClient.getInetAddress().toString());
				myThread.start();
				
			} catch (IOException e) {
				continue;
			}
		}
	}
	/**
	 * Erstellt einen MessageDispatcher fuer alle Clients um an diese die Nachrichten zu verteilen, die im Parameter uebergeben werden.
	 * @param messages: Eine ArrayList von Nachrichten die an die Clients verteilt werden sollen 
	 */
	public void dispatchMessage(ArrayList<Nachricht>  messages)
	{
		for(int i=0; i<myClients.size(); i++)
		{
			Thread myT = new Thread(new MessageDispatcher(myClients.get(i), messages));
			myT.start();
		}
		
	}
	/**
	 * Sendet eine Liste von Nachrichten an einen Client
	 * @param messages Die Nachrichten die versendet werden sollen
	 * @param myClient Das Socket an welches die Daten geschickt werden sollen
	 */
	public void sendMessage(ArrayList<Nachricht>  messages, Socket myClient)
	{
		
			Thread myT = new Thread(new MessageDispatcher(myClient, messages));
			myT.start();
		
		
	}
	/**
	 * Schreibt eine Nachricht auf das Nachrichtenboard und sendet dann alle neuen Nachrichten an alle Clients
	 * @param time 			Der Timestamp der Nachricht
	 * @param pInhalt 		Der Inhalt der Nachrichten
	 * @param pTheme		Das Thema der Nachricht
	 * @param pLength		Die Anzahl an Zeilen der Nachricht
	 */
	public synchronized void writeMessage(long time, String pInhalt, String pTheme,int pLength)
	{
		myN.addMessage(time, pInhalt,pTheme,pLength);
		dispatchMessage(myN.getNew());
	}
	/**
	 * Start des Programms mit Parametern
	 * @param args args[0] = Ip-Adresse; args[1] = Port der Anwendung
	 */
	public static void main(String args[])
	{
		try{
		Thread myT = new Thread(new Server(args[0],Integer.parseInt(args[1])), "server");
		myT.start();
		} catch (ArrayIndexOutOfBoundsException e)
		{
			System.out.println("Die Anzahl der Parameter ist falsch! Bitte geben sie als ersten Parameter die gewünschte IP und als zweiten Parameter den Port auf dem der Server laufen soll!");
		}
	}

}
