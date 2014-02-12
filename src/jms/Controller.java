package jms;

import org.apache.commons.cli2.OptionException;

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

/**
 * If debug is set to true exceptions will be printed to the console
 * 
 * 
 * @author Ari Ayvazyan
 * @version 12-02-2014
 * 
 */
class Debug {
	public static boolean debug=true; 
}