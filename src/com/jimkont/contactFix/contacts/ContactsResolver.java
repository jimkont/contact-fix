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

package com.jimkont.contactFix.contacts;

import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.*;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredPostal;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;

public class ContactsResolver extends Activity{

	public static long createNewContact(Activity activity, ContactData contact){

		ContentValues values = new ContentValues();
		values.put(Data.DISPLAY_NAME, contact.displayName);
		Uri rawContactUri = activity.getApplicationContext().getContentResolver().insert(RawContacts.CONTENT_URI, values);
		long rawContactID = ContentUris.parseId(rawContactUri);
		long contactID = getContactId(activity.getApplicationContext(), rawContactID);

		values.clear();
		values.put(Data.MIMETYPE, Data.CONTENT_TYPE);
		values.put(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, contact.displayName);
		values.put(Data.RAW_CONTACT_ID, rawContactID);
		activity.getApplicationContext().getContentResolver().insert(Data.CONTENT_URI, values);

		values.clear();
		values.put(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE);
		values.put(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, contact.displayName);
		values.put(Data.RAW_CONTACT_ID, rawContactID);
		activity.getApplicationContext().getContentResolver().insert(Data.CONTENT_URI, values);

		// number
		values.clear();
		values.put(Phone.NUMBER, contact.telephone);
		values.put(Phone.TYPE, Phone.TYPE_OTHER);
		values.put(Phone.MIMETYPE, Phone.CONTENT_ITEM_TYPE);
		values.put(Data.RAW_CONTACT_ID, rawContactID);
		activity.getApplicationContext().getContentResolver().insert(Data.CONTENT_URI, values);

		updateContactAddress(activity, contact, rawContactID);
		
		return contactID;
	}
	
	public static void updateContactAddress(Activity activity, ContactData contact, long rawContactID){

		// address
		ContentValues values = new ContentValues();
		values.put(StructuredPostal.STREET, contact.address);
		values.put(StructuredPostal.CITY, contact.city);
		values.put(StructuredPostal.POSTCODE, contact.postal);
		values.put(StructuredPostal.REGION, contact.nomos);
		values.put(StructuredPostal.COUNTRY, contact.country);
		values.put(StructuredPostal.TYPE, StructuredPostal.TYPE_OTHER);
		values.put(StructuredPostal.MIMETYPE, StructuredPostal.CONTENT_ITEM_TYPE);
		values.put(Data.RAW_CONTACT_ID, rawContactID);
		activity.getApplicationContext().getContentResolver().insert(Data.CONTENT_URI, values);

		//return contactID;
	}
	
	public static long getContactId(Context context, long rawContactId) {
		android.database.Cursor cur = null;
		try {
			cur = context.getContentResolver().query(ContactsContract.RawContacts.CONTENT_URI, new String[] { ContactsContract.RawContacts.CONTACT_ID }, ContactsContract.RawContacts._ID + "=" + rawContactId, null, null);
			if (cur.moveToFirst()) {
				return cur.getLong(cur.getColumnIndex(ContactsContract.RawContacts.CONTACT_ID));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cur != null) {
				cur.close();
			}
		}
		return -1l;
	}


	}
