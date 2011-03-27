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

import com.jimkont.contactFix.AsyncCallbackInterface;
import com.jimkont.contactFix.R;
import com.jimkont.contactFix.contacts.ContactData;
import com.jimkont.contactFix.contacts.ContactsResolver;
import com.jimkont.contactFix.provider.*;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class Result extends Activity implements AsyncCallbackInterface{
    private ProviderGR provider;
    private ProgressDialog dialog;

    private String extras_telephone;
    private String extras_contactID;
    private String extras_contactRawID;

    private TextView res_name;
    private TextView res_occupation;
    private TextView res_address;
    private TextView res_city;
    private TextView res_region;
    private Button btn_AddUpdate;
    private ContactData contact = null;

    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);

        // get info from caller intent
        Bundle extras = getIntent().getExtras();
        if(extras !=null) 
        {
            this.extras_telephone    = extras.getString("telephone");
            this.extras_contactID    = extras.getString("ContactID");
            this.extras_contactRawID = extras.getString("ContactRawID");
        }
        setContentView(R.layout.result);

        this.btn_AddUpdate = (Button)findViewById(R.id.result_btn_add_update);
        if (this.extras_contactID != "" && this.extras_contactRawID != "")
            this.btn_AddUpdate.setText(getString(R.string.result_update_btn));
        this.btn_AddUpdate.setEnabled(false);
        this.btn_AddUpdate.setOnClickListener(new OnClickListener() 
        {
            public void onClick(View v) 
            {
                //TODO check for updating a contact

                long contactID = ContactsResolver.createNewContact(Result.this, contact);

                Uri myPerson = ContentUris.withAppendedId
                (ContactsContract.Contacts.CONTENT_URI, contactID);
                Intent intent = new Intent(Intent.ACTION_EDIT,myPerson);
                startActivity(intent);


            }
        });


        TextView tv = (TextView) this.findViewById(R.id.result_header);
        tv.setText(tv.getText() + ": "+ extras_telephone);

        res_name = (TextView) this.findViewById(R.id.result_name);
        res_occupation = (TextView) this.findViewById(R.id.result_occupation);
        res_address = (TextView) this.findViewById(R.id.result_address);
        res_city = (TextView) this.findViewById(R.id.result_city_postal);
        res_region = (TextView) this.findViewById(R.id.result_region);

        this.provider = new ProviderGR(this);
        this.provider.execute(extras_telephone);
        this.dialog = ProgressDialog.show(Result.this, "",getString(R.string.alert_searching), true);

    }

    public void onGetResults()
    {
        contact = provider.getContactData();

        if (contact == null)
        {
            Toast toast = Toast.makeText(getApplicationContext(), R.string.alert_number_not_found, Toast.LENGTH_LONG);
            toast.show();
            this.finish();
        }
        else 
        {
            this.btn_AddUpdate.setEnabled(true);

            //name
            if (contact.displayName!="")
                res_name.setText(contact.displayName);
            else
                ((TableRow) res_name.getParent()).setVisibility(View.GONE);

            //occupation
            if (contact.occupation!="")
                res_occupation.setText(contact.occupation);
            else
                ((TableRow) res_occupation.getParent()).setVisibility(View.GONE);

            //address
            if (contact.address!="")
                res_address.setText(contact.address);
            else
                ((TableRow) res_address.getParent()).setVisibility(View.GONE);

            //city postal
            String cityPostal = contact.postal + " " + contact.city;
            if (cityPostal !="")
                res_city.setText(cityPostal);
            else
                ((TableRow) res_city.getParent()).setVisibility(View.GONE);

            //region
            if (contact.nomos!="")
                res_region.setText(contact.nomos);
            else
                ((TableRow) res_region.getParent()).setVisibility(View.GONE);
        }
        //dismiss wait dialog
        if (dialog.isShowing())
            dialog.dismiss();
    }
}
