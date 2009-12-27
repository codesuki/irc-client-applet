package irc;

public class IRCEvent {
	private String event;
	private IRCCallback callback;
	
	public IRCEvent(String event, IRCCallback callback) {
		this.event = event;
		this.callback = callback;
	}

	public IRCCallback getCallback() {
		return callback;
	}

	public void setCallback(IRCCallback callback) {
		this.callback = callback;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}
}
