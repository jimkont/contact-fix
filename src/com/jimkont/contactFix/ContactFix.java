/*
 * Copyright (C) 2011 kontokostas Dimitris (jimkont at gmail dot com) 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jimkont.contactFix;

import com.jimkont.contactFix.activities.ContactsTab;
import com.jimkont.contactFix.activities.LogsTab;
import com.jimkont.contactFix.activities.SearchTab;

import android.os.Bundle;
import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.widget.TabHost;
import android.widget.Toast;
import android.provider.Settings;


public class ContactFix extends TabActivity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Resources res = getResources(); // Resource object to get Drawables
        TabHost tabHost = getTabHost();  // The activity TabHost
        TabHost.TabSpec spec;  // Resusable TabSpec for each tab
        Intent intent;  // Reusable Intent for each tab

        // Initialize a TabSpec for each tab and add it to the TabHost
        intent = new Intent().setClass(this, LogsTab.class);
        spec = tabHost.newTabSpec(
                getString(R.string.tab_label_logs)).setIndicator(getString(R.string.tab_label_logs),
                        res.getDrawable(R.drawable.ic_tab_log)).setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent().setClass(this, ContactsTab.class);
		spec = tabHost.newTabSpec(
				getString(R.string.tab_label_contacts)).setIndicator(getString(R.string.tab_label_contacts),
				res.getDrawable(R.drawable.ic_tab_contact)).setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, SearchTab.class);
        spec = tabHost.newTabSpec(
                getString(R.string.tab_label_search)).setIndicator(getString(R.string.tab_label_search),
                        res.getDrawable(R.drawable.ic_tab_search)).setContent(intent);
        tabHost.addTab(spec);

        if (! Helper.hasInternetAccess(this) )
        {
            //open network settings
            new AlertDialog.Builder(this)
            .setTitle(R.string.alert_no_network)
            .setPositiveButton(R.string.btn_net_settings, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    try 
                    {
                        Intent i = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        ContactFix.this.getApplicationContext().startActivity(i);
                    }
                    catch (Exception e)
                    {
                        //TODO message as id
                        Toast toast = Toast.makeText(getApplicationContext(), "Cannot open network settings", Toast.LENGTH_LONG);
                        toast.show();
                    }
                }
            })
            .setNegativeButton(R.string.btn_net_exit, new DialogInterface.OnClickListener() 
            {
                public void onClick(DialogInterface dialog, int whichButton) 
                {
                    ContactFix.this.finish();
                
                }
            })
            .create()
            .show()
            ;
        }
        if (! Helper.hasInternetAccess(this) )
            this.finish();
    }
}
