package jms;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;

import GUIElements.ChatterView;

import jmsMethods.JMSChatClientMethoden;

/**
 * A Class which starts a method when a message is send to the chat after he connected
 * 
 * @author Wolfgang Mair
 * @version 2014-02-13
 */
public class ReadChat implements MessageListener{

	//Attribute
	private JMSChatClientMethoden method;
	private ChatterView chat;
	private Session session;
	private Connection connection;
	private MessageConsumer consumer;
	private Destination destination;
	private boolean go = true;

	/**
	 * A Constructor which defines the param for the connection and connects
	 * 
	 * @param method The JMSChatClientMethoden Object
	 * @param chat The GUI
	 */
	public ReadChat(JMSChatClientMethoden method, ChatterView chat){
		this.method = method;
		this.chat = chat;
		connection = method.createConnection();
		session = method.createSession(connection);
		destination = method.createDestinationTopic(session);
		consumer = method.createConsumer(session, destination);
	}
	
	@Override
	public void onMessage(Message message) {
	 if (message instanceof TextMessage) {
            try {
            	if(message != null){
            		chat.addListEntry(((TextMessage) message).getText());
            		if(Debug.debug){System.out.println(((TextMessage) message).getText());}
            	}
            }
            catch (Exception e) {
            	System.out.println("No text message received!");
                if(Debug.debug)e.printStackTrace();
            }
        }
		
	}
}