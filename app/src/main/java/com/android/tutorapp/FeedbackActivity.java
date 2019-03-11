package com.android.tutorapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import com.android.tutorapp.Utills.ConfigURL;
import com.android.tutorapp.Utills.ProgressDialogClass;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

public class FeedbackActivity extends AppCompatActivity {

    RatingBar rating_bar;
    Button btn_submit;
    String teacher_num = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        //getSupportActionBar().setTitle("Feedback");

        Intent intent = getIntent();
        teacher_num = intent.getStringExtra("num");

        btn_submit = findViewById(R.id.btn_submit_feedback);
        rating_bar = findViewById(R.id.rating_bar);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });

    }


    public void submit() {
        if (rating_bar.getRating() == 0.0) {
            Toast.makeText(FeedbackActivity.this, "Please Provide Rating", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.v("rrrrr", "" + rating_bar.getRating());
        ProgressDialogClass.showProgress(FeedbackActivity.this);
        AndroidNetworking.post(ConfigURL.URL_FEEDBACK_STUDENT)
                .addBodyParameter("feedback", "" + rating_bar.getRating())
                .addBodyParameter("student_num", "" + ConfigURL.getMobileNumber(FeedbackActivity.this))
                .addBodyParameter("teacher_num", "" + teacher_num)
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ProgressDialogClass.hideProgress();
                        // do anything with response
                        String check = "false";
                        try {
                            check = response.getString("error");
                            if (check.equals("false")) {
                                Toast.makeText(FeedbackActivity.this, "Feedback Done...", Toast.LENGTH_SHORT).show();
                                FeedbackActivity.super.onBackPressed();
                            } else {
                                FeedbackActivity.super.onBackPressed();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        ProgressDialogClass.hideProgress();
                    }
                });
    }
}
