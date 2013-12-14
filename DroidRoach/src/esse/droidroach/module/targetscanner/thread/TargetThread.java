/*
 * This file is part of the Droid Roach project
 * Copyright 2013 Stefano Gabryel
 * Author: Stefano Gabryel
 * License: GPL v3 - http://www.gnu.org/licenses/gpl-3.0.html
 * Author website: http://www.stefano-workshop.com
 * Project website: http://droid-roach.stefano-workshop.com
 * 
 */

package esse.droidroach.module.targetscanner.thread;

import java.net.InetAddress;
import java.util.Vector;

import esse.droidroach.module.targetscanner.PortObject;
import esse.droidroach.utility.NetScanner;

public class TargetThread extends Thread {
	private int port;
	private InetAddress target;
	private final int defaultTimeout = 1000;
	private Vector<PortObject> ports;
	private boolean isRunning = true;
	
	
	
	
	public TargetThread(InetAddress target, int port, Vector<PortObject> ports){
		this.target = target;
		this.port = port;
		this.ports = ports;
		
	}


	@Override
	public void run() {
		super.run();
		 if (NetScanner.checkPort(target, port, defaultTimeout))
			 confirmPort();
		 
		 
		 isRunning=false;	
	}
	
	
	public boolean isRunning(){
		return this.isRunning;
	}
	
	private void confirmPort(){
		this.ports.add(new PortObject(port));
	}
	
	

}
