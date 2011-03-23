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

package com.jimkont.contactFix.provider;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import android.os.AsyncTask;

import com.jimkont.contactFix.*;
import com.jimkont.contactFix.contacts.ContactData;



abstract class ProviderHTTPPost extends AsyncTask<String, Void, ContactData>
{
	protected static final int REGISTRATION_TIMEOUT = 30 * 1000; // ms
	protected static HttpClient mHttpClient;
	protected String telephone;
	protected String html = null;
	protected ContactData cn = null;
	public ContactData getContactData(){return cn;}
	
	protected AsyncCallbackInterface activity;
	
	abstract protected  ContactData getContents(String tel);
	
	abstract protected void getContact(String html);
	
	/* validate number according to provider */
	public static boolean validateTelephone(String number)	
	{
		return false;
	}

	public ProviderHTTPPost(AsyncCallbackInterface act) 
	{
		activity = act;
	}
		
	protected ContactData doInBackground(String... params) 
	{
        // params comes from the execute() call: params[0] is the telephone.
        return getContents(params[0]);
	}
	
	protected void onPreExecute()
	{
		super.onPreExecute();
		
	}

	protected void onPostExecute(ContactData result) 
	{
		super.onPostExecute(result);
		activity.onGetResults();
    }
	/*
	protected void onProgressUpdate(Void... values) 
	{
		super.onProgressUpdate(values);
	}
*/
	

	
	
	public static void maybeCreateHttpClient() 
	{
        if (mHttpClient == null) 
        {
            mHttpClient = new DefaultHttpClient();
            final HttpParams params = mHttpClient.getParams();
            HttpConnectionParams.setConnectionTimeout(params,
                REGISTRATION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(params, REGISTRATION_TIMEOUT);
            ConnManagerParams.setTimeout(params, REGISTRATION_TIMEOUT);
        }
    }

    /**
     * Connects to the server
     */
    public static String executePost(String url, ArrayList<NameValuePair> params) 
    {
        final ResponseHandler<String> responseHandler;

        
        HttpEntity entity = null;
        try 
        {
            entity = new UrlEncodedFormEntity(params);
        } 
        catch (final UnsupportedEncodingException e) 
        {
            // this should never happen.
            throw new AssertionError(e);
        }
        final HttpPost post = new HttpPost(url);
        post.addHeader(entity.getContentType());
        post.setEntity(entity);
        maybeCreateHttpClient();

        try 
        {
        	responseHandler = new BasicResponseHandler();
        	String responseBody = mHttpClient.execute(post, responseHandler);
        	return responseBody ;
        } catch (final IOException e) 
        {
            
            return null;
        } 
        finally 
        {
            
        }
    }
	
}
