/*
 * This file is part of the Droid Roach project
 * Copyright 2013 Stefano Gabryel
 * Author: Stefano Gabryel
 * License: GPL v3 - http://www.gnu.org/licenses/gpl-3.0.html
 * Author website: http://www.stefano-workshop.com
 * Project website: http://droid-roach.stefano-workshop.com
 * 
 */

package esse.droidroach;

import java.util.ArrayList;
import java.util.HashMap;

import esse.droidroach.core.RoachActivity;
import esse.pentest.droidroach.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class MainActivity extends RoachActivity {
	public static final String TAG = "Main";
	private ArrayList<HashMap<String, String>> menuItems;
	private ListView list;

	private static String ACTION_DIRECTORY_BRUTEFORCE = "dirbrute";
	private static String ACTION_SUBDOMAIN_ENUMERATION = "subdomainenum";
	private static String ACTION_FTPBRUTE = "ftpbrute";
	private static String ACTION_TARGET_SCAN = "targetscan";
	private static String ACTION_IP_RANGE_SCANNER = "rangescan";
	private static String ACTION_USER_ENUMERATOR = "userenum";
	private static String ACTION_SETTINGS = "settings";
	private static String ACTION_ABOUT = "about";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		list = (ListView) findViewById(R.id.list);
		initializeMenu();
	}

	private void initializeMenu() {
		menuItems = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> item;

		item = new HashMap<String, String>();
		item.put(MainAdapter.KEY_TITLE, getText(R.string.MainMenuDirBruteTitle).toString());
		item.put(MainAdapter.KEY_DESCRIPTION, getText(R.string.MainMenuDirBruteDescription).toString());
		item.put(MainAdapter.KEY_ACTION, ACTION_DIRECTORY_BRUTEFORCE);
		item.put(MainAdapter.KEY_ICON, Integer.toString(R.drawable.main_dir_brute));
		menuItems.add(item);

		item = new HashMap<String, String>();
		item.put(MainAdapter.KEY_TITLE, getText(R.string.MainMenuSubDomainEnumTitle).toString());
		item.put(MainAdapter.KEY_DESCRIPTION, getText(R.string.MainMenuSubDomainEnumDescription).toString());
		item.put(MainAdapter.KEY_ACTION, ACTION_SUBDOMAIN_ENUMERATION);
		item.put(MainAdapter.KEY_ICON, Integer.toString(R.drawable.main_subdomain_enum));
		menuItems.add(item);

		item = new HashMap<String, String>();
		item.put(MainAdapter.KEY_TITLE, getText(R.string.MainMenuFtpBruteTitle).toString());
		item.put(MainAdapter.KEY_DESCRIPTION, getText(R.string.MainMenuFtpBruteDescription).toString());
		item.put(MainAdapter.KEY_ACTION, ACTION_FTPBRUTE);
		item.put(MainAdapter.KEY_ICON, Integer.toString(R.drawable.main_ftp_brute));
		menuItems.add(item);
		
		item = new HashMap<String, String>();
		item.put(MainAdapter.KEY_TITLE, getText(R.string.MainMenuTargetScannerTitle).toString());
		item.put(MainAdapter.KEY_DESCRIPTION, getText(R.string.MainMenuTargetScannerDescription).toString());
		item.put(MainAdapter.KEY_ACTION, ACTION_TARGET_SCAN);
		item.put(MainAdapter.KEY_ICON, Integer.toString(R.drawable.main_target_scanner));
		menuItems.add(item);

		item = new HashMap<String, String>();
		item.put(MainAdapter.KEY_TITLE, getText(R.string.MainMenuRangeIpScannerTitle).toString());
		item.put(MainAdapter.KEY_DESCRIPTION, getText(R.string.MainMenuRangeIpScannerDescription).toString());
		item.put(MainAdapter.KEY_ACTION, ACTION_IP_RANGE_SCANNER);
		item.put(MainAdapter.KEY_ICON, Integer.toString(R.drawable.main_range_scanner));
		menuItems.add(item);

		/*
		item = new HashMap<String, String>();
		item.put(MainAdapter.KEY_TITLE, getText(R.string.MainMenuUsersEnumeratorTitle).toString());
		item.put(MainAdapter.KEY_DESCRIPTION, getText(R.string.MainMenuUsersEnumeratorDescription).toString());
		item.put(MainAdapter.KEY_ACTION, ACTION_USER_ENUMERATOR);
		item.put(MainAdapter.KEY_ICON, Integer.toString(R.drawable.main_user_enum));
		menuItems.add(item);
		*/

		item = new HashMap<String, String>();
		item.put(MainAdapter.KEY_TITLE, getText(R.string.MainMenuSettingsTitle).toString());
		item.put(MainAdapter.KEY_DESCRIPTION, getText(R.string.MainMenuSettingsDescription).toString());
		item.put(MainAdapter.KEY_ACTION, ACTION_SETTINGS);
		item.put(MainAdapter.KEY_ICON, Integer.toString(R.drawable.main_settings));
		menuItems.add(item);
		
		item = new HashMap<String, String>();
		item.put(MainAdapter.KEY_TITLE, getText(R.string.MainMenuAboutTitle).toString());
		item.put(MainAdapter.KEY_DESCRIPTION, getText(R.string.MainMenuAboutDescription).toString());
		item.put(MainAdapter.KEY_ACTION, ACTION_ABOUT );
		item.put(MainAdapter.KEY_ICON, Integer.toString(R.drawable.main_about));
		menuItems.add(item);

		MainAdapter adapter = new MainAdapter(this, menuItems);
		list.setAdapter(adapter);
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				String action = menuItems.get(position).get(MainAdapter.KEY_ACTION);

				if (action.equals(ACTION_DIRECTORY_BRUTEFORCE)) {
					Intent i = new Intent(MainActivity.this, esse.droidroach.module.dirbruteforce.Activity.class);
					startActivity(i);
				} else if (action.equals(ACTION_SUBDOMAIN_ENUMERATION)) {
					Intent i = new Intent(MainActivity.this, esse.droidroach.module.subdomainenumerator.Activity.class);
					startActivity(i);
				} else if (action.equals(ACTION_FTPBRUTE)) {
					Intent i = new Intent(MainActivity.this, esse.droidroach.module.ftpbruteforce.Activity.class);
					startActivity(i);
				} else if (action.equals(ACTION_SETTINGS)) {
					Intent i = new Intent(MainActivity.this, SettingsActivity.class);
					startActivity(i);
				} else if (action.equals(ACTION_IP_RANGE_SCANNER)) {
					Intent i = new Intent(MainActivity.this, esse.droidroach.module.rangeipscanner.Activity.class);
					startActivity(i);
				} else if (action.equals(ACTION_TARGET_SCAN)) {
					Intent i = new Intent(MainActivity.this, esse.droidroach.module.targetscanner.Activity.class);
					startActivity(i);
				} else if (action.equals(ACTION_ABOUT)) {
					Intent i = new Intent(MainActivity.this, esse.droidroach.About.class);
					startActivity(i);
				}
				

			}

		});

	}

}
