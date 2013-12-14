/*
 * This file is part of the Droid Roach project
 * Copyright 2013 Stefano Gabryel
 * Author: Stefano Gabryel
 * License: GPL v3 - http://www.gnu.org/licenses/gpl-3.0.html
 * Author website: http://www.stefano-workshop.com
 * Project website: http://droid-roach.stefano-workshop.com
 * 
 */

package esse.droidroach.module.rangeipscanner;

import java.util.Vector;

public class HostObject {
	private String ip;
	private Vector<Integer> openPorts;
	private boolean displayed = false;
	
	
	public HostObject (String ip){
		this.ip = ip;
	}
	
	/**
	 * add a port known to be open on this host
	 * @param port
	 */
	public void addOpenPort(int port) {
		openPorts.add(port);
	}

	/**
	 * get a vector containing the open ports
	 * @return
	 */
	public Vector<Integer> getOpenPorts() {
		return openPorts;
	}

	
	public String getIp() {
		return ip;
	}

	public boolean isDisplayed() {
		return displayed;
	}

	public void setDisplayed() {
		this.displayed = true;
	}

}
