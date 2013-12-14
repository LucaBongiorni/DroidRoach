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

import java.net.URI;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NetUtility {

	public static boolean checkIP(String value) {
		Pattern pattern = Pattern.compile("^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
				"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
				"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
				"([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");
		Matcher matcher = pattern.matcher(value);
		boolean IPcheck = matcher.matches();

		if (IPcheck)
			return true;

		return false;
	}
	
	public static boolean checkDomain(String domain){
		Pattern pattern;
		Matcher matcher;
		pattern = Pattern.compile("^[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$");
		matcher = pattern.matcher(domain);
		return matcher.matches();

	}

	public static boolean isURI(String str) {

		if (str.indexOf(':') == -1)
			return false;
		str = str.toLowerCase(Locale.ENGLISH).trim();

		if (!str.startsWith("http://") && !str.startsWith("https://") && !str.startsWith("ftp://"))
			return false;

		try {

			URI uri = new URI(str);
			String proto = uri.getScheme();

			if (proto == null)
				return false;

			if (proto.equals("http") || proto.equals("https") || proto.equals("ftp")) {

				if (uri.getHost() == null)
					return false;

				String path = uri.getPath();
				if (path != null) {

					int len = path.length();
					for (int i = 0; i < len; i++) {

						if ("?<>:*|\"".indexOf(path.charAt(i)) > -1)
							return false;
					}
				}
			}

			return true;
		} catch (Exception ex) {

			return false;
		}
	}

	// return the targeted url's domain
	public static String getDomain(String target) {
		Pattern p = Pattern.compile(".*?([^.]+\\.[^.]+)");
		URI uri;

		try {
			uri = new URI(target);

		} catch (Exception e) {
			return "http";
		}

		String host = uri.getHost();
		Matcher m = p.matcher(host);

		if (m.matches())
			return m.group(1);
		else
			return host;
	}

}
