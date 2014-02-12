package jms;
import org.apache.commons.cli2.*;
import org.apache.commons.cli2.builder.*;
import org.apache.commons.cli2.commandline.Parser;
import org.apache.commons.cli2.util.HelpFormatter;



/**
 * Verwaltet die Optionen und deren Argumente.
 * @author Ari Ayvazyan
 * @version 20.11.2013
 */
public class MyCommandLineParser {

	
	/** The server. */
	public String server="";
	
	/** The benutzername. */
	public String benutzername="";
	
	/** The topic. */
	public String topic="";
	
	
	/**
	 * Verarbeitet die argumente.
	 *
	 * @param args - Die zu parsende Argumente.
	 * @throws OptionException the option exception
	 */
	public MyCommandLineParser(String[] args) {

		DefaultOptionBuilder obuilder = new DefaultOptionBuilder();
		ArgumentBuilder abuilder = new ArgumentBuilder();
		GroupBuilder gbuilder = new GroupBuilder();
		
		/*
		 * Hier werden die zu verarbeitenden parameter angegeben.
		 */
		
		Option serverOption = obuilder.withLongName("server").withShortName("h").withRequired(false).withDescription("Der Server")
				.withArgument(abuilder.withName("server").withMinimum(1).withMaximum(1).create()).create();
		Option benutzernameOption = obuilder.withLongName("benutzername").withShortName("b").withRequired(false).withDescription("Der Benutzername")
				.withArgument(abuilder.withName("benutzername").withMinimum(1).withMaximum(1).create()).create();
		Option topicOption = obuilder.withLongName("topic").withShortName("t").withRequired(false).withDescription("Das topic")
				.withArgument(abuilder.withName("topic").withMinimum(1).withMaximum(1).create()).create();
				HelpFormatter hf=new HelpFormatter();
		hf.setHeader("JMS Chatter, von Ari Ayvazyan, Wolfgang Mairs");
		
		/*
		 * erstellen einer Optionsgruppe welche an den parser weitergegeben wird.
		 */
		Group options = gbuilder.withName("options")
				.withOption(serverOption)
				.withOption(benutzernameOption)
				.withOption(topicOption)
				.create();
		
		Parser parser = new Parser();
		parser.setHelpFormatter(hf);
		parser.setHelpTrigger("-h");
		parser.setGroup(options);
		try{
		//verarbeiten der argumente
			CommandLine cl = parser.parse(args);
		
		//auslesen der argumente
		
			if(cl.hasOption(serverOption)) {
			server = (String) cl.getValue(serverOption);
			}
			if(cl.hasOption(benutzernameOption)) {
			benutzername = (String) cl.getValue(benutzernameOption);
			}
			if(cl.hasOption(topicOption)) {
			topic = (String) cl.getValue(topicOption);
			}
		}catch(Exception e){
			hf.printHelp();
			if(Debug.debug==true){ e.printStackTrace(); }
		}
	}
	
}