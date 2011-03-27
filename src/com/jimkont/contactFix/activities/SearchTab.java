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
import com.jimkont.contactFix.provider.*;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class SearchTab extends Activity {

    private Button   btn_search_manual;
    private EditText txt_search_manual;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_search);

        this.txt_search_manual = (EditText) this.findViewById(R.id.entry);
        //this.txt_search_manual.setText("2331020043");

        //manual search
        this.btn_search_manual = (Button)this.findViewById(R.id.tab_search_btn);
        this.btn_search_manual.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                String tmp = txt_search_manual.getText().toString();
                if (!ProviderGR.validateTelephone(tmp)) {

                    AlertDialog.Builder alertbox = new AlertDialog.Builder(SearchTab.this);
                    if (tmp.length()==10 && tmp.charAt(0)!= '2')
                        alertbox.setMessage(getString(R.string.alert_not_landline));
                    else
                        alertbox.setMessage(getString(R.string.alert_incorrect_number));  
                    alertbox.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {}
                    });
                    alertbox.show();
                    return;
                }

                Intent newIntent = new Intent(SearchTab.this.getApplicationContext(), Result.class);
                newIntent.putExtra("telephone", tmp);
                startActivity(newIntent);
            }
        });    
    }
}
