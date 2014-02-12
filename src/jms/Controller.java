package jms;

import GUIElements.ConfigDialog;


/**
 * This class handles the program start and works as its backbone
 * 
 * @author Ari Ayvazyan
 * @version 12.02.2014
 */
public class Controller {
	public static void main(String[] args){
		System.out.println("Debug mode: "+Debug.debug);
		//process the arguments
		MyCommandLineParser mp= new MyCommandLineParser(args);
		//let the user check the arguments in a GUI
		ConfigDialog cf=new ConfigDialog(mp.benutzername, mp.topic, mp.server);
		
		
		
	}
}