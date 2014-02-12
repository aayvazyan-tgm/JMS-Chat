package jmsMethods;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import jms.Debug;

// TODO: Auto-generated Javadoc
/**
 * The Class JMSChatReceiver.
 */
public class JMSChatReceiver {

  /** The user. */
  private static String user = ActiveMQConnection.DEFAULT_USER;
  
  /** The password. */
  private static String password = ActiveMQConnection.DEFAULT_PASSWORD;
  
  /** The url. */
  private static String url = ActiveMQConnection.DEFAULT_BROKER_URL;
  
  /** The subject. */
  private static String subject = "VSDBChat";
	
  /**
   * The main method.
   *
   * @param args the arguments
   */
  public static void main( String[] args ) {
		
	  // Create the connection.
	  Session session = null;
	  Connection connection = null;
	  MessageConsumer consumer = null;
	  Destination destination = null;
			
	  try {
	    	
			ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(user, password, url);
			connection = connectionFactory.createConnection();
			connection.start();
		
			// Create the session
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			destination = session.createTopic( subject );
				  
			// Create the consumer
			consumer = session.createConsumer( destination );
				
			// Start receiving
			TextMessage message = (TextMessage) consumer.receive();
      if ( message != null ) {
      	System.out.println("Message received: " + message.getText() );
      	message.acknowledge();
      }
      connection.stop();
			
	  } catch (Exception e) {
	  	
	    System.out.println("[MessageConsumer] Something went wrong at receiving. : " + e);
	    if(Debug.debug==true){ e.printStackTrace(); }
	      
	  } finally {
	  	
			try { consumer.close(); } catch ( Exception e ) {}
			try { session.close(); } catch ( Exception e ) {}
			try { connection.close(); } catch ( Exception e ) {}
			
	  }

  } // end main
      
}
	
