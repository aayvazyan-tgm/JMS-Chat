package jmsMethods;
/**
 * @author Wolfgang Mair
 * @version 12.02.2014
 * The Enum Command defines aviable commands.
 */
public enum Command {
	/**The Help Command which shows you all commands*/
	HELP("/Help\t\t\t\t\tGibt die Commandos aus!"), 
	
	/**The Mail command which allows you to send a private message to another user*/
	MAIL("/Mail <ip_des_benutzers> <nachricht>\tSendet eine Nachricht einen bestimmten Benutzer!"),
	
	/**The Mailbox command which allows you to read your private messages */
	MAILBOX("/Mailbox\t\t\t\tLiest deine Mailbox Nachricht!"),
	
	/**The Exit command which allows you to close the program*/
	EXIT("/Exit\t\t\t\t\tBeendet den Chat!");
	
	//Attribute
	private String text;
	
	/**
	 * Ein Konstruktor zum definieren der Enums
	 * 
	 * @param hilfstext der Text der bei dem Hilfekommand ausgegeben wird
	 */
	private Command (String hilfstext){
		text = hilfstext;
	}
	
	/**
	 * Ein getter der für den Hilfecommand den jeweiligen Text des Enums ausgibt
	 * 
	 * @return   Der Hilfstext
	 */
	public String getHilfstext(){
		return this.text;
	}
	
}