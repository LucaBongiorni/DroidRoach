/*
 * This file is part of the Droid Roach project
 * Copyright 2013 Stefano Gabryel
 * Author: Stefano Gabryel
 * License: GPL v3 - http://www.gnu.org/licenses/gpl-3.0.html
 * Author website: http://www.stefano-workshop.com
 * Project website: http://droid-roach.stefano-workshop.com
 * 
 */

package esse.droidroach.generic;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnKeyListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;
import esse.droidroach.core.RoachActivity;
import esse.droidroach.utility.BusyBoxAccess;
import esse.pentest.droidroach.R;

public class GenericShellOutputActivity extends RoachActivity {
	private final int DIALOG_SHELL = 666;
	private ShellCodeExecution shellCodeExecution;
	private TextView resultTextView;
	
	
	@SuppressWarnings("deprecation")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_generic_shell_output);

		setConfirmOnBackPressed(true);
		resultTextView = (TextView) findViewById(R.id.shellOutputTextView);

		String command = getIntent().getExtras().getString("command");
		shellCodeExecution = new ShellCodeExecution();
		shellCodeExecution.execute(command);
		showDialog(DIALOG_SHELL);
		
	}
	
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_SHELL: {
			ProgressDialog dialog = new ProgressDialog(this);
			dialog.setTitle(R.string.GenericShellOutputActivityDialogTitle);
			dialog.setIndeterminate(true);
			dialog.setCancelable(false);
			dialog.setOnCancelListener(new OnCancelListener() {

				@Override
				public void onCancel(DialogInterface dialog) {
					finish();
				}
			});

			dialog.setOnKeyListener(new OnKeyListener() {
				@Override
				public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
					if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
						new AlertDialog.Builder(GenericShellOutputActivity.this).setIcon(android.R.drawable.ic_dialog_alert).setTitle(R.string.GenericQuitDialogTitle).setMessage(R.string.GenericQuitDialogDescription)
								.setPositiveButton(R.string.GenericQuitDialogOkButton, new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										finish();
									}

								}).setNegativeButton(R.string.GenericQuitDialogCancelButton, null).show();
					}
					return true;
				}
			});

			return dialog;
		}

		}
		return null;
	}
	
	private class ShellCodeExecution extends AsyncTask<String,String,String>{

		
		
		@SuppressWarnings("deprecation")
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			
			if (result!=null && result.length()>0)
				resultTextView.setText(result);
			else
				resultTextView.setText(R.string.GenericShellOutputActivityFailResult);
			
			try {
				dismissDialog(DIALOG_SHELL);
			} catch (Exception e) {

			}
			

			
				
				
			
		}

		@Override
		protected String doInBackground(String... params) {
			BusyBoxAccess busyBox = new BusyBoxAccess();
			if (busyBox.isAvailable())
				return busyBox.executeCommand(params[0]);
			else
				return getString(R.string.GenericShellOutputActivityNoBusyBox);
		}
		
	}
	

	
	
	

}
