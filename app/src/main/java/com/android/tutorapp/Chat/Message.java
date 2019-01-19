package com.android.tutorapp.Chat;

import org.json.JSONException;
import org.json.JSONObject;

public class Message {

    public String dateTime;
    public String message;
    public String name;
    public String mobile_no;
    public String message_id;
    public String message_key;

    public Message(JSONObject jsonObject) {
        try {
            this.dateTime = jsonObject.getString("Time");
            this.message = jsonObject.getString("Message");
            this.name = jsonObject.getString("Name");
            this.mobile_no = jsonObject.getString("MobileNo");
            this.message_id = jsonObject.getString("MessageId");
            this.message_key = jsonObject.getString("MessageKey");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Message(String dateTime, String message, String name, String mobile_no, String message_id, String message_key) {
        this.dateTime = dateTime;
        this.message = message;
        this.name = name;
        this.mobile_no = mobile_no;
        this.message_id = message_id;
        this.message_key = message_key;
    }

    public Message(String dateTime, String message, String name) {

        this.dateTime = dateTime;
        this.message = message;
    }

    public Message(String name, String message) {

        this.name = name;
        this.message = message;
    }


    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }

    public String getMessage_key() {
        return message_key;
    }

    public void setMessage_key(String message_key) {
        this.message_key = message_key;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
