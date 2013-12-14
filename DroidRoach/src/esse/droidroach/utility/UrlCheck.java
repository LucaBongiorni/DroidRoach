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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class UrlCheck {
	private int connectionTimeout = 2000;
	private int readTimeOut = 4000;
	private UserAgentGen UAGen;

	public UrlCheck(boolean randomEveryTime) {
		UAGen = new UserAgentGen(randomEveryTime);

	}

	public int launchCheck(String url, int amountOfTry) throws MalformedURLException, IOException {
		int result = 0;
		int i = 0;

		while (i < amountOfTry && result < 1) {
			result = download(url);
			i++;
		}

		return result;
	}

	private int download(String url) throws MalformedURLException, IOException {
		HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
		con.setRequestProperty("User-agent", UAGen.getUserAgent());
		con.setRequestProperty("Accept", " */*");
		con.setRequestProperty("Accept-Language", " en-US,en;q=0.5");
		// con.setRequestProperty("Accept-Encoding", "gzip, deflate");
		con.setRequestProperty("Accept-Charset", "UTF-8");
		con.setReadTimeout(this.connectionTimeout);
		con.setConnectTimeout(this.readTimeOut);
		con.connect();
		int responseCode = con.getResponseCode();
		con.disconnect();
		
		
		return responseCode;

	}

}
