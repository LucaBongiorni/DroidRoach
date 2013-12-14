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

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

public class NetScanner {

	public final static int[] DEFAULT_PORTS = { 80, 21, 22, 25, 53, 110, 137, 138, 139, 443, 445, 587, 993, 1433, 3306, 3389, 5800, 5900, 5938, 8080 };
	public final static int[] DEFAULT_PORTS_EXTENDED = { 20, 21, 22, 23, 25, 43, 53, 80, 101, 107, 110, 115, 118, 137, 138, 139, 143, 389, 443, 445, 465, 530, 587, 660, 666, 783, 843, 901, 991, 953, 992, 993, 995, 1194, 1241, 1433, 1434, 1521,
			1720, 2082, 2083, 2095, 2096, 2121, 2483, 2484, 2638, 3000, 3128, 3306, 3389, 3535, 3541, 3790, 3872, 4125, 4711, 5108, 5353, 5432, 5800, 5900, 5938, 5984, 6000, 6112, 8008, 8080, 8081 };

	public static int isReachable(InetAddress host, int timeout) {
		return isReachable(DEFAULT_PORTS, host, timeout);
	}

	public static int isReachable(int[] ports, InetAddress host, int timeout) {
		int loop = ports.length;

		for (int i = 0; i < loop; i++) {
			try {
				Socket s = new Socket();
				s.bind(null);
				s.connect(new InetSocketAddress(host, ports[i]), timeout);
				s.close();
				return ports[i];
			} catch (IOException e) {
			}
			
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {

			}
			
		}
		return -1;
	}

	// da testare
	public static boolean checkPort(InetAddress host, int port, int timeout) {
		try {
			Socket s = new Socket();
			s.bind(null);
			s.connect(new InetSocketAddress(host, port), timeout);
			s.close();
			return true;
		} catch (IOException e) {
		}

		return false;

	}

}
