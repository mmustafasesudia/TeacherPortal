package com.android.tutorapp.Schedule;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.android.tutorapp.R;
import com.android.tutorapp.Utills.ConfigURL;
import com.android.tutorapp.Utills.FragmentReplace;
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
public class ScheduleFragment extends Fragment {

    RadioButton b1, b2, b3, t1, t2, t3, t4, d1, d2, d3, d4, d5;
    TextView tv_no_day, tv_no_budget, tv_no_time;
    ImageView tv_budget, tv_time, tv_day;

    public ScheduleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_schedule_feagment, container, false);
        getActivity().setTitle("Schedule");

        tv_no_day = view.findViewById(R.id.tv_no_day);
        tv_no_budget = view.findViewById(R.id.tv_no_budget);
        tv_no_time = view.findViewById(R.id.tv_no_time);
        tv_budget = view.findViewById(R.id.tv_budget);
        tv_time = view.findViewById(R.id.tv_time);
        tv_day = view.findViewById(R.id.tv_day);

        b1 = view.findViewById(R.id.b1);
        b2 = view.findViewById(R.id.b2);
        b3 = view.findViewById(R.id.b3);

        t1 = view.findViewById(R.id.t1);
        t1.setVisibility(View.GONE);
        t2 = view.findViewById(R.id.t2);
        t2.setVisibility(View.GONE);
        t3 = view.findViewById(R.id.t3);
        t3.setVisibility(View.GONE);
        t4 = view.findViewById(R.id.t4);
        t4.setVisibility(View.GONE);

        d1 = view.findViewById(R.id.d2);
        d1.setVisibility(View.GONE);
        d2 = view.findViewById(R.id.d2);
        d2.setVisibility(View.GONE);
        d3 = view.findViewById(R.id.d3);
        d3.setVisibility(View.GONE);
        d4 = view.findViewById(R.id.d4);
        d4.setVisibility(View.GONE);
        d5 = view.findViewById(R.id.d5);
        d5.setVisibility(View.GONE);

        tv_budget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragmentName = null;
                Fragment UpdateBudget = new UpdateBudget();
                fragmentName = UpdateBudget;
                FragmentReplace.replaceFragment(getActivity(), fragmentName, R.id.fragment_container_drawer);

            }
        });
        tv_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragmentName = null;
                Fragment UpdateDay = new UpdateDay();
                fragmentName = UpdateDay;
                FragmentReplace.replaceFragment(getActivity(), fragmentName, R.id.fragment_container_drawer);

            }
        });
        tv_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragmentName = null;
                Fragment UpdateTime = new UpdateTime();
                fragmentName = UpdateTime;
                FragmentReplace.replaceFragment(getActivity(), fragmentName, R.id.fragment_container_drawer);

            }
        });


        getDays();
        getTime();
        getBudget();
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
                            if (jsonArray1.length() > 0) {

                            } else
                                tv_no_day.setVisibility(View.VISIBLE);

                            for (int j = 0; j < jsonArray1.length(); j++) {

                                JSONObject jsonObject1 = jsonArray1.getJSONObject(j);
                                //Toast.makeText(getActivity(), "" + jsonObject1.getString("day"), Toast.LENGTH_SHORT).show();
                                if (d1.getText().toString().equals(jsonObject1.getString("day"))) {
                                    d1.setVisibility(View.VISIBLE);
                                }
                                if (d2.getText().toString().equals(jsonObject1.getString("day"))) {
                                    d2.setVisibility(View.VISIBLE);
                                }
                                if (d3.getText().toString().equals(jsonObject1.getString("day"))) {
                                    d3.setVisibility(View.VISIBLE);
                                }
                                if (d4.getText().toString().equals(jsonObject1.getString("day"))) {
                                    d4.setVisibility(View.VISIBLE);
                                }
                                if (d5.getText().toString().equals(jsonObject1.getString("day"))) {
                                    d5.setVisibility(View.VISIBLE);
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

    public void getTime() {
        //ProgressDialogClass.showProgress(getActivity());
        AndroidNetworking.get(ConfigURL.URL_SCHEDULE_TIME)
                .addQueryParameter("user_num", ConfigURL.getMobileNumber(getActivity()))
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Toast.makeText(getActivity(),""+response,Toast.LENGTH_SHORT).show();
                        Log.v("Response", "" + response);

                        try {
                            JSONArray jsonArray1 = response.getJSONArray("Schedule");
                            if (jsonArray1.length() > 0) {

                            } else
                                tv_no_time.setVisibility(View.VISIBLE);

                            for (int j = 0; j < jsonArray1.length(); j++) {

                                JSONObject jsonObject1 = jsonArray1.getJSONObject(j);
                                //Toast.makeText(getActivity(), "" + jsonObject1.getString("schedule"), Toast.LENGTH_SHORT).show();
                                if (t1.getText().toString().equals(jsonObject1.getString("schedule"))) {
                                    t1.setVisibility(View.VISIBLE);
                                }
                                if (t2.getText().toString().equals(jsonObject1.getString("schedule"))) {
                                    t2.setVisibility(View.VISIBLE);
                                }
                                if (t3.getText().toString().equals(jsonObject1.getString("schedule"))) {
                                    t3.setVisibility(View.VISIBLE);
                                }
                                if (t4.getText().toString().equals(jsonObject1.getString("schedule"))) {
                                    t4.setVisibility(View.VISIBLE);
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

    public void getBudget() {
        //ProgressDialogClass.showProgress(getActivity());
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
                            if (jsonArray1.length() > 0) {

                            } else
                                tv_no_budget.setVisibility(View.VISIBLE);

                            for (int j = 0; j < jsonArray1.length(); j++) {

                                JSONObject jsonObject1 = jsonArray1.getJSONObject(j);
                                //Toast.makeText(getActivity(), "" + jsonObject1.getString("schedule"), Toast.LENGTH_SHORT).show();
                                if (b1.getText().toString().equals(jsonObject1.getString("budget"))) {
                                    b1.setVisibility(View.VISIBLE);
                                }
                                if (b2.getText().toString().equals(jsonObject1.getString("budget"))) {
                                    b2.setVisibility(View.VISIBLE);
                                }
                                if (b3.getText().toString().equals(jsonObject1.getString("budget"))) {
                                    b3.setVisibility(View.VISIBLE);
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

}
