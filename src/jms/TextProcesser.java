package jms;

/**
 * @author Ari Ayvazyan
 * @version 12.02.2014
 * 
 * The Interface TextProcesser, it handles a String and may or may not do strange things with it.
 * 
 */
public interface TextProcesser {
	
	/**
	 * Processes text.
	 *
	 * @param input the input to be processed
	 */
	public void processText(String input);
	
}
