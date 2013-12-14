/*
 * This file is part of the Droid Roach project
 * Copyright 2013 Stefano Gabryel
 * Author: Stefano Gabryel
 * License: GPL v3 - http://www.gnu.org/licenses/gpl-3.0.html
 * Author website: http://www.stefano-workshop.com
 * Project website: http://droid-roach.stefano-workshop.com
 * 
 */

package esse.droidroach.module.rangeipscanner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import esse.droidroach.core.RoachActivity;
import esse.droidroach.generic.GenericShellOutputActivity;
import esse.droidroach.module.rangeipscanner.thread.RangeThread;
import esse.droidroach.utility.IPv4;
import esse.pentest.droidroach.R;

public class Activity extends RoachActivity {
	private IPGetter ipv4Getter;
	private Vector<HostObject> hostsFound;
	private int maximumThreads = 60;
	private AsyncScan asyncScan;
	private ArrayList<HashMap<String, String>> results;
	private ListView list;
	private Adapter adapter;
	private ProgressDialog currentDialog;

	private static final int DIALOG_SCANNING = 443;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rangeipscanner);
		showTargetDialog(null);
		initialize();

		setConfirmOnBackPressed(true);

	}

	@Override
	protected void onStop() {
		super.onStop();

		if (asyncScan != null)
			asyncScan.stopRunning();
	}

	private void showShareOptions(final String host) {
		final AlertDialog.Builder menuAlert = new AlertDialog.Builder(this);
		final String[] menuList = { getString(R.string.GenericDialogListShareTargetScanner), getString(R.string.GenericDialogListShareWhois), getString(R.string.GenericDialogListShareNsLookup)};
		menuAlert.setTitle(R.string.GenericDialogListShareTitle);
		menuAlert.setItems(menuList, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {
				switch (item) {
				case 0:// scan target
					Intent scannerIntent = new Intent(Activity.this, esse.droidroach.module.targetscanner.Activity.class);
					scannerIntent.putExtra("ip", host);
					startActivity(scannerIntent);
					break;
				case 1:// whois
					Intent whoisIntent = new Intent(Activity.this, GenericShellOutputActivity.class);
					whoisIntent.putExtra("command", "whois " + host);
					startActivity(whoisIntent);
					break;
				case 2:// nslookup
					Intent nslookupIntent = new Intent(Activity.this, GenericShellOutputActivity.class);
					nslookupIntent.putExtra("command", "nslookup " + host);
					startActivity(nslookupIntent);
					break;
				}
			}
		});
		AlertDialog menuDrop = menuAlert.create();
		menuDrop.show();

	}

	private void initialize() {
		maximumThreads = Integer.parseInt(getRoachApplication().getDRPreferences().getString("IpRangeScanner_Threads", "60"));
		list = (ListView) findViewById(R.id.dirHostList);
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				

				HashMap<String, String> selectedItem = results.get(position);
				String host = selectedItem.get(Adapter.KEY_HOST);
				showShareOptions(host);
				
				

			}
		});

	}

	private void showTargetDialog(String passedUrl) {
		LinearLayout dialogLayout = new LinearLayout(this);
		dialogLayout.setOrientation(1);

		final EditText input = new EditText(this);

		input.setHint(R.string.RangeIPScannerActivityDialogHint);
		input.setSingleLine();
		input.setInputType(InputType.TYPE_CLASS_NUMBER);
		input.setKeyListener(DigitsKeyListener.getInstance("0123456789./"));
		if (passedUrl != null)
			input.setText(passedUrl);

		dialogLayout.addView(input);

		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle(getString(R.string.RangeIPScannerActivityDialogTitle));
		alert.setMessage(getString(R.string.RangeIPScannerActivityDialogDescription));
		alert.setView(dialogLayout);
		alert.setCancelable(false);
		alert.setPositiveButton(getString(R.string.RangeIPScannerActivityDialogOkButton), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				String ip = input.getText().toString();
				try {
					ipv4Getter = new IPGetter(new IPv4(ip));
					startScan();
				} catch (Exception e) {
					ipv4Getter = null;
					showWarningDialog(ip);
				}
			}
		});

		alert.setNegativeButton(getString(R.string.RangeIPScannerActivityDialogCancelButton), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				Activity.this.finish();
			}
		});

		alert.show();
	}

	private void showWarningDialog(final String triedIP) {
		LinearLayout dialogLayout = new LinearLayout(this);
		dialogLayout.setOrientation(1);

		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle(getString(R.string.RangeIPScannerActivityDialogTitle));
		alert.setMessage(getString(R.string.RangeIPScannerActivityDialogWarning));
		alert.setView(dialogLayout);
		alert.setCancelable(false);
		alert.setPositiveButton(getString(R.string.RangeIPScannerActivityDialogWarningOkButton), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				showTargetDialog(triedIP);
			}
		});

		alert.show();
	}

	@SuppressWarnings("deprecation")
	private void startScan() {
		hostsFound = new Vector<HostObject>();
		results = new ArrayList<HashMap<String, String>>();
		adapter = new Adapter(this, results);
		list.setAdapter(adapter);

		asyncScan = new AsyncScan();
		asyncScan.execute("");
		showDialog(DIALOG_SCANNING);

	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_SCANNING: {
			ProgressDialog dialog = new ProgressDialog(this);
			dialog.setTitle(R.string.RangeIPScannerDialogTitle);
			dialog.setMessage("0/" + ipv4Getter.getIPCount() + " " + getString(R.string.RangeIPScannerDialogText));
			dialog.setIndeterminate(true);
			dialog.setCancelable(false);
			dialog.setOnCancelListener(new OnCancelListener() {

				@Override
				public void onCancel(DialogInterface dialog) {
					asyncScan.stopRunning();
					finish();
				}
			});

			dialog.setOnKeyListener(new OnKeyListener() {
				@Override
				public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
					if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
						new AlertDialog.Builder(Activity.this).setIcon(android.R.drawable.ic_dialog_alert).setTitle(R.string.GenericQuitDialogTitle).setMessage(R.string.GenericQuitDialogDescription)
								.setPositiveButton(R.string.GenericQuitDialogOkButton, new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										asyncScan.stopRunning();
										finish();
									}

								}).setNegativeButton(R.string.GenericQuitDialogCancelButton, null).show();
					}
					return true;
				}
			});

			currentDialog = dialog;
			return dialog;
		}

		}
		return null;
	}

	private class AsyncScan extends AsyncTask<String, String, String> {
		private boolean isRunning = true;
		private Vector<RangeThread> threads;

		@SuppressWarnings("deprecation")
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			try {
				dismissDialog(DIALOG_SCANNING);
			} catch (Exception e) {

			}

			Toast.makeText(Activity.this, R.string.RangeIpScannerScanComplete, Toast.LENGTH_LONG).show();

		}

		@Override
		protected void onProgressUpdate(String... values) {
			super.onProgressUpdate(values);

			boolean refresh = false;
			int loop = hostsFound.size();
			for (int i = 0; i < loop; i++) {
				if (!hostsFound.get(i).isDisplayed()) {
					HashMap<String, String> newHost = new HashMap<String, String>();
					newHost.put(Adapter.KEY_HOST, hostsFound.get(i).getIp());
					results.add(newHost);
					hostsFound.get(i).setDisplayed();
					refresh = true;
				}

			}

			if (refresh) {
				adapter.notifyDataSetChanged();

			}

			currentDialog.setMessage(ipv4Getter.getUsedIp() + "/" + ipv4Getter.getIPCount() + " " + getString(R.string.RangeIPScannerDialogText));

		}

		@Override
		protected String doInBackground(String... params) {
			threads = new Vector<RangeThread>();

			for (int i = 0; i < maximumThreads; i++) {
				RangeThread t = new RangeThread(ipv4Getter, hostsFound);
				threads.add(t);
				t.start();
			}

			while (isRunning) {

				// check the running threads ##############################
				int notRunningThreads = 0;
				for (int i = 0; i < maximumThreads; i++) {
					if (!threads.get(i).isRunning())
						notRunningThreads++;
				}
				if (notRunningThreads == maximumThreads)
					isRunning = false;
				// check the running threads ##############################

				publishProgress(null);

				try {
					Thread.sleep(1500);
				} catch (InterruptedException e) {
				}

			}

			return null;
		}

		public void stopRunning() {
			for (int i = 0; i < maximumThreads; i++) {
				threads.get(i).stopRunning();
			}

		}

	}

}
