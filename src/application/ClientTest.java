package application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ClientTest implements Runnable {
	Socket serverConnection;

	public ClientTest(String ip, String port) {
		serverConnection = new Socket();
		try {
			serverConnection.connect(new InetSocketAddress(ip, Integer.parseInt(port)));

		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void main(String args[]) {
		Thread myT = new Thread(new ClientTest(args[0], args[1]));
		myT.start();
	}
	/**
	 * Es wird der AnswerHandler erstellt und gestartet.
	 * Außerdem wird ein Printwriter erstellt mit dem Nachrichten an den Server geschickt werden können
	 */
	@Override
	public void run() {
		Thread answer = new Thread(new AnswerHandler(serverConnection));

		answer.start();
		PrintWriter out;
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		try {
			out = new PrintWriter(serverConnection.getOutputStream(), true);
			while (true) {
				String input = in.readLine();
				out.println(input);
				out.flush();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
