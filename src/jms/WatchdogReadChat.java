package jms;

import GUIElements.ChatterView;

/**
 * A Watchdog which controles ReadChat Threads
 * 
 * @author Wolfgang Mair
 * @version 2014-02-12
 */
public class WatchdogReadChat {
	//Attributes
	private ReadChat th;
	private ChatterView cView;
	
	/**
	 * A Konstruktor which includes the Thread he shall close when the time is right
	 * @param chatV 
	 * 
	 * @param th The Thread
	 */
	public WatchdogReadChat(ChatterView chatV, ReadChat th){
		this.cView=chatV;
		this.th = th;
	}
	
	/**
	 * The Method which stops the Thread
	 */
	public void stopThread(){
		th.stopRead();
		cView.dispose();
	}
}
