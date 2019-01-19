package com.android.tutorapp.Profile;

import org.json.JSONException;
import org.json.JSONObject;

public class TutorProfile {
    String name, qualification, gender, dob, rating, image, lat, lng;

    public TutorProfile(String name, String qualification, String gender, String dob, String rating, String image) {
        this.name = name;
        this.qualification = qualification;
        this.gender = gender;
        this.dob = dob;
        this.rating = rating;
        this.image = image;
        this.image = image;
        this.image = image;
    }

    public TutorProfile(JSONObject jsonObject) {
        try {
            this.name = jsonObject.getString("Name");
            this.rating = jsonObject.getString("Rating");
            this.image = jsonObject.getString("Image");
            this.qualification = jsonObject.getString("Qualification");
            this.gender = jsonObject.getString("Gender");
            this.dob = jsonObject.getString("Date_Of_Birth");
            this.lat = jsonObject.getString("Latitude");
            this.lng = jsonObject.getString("Longitude");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
