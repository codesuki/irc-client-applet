package irc;

public class User {
	private String nick;
	private boolean isOp;
	private boolean isVoiced;
	private String modes;

	public User(String nick) {
		super();
		this.nick = nick;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public boolean isOp() {
		return isOp;
	}

	public void setOp(boolean isOp) {
		this.isOp = isOp;
	}

	public boolean isVoiced() {
		return isVoiced;
	}

	public void setVoiced(boolean isVoiced) {
		this.isVoiced = isVoiced;
	}
}
