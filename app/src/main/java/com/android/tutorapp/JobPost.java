package com.android.tutorapp;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.android.tutorapp.Utills.ConfigURL;
import com.android.tutorapp.Utills.ProgressDialogClass;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class JobPost extends Fragment {

    EditText et_job_title, et_jd, et_last_date;
    Button btn_post_job;
    final Calendar myCalendar = Calendar.getInstance();
    SimpleDateFormat sdf;

    public JobPost() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_job_post, container, false);

        et_job_title = view.findViewById(R.id.et_job_title);
        et_jd = view.findViewById(R.id.et_jd);
        et_last_date = view.findViewById(R.id.et_last_date);
        btn_post_job = view.findViewById(R.id.btn_post_job);
        btn_post_job.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_job_title.getText().toString().isEmpty()) {
                    et_job_title.setError("Field Cannot Be Empty");
                    return;
                }
                if (et_jd.getText().toString().isEmpty()) {
                    et_jd.setError("Field Cannot Be Empty");
                    return;
                }
                if (et_last_date.getText().toString().isEmpty()) {
                    et_last_date.setError("Field Cannot Be Empty");
                    return;
                }
                sendData();
            }
        });

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };
        et_last_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        return view;
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        sdf = new SimpleDateFormat(myFormat, Locale.US);

        et_last_date.setText(sdf.format(myCalendar.getTime()));
    }

    public void sendData() {
        ProgressDialogClass.showProgress(getActivity());

        AndroidNetworking.post(ConfigURL.URL_POST_JOB)
                .addBodyParameter("job_title", et_job_title.getText().toString())
                .addBodyParameter("job_description", et_jd.getText().toString())
                .addBodyParameter("org_number", ConfigURL.getMobileNumber(getActivity()))
                .addBodyParameter("last_date_for_apply", "" + sdf.format(myCalendar.getTime()))
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ProgressDialogClass.hideProgress();
                        try {
                            String msg = response.getString("message");
                            boolean error = false;

                            error = response.getBoolean("error");

                            if (!error) {
                                Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
                                getActivity().onBackPressed();
                            }
                            if (error) {
                                Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        ProgressDialogClass.hideProgress();
                        Log.v("Sign In", "" + error);
                    }
                });
    }

}
