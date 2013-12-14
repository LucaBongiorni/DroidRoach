/*
 * This file is part of the Droid Roach project
 * Copyright 2013 Stefano Gabryel
 * Author: Stefano Gabryel
 * License: GPL v3 - http://www.gnu.org/licenses/gpl-3.0.html
 * Author website: http://www.stefano-workshop.com
 * Project website: http://droid-roach.stefano-workshop.com
 * 
 */

package esse.droidroach.module.rangeipscanner.thread;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Vector;

import esse.droidroach.module.rangeipscanner.HostObject;
import esse.droidroach.module.rangeipscanner.IPGetter;
import esse.droidroach.utility.NetScanner;

public class RangeThread extends Thread {
	private IPGetter ipv4Getter;
	private Vector<HostObject> hostsFound;
	private final int DEFAULT_TIMEOUT = 400;
	private final int DEFAULT_TIMEOUT_ICMP = 700;
	private boolean isRunning = true;

	public RangeThread(IPGetter ipv4Getter, Vector<HostObject> hostsFound) {
		this.ipv4Getter = ipv4Getter;
		this.hostsFound = hostsFound;

	}

	@Override
	public void run() {
		super.run();

		String ip = ipv4Getter.getNextIp();
		while (ip != null && isRunning) {
			
			try {
				if (InetAddress.getByName(ip).isReachable(DEFAULT_TIMEOUT_ICMP)){
					addHost(ip);
					ip = ipv4Getter.getNextIp();
					continue;
				}
			} catch (UnknownHostException e1) {
			} catch (IOException e1) {
			}
				
			
			try {
				if (NetScanner.isReachable(InetAddress.getByName(ip), DEFAULT_TIMEOUT) > 0)
					addHost(ip);

			} catch (UnknownHostException e) {

			}
			
			
			ip = ipv4Getter.getNextIp();
			
			
			
			try {
				Thread.sleep(15);
			} catch (InterruptedException e) {
			}
			
		}

		isRunning = false;

	}

	public void addHost(String ip) {
		hostsFound.add(new HostObject(ip));
	}

	public boolean isRunning() {
		return this.isRunning;
	}

	public void stopRunning() {
		this.isRunning = false;
	}

}
