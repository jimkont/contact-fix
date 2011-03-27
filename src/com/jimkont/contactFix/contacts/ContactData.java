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

public class ContactData {
    public String displayName = "";
    public String lastName="";
    public String firstName="";
    public String occupation=""; 
    public String city="";
    public String address=""; 
    public String nomos="";
    public String postal="";
    public String telephone="";
    public String country="";

    public ContactData(){
        displayName = "";
        lastName="";
        firstName="";
        occupation=""; 
        city="";
        address=""; 
        nomos="";
        postal="";
        telephone="";
        country="";
    }

    public ContactData(String dn, String ln, String fn, String oc, String ct, String ad, String nm, String ps, String tl, String cntr){
        displayName = dn;
        lastName=ln;
        firstName=fn;
        occupation=oc; 
        city=ct;
        address=ad; 
        nomos=nm;
        postal=ps;
        telephone=tl;
        country=cntr;
    }

    public String toString(){
        String retStr = displayName;
        if (lastName!="") retStr+= "\n" + lastName;
        if (firstName!="") retStr+= "\n" + firstName;
        if (occupation!="") retStr+= "\n" + occupation;
        if (city!="") retStr+= "\n" + city;
        if (address!="") retStr+= "\n" + address;
        if (nomos!="") retStr+= "\n" + nomos;
        if (postal!="") retStr+= "\n" + postal;
        if (telephone!="") retStr+= "\n" + telephone;
        return 	retStr;
    }
}
