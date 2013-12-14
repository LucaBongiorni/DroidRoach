/*
 * This file is part of the Droid Roach project
 * Copyright 2013 Stefano Gabryel
 * Author: Stefano Gabryel
 * License: GPL v3 - http://www.gnu.org/licenses/gpl-3.0.html
 * Author website: http://www.stefano-workshop.com
 * Project website: http://droid-roach.stefano-workshop.com
 * 
 */

package esse.droidroach;

import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;
import esse.droidroach.core.RoachActivity;
import esse.droidroach.core.RoachApplication;
import esse.pentest.droidroach.R;

public class About extends RoachActivity {

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		TextView about = (TextView) findViewById(R.id.aboutTextView);
		about.setText(Html.fromHtml("<strong>Droid Roach " + RoachApplication.DROID_ROACH_VERSION
				+ "</strong> is a penetretion testing suite for Android developed by <a href=\"http://www.stefano-workshop.com\">Stefano \"Esse\" Gabryel</a> released under <a href=\"http://www.gnu.org/licenses/gpl-3.0.html\">GPL v3</a> license.<br/>"+
				"It comes without any warranty, implicit or explicit. It is distributed \"as it is\".<br/>" +
				"If you want more information about the project check the <a href=\"http://droid-roach.stefano-workshop.com\">official website</a>.<br/><br/>"+
				"<strong>With great power, comes great responsibility</strong><br/>"+
				"Remember, like any other penetration testing tools, Droid Roach should not be used for any illegal purpose.<br/>"+
				"I(the author) decline any responsibility for any direct and/or inderect damage you may cause to yourself or others using this tool.<br/>"+
				"Remember, if you cause damage to yourself or to others, there is only one person to blame: you.<br/><br/>" +
				"<strong>Credits:</strong><br/>" +
				"-Labiri aka Valerio Mostacci for making the Droid Roach Logo<br/>" +
				"-Saddam Abu Ghaida, Nicolai Tufar, Nico Coetzee for the IPv4 class<br/>" +
				"-Bret Taylor, Julian Robichaux for the FTPConnection class<br/>" +
				"-Laura Palumbo, my girlfriend, for helping me with some technical things and testing the application<br/><br/>" +
				"I've also used some icons from the <a href=\"http://tango.freedesktop.org\">Tango Desktop Project</a>"));
		about.setMovementMethod(LinkMovementMethod.getInstance());

	}

}
