package irc;

public class IRCEventData {
	private String event;
	private String target;
	private String sender;
	private String senderIdent;
	private String senderHost;
	private String text;

	public IRCEventData(String event, String target, String sender, String senderIdent, String senderHost, String text) {
		this.event = event;
		this.target = target;
		this.sender = sender;
		this.senderIdent = senderIdent;
		this.senderHost = senderHost;
		this.text = text;
	}

	public String getEvent() {
		return event;
	}

	public String getTarget() {
		return target;
	}

	public String getSender() {
		return sender;
	}

	public String getText() {
		return text;
	}

	public String getSenderHost() {
		return senderHost;
	}

	public String getSenderIdent() {
		return senderIdent;
	}
}
