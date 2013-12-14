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
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

@SuppressLint("Registered")
public class RoachActivity extends Activity {
	boolean confirmOnBackPressed = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}//end
	

	public final RoachApplication getRoachApplication() {
		return (RoachApplication) getApplication();
	}//end
	

	protected final void setConfirmOnBackPressed(boolean confirm) {
		confirmOnBackPressed = confirm;
	}//end

	
	
	@Override
	public void onBackPressed() {
		if (!confirmOnBackPressed) {
			super.onBackPressed();
		} else {

			new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle(R.string.GenericQuitDialogTitle)
					.setMessage(R.string.GenericQuitDialogDescription).setPositiveButton(R.string.GenericQuitDialogOkButton, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							finish();
						}

					}).setNegativeButton(R.string.GenericQuitDialogCancelButton, null).show();

		}
	}//end
	
	
	

}
