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

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import esse.droidroach.module.subdomainenumerator.SubObjectManager;

public class GoogleThread extends SubThread {
	private int pauseBetweenRequests = 1000;
	private int maxRandomicPause = 1500;

	private final int DOWNLOADTRIES = 2;

	private String query;
	private int pages;
	private SubObjectManager objManager;
	private final String SOURCE = "Google";

	public GoogleThread(boolean humanMode, String query, int pages, SubObjectManager objManager) {
		if (humanMode) {
			pauseBetweenRequests = 3000;
			maxRandomicPause = 4000;
		}

		this.query = query;
		this.pages = pages;

		this.objManager = objManager;

	}

	@Override
	public void run() {
		super.run();
		dlManager.setReferer("http://www.google.com");
		for (int i = 0; i < pages && isRunning(); i++) {
			String result = null;

			int selected_page = i * 10;
			String path = "http://www.google.it/search?q=site:" + query + "&safe=off&start=" + selected_page;

			try {
				result = dlManager.launchDownload(path, DOWNLOADTRIES);
			} catch (MalformedURLException e) {
				result = null;
			} catch (IOException e) {
				result = null;
			}

			if (result == null) {
				continue;
			} else if (result.equalsIgnoreCase("503")) {// google blocked us
				setBlocked();
				break;
			}

			final Pattern pattern = Pattern.compile("<cite>(.+?)</cite>");
			final Matcher matcher = pattern.matcher(result);

			while (matcher.find())
				objManager.addObject(googleClearResult(matcher.group(1)), SOURCE);

			dlManager.setReferer(path);
			subPause(pauseBetweenRequests, maxRandomicPause);

		}

		stopRunning();
	}

	private String googleClearResult(String toClear) {
		toClear = toClear.replace("http://", "");// remove http
		toClear = toClear.replace("https://", "");// remove https
		toClear = toClear.replace("<span class=bc>", "");// remove https

		int first_slash_index = toClear.indexOf("/");// search for the first "/" inside the string
		if (first_slash_index > 0) {
			toClear = toClear.substring(0, first_slash_index);// take the domain only without any subfolder
		}
		first_slash_index = toClear.indexOf(" &rsaquo;");
		if (first_slash_index > 0) {
			toClear = toClear.substring(0, first_slash_index);
		}

		return toClear.trim();
	}

}
