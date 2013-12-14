/*
 * This file is part of the Droid Roach project
 * Copyright 2013 Stefano Gabryel
 * Author: Stefano Gabryel
 * License: GPL v3 - http://www.gnu.org/licenses/gpl-3.0.html
 * Author website: http://www.stefano-workshop.com
 * Project website: http://droid-roach.stefano-workshop.com
 * 
 */

package esse.droidroach.module.subdomainenumerator;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * This class is just a container for the result of the Subdomain enumerator
 * 
 * 
 */
public class SubObject {
	private String subdomain;
	private String source;
	private String ip;
	private boolean isDisplayed = false;

	/**
	 * Class constructor, it just contains the subdomain and the source of this subdomain(example: google, bing or bruteforce)
	 * 
	 * @param subdomain
	 * @param ip
	 * @param source
	 */
	public SubObject(String subdomain, String source) {
		this.subdomain = subdomain;
		this.source = source;
		setIp();

	}

	private void setIp() {
		try {
			InetAddress address = InetAddress.getByName(subdomain);
			ip = address.getHostAddress();
		} catch (UnknownHostException e) {
			ip="";
		}
	}

	public String getSubdomain() {
		return subdomain;
	}

	public String getSource() {
		return source;
	}
	
	public String getIp(){
		return ip;
	}

	public boolean isDisplayed() {
		return isDisplayed;
	}

	public void setDisplayed() {
		this.isDisplayed = true;
	}

}
