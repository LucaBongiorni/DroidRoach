/*
 * This file is part of the Droid Roach project
 * Copyright 2013 Stefano Gabryel
 * Author: Stefano Gabryel
 * License: GPL v3 - http://www.gnu.org/licenses/gpl-3.0.html
 * Author website: http://www.stefano-workshop.com
 * Project website: http://droid-roach.stefano-workshop.com
 * 
 */

package esse.droidroach.module.dirbruteforce.thread;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.Vector;



import esse.droidroach.module.dirbruteforce.DirBruteObject;
import esse.droidroach.module.dirbruteforce.List;
import esse.droidroach.utility.UrlCheck;

public class ScanThread extends Thread {
	private DirBruteObject target;
	private UrlCheck urlCheck;
	private Vector<DirBruteObject> result;
	private int amountOfTry = 3;
	private boolean running = true;

	int goodResponses[] = new int[] { 200, 202, 206, 207, 300, 301, 302, 303, 307, 401, 402, 403, 406, 451 };

	public ScanThread(DirBruteObject target, UrlCheck urlCheck, Vector<DirBruteObject> result) {
		this.target = target;
		this.urlCheck = urlCheck;
		this.result = result;
		target.setBruted();
	}

	@Override
	public void run() {
		super.run();
		String url = target.getUrl();

		if (!url.endsWith("/"))
			url = url + "/";

		int loop = List.getDirList().length;
		for (int i = 0; i < loop && running == true; i++) {
			int urlCheckResult = 0;
			String urlToScan = url + List.getDirList()[i] + "/";

			try {
				urlCheckResult = urlCheck.launchCheck(urlToScan, amountOfTry);
			} catch (MalformedURLException e1) {
				urlCheckResult = 0;
			} catch (IOException e1) {
				urlCheckResult = 0;

			}
			
			if (Arrays.binarySearch(goodResponses, urlCheckResult) >= 0) {
				int loopResult = result.size();
				boolean toInsert = true;

				for (int y = 0; y < loopResult; y++) {
					if (urlToScan.equals(result.get(y).getUrl())) {
						toInsert = false;
						break;
					}
				}

				if (toInsert) {
					result.add(new DirBruteObject(urlToScan, urlCheckResult, true, target.getDepth() - 1));
					searchForFiles(urlToScan);
				}
			}

			try {
				Thread.sleep(150);
			} catch (InterruptedException e) {

			}

		}

		stopRunning();

	}

	private void searchForFiles(String target) {
		int loop = List.getDirList().length;
		for (int i = 0; i < loop && running == true; i++) {
			int urlCheckResult = 0;

			int extLoop = List.getExtList().length;
			for (int y = 0; y < extLoop && running; y++) {
				String urlToScan = target  + List.getDirList()[i] + "." + List.getExtList()[y];

				try {
					urlCheckResult = urlCheck.launchCheck(urlToScan, amountOfTry);
				} catch (MalformedURLException e1) {
					urlCheckResult = 0;

				} catch (IOException e1) {
					urlCheckResult = 0;

				}
			
				if (Arrays.binarySearch(goodResponses, urlCheckResult) >= 0) {
					int loopResult = result.size();
					boolean toInsert = true;

					for (int x = 0; x < loopResult; x++) {
						if (urlToScan.equals(result.get(x).getUrl())) {
							toInsert = false;
							break;
						}
					}

					if (toInsert) {
						result.add(new DirBruteObject(urlToScan, urlCheckResult, false, 0));
					}
				}
				
				
				
				try {
					Thread.sleep(150);
				} catch (InterruptedException e) {

				}

			}
			
			

		}

	}

	public boolean isRunning() {
		return running;
	}

	public void stopRunning() {
		this.running = false;
	}

}
