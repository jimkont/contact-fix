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

import java.util.ArrayList;
import java.util.List;

import com.jimkont.contactFix.R;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class ContactsTab extends Activity {
    Toast mToast;
    Button btn_search_contacts;
    Uri uri;
    List<String> phones = new ArrayList<String>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_contacts);

        pickContact();

        this.btn_search_contacts = (Button)this.findViewById(R.id.tab_contact_btn_search);
        this.btn_search_contacts.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                pickContact();
            }
        });    

    }//onCreate

    protected void pickContact() {
        //contact number picker
        startActivityForResult(
                new Intent(Intent.ACTION_PICK,
                        Uri.parse("content://contacts/people")),
                        R.id.contacts_pick_contact);

    }

    protected void selectContactNumber()
    {
        /*
         It is easy to find all raw contacts in a Contact:


          Cursor c = getContentResolver().query(RawContacts.CONTENT_URI,
          new String[]{RawContacts._ID, RawContacts.ACCOUNT_NAME},
          RawContacts.CONTACT_ID + "=?",
          new String[]{String.valueOf(contactId)}, null);

         * */


        /*

        Finding all Data of a given type for a given raw contact

     Cursor c = getContentResolver().query(Data.CONTENT_URI,
              new String[] {Data._ID, Phone.NUMBER, Phone.TYPE, Phone.LABEL},
              Data.RAW_CONTACT_ID + "=?" + " AND "
                      + Data.MIMETYPE + "='" + Phone.CONTENT_ITEM_TYPE + "'",
              new String[] {String.valueOf(rawContactId)}, null);

         */

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == R.id.contacts_pick_contact) {
            if (resultCode == RESULT_OK) {
                String contactID = data.getData().getLastPathSegment();
                //String contactID.getLastPathSegment();
                Toast toast = Toast.makeText(getApplicationContext(), contactID, Toast.LENGTH_LONG);
                toast.show();

            }
        }
    }

    /*
    protected void getContactInfo(Intent intent)
    {

        Cursor cursor =  managedQuery(intent.getData(), null, null, null, null);      
        while (cursor.moveToNext()) 
        {           
            String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME)); 

            String hasPhone = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

            if ( hasPhone.equalsIgnoreCase("1"))
                hasPhone = "true";
            else
                hasPhone = "false" ;

            if (Boolean.parseBoolean(hasPhone)) 
            {
                Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ contactId,null, null);
                while (phones.moveToNext()) 
                {
                    String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                }
                phones.close();
            }

            // Find Email Addresses
            Cursor emails = getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,null,ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + contactId,null, null);
            while (emails.moveToNext()) 
            {
                String emailAddress = emails.getString(emails.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
            }
            emails.close();

            Cursor address = getContentResolver().query(
                    ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.StructuredPostal.CONTACT_ID + " = " + contactId,
                    null, null);
            while (address.moveToNext()) 
            { 
                            // These are all private class variables, don't forget to create them.
            poBox      = address.getString(address.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.POBOX));
            street     = address.getString(address.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.STREET));
            city       = address.getString(address.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.CITY));
            state      = address.getString(address.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.REGION));
            postalCode = address.getString(address.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE));
            country    = address.getString(address.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY));
            type       = address.getString(address.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.TYPE));
            }  //address.moveToNext()   
        }  //while (cursor.moveToNext())        
        //cursor.close(); 
    }//getContactInfo
     */
}