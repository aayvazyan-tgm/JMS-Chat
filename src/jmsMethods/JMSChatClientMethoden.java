package jmsMethods;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import jms.Debug;

// TODO: Auto-generated Javadoc
/**
 * The Class JMSChatClientMethoden.
 */
public class JMSChatClientMethoden {

	/** The user. */
	private final String user;
	
	/** The topic. */
	private final String topic;
	
	/** The url. */
	private final String url;


	// Create the connection.
	/** The session. */
	private Session session = null;
	
	/** The connection. */
	private Connection connection = null;
	
	/** The producer. */
	private MessageProducer producer = null;
	
	/** The destination. */
	private Destination destination = null;
	
	/** The consumer. */
	private MessageConsumer consumer = null;


	/**
	 * Instantiates a new jMS chat client methoden.
	 *
	 * @param server the server
	 * @param user the user
	 * @param topic the topic
	 */
	public JMSChatClientMethoden(String server, String user, String topic){
		this.url = "failover://tcp://"+server;
		this.topic = topic;
		this.user = user;
	}

	/**
	 * Creates the consumer.
	 *
	 * @param session the session
	 * @param destination the destination
	 * @return the message consumer
	 */
	public MessageConsumer createConsumer(Session session, Destination destination){
		try{
			// Create the consumer
			consumer = session.createConsumer( destination );
			return consumer;
		}
		catch(JMSException jmse){
			System.out.println("Fehler bei dem Consumer!");
			if(Debug.debug){jmse.printStackTrace();}
			return null;
		}

	}		
	
	/**
	 * Recieve message.
	 *
	 * @param consumer the consumer
	 */
	public void recieveMessage(MessageConsumer consumer){
		try{
			// Start receiving
			TextMessage message = (TextMessage) consumer.receive();
			if ( message != null ) {
				System.out.println("Message received: " + message.getText() );
				message.acknowledge();
			}
		}
		catch(JMSException jmse){
			System.out.println("Fehler beim Empfangen!");
			if(Debug.debug){jmse.printStackTrace();}
		}

	}
	
	/**
	 * Creates the connection.
	 *
	 * @return the connection
	 */
	public Connection createConnection(){
		try{
			ConnectionFactory connectionFactory = new ActiveMQConnectionFactory( this.user, null, this.url );
			connection = connectionFactory.createConnection();
			connection.start();
			return connection;
		}
		catch(JMSException jmse){
			System.out.println("Fehler beim Verbinden!");
			if(Debug.debug){jmse.printStackTrace();}
			return null;
		}
	}

	/**
	 * Creates the session.
	 *
	 * @param connection the connection
	 * @return the session
	 */
	public Session createSession(Connection connection){
		try{
			// Create the session
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			return session;
		}
		catch(JMSException jmse){
			System.out.println("Fehler bei der Session!");
			if(Debug.debug){jmse.printStackTrace();}
			return null;
		}
	}

	/**
	 * Creates the destination topic.
	 *
	 * @param session the session
	 * @return the destination
	 */
	public Destination createDestinationTopic(Session session){
		try {
			destination = session.createTopic(this.topic);
			return destination;
		} catch (JMSException jmse) {
			System.out.println("Fehler bei der TopicDestination!");
			if(Debug.debug){jmse.printStackTrace();}
			return null;
		}
	}

	/**
	 * Creates the producer.
	 *
	 * @param session the session
	 * @param destination the destination
	 * @return the message producer
	 */
	public MessageProducer createProducer(Session session, Destination destination){
		try{
			// Create the producer.
			producer = session.createProducer(destination);
			producer.setDeliveryMode( DeliveryMode.NON_PERSISTENT );
			return producer;
		}
		catch(JMSException jmse){
			System.out.println("Fehler bei dem Producer!");
			if(Debug.debug){jmse.printStackTrace();}
			return null;
		}
	}

	/**
	 * Creates the private destination.
	 *
	 * @param session the session
	 * @param destIP the dest ip
	 * @return the destination
	 */
	public Destination createPrivateDestination(Session session, String destIP){
		try {
			destination = session.createQueue(destIP);
			return destination;
		} catch (JMSException jmse) {
			System.out.println("Fehler bei der PrivateDestination!");
			if(Debug.debug){jmse.printStackTrace();}
			return null;
		}
	}

	/**
	 * Private message.
	 *
	 * @param session the session
	 * @param destIP the dest ip
	 * @param msg the msg
	 */
	public void privateMessage(Session session, String destIP, String msg){
		TextMessage message = null;
		try {
			message = session.createTextMessage( user+"["+InetAddress.getLocalHost().getHostAddress()+"] whispered: "+msg);
			Destination pdest = this.createPrivateDestination(session, destIP);
			this.createProducer(session, pdest).send(message);

		} catch (JMSException jmse) {
			System.out.println("Fehler bei der PrivateMessage");
			if(Debug.debug){jmse.printStackTrace();}
		}
		catch (UnknownHostException uhe){
			System.out.println("Unbekannter Host");
			if(Debug.debug){uhe.printStackTrace();}
		}
	}

	/**
	 * Help message.
	 */
	public void helpMessage(){
		for(Command c : Command.values()){
			System.out.println(c.getHilfstext());
		}
	}

	/**
	 * Read mail.
	 *
	 * @param session the session
	 */
	public void readMail(Session session){
		try{
			//Erstellen deiner home destination
			Destination home = this.createPrivateDestination(session, InetAddress.getLocalHost().getHostAddress());
			
			TextMessage message = (TextMessage) this.createConsumer(session, home).receive();

			if ( message != null ) {
				System.out.println("Message received: " + message.getText() );
				message.acknowledge();
			}
		}
		catch(JMSException jmse){
			System.out.println("Fehler beim Empfangen!");
			if(Debug.debug){jmse.printStackTrace();}
		} catch (UnknownHostException e) {
			System.out.println("Fehler du hast keine IP?");
		}
	}

	/**
	 * Use commands.
	 *
	 * @param connection the connection
	 * @param session the session
	 * @param consumer the consumer
	 * @param producer the producer
	 * @param msg the msg
	 */
	public void useCommands(Connection connection, Session session, MessageConsumer consumer, MessageProducer producer, String msg){
		String[] splitMsg = msg.replace("/", "").split(" ");

		switch(Command.valueOf(splitMsg[0])){
		case MAIL : 

			if(splitMsg[1].split(".").length == 4)
				this.privateMessage(session, splitMsg[1], msg);
			else
				System.out.println("Falsche Empfaenger Eingabe!");
			break;


		case HELP :
			this.helpMessage();
			break;

		case MAILBOX : 
			this.readMail(session);
			break;
		
		
		case EXIT :
			this.closeAll(connection, session, consumer, producer);
			break;
			
		default : 
			System.out.println("Unbekannter Befehl!");
		}
	}

	/**
	 * Send message.
	 *
	 * @param connection the connection
	 * @param session the session
	 * @param consumer the consumer
	 * @param producer the producer
	 * @param msg the msg
	 */
	public void sendMessage(Connection connection, Session session, MessageConsumer consumer, MessageProducer producer, String msg){
		try{
			if(msg.charAt(0) == '/'){
				this.useCommands(connection, session, consumer, producer, msg);
			}


			else{
				// Create the message
				TextMessage message = session.createTextMessage( user+"["+InetAddress.getLocalHost().getHostAddress()+"]: "+msg);
				producer.send(message);
				System.out.println( message.getText());
			}
		}
		catch(JMSException jmse){
			System.out.println("Fehler beim Senden der Message!");
			if(Debug.debug){jmse.printStackTrace();}
		} catch (UnknownHostException uhe) {
			System.out.println("Unbekannter Host");
			if(Debug.debug){uhe.printStackTrace();}
		}
	}
	
	/**
	 * Close all.
	 *
	 * @param connection the connection
	 * @param session the session
	 * @param consumer the consumer
	 * @param producer the producer
	 */
	public void closeAll(Connection connection, Session session, MessageConsumer consumer, MessageProducer producer){
		try {producer.close();} catch (JMSException e) {System.out.println("Fehler beim beenden vom producer");}
		try {consumer.close();} catch (JMSException e) {System.out.println("Fehler beim beenden vom consumer");}
		try {session.close();} catch (JMSException e) {System.out.println("Fehler beim beenden von der session");}
		try {connection.close();} catch (JMSException e) {System.out.println("Fehler beim beenden von der connection");}
	}
}