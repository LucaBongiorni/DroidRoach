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

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import esse.pentest.droidroach.R;

public class Adapter extends BaseAdapter {
	 
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;
    
    public static String KEY_HOST="key_host";

 
    public Adapter (Activity activity, ArrayList<HashMap<String, String>> data) {
        this.activity = activity;
        this.data=data;
        inflater = (LayoutInflater)this.activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }
 
    public int getCount() {
        return data.size();
    }
 
    public Object getItem(int position) {
        return position;
    }
 
    public long getItemId(int position) {
        return position;
    }
 
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.activity_rangeipscanner_row, null);
 
        TextView hostText= (TextView)vi.findViewById(R.id.host); // path

 
        
        HashMap<String, String> host = new HashMap<String, String>();
        host = data.get(position);
 
        // Setting all values in listview
        hostText.setText(host.get(KEY_HOST));
        


        return vi;
    }
}
