/*
 * This file is part of the Droid Roach project
 * Copyright 2013 Stefano Gabryel
 * Author: Stefano Gabryel
 * License: GPL v3 - http://www.gnu.org/licenses/gpl-3.0.html
 * Author website: http://www.stefano-workshop.com
 * Project website: http://droid-roach.stefano-workshop.com
 * 
 */

package esse.droidroach.core;


import esse.pentest.droidroach.R;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class RoachApplication extends Application {
	public static int CONNECTION_TYPE_NOT_CONNECTED = 0;
	public static int CONNECTION_TYPE_UNKNOWN = 1;
	public static int CONNECTION_TYPE_WIFI = 2;
	public static int CONNECTION_TYPE_3G = 3;
	private SharedPreferences preferences;
	public static final String DROID_ROACH_VERSION = "v0.9b";

	
	
	
	

	@Override
	public void onCreate() {
		super.onCreate();
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		
		//if not connected to a network just send a warning to the user
		if (isConnected() == CONNECTION_TYPE_NOT_CONNECTED)
			Toast.makeText(this, R.string.RoachBaseActivityNoConnection, Toast.LENGTH_LONG).show();
		
		
		
	}
	
	
	public SharedPreferences getDRPreferences(){
		return preferences;
	}
	





	public int isConnected() {
		final ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (wifiNetwork != null && wifiNetwork.isConnected()) {
			return CONNECTION_TYPE_WIFI;
		}

		NetworkInfo mobileNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (mobileNetwork != null && mobileNetwork.isConnected()) {
			return CONNECTION_TYPE_3G;
		}

		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		if (activeNetwork != null && activeNetwork.isConnected()) {
			return CONNECTION_TYPE_UNKNOWN;
		}

		return CONNECTION_TYPE_NOT_CONNECTED;
	}

}
