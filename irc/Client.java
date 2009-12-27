package irc;

import java.io.*;
import java.net.*;
import java.util.LinkedList;

public class Client extends Thread {
	private String host;
	private int port;
	
	private String nick;
	
	private Socket socket;
	private PrintWriter out;
	private BufferedReader in;

	private LinkedList<IRCEvent> callbacks;
	
	public Client(String host, int port, String nick) {
		this.host = host;
		this.port = port;
		this.nick = nick;
		
		callbacks = new LinkedList<IRCEvent>();
	}
	
	public boolean connect() {
		try {
            socket = new Socket(host, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (Exception e) {
            System.err.println("connection failed!");
        }
        
        out.println("NICK " + nick);
        out.println("USER b0t 0 0 :written in java :(");
        
        start();
        
		return true;
	}
	
	public void run() {
		while (socket.isConnected()) {
			try {
				String data = in.readLine();
				parseLine(data);
				System.out.println(data);
			} catch (IOException e) {
				System.err.println("receive failed!");
			}
		}
	}
	
	public void parseLine(String data) {
		if (data.startsWith("PING")) {
			out.println("PONG " + data.substring(data.indexOf(":"), data.length()));
	        //out.println("JOIN #c++");
	        return;
		} else if(data.startsWith("NOTICE AUTH")) {
			String [] splitData;
			splitData = data.split(":");
			IRCEventData eventData = new IRCEventData("NOTICE AUTH", null, null, null, null, splitData[1]);
			callback(eventData);
			return;
		}
	
		String [] splitData;
		splitData = data.split(" ");
		
		if (splitData[1].equals("PRIVMSG")) {
			splitData = data.split(":", 3);
			
			String text = splitData[2];
			
			String [] splitInfo = splitData[1].split(" ");
			
			String target = splitInfo[2];
			String sender = splitInfo[0];
			
			String senderIdent = sender.substring(sender.indexOf("!"), sender.indexOf("@"));
			String senderHost = sender.substring(sender.indexOf("@"), sender.length());
			sender = sender.substring(0, sender.indexOf("!"));
			
			IRCEventData eventData = new IRCEventData("PRIVMSG", target, sender, senderIdent, senderHost, text);
			callback(eventData);
		} else if (splitData[1].equals("JOIN")) {
			String target = splitData[2];
			
			String sender = splitData[0].substring(splitData[0].indexOf(":")+1, splitData[0].indexOf("!"));
			String senderIdent = splitData[0].substring(splitData[0].indexOf("!"), splitData[0].indexOf("@"));
			String senderHost = splitData[0].substring(splitData[0].indexOf("@"), splitData[0].length());
			
			IRCEventData eventData = new IRCEventData("JOIN", target, sender, senderIdent, senderHost, null);
			callback(eventData);
		} else if (splitData[1].equals("PART")) {
			String target = splitData[2];
			
			String sender = splitData[0].substring(splitData[0].indexOf(":")+1, splitData[0].indexOf("!"));
			String senderIdent = splitData[0].substring(splitData[0].indexOf("!"), splitData[0].indexOf("@"));
			String senderHost = splitData[0].substring(splitData[0].indexOf("@"), splitData[0].length());
			
			IRCEventData eventData = new IRCEventData("PART", target, sender, senderIdent, senderHost, null);
			callback(eventData);
		} else if (splitData[1].equals("TOPIC")) {
			splitData = data.split(":", 3);
			String text = splitData[2];
			String [] splitInfo = splitData[1].split(" ");
			String sender = splitInfo[0];
			
			String senderIdent = sender.substring(sender.indexOf("!"), sender.indexOf("@"));
			String senderHost = sender.substring(sender.indexOf("@"), sender.length());
			sender = sender.substring(0, sender.indexOf("!"));
			
			IRCEventData eventData = new IRCEventData("TOPIC", splitInfo[2], sender, senderIdent, senderHost, text);
			callback(eventData);
		} else if (splitData[1].equals("353")) {
			splitData = data.split(":", 3);
			String text = splitData[2];
			String [] splitInfo = splitData[1].split(" ");
			String sender = splitInfo[0];
			
			IRCEventData eventData = new IRCEventData("353", null, sender, null, null, text);
			callback(eventData);
		} else if (splitData[1].equals("332")) {
			splitData = data.split(":", 3);
			String text = splitData[2];
			String [] splitInfo = splitData[1].split(" ");
			String sender = splitInfo[0];
			String target = splitInfo[3];
			
			IRCEventData eventData = new IRCEventData("332", target, sender, null, null, text);
			callback(eventData);
		}
	}
	
	public void registerCallback(String event, IRCCallback callback) {
		callbacks.add(new IRCEvent(event, callback));
	}
	
	public void callback(IRCEventData data) {
		for(int i = 0; i < callbacks.size(); i++) {
			if (callbacks.get(i).getEvent().equals(data.getEvent())) {
				callbacks.get(i).getCallback().callback(data);
			}
		}
	}
	
	public void join(String channel, String password) {
		
	}
	
	public void part(String channel) {
		
	}
	
	public void privmsg(String target, String message) {
		out.println("PRIVMSG " + target + " :" + message);
	}
	
	public void notice(String target, String message) {
		
	}
	
	public void rawmsg(String msg) {
		out.println(msg);
	}

	/**
	 * @return the nick
	 */
	public String getNick() {
		return nick;
	}

	/**
	 * @param nick the nick to set
	 */
	public void setNick(String nick) {
		this.nick = nick;
	}
}
