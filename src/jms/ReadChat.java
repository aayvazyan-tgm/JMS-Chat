package jms;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.MessageConsumer;
import javax.jms.Session;

import GUIElements.ChatterView;

import jmsMethods.JMSChatClientMethoden;

/**
 * A thread which never stops to read the chat after he connected
 * 
 * @author Wolfgang Mair
 * @version 2014-02-12
 */
public class ReadChat implements Runnable{

	//Attribute
	private JMSChatClientMethoden method;
	private ChatterView chat;
	private Session session;
	private Connection connection;
	private MessageConsumer consumer;
	private Destination destination;
	private boolean go = true;
	
	/**
	 * A Constructor which defines the param for the connection
	 * 
	 * @param method The JMSChatClientMethoden Object
	 * @param chat The GUI
	 */
	public ReadChat(JMSChatClientMethoden method, ChatterView chat){
		this.method = method;
		this.chat = chat;
	}
	
	@Override
	public void run() {
		connection = method.createConnection();
		session = method.createSession(connection);
		destination = method.createDestinationTopic(session);
		consumer = method.createConsumer(session, destination);
		
		//Never stops until the Watchdogs uses the Method
		while(go){
			chat.addListEntry(method.recieveMessage(consumer));
			
		}
		
		//closes all the creates connections
		method.closeAll(connection, session, consumer, null);
		
	} 
	
	/**
	 * A Method which stops the Thread
	 */
	public void stopRead(){
		this.go = false;
	}
}
