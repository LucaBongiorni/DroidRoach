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



import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import esse.droidroach.core.RoachActivity;
import esse.droidroach.utility.NetUtility;
import esse.pentest.droidroach.R;

public class Activity extends RoachActivity {
	@SuppressWarnings("unused")
	private String TAG = "SubDomainEnumerator";
	private EditText domainEdit;
	private EditText googlePagesEdit;
	private EditText bingPagesEdit;
	private CheckBox bruteForceCheck;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_subdomain);
		initialize();
		
		
		
		
	}
	
	public void initialize(){
		domainEdit = (EditText) findViewById(R.id.domainName);
		googlePagesEdit = (EditText) findViewById(R.id.googlePages);
		bingPagesEdit = (EditText) findViewById(R.id.bingPages);
	    bruteForceCheck= (CheckBox) findViewById(R.id.bruteForceCheck);
		
		Button clickButton = (Button) findViewById(R.id.startButton);
		clickButton.setOnClickListener( new OnClickListener() {

            @Override
            public void onClick(View v) {
            	
            	if( domainEdit.getText().toString().length()==0 || bingPagesEdit.getText().toString().length()==0 || googlePagesEdit.getText().toString().length()==0){
        			Toast.makeText(Activity.this, R.string.SubDomainIncorrectInput, Toast.LENGTH_LONG).show();
        			return;
        		}
        		else if (Integer.parseInt(googlePagesEdit.getText().toString()) ==0 && Integer.parseInt(bingPagesEdit.getText().toString())==0 && !bruteForceCheck.isChecked()){
        			Toast.makeText(Activity.this, R.string.SubDomainNoSearchSelected, Toast.LENGTH_LONG).show();	
        			return;
        		}else if(!NetUtility.checkDomain(domainEdit.getText().toString().trim())) {
        			Toast.makeText(Activity.this, R.string.SubDomainInvalidDomain, Toast.LENGTH_LONG).show();	
        			return;
        		}
            	
            	
    			Intent i = new Intent(Activity.this, Result.class);
    			i.putExtra("domain", domainEdit.getText().toString().trim());
    			i.putExtra("googlePages", Integer.parseInt(googlePagesEdit.getText().toString()));
    			i.putExtra("bingPages", Integer.parseInt(bingPagesEdit.getText().toString()));
    			i.putExtra("brute_force", bruteForceCheck.isChecked());
    			startActivity(i);

            }
        });

	}
	
	



	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}
	
	
	
	

}
