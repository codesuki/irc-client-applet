package irc;

import java.util.*;

public class Channel {
	private String name;
	private String topic;
	private String modes;
	private LinkedList<User> users;
	
	public Channel(String name, String topic) {
		super();
		this.name = name;
		this.topic = topic;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getName() {
		return name;
	}
	
	public void parseUserlist(String data) {
		
	}
	
	public void join(String nickname) {
		
	}
	
	public void part(String nickname) {
		
	}
}
