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
public class UpdateBudget extends Fragment {


    Button btn_update_budget;
    RadioButton b1, b2, b3;

    public UpdateBudget() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_update_budget, container, false);

        btn_update_budget = view.findViewById(R.id.btn_update_budget);
        b1 = view.findViewById(R.id.b1);
        b2 = view.findViewById(R.id.b2);
        b3 = view.findViewById(R.id.b3);
        btn_update_budget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String budget = "";
                if (b1.isChecked()) {
                    budget = b1.getText().toString();
                } else if (b2.isChecked()) {
                    budget = b2.getText().toString();
                } else if (b3.isChecked()) {
                    budget = b3.getText().toString();
                }
                if (budget == null || budget.isEmpty() || budget.equals("null")) {
                    Toast.makeText(getActivity(), "Please select budget..", Toast.LENGTH_SHORT).show();
                    return;
                }

                setBudget(budget);
            }
        });

        getBudget();

        return view;
    }

    public void getBudget() {
        ProgressDialogClass.showProgress(getActivity());
        AndroidNetworking.get(ConfigURL.URL_SCHEDULE_BUDGET)
                .addQueryParameter("user_num", ConfigURL.getMobileNumber(getActivity()))
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Toast.makeText(getActivity(),""+response,Toast.LENGTH_SHORT).show();
                        Log.v("Response", "" + response);

                        try {
                            JSONArray jsonArray1 = response.getJSONArray("Budget");

                            for (int j = 0; j < jsonArray1.length(); j++) {

                                JSONObject jsonObject1 = jsonArray1.getJSONObject(j);
                                //Toast.makeText(getActivity(), "" + jsonObject1.getString("schedule"), Toast.LENGTH_SHORT).show();
                                if (b1.getText().toString().equals(jsonObject1.getString("budget"))) {
                                    b1.setChecked(true);
                                }
                                if (b2.getText().toString().equals(jsonObject1.getString("budget"))) {
                                    b2.setChecked(true);
                                }
                                if (b3.getText().toString().equals(jsonObject1.getString("budget"))) {
                                    b3.setChecked(true);
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


    public void setBudget(String budget) {
        ProgressDialogClass.showProgress(getActivity());
        AndroidNetworking.post(ConfigURL.URL_SCHEDULE_BUDGET)
                .addBodyParameter("mobileNo", ConfigURL.getMobileNumber(getActivity()))
                .addBodyParameter("budget", budget)
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
