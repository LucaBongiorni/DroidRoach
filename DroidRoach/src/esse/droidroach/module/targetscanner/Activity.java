/*
 * This file is part of the Droid Roach project
 * Copyright 2013 Stefano Gabryel
 * Author: Stefano Gabryel
 * License: GPL v3 - http://www.gnu.org/licenses/gpl-3.0.html
 * Author website: http://www.stefano-workshop.com
 * Project website: http://droid-roach.stefano-workshop.com
 * 
 */

package esse.droidroach.module.targetscanner;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Vector;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnKeyListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import esse.droidroach.core.RoachActivity;
import esse.droidroach.generic.GenericShellOutputActivity;
import esse.droidroach.module.targetscanner.thread.TargetThread;
import esse.droidroach.utility.NetScanner;
import esse.droidroach.utility.NetUtility;
import esse.pentest.droidroach.R;

public class Activity extends RoachActivity {
	private String ip;
	private int startingPort = -1;
	private int endPort = -1;
	private final int SUPERFICIAL_MODE = 0;
	private final int DEEP_MODE = 1;
	private final int CUSTOM_MODE = 2;
	private int scanMode = -1;
	private ArrayList<HashMap<String, String>> results;
	private ListView list;
	private Adapter adapter;
	private AsyncScanner asyncScanner;
	private TextView ipTextView;
	private final int DIALOG_SCANNING = 666;
	@SuppressWarnings("unused")
	private ProgressDialog currentDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_targetscanner);

		setConfirmOnBackPressed(true);

		initialize();

	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.target_scanner_menu, menu);
		return true;
	}
	
	
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.ftpBrute:
			Intent ftpbruteIntent = new Intent(Activity.this, esse.droidroach.module.ftpbruteforce.Activity.class);
			ftpbruteIntent.putExtra("ip", ip);
			startActivity(ftpbruteIntent);
			break;
		case R.id.whois:
			Intent whoisIntent = new Intent(Activity.this, GenericShellOutputActivity.class);
			whoisIntent.putExtra("command", "whois " + ip);
			startActivity(whoisIntent);
			break;
		case R.id.nslookup:
			Intent nslookupIntent = new Intent(Activity.this, GenericShellOutputActivity.class);
			nslookupIntent.putExtra("command", "nslookup " + ip);
			startActivity(nslookupIntent);
			break;

		}

		return super.onOptionsItemSelected(item);
	}

	private void initialize() {
		ipTextView = (TextView) findViewById(R.id.ipTextView);

		results = new ArrayList<HashMap<String, String>>();
		list = (ListView) findViewById(R.id.infoList);
		adapter = new Adapter(this, results);
		list.setAdapter(adapter);

		ip = null;

		// if a value is passed inside the intent we can start the scan if not we ask to user to insert a value
		try {
			ip = getIntent().getExtras().getString("ip");
			ipTextView.setText(ip);
			scanModeSelect();
		} catch (Exception e) {
			showTargetDialog(null);
		}

	}

	private void showTargetDialog(String passedIP) {
		LinearLayout dialogLayout = new LinearLayout(this);
		dialogLayout.setOrientation(1);

		final EditText input = new EditText(this);

		input.setSingleLine();

		if (passedIP != null)
			input.setText(passedIP);

		dialogLayout.addView(input);

		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle(getString(R.string.TargetScannerActivityDialogTitle));
		alert.setMessage(getString(R.string.TargetScannerActivityDialogDescription));
		alert.setView(dialogLayout);
		alert.setCancelable(false);
		alert.setPositiveButton(getString(R.string.TargetScannerActivityDialogOkButton), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				ip = input.getText().toString();
				if (NetUtility.checkIP(ip) || NetUtility.checkDomain(ip)) {
					scanModeSelect();
				} else {
					showWarningDialog(ip);

				}

			}
		});

		alert.setNegativeButton(getString(R.string.TargetScannerActivityDialogCancelButton), new DialogInterface.OnClickListener() {
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
		alert.setTitle(getString(R.string.TargetScannerActivityDialogTitle));
		alert.setMessage(getString(R.string.TargetScannerActivityDialogWarning));
		alert.setView(dialogLayout);
		alert.setCancelable(false);
		alert.setPositiveButton(getString(R.string.TargetScannerActivityDialogWarningOkButton), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				showTargetDialog(triedIP);
			}
		});

		alert.show();
	}

	private void showPortWarningDialog() {
		LinearLayout dialogLayout = new LinearLayout(this);
		dialogLayout.setOrientation(1);

		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle(getString(R.string.TargetScannerActivityDialogPortTitle));
		alert.setMessage(getString(R.string.TargetScannerActivityDialogPortWarning));
		alert.setView(dialogLayout);
		alert.setCancelable(false);
		alert.setPositiveButton(getString(R.string.TargetScannerActivityDialogPortWarningOkButton), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				selectPorts();
			}
		});

		alert.show();
	}

	private void scanModeSelect() {
		LinearLayout dialogLayout = new LinearLayout(this);
		dialogLayout.setOrientation(1);
		CharSequence[] choicesArray = { getText(R.string.TargetScannerActivityDialogScanTypeSuperficial), getText(R.string.TargetScannerActivityDialogScanTypeDeep), getText(R.string.TargetScannerActivityDialogScanTypeCustom) };
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle(getString(R.string.TargetScannerActivityDialogScanTypeTitle));
		alert.setView(dialogLayout);
		alert.setSingleChoiceItems(choicesArray, 1, null);
		alert.setPositiveButton(getString(R.string.TargetScannerActivityDialogOkButton), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				scanMode = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
				if (scanMode == 2)
					selectPorts();
				else
					startScan();
			}
		});

		alert.setNegativeButton(getString(R.string.TargetScannerActivityDialogCancelButton), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				Activity.this.finish();
			}
		});

		alert.show();

	}

	@Override
	protected void onStop() {
		super.onStop();

		if (asyncScanner != null)
			asyncScanner.stopScan();
	}

	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_SCANNING: {
			ProgressDialog dialog = new ProgressDialog(this);
			dialog.setTitle(R.string.TargetScannerActivityScanningDialogText);
			dialog.setIndeterminate(true);
			dialog.setCancelable(false);
			dialog.setOnCancelListener(new OnCancelListener() {

				@Override
				public void onCancel(DialogInterface dialog) {
					asyncScanner.stopScan();
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
										asyncScanner.stopScan();
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

	private void selectPorts() {
		LinearLayout dialogLayout = new LinearLayout(this);
		dialogLayout.setOrientation(1);

		// dialog to pick up specific ports
		final EditText input1 = new EditText(this);
		final EditText input2 = new EditText(this);
		input1.setHint(R.string.TargetScannerActivityDialogPortMinHint);
		input1.setSingleLine();
		input1.setInputType(InputType.TYPE_CLASS_NUMBER);
		input1.setKeyListener(DigitsKeyListener.getInstance("0123456789"));

		input2.setHint(R.string.TargetScannerActivityDialogPortMaxHint);
		input2.setSingleLine();
		input2.setInputType(InputType.TYPE_CLASS_NUMBER);
		input2.setKeyListener(DigitsKeyListener.getInstance("0123456789"));

		dialogLayout.addView(input1);
		dialogLayout.addView(input2);
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle(getString(R.string.TargetScannerActivityDialogPortTitle));
		alert.setMessage(getString(R.string.TargetScannerActivityDialogPortText));
		alert.setView(dialogLayout);
		alert.setCancelable(false);
		alert.setPositiveButton(getString(R.string.TargetScannerActivityDialogOkButton), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				try {
					if (Integer.parseInt(input1.getText().toString()) < Integer.parseInt(input2.getText().toString())) {
						startingPort = Integer.parseInt(input1.getText().toString());
						endPort = Integer.parseInt(input2.getText().toString());
						startScan();
					} else if (Integer.parseInt(input1.getText().toString()) > Integer.parseInt(input2.getText().toString())) {
						startingPort = Integer.parseInt(input2.getText().toString());
						endPort = Integer.parseInt(input1.getText().toString());
						startScan();
					} else {
						showPortWarningDialog();
					}
				} catch (Exception e) {
					showPortWarningDialog();
				}

			}
		});

		alert.setNegativeButton(getString(R.string.TargetScannerActivityDialogCancelButton), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				Activity.this.finish();
			}
		});

		alert.show();
	}



	@SuppressWarnings("deprecation")
	private void startScan() {
		// hide the keyboard
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
		// hide the keyboard

		ipTextView.setText(ip);
		asyncScanner = new AsyncScanner();
		asyncScanner.execute("");
		showDialog(DIALOG_SCANNING);
	}
	
	
	class CustomComparator implements Comparator<HashMap<String, String>> {

	    @Override
	    public int compare(HashMap<String, String> lhs, HashMap<String, String> rhs) {
	    	return lhs.get(Adapter.KEY_PORT).compareTo(rhs.get(Adapter.KEY_PORT));
	    }
	}

	private class AsyncScanner extends AsyncTask<String, String, String> {
		private int maxThreads = 6;
		private Vector<PortObject> ports;
		private Vector<TargetThread> threads;
		private boolean isRunning = true;

		public AsyncScanner() {
			this.ports = new Vector<PortObject>();
			this.threads = new Vector<TargetThread>();
		}

		@SuppressWarnings("deprecation")
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			displayData();

			try {
				dismissDialog(DIALOG_SCANNING);
			} catch (Exception e) {

			}

			Toast.makeText(Activity.this, R.string.RangeIpScannerScanComplete, Toast.LENGTH_LONG).show();
		}

		@Override
		protected void onProgressUpdate(String... values) {
			super.onProgressUpdate(values);
			displayData();

		}

		@Override
		protected String doInBackground(String... params) {

			switch (scanMode) {

			case SUPERFICIAL_MODE:
				defaultScan(NetScanner.DEFAULT_PORTS);
				break;
			case DEEP_MODE:
				defaultScan(NetScanner.DEFAULT_PORTS_EXTENDED);
				break;
			case CUSTOM_MODE:
				customScan();
				break;

			}

			return null;
		}

		private void defaultScan(int[] ports) {

			int selectedPort = 0;
			int portsCount = ports.length;

			while (isRunning) {

				for (int i = 0; i < maxThreads && selectedPort < portsCount; i++) {
					try {
						TargetThread t = new TargetThread(InetAddress.getByName(ip), ports[selectedPort], this.ports);
						t.start();
						threads.add(t);
					} catch (UnknownHostException e) {
					}
					selectedPort++;
				}

				int loop = threads.size();
				for (int i = 0; i < loop; i++) {
					if (!threads.get(i).isRunning()) {
						threads.remove(i);
						loop = threads.size();
					}
				}

				publishProgress(null);

				if (threads.size() == 0)
					isRunning = false;

				try {
					Thread.sleep(1500);
				} catch (InterruptedException e) {
				}

			}

		}

		private void customScan() {

			while (isRunning) {

				Log.d("target", "starting with port" + startingPort + " and endport:" + endPort);

				int threadsCount = threads.size();
				while (threadsCount < maxThreads && startingPort <= endPort) {
					try {
						TargetThread t = new TargetThread(InetAddress.getByName(ip), startingPort++, this.ports);
						t.start();
						threads.add(t);
					} catch (UnknownHostException e) {
					}

				}

				int loop = threads.size();
				for (int i = 0; i < loop; i++) {
					if (!threads.get(i).isRunning()) {
						threads.remove(i);
						loop = threads.size();
					}
				}

				publishProgress(null);

				if (threads.size() == 0)
					isRunning = false;

				try {
					Thread.sleep(1500);
				} catch (InterruptedException e) {
				}

			}
		}

		private void displayData() {
			int loop = ports.size();
			boolean refresh = false;
			/*
			 * Collections.sort(results, new Comparator<HashMap<String, String>>() {
			 * 
			 * @Override public int compare(HashMap<String, String> hash1, HashMap<String, String> hash2) { return
			 * hash1.get(Adapter.KEY_PORT).compareTo(hash2.get(Adapter.KEY_PORT)); } });
			 */

			// sorting purpose, this may still leads to bad-sorted object, need to find a better way to sort items
			
	
			Collections.sort(ports, new Comparator<PortObject>() {
				@Override
				public int compare(PortObject p1, PortObject p2) {
					return Integer.toString(p1.getPort()).compareTo(Integer.toString(p1.getPort()));
				}
			});
			
	

			for (int i = 0; i < loop; i++) {
				if (!ports.get(i).isDisplayed()) {
					ports.get(i).setDisplayed();
					refresh = true;
					HashMap<String, String> p = new HashMap<String, String>();
					p.put(Adapter.KEY_PORT, Integer.toString(ports.get(i).getPort()));
					p.put(Adapter.KEY_SERVICE, ports.get(i).getService());
					results.add(p);
				}

			}
			

			if (refresh)
				adapter.notifyDataSetChanged();

		}

		private void stopScan() {
			isRunning = false;
		}

	}
}
