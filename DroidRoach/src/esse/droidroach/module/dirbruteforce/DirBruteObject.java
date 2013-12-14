/*
 * This file is part of the Droid Roach project
 * Copyright 2013 Stefano Gabryel
 * Author: Stefano Gabryel
 * License: GPL v3 - http://www.gnu.org/licenses/gpl-3.0.html
 * Author website: http://www.stefano-workshop.com
 * Project website: http://droid-roach.stefano-workshop.com
 * 
 */

package esse.droidroach.module.dirbruteforce;



public class DirBruteObject {
	private String url;
	private int responseCode;
	private boolean isDirectory;
	private boolean bruted=false;
	private boolean isDisplayed=false;
	private int depth;
	
	public DirBruteObject(String url, int responseCode, boolean isDirectory, int depth){
		this.url=url;
		this.responseCode=responseCode;
		this.isDirectory=isDirectory;
		this.depth=depth;
	}
	
	
	public String getUrl() {
		return url;
	}
	public int getResponseCode() {
		return responseCode;
	}
	public boolean isDirectory() {
		return isDirectory;
	}


	public boolean alreadyBruted() {
		return bruted;
	}


	public void setBruted() {
		this.bruted = true;
	}


	public boolean isDisplayed() {
		return isDisplayed;
	}


	public void setDisplayed() {
		this.isDisplayed = true;
	}


	public int getDepth() {
		return depth;
	}

	

}
