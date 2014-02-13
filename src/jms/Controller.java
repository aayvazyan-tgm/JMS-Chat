package jms;

import java.awt.EventQueue;

import jmsMethods.JMSChatClientMethoden;

import GUIElements.ChatterView;
import GUIElements.ConfigDialog;


/**
 * This class handles the program start and works as its backbone
 * 
 * @author Ari Ayvazyan
 * @version 12.02.2014
 */
public class Controller {
	public static void main(String[] args){
		if(Debug.debug)System.out.println("Debug mode: "+Debug.debug);
		
		//process the arguments
		MyCommandLineParser mp= new MyCommandLineParser(args);
		
		//let the user check the arguments in a GUI
		ConfigDialog cf=new ConfigDialog(mp.benutzername, mp.topic, mp.server);
		cf.setVisible(true);
		
		//start the connection 
		JMSChatClientMethoden methodClass=new JMSChatClientMethoden(cf.txtServer.getText(), cf.txtUser.getText(), cf.txtTopic.getText());
		
		//Start the GUI
		ChatterView chatV = new ChatterView(new TextHandler(methodClass));
		chatV.setVisible(true);
		methodClass.setChatterView(chatV);
		//start the receiver thread
		ReadChat readChat = new ReadChat(methodClass,chatV);
		TextReader reader = new TextReader(readChat,methodClass);
		
	}
}