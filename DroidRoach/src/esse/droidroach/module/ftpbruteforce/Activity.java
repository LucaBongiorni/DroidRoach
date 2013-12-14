/*
 * This file is part of the Droid Roach project
 * Copyright 2013 Stefano Gabryel
 * Author: Stefano Gabryel
 * License: GPL v3 - http://www.gnu.org/licenses/gpl-3.0.html
 * Author website: http://www.stefano-workshop.com
 * Project website: http://droid-roach.stefano-workshop.com
 * 
 */

package esse.droidroach.module.ftpbruteforce;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Vector;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnKeyListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import esse.droidroach.core.RoachActivity;
import esse.droidroach.module.ftpbruteforce.thread.BruteThread;
import esse.droidroach.utility.NetUtility;
import esse.pentest.droidroach.R;

public class Activity extends RoachActivity {
	private final String lowerCaseAlphabetTag = "[a-z]";
	private final String upperCaseAlphabetTag = "[A-Z]";
	private final String numbersTag = "[0-9]";

	private TextView labelThreads;
	private Button upperButton;
	private Button lowerButton;
	private Button numbersButton;
	private Button clearButton;
	private Button startButton;

	private RadioButton radioPwdList;
	private RadioButton radioPwdGenerator;

	private SeekBar sbThreads;

	private EditText editIp;
	private EditText editPort;
	private EditText editUser;
	private EditText editPassword;
	private EditText editPasswordMin;
	private EditText editPasswordMax;

	private static final int DIALOG_BRUTE = 666;
	private ProgressDialog currentDialog;

	private BruteAsync bruteAsync;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ftpbrute);
		initialize();

	}

	private void initialize() {
		editIp = (EditText) findViewById(R.id.editIp);
		
		try {
			String ip = getIntent().getExtras().getString("ip");
			editIp.setText(ip);
		} catch (Exception e) {

		}
		
		editPort = (EditText) findViewById(R.id.editPort);
		editUser = (EditText) findViewById(R.id.editUser);
		editPassword = (EditText) findViewById(R.id.editPassword);
		editPasswordMin = (EditText) findViewById(R.id.editMinPassword);
		editPasswordMax = (EditText) findViewById(R.id.editMaxPassword);

		radioPwdList = (RadioButton) findViewById(R.id.radioButtonPwdList);
		radioPwdGenerator = (RadioButton) findViewById(R.id.radioButtonPwdGenerator);

		upperButton = (Button) findViewById(R.id.buttonUpperAlphabet);
		lowerButton = (Button) findViewById(R.id.buttonLowerAlphabet);
		numbersButton = (Button) findViewById(R.id.buttonAddNumber);
		clearButton = (Button) findViewById(R.id.buttonClear);
		startButton = (Button) findViewById(R.id.buttonStart);

		startButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (validateData())
					startBruteForce();
			}
		});

		radioPwdList.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

				if (isChecked)
					passwordUIChangeStatus(false);
				else
					passwordUIChangeStatus(true);

			}
		});

		upperButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (editPassword.getText().toString().indexOf(upperCaseAlphabetTag) == -1)
					editPassword.setText(editPassword.getText().toString() + upperCaseAlphabetTag);
				else
					editPassword.setText(editPassword.getText().toString().replace(upperCaseAlphabetTag, ""));

			}

		});

		lowerButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (editPassword.getText().toString().indexOf(lowerCaseAlphabetTag) == -1)
					editPassword.setText(editPassword.getText().toString() + lowerCaseAlphabetTag);
				else
					editPassword.setText(editPassword.getText().toString().replace(lowerCaseAlphabetTag, ""));

			}

		});

		numbersButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (editPassword.getText().toString().indexOf(numbersTag) == -1)
					editPassword.setText(editPassword.getText().toString() + numbersTag);
				else
					editPassword.setText(editPassword.getText().toString().replace(numbersTag, ""));

			}

		});

		clearButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				editPassword.setText("");

			}

		});

		labelThreads = (TextView) findViewById(R.id.labelThreads);
		sbThreads = (SeekBar) findViewById(R.id.seekBarThreads);
		labelThreads.setText(getString(R.string.FtpBruteThreadsLabel) + ": " + sbThreads.getProgress());
		sbThreads.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				labelThreads.setText(getString(R.string.FtpBruteThreadsLabel) + ": " + progress);

			}
		});

	}

	public void passwordUIChangeStatus(boolean status) {
		editPassword.setEnabled(status);
		editPasswordMin.setEnabled(status);
		editPasswordMax.setEnabled(status);
		upperButton.setEnabled(status);
		lowerButton.setEnabled(status);
		numbersButton.setEnabled(status);
		clearButton.setEnabled(status);
	}

	public boolean validateData() {

		if (!NetUtility.checkDomain(editIp.getText().toString()) && !NetUtility.checkIP(editIp.getText().toString())) {
			Toast.makeText(this, R.string.FtpBruteInvalidTarget, Toast.LENGTH_LONG).show();
			return false;
		}

		if (editPort.getText().toString() == null || editPort.getText().toString().equals("")) {

			Toast.makeText(this, R.string.FtpBruteInvalidPort, Toast.LENGTH_LONG).show();
			return false;
		}

		if (editUser.getText().toString().length() == 0) {
			Toast.makeText(this, R.string.FtpBruteNoUser, Toast.LENGTH_LONG).show();
			return false;
		}

		if (radioPwdGenerator.isChecked() && editPassword.getText().toString().length() == 0) {
			Toast.makeText(this, R.string.FtpBruteNoPassword, Toast.LENGTH_LONG).show();
			return false;
		}

		if (sbThreads.getProgress() == 0) {
			Toast.makeText(this, R.string.FtpBruteNoThreads, Toast.LENGTH_LONG).show();
			return false;
		}

		if (radioPwdGenerator.isChecked())
			if (editPasswordMin.getText().toString() == null || editPasswordMax.getText().toString() == null || editPasswordMin.getText().toString().equals("") || editPasswordMax.getText().toString().equals("")) {
				Toast.makeText(this, R.string.FtpBruteNoPasswordMinMax, Toast.LENGTH_LONG).show();
				return false;
			} else {
				int pwdMin = Integer.parseInt(editPasswordMin.getText().toString());
				int pwdMax = Integer.parseInt(editPasswordMax.getText().toString());
				if (pwdMin == 0 || pwdMax == 0 || pwdMin > pwdMax) {
					Toast.makeText(this, R.string.FtpBruteInvalidPasswordLength, Toast.LENGTH_LONG).show();
					return false;

				}

			}

		return true;
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_BRUTE: {
			ProgressDialog dialog = new ProgressDialog(this);
			dialog.setTitle(R.string.FtpBruteDialogTitle);
			dialog.setMessage("0 " + getString(R.string.FtpBruteDialogText));
			dialog.setIndeterminate(true);
			dialog.setCancelable(false);
			dialog.setOnCancelListener(new OnCancelListener() {

				@Override
				public void onCancel(DialogInterface dialog) {
					bruteAsync.stopAsyncTask();
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
										bruteAsync.stopAsyncTask();
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

	@SuppressWarnings("deprecation")
	public void startBruteForce() {
		bruteAsync = new BruteAsync();
		bruteAsync.execute("");
		showDialog(DIALOG_BRUTE);
	}

	public void displayMessage(String message) {
		
		new AlertDialog.Builder(Activity.this).setIcon(android.R.drawable.ic_dialog_alert).setTitle(R.string.FtpBruteResultDialogTitle).setMessage(message)
		.setPositiveButton(R.string.GenericQuitDialogOkButton, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				finish();
			}

		}).show();
		
	}

	public class BruteAsync extends AsyncTask<String, String, String> {
		private Vector<BruteThread> runningThreads;
		private PasswordGetter passwordGetter;
		private boolean isRunning = true;
		private String ip;
		private String user;
		private String port;
		private int maxThreads;
		private String passwordFound = null;

		private static final String RESULT_PASSWORD_FOUND = "result_ok";
		private static final String RESULT_INVALID_DOMAIN = "result_invalid_domain";
		private static final String RESULT_FINISHED = "result_finished";
		private static final String RESULT_LOCKED_OUT = "result_locked_out";

		@SuppressWarnings("deprecation")
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			try {
				dismissDialog(DIALOG_BRUTE);
			} catch (Exception e) {

			}

			if (result.equals(RESULT_INVALID_DOMAIN))
				displayMessage(getText(R.string.FtpBruteResultInvalidDomain).toString());

			if (result.equals(RESULT_PASSWORD_FOUND))
				displayMessage(getText(R.string.FtpBruteResultPasswordFound).toString()+" "+passwordFound);

			if (result.equals(RESULT_FINISHED))
				displayMessage(getText(R.string.FtpBruteResultNothingFound).toString());

			if (result.equals(RESULT_LOCKED_OUT))
				displayMessage(getText(R.string.FtpBruteResultTargetUnreachable).toString());

		}

		@Override
		protected void onProgressUpdate(String... values) {
			super.onProgressUpdate(values);
			currentDialog.setMessage(Integer.toString(passwordGetter.getRequestedWords()) + " " + getText(R.string.FtpBruteDialogText));
		}

		@Override
		protected String doInBackground(String... params) {
			runningThreads = new Vector<BruteThread>();
			ip = editIp.getText().toString();
			user = editUser.getText().toString();
			port = editPort.getText().toString();

			maxThreads = sbThreads.getProgress();

			if (NetUtility.checkDomain(ip)) {
				try {
					InetAddress inet = java.net.InetAddress.getByName(ip);
					ip = inet.getHostAddress();
				} catch (UnknownHostException e) {
					return RESULT_INVALID_DOMAIN;

				}
			}

			if (radioPwdGenerator.isChecked())
				wordGenBrute();
			else
				listBrute();

			while (isRunning) {
				int locked = 0;
				int notRunning = 0;

				for (int i = 0; i < maxThreads; i++) {
					if (runningThreads.get(i).getPasswordFound() != null) {
						passwordFound = runningThreads.get(i).getPasswordFound();
						stopAsyncTask();
						return RESULT_PASSWORD_FOUND;
					}

					if (runningThreads.get(i).isLockedOut())
						locked++;

					if (!runningThreads.get(i).isRunning())
						notRunning++;

				}

				if (locked == maxThreads)
					return RESULT_LOCKED_OUT;

				if (notRunning == maxThreads)
					isRunning = false;

				publishProgress(null);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			return RESULT_FINISHED;
		}

		public void stopAsyncTask() {
			for (int i = 0; i < maxThreads; i++) {
				runningThreads.get(i).stopRunning();
			}

		}

		private void listBrute() {

			passwordGetter = new PasswordGetter();

			for (int i = 0; i < maxThreads; i++) {
				BruteThread bt = new BruteThread(passwordGetter, ip, port, user);
				bt.start();
				runningThreads.add(bt);
			}

		}

		private void wordGenBrute() {

			String passwordAlphabet = editPassword.getText().toString();
			int passwordMinLength = Integer.parseInt(editPasswordMin.getText().toString());
			int passwordMaxLength = Integer.parseInt(editPasswordMax.getText().toString());

			passwordAlphabet.replace(lowerCaseAlphabetTag, "abcdefghijklmnopqrstuvwxyz");
			passwordAlphabet.replace(upperCaseAlphabetTag, "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
			passwordAlphabet.replace(numbersTag, "0123456789");

			passwordGetter = new PasswordGetter(passwordMinLength, passwordMaxLength, passwordAlphabet);

			for (int i = 0; i < maxThreads; i++) {
				BruteThread bt = new BruteThread(passwordGetter, ip, port, user);
				bt.start();
				runningThreads.add(bt);
			}

		}

	}

}
