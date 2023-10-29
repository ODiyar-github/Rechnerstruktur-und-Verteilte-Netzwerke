package application;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
public class MessageDispatcher implements Runnable{
	Socket myClient;
	ArrayList<Nachricht> messages;
	/**
	 * 
	 * @param pClient Der Client an den die Nachrichten gesendet werden
	 * @param pMessages Die Nachrichten die versendet werden 
	 */
	public MessageDispatcher(Socket pClient, ArrayList<Nachricht> pMessages)
	{
		myClient = pClient;
		messages = pMessages;
	}
	/**
	 * Die Run Methode verteilt die Nachrichten die übergeben worden sind an den Client der übergeben wurde
	 */
	@Override
	public void run()
	{
		try {
			PrintWriter out = new PrintWriter(myClient.getOutputStream(), true);
			out.println("N " + messages.size());
			
			for(int i=0; i<messages.size(); i++)
			{
				out.println(messages.get(i).getLength());
				out.println(messages.get(i).getTimestamp() + " " + messages.get(i).getTheme());
				out.println(messages.get(i).getInhalt());
			}
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}
}
