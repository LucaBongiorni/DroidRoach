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

import java.util.List;

import esse.droidroach.utility.IPv4;

public class IPGetter {
	private List<String> addresses;
	private int currentIP = -1;
	private int countIP = 0;

	public IPGetter(IPv4 ipv4) {
		addresses = ipv4.getAvailableIPs(999999999);
		countIP = addresses.size();
	}

	public String getNextIp() {

		if (currentIP < countIP-1) {
			currentIP++;
			return addresses.get(currentIP);
		}

		return null;
	}

	public int getIPCount() {
		return countIP;
	}

	public int getUsedIp() {
		return currentIP+1;
	}

}
