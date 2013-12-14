/*
 * This file is part of the Droid Roach project
 * Copyright 2013 Stefano Gabryel
 * Author: Stefano Gabryel
 * License: GPL v3 - http://www.gnu.org/licenses/gpl-3.0.html
 * Author website: http://www.stefano-workshop.com
 * Project website: http://droid-roach.stefano-workshop.com
 * 
 */

package esse.droidroach.module.targetscanner;

public class PortObject {
	private int port;
	private String service;
	private boolean isDisplayed=false;
	
	
	public PortObject(int port){
		this.port = port;
		service = ServicesList.getServiceName(port);
	}
	
	public String getService(){
		return service;
	}
	
	public int getPort(){
		return port;
	}

	public boolean isDisplayed() {
		return isDisplayed;
	}

	public void setDisplayed() {
		this.isDisplayed = true;
	}

}
