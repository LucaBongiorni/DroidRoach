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

import java.net.InetAddress;
import java.net.UnknownHostException;

import esse.droidroach.module.subdomainenumerator.List;
import esse.droidroach.module.subdomainenumerator.SubObjectManager;

public class BruteThread extends SubThread {
	private int pauseBetweenRequests = 50;
	private int maxRandomicPause = 300;

	private SubObjectManager objManager;
	private final String SOURCE = "Brute Force";
	private String[] subdomains;
	private String target;

	public BruteThread(SubObjectManager objManager, String target) {
		this.objManager = objManager;
		this.target = target;
		subdomains = List.getSubdomainsList();
	}

	@Override
	public void run() {
		super.run();

		int loop = subdomains.length;

		for (int i = 0; i < loop && isRunning(); i++) {
			String subdomain = String.format("%s.%s", subdomains[i], target);

			if (!objManager.subdomainExists(subdomain))
				if (checkSubdomain(subdomain))
					objManager.addObject(subdomain, SOURCE);
			
			
			subPause(pauseBetweenRequests, maxRandomicPause);
		}

		stopRunning();
	

	}

	private boolean checkSubdomain(String subdomain) {

		InetAddress inetAddress = null;

		try {
			inetAddress = InetAddress.getByName(subdomain);
			inetAddress.getHostAddress();
			return true;
		} catch (UnknownHostException e) {
			return false;
		}

	}

}
