/*
 * This file is part of the Droid Roach project
 * Copyright 2013 Stefano Gabryel
 * Author: Stefano Gabryel
 * License: GPL v3 - http://www.gnu.org/licenses/gpl-3.0.html
 * Author website: http://www.stefano-workshop.com
 * Project website: http://droid-roach.stefano-workshop.com
 * 
 */

package esse.droidroach.utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;



public class BusyBoxAccess {
	private boolean isBusyBoxAvailable = false;
	
	
	public BusyBoxAccess(){
		checkBusyBox();
	}
	
	private void checkBusyBox(){
		String outputStr = executeCommand("busybox");
		
		if (outputStr != null && outputStr.indexOf("BusyBox")>-1)
			isBusyBoxAvailable = true;
			
		
	}
	
	public boolean isAvailable(){
		return isBusyBoxAvailable;
	}
	
	
	public String executeCommand(String command){
	
		try {
	        Process chmod = Runtime.getRuntime().exec(command);

	        BufferedReader reader = new BufferedReader(
	                new InputStreamReader(chmod.getInputStream()));

	        int read;
	        char[] buffer = new char[4096];
	        StringBuffer output = new StringBuffer();
	        while ((read = reader.read(buffer)) > 0) {
	            output.append(buffer, 0, read);
	        }
	        reader.close();
	        chmod.waitFor();
	        return output.toString();
	    } catch (IOException e) {
	    	
	    } catch (InterruptedException e) {

	    }
		
		return null;
		
	}

}
