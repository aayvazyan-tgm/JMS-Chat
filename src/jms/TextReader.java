package jms;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;

import jmsMethods.JMSChatClientMethoden;

public class TextReader {
	
	private Session session;
	private Connection connection;
	private MessageConsumer consumer;
	private Destination destination; 
	
	public TextReader(ReadChat read, JMSChatClientMethoden method){
		connection = method.createConnection();
		session = method.createSession(connection);
		destination = method.createDestinationTopic(session);
		consumer = method.createConsumer(session, destination);
		try {
			consumer.setMessageListener(read);
		} catch (JMSException e) {
			System.out.println("Fehler beim einbinden von MessageListener");
			if(Debug.debug){e.printStackTrace();}
		}
	}
	
}
