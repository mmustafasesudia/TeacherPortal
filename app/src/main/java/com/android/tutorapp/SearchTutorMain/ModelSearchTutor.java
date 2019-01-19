package com.android.tutorapp.SearchTutorMain;

import org.json.JSONException;
import org.json.JSONObject;

public class ModelSearchTutor {

    String t_name;
    String t_mobile_no;
    String t_rating;
    String t_image;
    String t_distance;
    String t_known;
    String t_lat;
    String t_lng;
    String t_address;


    public ModelSearchTutor(String t_name, String t_mobile_no, String t_rating, String t_image, String t_distance) {
        this.t_name = t_name;
        this.t_mobile_no = t_mobile_no;
        this.t_rating = t_rating;
        this.t_image = t_image;
        this.t_distance = t_distance;
    }

    public ModelSearchTutor(JSONObject jsonObject) {
        try {
            this.t_name = jsonObject.getString("TutorName");
            this.t_image = jsonObject.getString("Image");
            this.t_distance = jsonObject.getString("Distance");
            this.t_rating = jsonObject.getString("Rating");
            this.t_mobile_no = jsonObject.getString("TutorMobileNo");
            this.t_known = jsonObject.getString("Known");
            this.t_lat = jsonObject.getString("Latitude");
            this.t_lng = jsonObject.getString("Longitude");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getT_lat() {
        return t_lat;
    }

    public void setT_lat(String t_lat) {
        this.t_lat = t_lat;
    }

    public String getT_lng() {
        return t_lng;
    }

    public void setT_lng(String t_lng) {
        this.t_lng = t_lng;
    }

    public String getT_address() {
        return t_address;
    }

    public String getT_known() {
        return t_known;
    }

    public void setT_known(String t_known) {
        this.t_known = t_known;
    }

    public String getT_name() {
        return t_name;
    }

    public void setT_name(String t_name) {
        this.t_name = t_name;
    }

    public String getT_mobile_no() {
        return t_mobile_no;
    }

    public void setT_mobile_no(String t_mobile_no) {
        this.t_mobile_no = t_mobile_no;
    }

    public String getT_rating() {
        return t_rating;
    }

    public void setT_rating(String t_rating) {
        this.t_rating = t_rating;
    }

    public String getT_image() {
        return t_image;
    }

    public void setT_image(String t_image) {
        this.t_image = t_image;
    }

    public String getT_distance() {
        return t_distance;
    }

    public void setT_distance(String t_distance) {
        this.t_distance = t_distance;
    }

    public void setT_address(String t_address) {
        this.t_address = t_address;
    }
}
