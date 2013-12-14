/*
 * This file is part of the Droid Roach project
 * Copyright 2013 Stefano Gabryel
 * Author: Stefano Gabryel
 * License: GPL v3 - http://www.gnu.org/licenses/gpl-3.0.html
 * Author website: http://www.stefano-workshop.com
 * Project website: http://droid-roach.stefano-workshop.com
 * 
 */

package esse.droidroach.module.subdomainenumerator.thread;

import java.util.Random;

import esse.droidroach.utility.Downloader;

public class SubThread extends Thread {
	private boolean isRunning = true;
	protected Downloader dlManager;
	private boolean blocked=false;

	public SubThread() {
		dlManager = new Downloader(false);// the boolean is used to determine if we need a random user agent or a static one
	}

	public final void stopRunning() {
		isRunning = false;
	}

	public final boolean isRunning() {
		return isRunning;
	}
	
	
	/**
	 * Call this function to do a pause equal to minimum plus a random value between 0 and randomPlus, I use this to simulate a real user
	 */
	protected final void subPause(int minimum, int randomPlus){
		Random randomGenerator = new Random();
		int pause=minimum + randomGenerator.nextInt(randomPlus);
		
		try {
			Thread.sleep(pause);
		} catch (InterruptedException e) {

		}
		
		
	}

	public final boolean isBlocked() {
		return blocked;
	}

	protected final void setBlocked() {
		this.blocked = true;
	}

}
