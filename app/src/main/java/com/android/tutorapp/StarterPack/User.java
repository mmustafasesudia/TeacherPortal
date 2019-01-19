package com.android.tutorapp.StarterPack;

import org.json.JSONException;
import org.json.JSONObject;

public class User {

    public String uname;
    public String utype;
    public String umobile;

    public User(JSONObject jsonObject) {
        try {
            this.uname = jsonObject.getString("userName");
            this.utype = jsonObject.getString("Type");
            this.umobile = jsonObject.getString("MobileNo");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public User(String uname, String utype , String umobile) {
        this.uname = uname;
        this.utype = utype;
        this.umobile = umobile;
    }

    public String getUmobile() {
        return umobile;
    }

    public void setUmobile(String umobile) {
        this.umobile = umobile;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getUtype() {
        return utype;
    }

    public void setUtype(String utype) {
        this.utype = utype;
    }

}
