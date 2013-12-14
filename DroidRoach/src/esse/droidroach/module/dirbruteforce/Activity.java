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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import esse.droidroach.core.RoachActivity;
import esse.droidroach.module.dirbruteforce.thread.ScanThread;
import esse.droidroach.utility.NetUtility;
import esse.droidroach.utility.UrlCheck;
import esse.pentest.droidroach.R;

public class Activity extends RoachActivity {
	private String TAG = "DirBrute";
	private int maxNumberOfThreads = 0;
	private int scanDepth = 0;
	private AsyncScan asyncScan;
	private ArrayList<HashMap<String, String>> displayedResult;
	private ListView list;
	private Adapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_dirbrute);
		setProgressBarIndeterminateVisibility(false); 
		initialize();
		showTargetDialog(null);
		setConfirmOnBackPressed(true);

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		if (asyncScan != null)
			asyncScan.stop();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.dirbrute_menu, menu);
		return true;
	}
	


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch(item.getItemId()){
			case R.id.stopScan:
				asyncScan.stop();
				Toast.makeText(this, R.string.DirBruteActivityScanStop, Toast.LENGTH_SHORT).show();
				break;
			case R.id.exportResult:
				generateReport();
				break;
		
		
		}
		

		return super.onOptionsItemSelected(item);
	}
	
	
	
	/**
	 * Call this function to generate a text report of the result and create a new intent to share the text to other apps
	 */
	private void generateReport(){
		String report="";
		
		
		int loop=displayedResult.size();
		for (int i=0; i<loop; i++){
			String url=displayedResult.get(i).get(Adapter.KEY_PATH);
			String response= displayedResult.get(i).get(Adapter.KEY_RESPONSECODE);
			report +=response +": "+url+"\r\n";
		}
		
		if (loop>0){
			Intent i = new Intent(android.content.Intent.ACTION_SEND);
			i.setType("text/plain");  
			i.putExtra(android.content.Intent.EXTRA_TEXT, report);
			startActivity(i);
		}
		else{
			Toast.makeText(this, R.string.DirBruteActivityNothingToShare, Toast.LENGTH_SHORT).show();
		}

		
	}

	
	/**
	 * Call this function to initialize variables and read preferences
	 */
	private void initialize() {

		try {
			maxNumberOfThreads = Integer.parseInt(getRoachApplication().getDRPreferences().getString("DirBrute_threads_number", "6"));
			scanDepth = Integer.parseInt(getRoachApplication().getDRPreferences().getString("DirBrute_scan_depth", "8"));
		} catch (Exception e) {
			maxNumberOfThreads = 6;
			scanDepth = 8;

		}
	
		

		list = (ListView) findViewById(R.id.dirbrutelist);
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				final HashMap<String, String> selectedItem = displayedResult.get(position);
				showBrowserDialog(selectedItem.get(Adapter.KEY_PATH));
			}
		});

	}

	
	/**
	 * Call this function to open the "do you want to open it in the browser?" dialog.
	 * @param url
	 */
	private void showBrowserDialog(final String url) {
		LinearLayout dialogLayout = new LinearLayout(this);
		dialogLayout.setOrientation(1);

		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle(getString(R.string.DirBruteActivityItemDialogTitle));
		alert.setMessage(getString(R.string.DirBruteActivityItemDialogDescription));
		alert.setView(dialogLayout);
		alert.setPositiveButton(getString(R.string.DirBruteActivityItemDialogOkButton), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {

				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(url));
				startActivity(i);

			}
		});

		alert.setNegativeButton(getString(R.string.DirBruteActivityItemDialogCancelButton), null);

		alert.show();
	}

	
	/**
	 * Call this function to start a scan
	 * @param target
	 */
	private void startScan(String target) {
		displayedResult = new ArrayList<HashMap<String, String>>();
		adapter = new Adapter(Activity.this, displayedResult);
		list.setAdapter(adapter);
		asyncScan = new AsyncScan(maxNumberOfThreads);
		asyncScan.execute(target);
		
		setProgressBarIndeterminateVisibility(true);
	}

	private void showTargetDialog(String passedUrl) {
		LinearLayout dialogLayout = new LinearLayout(this);
		dialogLayout.setOrientation(1);

		final EditText input = new EditText(this);

		input.setHint(R.string.DirBruteActivityDialogHint);
		input.setSingleLine();
		if (passedUrl != null)
			input.setText(passedUrl);

		dialogLayout.addView(input);

		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle(getString(R.string.DirBruteActivityDialogTitle));
		alert.setMessage(getString(R.string.DirBruteActivityDialogDescription));
		alert.setView(dialogLayout);
		alert.setCancelable(false);
		alert.setPositiveButton(getString(R.string.DirBruteActivityDialogOkButton), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				String url = input.getText().toString();
				if (!NetUtility.isURI(url)) {
					showWarningDialog(url);
				} else {
					startScan(url);
				}

			}
		});

		alert.setNegativeButton(getString(R.string.DirBruteActivityDialogCancelButton), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				Activity.this.finish();
			}
		});

		alert.show();
	}

	
	private void showWarningDialog(final String triedUrl) {
		LinearLayout dialogLayout = new LinearLayout(this);
		dialogLayout.setOrientation(1);

		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle(getString(R.string.DirBruteActivityDialogTitle));
		alert.setMessage(getString(R.string.DirBruteActivityDialogWarning));
		alert.setView(dialogLayout);
		alert.setCancelable(false);
		alert.setPositiveButton(getString(R.string.DirBruteActivityDialogWarningOkButton), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				showTargetDialog(triedUrl);
			}
		});

		alert.show();
	}

	private class AsyncScan extends AsyncTask<String, String, String> {
		boolean running = true;
		int threadsLimit = 0;
		int runningThreads = 0;
		UrlCheck urlCheck;
		Vector<DirBruteObject> result;
		Vector<ScanThread> threads;

		public AsyncScan(int maxNumberOfThreads) {
			threadsLimit = maxNumberOfThreads;
			threads = new Vector<ScanThread>();
			urlCheck = new UrlCheck(true);
			result = new Vector<DirBruteObject>();
		}

		/**
		 * Call this function to stop the async task
		 */
		public void stop() {
			running = false;
		}

		/**
		 * Call this function to stop all threads
		 */
		private void stopAllThreads() {
			for (ScanThread t : threads) {
				t.stopRunning();
			}
			threads.clear();

		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			showData();
			stopAllThreads();
			setProgressBarIndeterminateVisibility(false);
		}

		@Override
		protected void onProgressUpdate(String... values) {
			super.onProgressUpdate(values);
			showData();
		}

		/**
		 * Call this function to display the results
		 */
		private void showData() {
			int loop = result.size();
			boolean refresh = false;

			for (int i = 0; i < loop; i++) {
				if (!result.get(i).isDisplayed()) {
					HashMap<String, String> line = new HashMap<String, String>();
					line.put(Adapter.KEY_PATH, result.get(i).getUrl());
					line.put(Adapter.KEY_RESPONSECODE, Integer.toString(result.get(i).getResponseCode()));

					if (result.get(i).isDirectory())
						line.put(Adapter.KEY_ICON, Adapter.TYPE_DIRECTORY);
					else
						line.put(Adapter.KEY_ICON, Adapter.TYPE_FILE);

					result.get(i).setDisplayed();
					displayedResult.add(line);
					refresh = true;
				}

			}

			if (refresh)
				adapter.notifyDataSetChanged();
		}

		@Override
		protected String doInBackground(String... params) {
			String url = params[0];
			DirBruteObject targetone = new DirBruteObject(url, 200, true, scanDepth);
			ScanThread scanthread = new ScanThread(targetone, urlCheck, result);
			scanthread.start();
			threads.add(scanthread);
			runningThreads = 1;

			int upgradesAlternator = 0;// when it's = 1 publishProgress(null) gets called in side the while loop

			while (runningThreads > 0 && running) {
				try {
					Thread.sleep(1600);
				} catch (InterruptedException e) {

				}

				// loop to scan found directories
				int loop = result.size();
				for (int i = 0; i < loop && running; i++) {
					Log.d(TAG, "already bruted:" + result.get(i).alreadyBruted() + " runningThreads:" + runningThreads + " tLimit:" + threadsLimit + " depth:"
							+ result.get(i).getDepth());

					if (!result.get(i).alreadyBruted() && runningThreads < threadsLimit && result.get(i).getDepth() > 0) {
						ScanThread scanthread1 = new ScanThread(result.get(i), urlCheck, result);
						scanthread1.start();
						threads.add(scanthread1);
					}

					try {
						Thread.sleep(5);
					} catch (InterruptedException e) {

					}

				}

				// loop to remove not running threads
				loop = threads.size();
				for (int i = 0; i < loop; i++) {
					if (!threads.get(i).isRunning()) {
						threads.remove(i);
						break;
					}

					try {
						Thread.sleep(5);
					} catch (InterruptedException e) {

					}

				}

				if (upgradesAlternator == 1)
					publishProgress(null);

				runningThreads = threads.size();
				upgradesAlternator = 1 - upgradesAlternator;
			}

			return null;
		}

	}

}
