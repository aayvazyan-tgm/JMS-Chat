package jms;

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

public class JMSChatClientMethoden {

	private final String user;
	private final String topic;
	private final String url;


	// Create the connection.
	private Session session = null;
	private Connection connection = null;
	private MessageProducer producer = null;
	private Destination destination = null;
	private MessageConsumer consumer = null;


	public JMSChatClientMethoden(String server, String user, String topic){
		this.url = "failover://tcp://"+server;
		this.topic = topic;
		this.user = user;
	}

	public MessageConsumer createConsumer(Session session, Destination destination){
		try{
			// Create the consumer
			consumer = session.createConsumer( destination );
			return consumer;
		}
		catch(JMSException jmse){
			System.out.println("Fehler bei dem Consumer!");
			return null;
		}

	}		
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
		}

	}
	public Connection createConnection(){
		try{
			ConnectionFactory connectionFactory = new ActiveMQConnectionFactory( this.user, null, this.url );
			connection = connectionFactory.createConnection();
			connection.start();
			return connection;
		}
		catch(JMSException jmse){
			System.out.println("Fehler beim Verbinden!");
			return null;
		}
	}

	public Session createSession(Connection connection){
		try{
			// Create the session
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			return session;
		}
		catch(JMSException jmse){
			System.out.println("Fehler bei der Session!");
			return null;
		}
	}

	public Destination createDestinationTopic(Session session){
		try {
			destination = session.createTopic(this.topic);
			return destination;
		} catch (JMSException jmse) {
			System.out.println("Fehler bei der TopicDestination!");
			return null;
		}
	}

	public MessageProducer createProducer(Session session, Destination destination){
		try{
			// Create the producer.
			producer = session.createProducer(destination);
			producer.setDeliveryMode( DeliveryMode.NON_PERSISTENT );
			return producer;
		}
		catch(JMSException jmse){
			System.out.println("Fehler bei dem Producer!");
			return null;
		}
	}

	public Destination createPrivateDestination(Session session, String destIP){
		try {
			destination = session.createQueue(destIP);
			return destination;
		} catch (JMSException jmse) {
			System.out.println("Fehler bei der PrivateDestination!");
			return null;
		}
	}

	public void privateMessage(Session session, String destIP, String msg){
		TextMessage message = null;
		try {
			message = session.createTextMessage( user+"["+InetAddress.getLocalHost().getHostAddress()+"] whispered: "+msg);
			Destination pdest = this.createPrivateDestination(session, destIP);
			this.createProducer(session, pdest).send(message);

		} catch (JMSException jmse) {
			System.out.println("Fehler bei der PrivateMessage");
		}
		catch (UnknownHostException uhe){
			System.out.println("Unbekannter Host");
		}
	}

	public void sendMessage(Session session, MessageProducer producer, String msg){
		try{
			if(msg.split(" ")[0].equals(Command._MAIL.toString())){
				if(msg.split(" ")[1].split(".").length == 4)
					this.privateMessage(session, msg.split(" ")[1], msg);
				else
					System.out.println("Falsche Empfaenger Eingabe!");
			}
			else{
				// Create the message
				TextMessage message = session.createTextMessage( user+"["+InetAddress.getLocalHost().getHostAddress()+"]: "+msg);
				producer.send(message);
				System.out.println( message.getText() );
			}
		}
		catch(JMSException jmse){
			System.out.println("Fehler beim Senden der Message!");
		} catch (UnknownHostException uhe) {
			System.out.println("Unbekannter Host");
		}
	}
}
