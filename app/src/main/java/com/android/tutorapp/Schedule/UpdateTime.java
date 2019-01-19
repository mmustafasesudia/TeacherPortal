package com.android.tutorapp.Schedule;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.tutorapp.R;
import com.android.tutorapp.Utills.ConfigURL;
import com.android.tutorapp.Utills.ProgressDialogClass;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class UpdateTime extends Fragment {


    Button btn_update_budget;
    RadioButton t1, t2, t3, t4;

    public UpdateTime() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_update_time, container, false);

        btn_update_budget = view.findViewById(R.id.btn_update_budget);
        t1 = view.findViewById(R.id.t1);
        t2 = view.findViewById(R.id.t2);
        t3 = view.findViewById(R.id.t3);
        t4 = view.findViewById(R.id.t4);
        btn_update_budget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String time = "";
                if (t1.isChecked()) {
                    time = t1.getText().toString();
                } else if (t2.isChecked()) {
                    time = t2.getText().toString();
                } else if (t3.isChecked()) {
                    time = t3.getText().toString();
                } else if (t4.isChecked()) {
                    time = t4.getText().toString();
                }
                if (time == null || time.isEmpty() || time.equals("null")) {
                    Toast.makeText(getActivity(), "Please select Day..", Toast.LENGTH_SHORT).show();
                    return;
                }

                setTime(time);
            }
        });

        getTime();

        return view;
    }

    public void getTime() {
        //ProgressDialogClass.showProgress(getActivity());
        AndroidNetworking.get(ConfigURL.URL_SCHEDULE_TIME)
                .addQueryParameter("user_num",ConfigURL.getMobileNumber(getActivity()))
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Toast.makeText(getActivity(),""+response,Toast.LENGTH_SHORT).show();
                        Log.v("Response", "" + response);

                        try {
                            JSONArray jsonArray1 = response.getJSONArray("Schedule");

                            for (int j = 0; j < jsonArray1.length(); j++) {

                                JSONObject jsonObject1 = jsonArray1.getJSONObject(j);
                                //Toast.makeText(getActivity(), "" + jsonObject1.getString("schedule"), Toast.LENGTH_SHORT).show();
                                if (t1.getText().toString().equals(jsonObject1.getString("schedule"))) {
                                    t1.setVisibility(View.GONE);
                                }
                                if (t2.getText().toString().equals(jsonObject1.getString("schedule"))) {
                                    t2.setVisibility(View.GONE);
                                }
                                if (t3.getText().toString().equals(jsonObject1.getString("schedule"))) {
                                    t3.setVisibility(View.GONE);
                                }
                                if (t4.getText().toString().equals(jsonObject1.getString("schedule"))) {
                                    t4.setVisibility(View.GONE);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        // ProgressDialogClass.hideProgress();
                    }

                    @Override
                    public void onError(ANError anError) {
                        // ProgressDialogClass.hideProgress();
                    }
                });
    }


    public void setTime(String budget) {
        ProgressDialogClass.showProgress(getActivity());
        AndroidNetworking.post(ConfigURL.URL_SCHEDULE_TIME)
                .addBodyParameter("MobileNo", ConfigURL.getMobileNumber(getActivity()))
                .addBodyParameter("schedule", budget)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Toast.makeText(getActivity(),""+response,Toast.LENGTH_SHORT).show();
                        Log.v("Response", "" + response);

                        try {
                            String msg = response.getString("message");
                            boolean error = false;

                            error = response.getBoolean("error");

                            if (!error) {
                                Toast.makeText(getActivity(), "" + msg, Toast.LENGTH_SHORT).show();
                                getActivity().onBackPressed();
                            }
                            if (error) {
                                Toast.makeText(getActivity(), "" + msg, Toast.LENGTH_SHORT).show();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        ProgressDialogClass.hideProgress();

                    }

                    @Override
                    public void onError(ANError anError) {
                        ProgressDialogClass.hideProgress();

                    }
                });
    }

}
