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

	public void createConsumer(){
		try{
			// Create the consumer
			consumer = session.createConsumer( destination );
		}
		catch(JMSException jmse){
			System.out.println("Fehler bei dem Consumer!");
		}

	}		
	public void recieveMessage(){
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
	public void createConnection(String user, String url){
		try{
			ConnectionFactory connectionFactory = new ActiveMQConnectionFactory( user, null, url );
			connection = connectionFactory.createConnection();
			connection.start();
		}
		catch(JMSException jmse){
			System.out.println("Fehler beim Verbinden!");
		}
	}

	public void createSession(String topic){
		try{
			// Create the session
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			destination = session.createTopic( topic );
		}
		catch(JMSException jmse){
			System.out.println("Fehler bei der Session!");
		}
	}

	public void createProducer(){
		try{
			// Create the producer.
			producer = session.createProducer(destination);
			producer.setDeliveryMode( DeliveryMode.NON_PERSISTENT );
		}
		catch(JMSException jmse){
			System.out.println("Fehler bei dem Producer!");
		}
	}

	public void sendMessage(String msg){
		try{
			// Create the message
			TextMessage message = session.createTextMessage( user+"["+InetAddress.getLocalHost().getHostAddress()+"]: "+msg);
			producer.send(message);
			System.out.println( message.getText() );
			connection.stop();
		}
		catch(JMSException jmse){
			System.out.println("Fehler beim Senden der Message!");
		} catch (UnknownHostException uhe) {
			System.out.println("Unbekannter Host");
		}
	}


	public void closeAll(){
		try { producer.close(); } catch ( Exception e ) {System.out.println("Fehler beim Beenden vom Producer");}
		try { consumer.close(); } catch ( Exception e ) {System.out.println("Fehler beim Beenden vom Consumer");}
		try { session.close(); } catch ( Exception e ) {System.out.println("Fehler beim Beenden von der Session");}
		try { connection.close(); } catch ( Exception e ) {System.out.println("Fehler beim Beenden von der Connection");}
	}	

}
