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

import esse.pentest.droidroach.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class Adapter  extends BaseAdapter  {
	public static final String KEY_SUBDOMAIN="subdomain_1";
	public static final String KEY_IP="ip_1";
	public static final String KEY_SOURCE="source_1";
	
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;
	
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
            vi = inflater.inflate(R.layout.result_subdomain_row, null);
 
        TextView subdomain = (TextView)vi.findViewById(R.id.subdomain);
        TextView ip = (TextView)vi.findViewById(R.id.ip); 
        TextView source = (TextView)vi.findViewById(R.id.source);
        
        HashMap<String, String> actions = new HashMap<String, String>();
        actions = data.get(position);
 

        subdomain.setText(actions.get(KEY_SUBDOMAIN));
        ip.setText(actions.get(KEY_IP));
        source.setText(actions.get(KEY_SOURCE));
        

        return vi;
    }

}
