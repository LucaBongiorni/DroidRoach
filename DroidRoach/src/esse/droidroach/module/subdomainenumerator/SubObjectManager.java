/*
 * This file is part of the Droid Roach project
 * Copyright 2013 Stefano Gabryel
 * Author: Stefano Gabryel
 * License: GPL v3 - http://www.gnu.org/licenses/gpl-3.0.html
 * Author website: http://www.stefano-workshop.com
 * Project website: http://droid-roach.stefano-workshop.com
 * 
 */

package esse.droidroach.module.subdomainenumerator;

import java.util.Vector;

public class SubObjectManager {
	private Vector<SubObject> objects;
	private String originalDomain;

	public SubObjectManager(Vector<SubObject> results, String originalDomain) {
		this.objects = results;
		this.originalDomain = originalDomain;
	}
	
	public boolean subdomainExists(String subdomain){
		
		int loop=objects.size();
		for (int i=0; i<loop; i++)
			if (objects.get(i).getSubdomain().equals(subdomain))
				return true;
		
		return false;
		
	}

	public boolean addObject(String subdomain, String source) {


		for (int i=0; i<objects.size(); i++)
			if (objects.get(i).getSubdomain().equals(subdomain))
				return false;

		if (!subdomain.endsWith("."+originalDomain))
			return false;
		
		objects.add(new SubObject(subdomain, source));

		return true;
	}

}
