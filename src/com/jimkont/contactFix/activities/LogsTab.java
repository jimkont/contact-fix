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

package com.jimkont.contactFix.activities;

import com.jimkont.contactFix.R;
import com.jimkont.contactFix.provider.ProviderGR;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;


public class LogsTab extends ListActivity {

	Cursor cursor = null;
	String strUriCalls="content://call_log/calls";
	Uri UriCalls = Uri.parse(strUriCalls);
	int logsColumnsNumber;
	int logsColumnsName;
	int logsColumnsType;
	int logsColumnsDate;
	List<String> numbersLst = new ArrayList<String>();
	Set <String> numbersMap = new HashSet<String>();
	ArrayList<HashMap<String, String>> logList = new ArrayList<HashMap<String, String>>();

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab_logs);
		
		getLogNumbers();
		Collections.reverse(logList);
		
		setListAdapter(new MySimpleAdapter(this, logList, R.layout.tab_logs_view_item, 
	            new String[] {"number", "date", "type"}, 
	            new int[] {R.id.tab_logs_viewitem_number, R.id.tab_logs_viewitem_date, R.id.tab_logs_viewitem_type}));
		
		ListView lv = this.getListView();
		lv.setTextFilterEnabled(true);

		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
			{
				String telephone = (String) view.getTag(R.id.tag_logtabitem);

				Intent newIntent = new Intent(LogsTab.this.getApplicationContext(), Result.class);
            	newIntent.putExtra("telephone", telephone);
            	startActivity(newIntent);
				//Toast mToast = Toast.makeText(this, number, Toast.LENGTH_LONG);
                //mToast.show();
			}
		});
	}
	
	private void getLogNumbers()
	{
		if (cursor == null) 
		{

			cursor = LogsTab.this.getContentResolver().query(UriCalls, null, null, null, null);
			logsColumnsNumber = cursor.getColumnIndex(android.provider.CallLog.Calls.NUMBER);
			logsColumnsName = cursor.getColumnIndex(android.provider.CallLog.Calls.CACHED_NAME);
			logsColumnsType = cursor.getColumnIndex(android.provider.CallLog.Calls.TYPE);
			logsColumnsDate = cursor.getColumnIndex(android.provider.CallLog.Calls.DATE);
		}
		if (cursor.getCount()<=0) 
		{ 
			Toast.makeText(getApplicationContext(), "Call log empty",Toast.LENGTH_SHORT).show(); 
		} 
		while (cursor.moveToNext()) 
		{ 
			String curNum = cursor.getString(logsColumnsNumber);
			String curName = cursor.getString(logsColumnsName);
			if ( (curName == null || curName.length() == 0 ) &&
					ProviderGR.validateTelephone(curNum) && 
					!numbersMap.contains(curNum))
			{
				//add to map for distinct
				numbersMap.add(curNum);

				//get date
				Date date = new Date(Long.parseLong(cursor.getString(logsColumnsDate)));
			    Calendar cl = Calendar.getInstance();
			    cl.setTime(date);
			    String dt = android.text.format.DateUtils.formatDateTime(
			    		getApplicationContext(), 
			    		cl.getTimeInMillis(), 
			    		android.text.format.DateUtils.FORMAT_ABBREV_MONTH) + "\n" +
			    		android.text.format.DateUtils.formatDateTime(
					    		getApplicationContext(), 
					    		cl.getTimeInMillis(), 
					    		android.text.format.DateUtils.FORMAT_SHOW_TIME);
				//numbersLst.add(cursor.getInt(logsColumnsType));
				//cl.toString());
			    HashMap<String, String> map = new HashMap<String, String>();
				map.put("number", curNum); 
				map.put("date"  , dt);
				map.put("type"  , cursor.getString(logsColumnsType));
				logList.add(map);
			}
			
		} 
	} 

	private class MySimpleAdapter extends SimpleAdapter {

		Context context;
        public MySimpleAdapter(Context context,
				List<? extends Map<String, ?>> data, int resource,
				String[] from, int[] to) {

        	super(context, data, resource, from, to);
			this.context = context;
		}
        
        public String getNumberFromPosition(int position)
        {
        	if (position > getCount())
        		return "";
        	return ((HashMap<String, String>) this.getItem(position) ).get("number");
        }

		public View getView(int pos, View inView, ViewGroup parent) {
		       View v = inView;
		       if (v == null) {
		            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		            v = inflater.inflate(R.layout.tab_logs_view_item, null);
		       }
		       
		       HashMap<String, String> obj = (HashMap<String, String>) this.getItem(pos);
		       
		       TextView lNum  = (TextView) v.findViewById(R.id.tab_logs_viewitem_number);
		       TextView lDate = (TextView) v.findViewById(R.id.tab_logs_viewitem_date);
		       ImageView iv =  (ImageView) v.findViewById(R.id.tab_logs_viewitem_type);
		       
		       String number = obj.get("number") ;
		       lNum.setText(number);
		       lDate.setText(obj.get("date"));
		       
		       int lResID = R.drawable.ic_log_view_calltype_missed;
		       switch ( Integer.parseInt(obj.get("type")) ) {
		       case 0 :
		    	   lResID = R.drawable.ic_log_view_calltype_missed;
		    	   break;
		       case 1:
		    	   lResID = R.drawable.ic_log_view_calltype_incoming;
		    	   break;
		       case 2:
		    	   lResID = R.drawable.ic_log_view_calltype_outgoing;
		    	   break;
		       }
		       iv.setImageResource(lResID);
		       v.setTag(R.id.tag_logtabitem, number );
		       
	               
		       return(v);
		}

	}
	
}
