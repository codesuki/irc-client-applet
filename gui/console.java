package gui;

import irc.Client;
import irc.IRCCallback;
import irc.IRCEventData;

public class console implements IRCCallback {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		console con = new console();
		Client c = new Client("irc.quakenet.org", 6667, "kiwikiwi");
		c.connect();
		c.registerCallback("PRIVMSG", con);
		c.registerCallback("JOIN", con);
	}

	public void callback(IRCEventData data) {
		if (data.getEvent().equals("PRIVMSG")) {
			System.out.println("CONSOLE: " + data.getSender() + " -> " + data.getTarget() + ": " + data.getText());
		} else if (data.getEvent().equals("JOIN")) {
			System.out.println("CONSOLE: JOIN " + data.getSender() + " -> " + data.getTarget());
		}
	}

}
