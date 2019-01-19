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
public class UpdateDay extends Fragment {


    Button btn_update_budget;
    RadioButton d1, d2, d3, d4, d5;

    public UpdateDay() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_update_day, container, false);

        btn_update_budget = view.findViewById(R.id.btn_update_budget);
        d1 = view.findViewById(R.id.d2);
        d2 = view.findViewById(R.id.d2);
        d3 = view.findViewById(R.id.d3);
        d4 = view.findViewById(R.id.d4);
        d5 = view.findViewById(R.id.d5);

        btn_update_budget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String day = "";
                if (d1.isChecked()) {
                    day = d1.getText().toString();
                } else if (d2.isChecked()) {
                    day = d2.getText().toString();
                } else if (d3.isChecked()) {
                    day = d3.getText().toString();
                } else if (d4.isChecked()) {
                    day = d4.getText().toString();
                } else if (d5.isChecked()) {
                    day = d5.getText().toString();
                }
                if (day == null || day.isEmpty() || day.equals("null")) {
                    Toast.makeText(getActivity(), "Please select Day..", Toast.LENGTH_SHORT).show();
                    return;
                }

                setDay(day);
            }
        });

        getDays();

        return view;
    }

    public void getDays() {
        ProgressDialogClass.showProgress(getActivity());
        AndroidNetworking.get(ConfigURL.URL_SCHEDULE_DAY)
                .addQueryParameter("user_num", ConfigURL.getMobileNumber(getActivity()))
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Toast.makeText(getActivity(),""+response,Toast.LENGTH_SHORT).show();
                        Log.v("Response", "" + response);

                        try {
                            JSONArray jsonArray1 = response.getJSONArray("Days");

                            for (int j = 0; j < jsonArray1.length(); j++) {

                                JSONObject jsonObject1 = jsonArray1.getJSONObject(j);
                                //Toast.makeText(getActivity(), "" + jsonObject1.getString("day"), Toast.LENGTH_SHORT).show();
                                if (d1.getText().toString().equals(jsonObject1.getString("day"))) {
                                    d1.setVisibility(View.GONE);
                                }
                                if (d2.getText().toString().equals(jsonObject1.getString("day"))) {
                                    d2.setVisibility(View.GONE);
                                }
                                if (d3.getText().toString().equals(jsonObject1.getString("day"))) {
                                    d3.setVisibility(View.GONE);
                                }
                                if (d4.getText().toString().equals(jsonObject1.getString("day"))) {
                                    d4.setVisibility(View.GONE);
                                }
                                if (d5.getText().toString().equals(jsonObject1.getString("day"))) {
                                    d5.setVisibility(View.GONE);
                                }

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

    public void setDay(String budget) {
        ProgressDialogClass.showProgress(getActivity());
        AndroidNetworking.post(ConfigURL.URL_SCHEDULE_DAY)
                .addBodyParameter("MobileNo", ConfigURL.getMobileNumber(getActivity()))
                .addBodyParameter("day", budget)
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
