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

import com.jimkont.contactFix.*;
import com.jimkont.contactFix.contacts.ContactData;

import java.util.ArrayList;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class ProviderGR extends ProviderHTTPPost
{

	public ProviderGR(AsyncCallbackInterface act) 
	{
		super(act);
	}
		

	public static boolean validateTelephone(String number)	
	{
		// number must start with '+302' or '2'
		if (number == null)
			return false;

		if (number.length()== 10 && number.substring(0,1).equalsIgnoreCase("2"))
			return true;
		if (number.length()== 13 && number.substring(0,4).equalsIgnoreCase("+302"))
			return true;
		return false;
	}
	
	protected ContactData getContents(String tel)
	{
			if (this.telephone == tel)
				return cn;
			this.telephone = tel;
			if (!validateTelephone(this.telephone))
				return null;
			if (this.telephone.length()==13)
				this.telephone = this.telephone.substring(3, 13);
	        // Add your data
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("x_nomosid_search", ""));
			params.add(new BasicNameValuePair("x_poli2", ""));
			params.add(new BasicNameValuePair("x_surname", ""));
			params.add(new BasicNameValuePair("x_fname", ""));
			params.add(new BasicNameValuePair("x_pname", ""));
			params.add(new BasicNameValuePair("x_tel", telephone));
			params.add(new BasicNameValuePair("x_bigcity", "ΑΘΗΝΑ"));
			params.add(new BasicNameValuePair("x_daslocality", ""));
			params.add(new BasicNameValuePair("x_nomosid", ""));
			params.add(new BasicNameValuePair("x_poli", ""));
			params.add(new BasicNameValuePair("x_area", ""));
			params.add(new BasicNameValuePair("x_das", ""));
			params.add(new BasicNameValuePair("x_area_latin", ""));
			params.add(new BasicNameValuePair("Image81.x", "0"));
			params.add(new BasicNameValuePair("Image81.y", "0"));
	        
	        //html = executePost("http://www.whitepages.gr/gr/results.aspx", params );
	        //getContact(html);
			cn = new ContactData();
			cn.displayName = "Kontokostas Dimitris";
			cn.lastName="Kontokostas";
			cn.firstName="Dimitris";
			//cn.occupation="Teacher"; 
			cn.city="Veria";
			cn.address="Trempesinas 18"; 
			cn.nomos="Imathia";
			cn.postal="59100";
			cn.telephone="2331025316";
	        return cn;
			
	}
	
	protected void getContact(String html)
	{
		if (html == null)
			return ;
		int start = html.indexOf("span id=\"reslist_ctl01_surname\"");
		if (start == -1)
			return ;
		html = html.substring(start-1, html.indexOf("</table>",start)); //trim html string to results table
		cn = new ContactData();

		//name / last name
		start = html.indexOf(">")+1;
		int end = html.indexOf("<", start);
		cn.displayName = html.substring(start, end).trim();
		cn.displayName = cn.displayName.replaceAll("&nbsp;", " ");
		int sep = cn.displayName.indexOf(" ");
		cn.lastName = cn.displayName.substring(0,sep).trim();
		cn.firstName = cn.displayName.substring(sep+1, cn.displayName.length()).trim();
		
		//occupation
		start = html.indexOf("<br>", start);
		start = html.indexOf(">",start)+1;
		end = html.indexOf("<", start);
		if (end>start)
			cn.occupation = html.substring(start, end).trim();
		else 
		{
			start = html.indexOf("reslist_ctl01_occupation", end);
			start = html.indexOf(">",start)+1;
			end = html.indexOf("<", start);
			if (end>start)
				cn.occupation = html.substring(start, end).trim();
		}
		
		int nameEnd = end; //start index for searching
		
		//city
		start = html.indexOf("<div align=\"left\">", nameEnd);
		start = html.indexOf(">",start)+1;
		end = html.indexOf("<", start);
		if (end>start)
			cn.city = html.substring(start, end).trim();
		//reslist_ctl01_straddr
		start = html.indexOf("reslist_ctl01_straddr", nameEnd);
		start = html.indexOf(">",start)+1;
		end = html.indexOf("<", start);
		if (end>start)
			cn.address = html.substring(start, end).trim();
		//reslist_ctl01_nomos
		start = html.indexOf("reslist_ctl01_nomos", nameEnd);
		start = html.indexOf(">",start)+1;
		end = html.indexOf("<", start);
		if (end>start)
			cn.nomos = html.substring(start, end).trim();
		//reslist_ctl01_postal
		start = html.indexOf("reslist_ctl01_postal", nameEnd);
		start = html.indexOf(">",start)+1;
		end = html.indexOf("<", start);
		if (end>start)
			cn.postal = html.substring(start, end).trim();
		
		//reslist_ctl01_telnumber
		start = html.indexOf("reslist_ctl01_telnumber", nameEnd);
		start = html.indexOf(">",start)+1;
		start = html.indexOf(">",start)+1;
		end = html.indexOf("<", start);
		if (end>start)
			cn.telephone = html.substring(start, end).trim();

	}
}
