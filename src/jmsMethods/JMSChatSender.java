package jmsMethods;

import java.net.InetAddress;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import jms.Debug;

/**
 * The Class JMSChatSender who is sending a message into a chatroom and then shuts off
 */
public class JMSChatSender {

	/** The user. */
	private static String user = ActiveMQConnection.DEFAULT_USER;

	/** The password. */
	private static String password = ActiveMQConnection.DEFAULT_PASSWORD;

	/** The url. */
	private static String url = ActiveMQConnection.DEFAULT_BROKER_URL;

	/** The Chatroom name. */
	private static String subject = "VSDBChat";

	/**
	 * The main method which enables all the textsending and stuff.
	 *
	 * @param args the arguments
	 */
	public static void main( String[] args ) {

		// Create the connection.
		Session session = null;
		Connection connection = null;
		MessageProducer producer = null;
		Destination destination = null;

		try {

			//Creates a new Connection between the User and the Server on which activemq is running
			ConnectionFactory connectionFactory = new ActiveMQConnectionFactory( user, password, url );
			connection = connectionFactory.createConnection();
			connection.start();

			// Create the session
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			
			//Creates the Chatroom in which the users may join
			destination = session.createTopic( subject );

			// Create the producer who writes messages which he sends in the chatroom he joined
			producer = session.createProducer(destination);
			producer.setDeliveryMode( DeliveryMode.NON_PERSISTENT );

			// Create the message
			TextMessage message = session.createTextMessage( "MaxMustermann [ "+InetAddress.getLocalHost().getHostAddress()+" ]: This message was sent at (ms): " + System.currentTimeMillis() );
			//Its about sending a message
			producer.send(message);
			System.out.println( message.getText() );
			connection.stop();

		} catch (Exception e) {

			System.out.println("[MessageProducer] A Error occured trying to send a message: " + e);
			if(Debug.debug==true){ e.printStackTrace(); }

		} finally {

			//Closing of the producer session and connection
			try { producer.close(); } catch ( Exception e ) {}
			try { session.close(); } catch ( Exception e ) {}
			try { connection.close(); } catch ( Exception e ) {}

		}

	} // end main

}
