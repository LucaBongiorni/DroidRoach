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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ListView;
import android.widget.Toast;
import esse.droidroach.core.RoachActivity;
import esse.droidroach.module.subdomainenumerator.thread.BingThread;
import esse.droidroach.module.subdomainenumerator.thread.BruteThread;
import esse.droidroach.module.subdomainenumerator.thread.GoogleThread;
import esse.pentest.droidroach.R;

public class Result extends RoachActivity {
	private Vector<SubObject> results;
	private String domain;
	private int googlePages = 0;
	private int bingPages = 0;
	private boolean bruteForce = false;
	private boolean humanMode = false;
	private AsyncSearch asyncSearch;
	private ArrayList<HashMap<String, String>> displayedResult;
	private ListView list;
	private Adapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// set content
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.result_subdomain);
		setProgressBarIndeterminateVisibility(true);
		setConfirmOnBackPressed(true);
		initialize();
		asyncSearch = new AsyncSearch();
		asyncSearch.execute("");

	}

	private void initialize() {
		results = new Vector<SubObject>();
		displayedResult = new ArrayList<HashMap<String, String>>();

		domain = getIntent().getExtras().getString("domain");
		googlePages = getIntent().getExtras().getInt("googlePages");
		bingPages = getIntent().getExtras().getInt("bingPages");
		bruteForce = getIntent().getExtras().getBoolean("brute_force");

		list = (ListView) findViewById(R.id.subdomainlist);
		adapter = new Adapter(this, displayedResult);
		list.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.subdomain_menu, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.stopScan:
			if (asyncSearch.isRunning) {
				asyncSearch.stopAsync();
				Toast.makeText(this, R.string.SubDomainScanStop, Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.exportResult:
			generateReport();
			break;
		case R.id.scanAgain:
			scanAgainExcludingCurrentResult();
			break;

		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onStop() {
		if (asyncSearch != null)
			asyncSearch.stopAsync();

		super.onStop();
	}

	public void generateReport() {
		String report = "";

		int loop = displayedResult.size();
		for (int i = 0; i < loop; i++) {
			String subdomain = displayedResult.get(i).get(Adapter.KEY_SUBDOMAIN);
			String ip = displayedResult.get(i).get(Adapter.KEY_IP);
			report += ip + " - " + subdomain + "\r\n";
		}

		if (loop > 0) {
			Intent i = new Intent(android.content.Intent.ACTION_SEND);
			i.setType("text/plain");
			i.putExtra(android.content.Intent.EXTRA_TEXT, report);
			startActivity(i);
		} else {
			Toast.makeText(this, R.string.SubDomainNothingToShare, Toast.LENGTH_SHORT).show();
		}
	}

	public void scanAgainExcludingCurrentResult() {

		if (!asyncSearch.isRunning) {
			asyncSearch = new AsyncSearch();
			asyncSearch.setSearchExcludingAlreadyFoundSub();
			bruteForce = false;
			asyncSearch.execute("");
			setProgressBarIndeterminateVisibility(true);
		} else {
			Toast.makeText(this, R.string.SubDomainCantStartSearch, Toast.LENGTH_LONG).show();
		}
	}

	private class AsyncSearch extends AsyncTask<String, String, String> {
		private SubObjectManager objManager;
		private boolean isRunning = true;
		private boolean searchExcludingAlreadyFoundSub = false;
		private GoogleThread googleThread = null;
		private BingThread bingThread = null;
		private BruteThread bruteThread = null;

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			showData();
			setProgressBarIndeterminateVisibility(false);

			if ((bingThread != null && bingThread.isBlocked()))
				Toast.makeText(Result.this, R.string.SubDomainBingBlocked, Toast.LENGTH_LONG).show();

			if ((googleThread != null && googleThread.isBlocked()))
				Toast.makeText(Result.this, R.string.SubDomainGoogleBlocked, Toast.LENGTH_LONG).show();

		}

		@Override
		protected void onProgressUpdate(String... values) {
			super.onProgressUpdate(values);
			showData();
		}

		@Override
		protected String doInBackground(String... params) {
			String query = domain;
			objManager = new SubObjectManager(results, domain);

			if (searchExcludingAlreadyFoundSub)
				query = buildQueryWithExclusions();

			if (googlePages > 0) {
				googleThread = new GoogleThread(humanMode, query, googlePages, objManager);
				googleThread.start();
			}

			if (bingPages > 0) {
				bingThread = new BingThread(humanMode, query, bingPages, objManager);
				bingThread.start();
			}

			if (bruteForce) {
				bruteThread = new BruteThread(objManager, domain);
				bruteThread.start();
			}

			while (isRunning) {

				if ((bruteThread == null || !bruteThread.isRunning()) && (googleThread == null || !googleThread.isRunning())
						&& (bingThread == null || !bingThread.isRunning())) {
					isRunning = false;
				}

				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {

				}

				publishProgress(null);
			}

			return null;
		}

		public void stopAsync() {
			if ((bruteThread != null && bruteThread.isRunning()))
				bruteThread.stopRunning();

			if ((bingThread != null && bingThread.isRunning()))
				bingThread.stopRunning();

			if ((googleThread != null && googleThread.isRunning()))
				googleThread.stopRunning();

		}

		private void showData() {
			boolean refresh = false;

			int loop = results.size();

			for (int i = 0; i < loop; i++) {
				SubObject obj = results.get(i);

				if (!obj.isDisplayed()) {
					obj.setDisplayed();

					HashMap<String, String> row = new HashMap<String, String>();
					row.put(Adapter.KEY_SUBDOMAIN, obj.getSubdomain());
					row.put(Adapter.KEY_SOURCE, obj.getSource());
					row.put(Adapter.KEY_IP, obj.getIp());
					displayedResult.add(row);
					refresh = true;
				}
			}

			if (refresh)
				adapter.notifyDataSetChanged();

		}

		private String buildQueryWithExclusions() {
			String search = domain;
			int loop = results.size();

			if (loop > 0) {
				for (int i = 0; i < loop; i++) {
					search += "+-site:domainfound" + results.get(i).getSubdomain();

				}
			} else {
				return domain;
			}

			return search;
		}

		public void setSearchExcludingAlreadyFoundSub() {

			this.searchExcludingAlreadyFoundSub = true;
		}

	}

}
