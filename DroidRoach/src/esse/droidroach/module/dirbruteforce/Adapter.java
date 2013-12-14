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

import esse.pentest.droidroach.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
 
public class Adapter extends BaseAdapter {
 
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;
    
    public static String KEY_PATH="key_dir1";
    public static String KEY_RESPONSECODE="key_respons1";
    public static String KEY_ICON="key_icon1";//not used
    
    public static String TYPE_DIRECTORY="type_dir1";//not used
    public static String TYPE_FILE="type_file1";//not used
 
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
            vi = inflater.inflate(R.layout.activity_dirbrute_row, null);
 
        TextView path = (TextView)vi.findViewById(R.id.DBpath); // path
        TextView responsecode = (TextView)vi.findViewById(R.id.DBresponse); // response code
 
        HashMap<String, String> actions = new HashMap<String, String>();
        actions = data.get(position);
 
        // Setting all values in listview
        path.setText(actions.get(KEY_PATH));
        
        String response=actions.get(KEY_RESPONSECODE);
        
        responsecode.setText(response);
        if (response.equals("200"))
        	responsecode.setTextColor(Color.parseColor("#1b5c00"));
        
   
        //int resource=Integer.parseInt(actions.get(KEY_ICON));
        //icon.setImageResource(resource);

        return vi;
    }
}
