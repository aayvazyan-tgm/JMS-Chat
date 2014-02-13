package jmsMethods;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import GUIElements.ChatterView;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import jms.Debug;

/**
 * @author Wolfgang Mair
 * The Class JMSChatClientMethoden enables useful Methods like sending messages to 
 * an activemq server or using Commands.
 * ChatView needs to be set asap after creating a object of this class!
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
	
	/** The ChatterView for output. */
	private ChatterView cView;


	/**
	 * Ein Konstruktor der die Methoden für den JMS Chat benutzbar macht.
	 *
	 * @param server the server on which activemq is running
	 * @param user The name of the user
	 * @param topic The Chatroom
	 */
	public JMSChatClientMethoden(String server, String user, String topic){
		this.url = "tcp://"+server+":61616";
		this.topic = topic;
		this.user = user;
	}

	/**
	 * Creates the consumer which is able to recieve Messages.
	 *
	 * @param session The session of the User
	 * @param destination The Destination definded by the chatroom
	 * @return   A message consumer which is able to recieve messages from the Chatroom
	 */
	public MessageConsumer createConsumer(Session session, Destination destination){
		try{
			// Create the consumer
			consumer = session.createConsumer( destination );
			return consumer;
		}
		catch(JMSException jmse){
			cView.addListEntry("Fehler bei dem Consumer!");
			if(Debug.debug){jmse.printStackTrace();}
			return null;
		}

	}		

	/**
	 * A Method which Creates the connection between the user and the Server with the activemq running.
	 *
	 * @return   the connection 
	 */
	public Connection createConnection(){
		try{
			ConnectionFactory connectionFactory = new ActiveMQConnectionFactory( this.user, null, this.url );
			connection = connectionFactory.createConnection();
			connection.start();
			return connection;
		}
		catch(JMSException jmse){
			cView.addListEntry("Fehler beim Verbinden!");
			if(Debug.debug){jmse.printStackTrace();}
			return null;
		}
	}

	/**
	 * Creates the session based on the Connection.
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
			cView.addListEntry("Fehler bei der Session!");
			if(Debug.debug){jmse.printStackTrace();}
			return null;
		}
	}

	/**
	 * Creates the destination topic wich is defined by the chatroom name and the session.
	 *
	 * @param session the session
	 * @return   the destination
	 */
	public Destination createDestinationTopic(Session session){
		try {
			destination = session.createTopic(this.topic);
			return destination;
		} catch (JMSException jmse) {
			cView.addListEntry("Fehler bei der TopicDestination!");
			if(Debug.debug){jmse.printStackTrace();}
			return null;
		}
	}

	/**
	 * Creates the producer which is able to write Messages towards the destination he was created with.
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
			cView.addListEntry("Fehler bei dem Producer!");
			if(Debug.debug){jmse.printStackTrace();}
			return null;
		}
	}

	/**
	 * Creates the private destination which is the Mailbox of a User.
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
			cView.addListEntry("Fehler bei der PrivateDestination!");
			if(Debug.debug){jmse.printStackTrace();}
			return null;
		}
	}

	/**
	 * Sends a Private message to a User.
	 *
	 * @param connection the connection
	 * @param destIP the dest ip
	 * @param msg the msg
	 */
	public void privateMessage(Connection connection, String destIP, String msg){
		TextMessage message = null;
		try {
			Session temp = this.createSession(connection);
			message = session.createTextMessage( user+"["+InetAddress.getLocalHost().getHostAddress()+"] mailed: "+msg);
			Destination pdest = this.createPrivateDestination(temp, destIP);


			MessageProducer pro = this.createProducer(temp, pdest);
			pro.send(message);

			pro.close();
			temp.close();

		} catch (JMSException jmse) {
			cView.addListEntry("Fehler bei der PrivateMessage");
			if(Debug.debug){jmse.printStackTrace();}
		}
		catch (UnknownHostException uhe){
			cView.addListEntry("Unbekannter Host");
			if(Debug.debug){uhe.printStackTrace();}
		}
	}

	/**
	 * A Method which shows you Help message of every command.
	 *
	 * @return the string
	 */
	public String helpMessage(){
		String text = "";
		for(Command c : Command.values()){
			text += c.getHilfstext();
			text += "\n";
		}
		return text;
	}

	/**
	 * A Method which allows you to read your own Mailbox.
	 *
	 * @param connection the connection
	 * @return the string
	 */
	public String readMail(Connection connection){
		String text = "";
		TextMessage textMessage = null;

		try{
			Session session = this.createSession(connection);
			//Erstellen deiner home destination
			Destination home = this.createPrivateDestination(session, InetAddress.getLocalHost().getHostAddress());

			MessageConsumer cons = this.createConsumer(session, home);

			Message message = cons.receive(200);

			if (message instanceof TextMessage){
				textMessage = (TextMessage) message;
			} 

			if ( message != null ) {
				text = textMessage.getText();
				cView.addListEntry(text);
				message.acknowledge();
			}
			cons.close();
			return text;
		}
		catch(JMSException jmse){
			cView.addListEntry("Fehler beim Empfangen!");
			if(Debug.debug){jmse.printStackTrace();}
			return text;
		} catch (UnknownHostException e) {
			cView.addListEntry("Fehler du hast keine IP?");
			return text;
		}
	}

	/**
	 * A Method which recognizes which command was used and launches the right method.
	 *
	 * @param connection the connection
	 * @param session the session
	 * @param consumer the consumer
	 * @param producer the producer
	 * @param msg the msg
	 */
	public void useCommands(Connection connection, Session session, MessageConsumer consumer, MessageProducer producer, String msg){
		String[] splitMsg = msg.replace("/", "").split(" ");
		String messageFinal = "";

		if(Debug.debug)cView.addListEntry("Ausführen des Commands : "+Command.valueOf(splitMsg[0].toUpperCase()));

		switch(Command.valueOf(splitMsg[0].toUpperCase())){
		case MAIL : 

			if(splitMsg.length >= 3){
				if(splitMsg[1].split("\\.").length == 4){
					for(int i = 3; i < splitMsg.length; i++){
						messageFinal += splitMsg[i];
					}
					this.privateMessage(connection, splitMsg[1], messageFinal);
				}
				else
					cView.addListEntry("Falsche Empfaenger Eingabe!"+splitMsg[1]+ splitMsg[1].split("\\.").length);
			}
			else
				cView.addListEntry("Falsche Empfaenger Eingabe!");
			break;


		case HELP :
			this.helpMessage();
			cView.addListEntry(this.helpMessage());
			break;

		case MAILBOX : 
			this.readMail(connection);
			break;


		case EXIT :

			this.closeAll(connection, session, consumer, producer);
			System.exit(0);
			break;

		default : 
			cView.addListEntry("Unbekannter Befehl!");
		}
	}

	/**
	 * A Method which allows you to send a text into a Chatroom.
	 *
	 * @param connection the connection
	 * @param session the session
	 * @param consumer the consumer
	 * @param producer the producer
	 * @param msg the msg
	 */
	public void sendMessage(Connection connection, Session session, MessageConsumer consumer, MessageProducer producer, String msg){
		try{
			if(msg.length() != 0){
				if(msg.charAt(0) == '/'){
					this.useCommands(connection, session, consumer, producer, msg);
				}
				else{
					// Create the message
					TextMessage message = session.createTextMessage( user+"["+InetAddress.getLocalHost().getHostAddress()+"]: "+msg);
					producer.send(message);
					if(Debug.debug){cView.addListEntry("gesendet:" + message.getText());}
				}
			}

		}
		catch(JMSException jmse){
			cView.addListEntry("Fehler beim Senden der Message!");
			if(Debug.debug){jmse.printStackTrace();}
		} catch (UnknownHostException uhe) {
			cView.addListEntry("Unbekannter Host");
			if(Debug.debug){uhe.printStackTrace();}
		}
	}
	
	/**
	 * Sets the chatter view.
	 *
	 * @param cv the new chatter view
	 */
	public void setChatterView(ChatterView cv){
		this.cView=cv;
	}
	

	/**
	 * A Method which closes the Connection, session, producer and the consumer.
	 *
	 * @param connection the connection
	 * @param session the session
	 * @param consumer the consumer
	 * @param producer the producer
	 */
	public void closeAll(Connection connection, Session session, MessageConsumer consumer, MessageProducer producer){
		try {if(producer != null)producer.close();} catch (JMSException e) {cView.addListEntry("Fehler beim beenden vom producer");}
		try {if(consumer != null)consumer.close();} catch (JMSException e) {cView.addListEntry("Fehler beim beenden vom consumer");}
		try {if(session != null)session.close();} catch (JMSException e) {cView.addListEntry("Fehler beim beenden von der session");}
		try {if(connection != null)connection.close();} catch (JMSException e) {cView.addListEntry("Fehler beim beenden von der connection");}
	}
}
