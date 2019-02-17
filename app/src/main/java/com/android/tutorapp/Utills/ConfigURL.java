package com.android.tutorapp.Utills;

import android.content.Context;
import android.content.SharedPreferences;

public class ConfigURL {

    public static final String PUSH_NOTIFICATION = "unique_name";

    public static String HOST = "itehadmotors.com";

    public static final String URL_IS_STUDENT_REGISTERED = "http://" + HOST + "/Teacher_Portal/v1/userRegister";
    public static final String URL_LOGIN_STUDENT = "http://" + HOST + "/Teacher_Portal/v1/login/user";
    public static final String URL_GET_PERSON_CHAT = "http://" + HOST + "/Teacher_Portal/v1/listofchat";
    public static final String URL_LIST_OF_SINGLE_CHAT = "http://" + HOST + "/Teacher_Portal/v1/listofmessageofsinglechat";
    public static final String URL_SEND_MESSAGE = "http://" + HOST + "/Teacher_Portal/v1/sendmessage";
    public static final String URL_SEND_REQUEST = "http://" + HOST + "/Teacher_Portal/v1/request";
    public static final String URL_GET_ALL_TUTORS = "http://" + HOST + "/Teacher_Portal/v1/tutors";
    public static final String URL_GET_ALL_TUTORS_FILTER = "http://" + HOST + "/Teacher_Portal/v1/tutorswrtfilter";
    public static final String URL_GET_TUTOR_PROFILE = "http://" + HOST + "/Teacher_Portal/v1/tutorprofile";
    public static final String URL_PROFILE_CHANGE_PASSWORD = "http://" + HOST + "/Teacher_Portal/v1/updatepass";
    public static final String URL_SCHEDULE_DAY = "http://" + HOST + "/Teacher_Portal/v1/day";
    public static final String URL_SCHEDULE_TIME = "http://" + HOST + "/Teacher_Portal/v1/schedule";
    public static final String URL_SCHEDULE_COURSE = "http://" + HOST + "/Teacher_Portal/v1/course";
    public static final String URL_SCHEDULE_BUDGET = "http://" + HOST + "/Teacher_Portal/v1/budget";
    public static final String URL_STUDENT_ORG_PROFILE_VIEW = "http://" + HOST + "/Teacher_Portal/v1/studentorgprofile/";
    public static final String URL_STUDENT_REQUEST_ACCEPT = "http://" + HOST + "/Teacher_Portal/v1/requestaccept";
    public static final String URL_FORGOT_PASS = "http://" + HOST + "/Teacher_Portal/v1/forgotpass";
    public static final String URL_IS_PERSON_EXIST = "http://" + HOST + "/Teacher_Portal/v1/isPersonExistNumberCheck";
    public static final String URL_POST_JOB = "http://" + HOST + "/Teacher_Portal/v1/job";
    public static final String URL_GET_SEARCH_JOB = "http://" + HOST + "/Teacher_Portal/v1/searchjob";
    public static final String URL_REQUEST = "http://" + HOST + "/Teacher_Portal/v1/requestjob";
    public static final String URL_POST_FEEDBACK = "http://" + HOST + "/Teacher_Portal/v1/feedback";
    public static final String URL_LIST_JOBS = "http://" + HOST + "/Teacher_Portal/v1/activejob";
    public static final String URL_REQUEST_ACCEPT = "http://" + HOST + "/Teacher_Portal/v1/jobrequestaccept";


    public static String Filtercourse = "";
    public static String Filtertime = "";
    public static String Filterday = "";
    public static String Filterbudget = "";
    public static String[] suggestions = new String[]{"Chemistry", "Biology", "Math", "Physics", "English", "Urdu", "Science", "Computer", "Zoology"};


    public static String getLoginState(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("PREFRENCE", Context.MODE_PRIVATE);
        if (prefs.getString("LOGIN", "").length() > 0) {
            return prefs.getString("LOGIN", "");
        } else
            return "";
    }


    //Shared Prefernce
    public static String getEmail(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("PREFRENCE", Context.MODE_PRIVATE);
        if (prefs.getString("EMAIL", "").length() > 0) {
            return prefs.getString("EMAIL", "");
        } else
            return "";
    }

    public static String getMobileNumber(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("PREFRENCE", Context.MODE_PRIVATE);
        if (prefs.getString("PHONE", "").length() > 0) {
            return prefs.getString("PHONE", "");
        } else
            return "";
    }

    public static String getName(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("PREFRENCE", Context.MODE_PRIVATE);
        if (prefs.getString("NAME", "").length() > 0) {
            return prefs.getString("NAME", "");
        } else
            return "";
    }

    public static String getType(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("PREFRENCE", Context.MODE_PRIVATE);
        if (prefs.getString("TYPE", "").length() > 0) {
            return prefs.getString("TYPE", "");
        } else
            return "";
    }

    public static String getGender(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("PREFRENCE", Context.MODE_PRIVATE);
        if (prefs.getString("GENDER", "").length() > 0) {
            return prefs.getString("GENDER", "");
        } else
            return "";
    }

    public static String getDOB(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("PREFRENCE", Context.MODE_PRIVATE);
        if (prefs.getString("DOB", "").length() > 0) {
            return prefs.getString("DOB", "");
        } else
            return "";
    }

    public static String getImage(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("PREFRENCE", Context.MODE_PRIVATE);
        if (prefs.getString("IMAGE", "").length() > 0) {
            return prefs.getString("IMAGE", "");
        } else
            return "";
    }

    public static String getD(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("PREFRENCE", Context.MODE_PRIVATE);
        if (prefs.getString("D", "").length() > 0) {
            return prefs.getString("D", "");
        } else
            return "";
    }

    public static String getM(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("PREFRENCE", Context.MODE_PRIVATE);
        if (prefs.getString("M", "").length() > 0) {
            return prefs.getString("M", "");
        } else
            return "";
    }

    public static String getY(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("PREFRENCE", Context.MODE_PRIVATE);
        if (prefs.getString("Y", "").length() > 0) {
            return prefs.getString("Y", "");
        } else
            return "";
    }

    public static String getQualification(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("PREFRENCE", Context.MODE_PRIVATE);
        if (prefs.getString("QUALIFICATION", "").length() > 0) {
            return prefs.getString("QUALIFICATION", "");
        } else
            return "";
    }

    //clear shared
    public static void clearshareprefrence(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("PREFRENCE", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }

}
