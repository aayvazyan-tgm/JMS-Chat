package jms;

import javax.jms.Connection;
import javax.jms.Destination;

import javax.jms.MessageProducer;
import javax.jms.Session;

import jmsMethods.JMSChatClientMethoden;

/**
 * @author Wolfgang Mair
 * @version 12.02.2014
 * The Class TextHandler handles the user input.
 */
public class TextHandler implements TextProcesser{
	
	private JMSChatClientMethoden method;
	private Session session;
	private Connection connection;
	private MessageProducer producer;
	private Destination destination;
	
	/**
	 * Instantiates a new text handler.
	 *
	 * @param method The JMSChatClientMethoden Object used to create the producer, connection, etc.
	 */
	public TextHandler(JMSChatClientMethoden method) {
		this.method = method;
		connection = method.createConnection();
		session = method.createSession(connection);
		destination = method.createDestinationTopic(session);
		producer = method.createProducer(session, destination);
		
	}
	
	/* (non-Javadoc)
	 * @see jms.TextProcesser#processText(java.lang.String)
	 */
	@Override
	public void processText(String input) {
		//sends a text
		method.sendMessage(connection, session, null, producer, input);
	}

}
