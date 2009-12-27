package gui;

import irc.Client;
import irc.IRCCallback;
import irc.IRCEventData;

import java.awt.BorderLayout;
import java.awt.Toolkit;

import javax.swing.DefaultListModel;
import javax.swing.JPanel;
import javax.swing.JApplet;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JList;
import java.awt.Dimension;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.awt.Font;

public class IRCApplet extends JApplet implements IRCCallback {

	private JPanel jContentPane = null;
	private JScrollPane userScrollPane = null;
	private JTextField textField = null;
	private JTextField topicField = null;
	private JScrollPane channelScroll = null;
	private JTextPane channelPane = null;
	private JList userList = null;
	private Client irc = null;  //  @jve:decl-index=0:
	private DefaultListModel listmodel = null;
	
	private String header = "<html>\n  <head>\n\n  </head>\n  <body>\n    <p style=\"margin-top: 0; font-family: verdana; font-size: 12pt;\">\n";  //  @jve:decl-index=0:
	private String footer = " \n    </p>\n  </body>\n</html>\n";  //  @jve:decl-index=0:
	private String messages = "";  //  @jve:decl-index=0:
	
	private String host;  //  @jve:decl-index=0:
	private String nick;  //  @jve:decl-index=0:
	private String chan;
	private int port;

	/**
	 * This is the xxx default constructor
	 */
	public IRCApplet() {
		super();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	public void init() {
		this.setSize(600, 400);
		this.setContentPane(getJContentPane());
		
		host = getParameter("host");
		port = Integer.parseInt(getParameter("port"));
		nick = getParameter("nick");
		
//		host = "irc.quakenet.org";
//		port = 6667;
//		nick = "blablaahahaha";
		
		this.irc = new Client(host, port, nick);
		irc.registerCallback("PRIVMSG", this);
		irc.registerCallback("353", this);
		irc.registerCallback("JOIN", this);
		irc.registerCallback("PART", this);
		irc.registerCallback("TOPIC", this);
		irc.registerCallback("332", this);
		irc.registerCallback("NOTICE AUTH", this);
        irc.registerCallback("QUIT", this);
        irc.registerCallback("NICK", this);
        irc.connect();
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getUserScrollPane(), BorderLayout.EAST);
			jContentPane.add(getTextField(), BorderLayout.SOUTH);
			jContentPane.add(getTopicField(), BorderLayout.NORTH);
			jContentPane.add(getChannelScroll(), BorderLayout.CENTER);
		}
		return jContentPane;
	}

	/**
	 * This method initializes userScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getUserScrollPane() {
		if (userScrollPane == null) {
			userScrollPane = new JScrollPane();
			userScrollPane.setPreferredSize(new Dimension(120, 0));
			userScrollPane.setViewportView(getUserList());
		}
		return userScrollPane;
	}

	/**
	 * This method initializes textField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTextField() {
		if (textField == null) {
			textField = new JTextField();
			textField.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if (textField.getText().startsWith("/")) {
						irc.rawmsg(textField.getText().substring(1,textField.getText().length()));
					} else {
						irc.privmsg("#c++", textField.getText());
						
						Calendar cal = Calendar.getInstance();
						DateFormat df = DateFormat.getTimeInstance(DateFormat.MEDIUM);
						messages += "[" + df.format(cal.getTime()) + "] ";
						
						messages += "<b>" + irc.getNick() + "</b>" + ": " + textField.getText() + "<br>";
						channelPane.setText(header + messages + footer);
					}
					textField.setText("");
				}
			});
		}
		return textField;
	}

	/**
	 * This method initializes topicField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getTopicField() {
		if (topicField == null) {
			topicField = new JTextField();
		}
		return topicField;
	}

	/**
	 * This method initializes channelScroll	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getChannelScroll() {
		if (channelScroll == null) {
			channelScroll = new JScrollPane();
			channelScroll.setViewportView(getChannelPane());
		}
		return channelScroll;
	}

	/**
	 * This method initializes channelPane	
	 * 	
	 * @return javax.swing.JTextPane	
	 */
	private JTextPane getChannelPane() {
		if (channelPane == null) {
			channelPane = new JTextPane();
			channelPane.setContentType("text/html");
			channelPane.setEditable(false);
		}
		return channelPane;
	}

	/**
	 * This method initializes userList	
	 * 	
	 * @return javax.swing.JList	
	 */
	private JList getUserList() {
		if (userList == null) {
			listmodel = new DefaultListModel();
			userList = new JList(listmodel);
			userList.setFont(new Font("Verdana", Font.BOLD, 12));
		}
		return userList;
	}

    public void callback(IRCEventData data) {
		if (data.getEvent().equals("PRIVMSG")) {
			Calendar cal = Calendar.getInstance();
			DateFormat df = DateFormat.getTimeInstance(DateFormat.MEDIUM);
			messages += "[" + df.format(cal.getTime()) + "] ";
			
			Toolkit.getDefaultToolkit().beep();
			
			messages += "<b>" + data.getSender() + "</b>" + ": " + data.getText() + "<br>";
			channelPane.setText(header + messages + footer);
		} else if (data.getEvent().equals("JOIN")) {
			Calendar cal = Calendar.getInstance();
			DateFormat df = DateFormat.getTimeInstance(DateFormat.MEDIUM);
			messages += "[" + df.format(cal.getTime()) + "] ";
			messages += "<b>" + data.getSender() + "</b>" + " JOINED" + "<br>";
			channelPane.setText(header + messages + footer);
		} else if (data.getEvent().equals("PART")) {
			Calendar cal = Calendar.getInstance();
			DateFormat df = DateFormat.getTimeInstance(DateFormat.MEDIUM);
			messages += "[" + df.format(cal.getTime()) + "] ";
			messages += "<b>" + data.getSender() + "</b>" + " PARTED" + "<br>";
			channelPane.setText(header + messages + footer);
		} else if (data.getEvent().equals("TOPIC")) {
			topicField.setText(data.getText());
			messages += "<b>" + data.getSender() + "</b>" + " sets topic to <b>" + data.getText() + "</b><br>";
			channelPane.setText(header + messages + footer);
		} else if (data.getEvent().equals("332")) {
			topicField.setText(data.getText());
			messages += "<b>" + data.getSender() + "</b>" + " sets topic to <b>" + data.getText() + "</b><br>";
			channelPane.setText(header + messages + footer);
		} else if (data.getEvent().equals("353")) {
			String[] users = data.getText().split(" ");
			for (String user : users) { listmodel.addElement(user); }
        } else if (data.getEvent().equals("NOTICE AUTH")) {
			messages += data.getText() + "<br>";
			channelPane.setText(header + messages + footer);
		} else if (data.getEvent().equals("QUIT")) {
			Calendar cal = Calendar.getInstance();
			DateFormat df = DateFormat.getTimeInstance(DateFormat.MEDIUM);
			messages += "[" + df.format(cal.getTime()) + "] ";
			messages += "<b>" + data.getSender() + "</b>" + " QUIT ( " + data.getText() + " )<br>";
			channelPane.setText(header + messages + footer);
        } else if (data.getEvent().equals("NICK")) {
			Calendar cal = Calendar.getInstance();
			DateFormat df = DateFormat.getTimeInstance(DateFormat.MEDIUM);
			messages += "[" + df.format(cal.getTime()) + "] ";
			messages += "<b>" + data.getSender() + "</b>" + " changes his nickname to <b>" + data.getText() + "</b><br>";
			channelPane.setText(header + messages + footer);
        }
    }

}
