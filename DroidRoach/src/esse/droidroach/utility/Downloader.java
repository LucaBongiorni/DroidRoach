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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.zip.GZIPInputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;



public class Downloader {
	private int connectionTimeout=2000;
	private int readTimeOut=4000;
	private UserAgentGen UAGen;
	private int responseCode=0;
	private String referer=null;
	
	
	public Downloader(boolean randomUAEveryTime){
		UAGen= new UserAgentGen(randomUAEveryTime);
		
	}
	
	
	
	/**
	 * Used to set the referer for the http requests
	 * @param referer
	 */
	public void setReferer(String referer){
		this.referer=referer;
	}
	
	public String launchDownload(String url, int amountOfTry) throws MalformedURLException, IOException{
		String result=null;
		int i=0;
		
		while (i<amountOfTry && result==null){
			result=download(url);
			i++;
		}
		
		
		
		return result;
	}
	
	
	

	private String download(String url) throws MalformedURLException, IOException {
		HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
		con.setRequestProperty("User-agent", UAGen.getUserAgent());
		con.setRequestProperty("Accept", " */*");
		con.setRequestProperty("Accept-Language", " en-US,en;q=0.5");
		// con.setRequestProperty("Accept-Encoding", "gzip, deflate");
		con.setRequestProperty("Accept-Charset", "UTF-8");
		
		if (referer!=null)
			con.setRequestProperty("Referer", referer);
		
		con.setReadTimeout(this.connectionTimeout);
		con.setConnectTimeout(this.readTimeOut);
		con.connect();

		responseCode=con.getResponseCode();

		
		if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
			String encoding = con.getContentEncoding();
			InputStream inStr = null;
			if (encoding != null && encoding.equalsIgnoreCase("gzip")) {
				inStr = new GZIPInputStream(con.getInputStream());
				return inputStreamToString(inStr);
			} else if (encoding != null && encoding.equalsIgnoreCase("deflate")) {
				inStr = new InflaterInputStream(con.getInputStream(), new Inflater(true));
				return inputStreamToString(inStr);
			} else {
				return inputStreamToString(con.getInputStream());
			}
		} else {
			return Integer.toString(con.getResponseCode());
		}
	}
	
	private String inputStreamToString(InputStream in) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(in));
		StringBuilder stringBuilder = new StringBuilder();
		String line = null;

		while ((line = bufferedReader.readLine()) != null) {
			stringBuilder.append(line);
		}

		bufferedReader.close();
		return stringBuilder.toString();
	}

	
	
	public int getResponseCode() {
		return responseCode;
	}



}
