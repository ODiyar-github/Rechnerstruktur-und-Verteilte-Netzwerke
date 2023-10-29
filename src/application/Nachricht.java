package application;
/**
 * Nachricht ist die Klasse die eine Nachricht darstellt, die auf dem Board landet
 * Sie beinhaltet folgende Attribute:
 * long timestamp: Die Zeit in ms in UTC
 * String inhalt: Der Inhalt der Nachricht
 * String Thema: Das Thema der Nachricht
 * int length: Die Anzahl der Zeilen
 * 
 * @author Friedemann Runte/Diyar Omar
 *
 */
public class Nachricht implements Comparable<Nachricht>
{
	long timestamp;
	String inhalt;
	String theme;
	int length;
	public Nachricht(long pTime,String pInhalt, String pTheme, int pLength)
	{
		timestamp = pTime;
		inhalt = pInhalt;
		theme = pTheme;
		length = pLength;
	}
	public String getTheme()
	{
		return theme;
	}
	public String getInhalt()
	{
		return inhalt;
	}
	public long getTimestamp()
	{
		return timestamp;
	}
	public void show()
	{
		System.out.println("TIME : " + timestamp + " THEME: " + theme + " INHALT: " + inhalt + " LENGTH: " + length);
	}
	public int getLength()
	{
		return length;
	}
	@Override
	public int compareTo(Nachricht arg0) {
		return (int)(this.getTimestamp() - arg0.getTimestamp());
	}
}