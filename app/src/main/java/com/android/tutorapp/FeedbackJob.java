package com.android.tutorapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.tutorapp.Utills.ConfigURL;
import com.android.tutorapp.Utills.ProgressDialogClass;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class FeedbackJob extends Fragment implements View.OnClickListener {

    Spinner sp_order_edit_list;
    Button btn_submit;
    LinearLayout ll;
    RatingBar rating_bar;

    ArrayList<String> stringArrayList = new ArrayList<>();
    HashMap<String, String> stringHashMap;


    public FeedbackJob() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_feedback_job, container, false);


        sp_order_edit_list = view.findViewById(R.id.sp_view_order_list);
        btn_submit = view.findViewById(R.id.btn_submit_feedback);
        ll = view.findViewById(R.id.ll);
        ll.setVisibility(View.GONE);
        rating_bar = view.findViewById(R.id.rating_bar);
        btn_submit.setOnClickListener(this);

        loadList();
        return view;
    }


    public void loadList() {
        ProgressDialogClass.showProgress(getActivity());

        AndroidNetworking.get(ConfigURL.URL_LIST_JOBS)
                .addQueryParameter("user_num", ConfigURL.getMobileNumber(getActivity()))
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ProgressDialogClass.hideProgress();
                        stringArrayList = new ArrayList<>();
                        stringArrayList.add("Please select Order to edit");
                        stringHashMap = new HashMap<>();

                        try {
                            JSONArray jsonArray = response.getJSONArray("Jobs");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                stringArrayList.add(jsonObject.getString("job_id") + "  (" + jsonObject.getString("job_title") + ")");
                                Log.v("LLL", "" + jsonObject.getString("job_id"));
                                stringHashMap.put(jsonObject.getString("job_id") + "  (" + jsonObject.getString("job_title") + ")", jsonObject.getString("job_id"));
                            }
                            ArrayAdapter<String> adap = new ArrayAdapter<String>(getActivity(),
                                    android.R.layout.simple_spinner_item, stringArrayList);
                            adap.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                            sp_order_edit_list.setAdapter(adap);
                            sp_order_edit_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    if (position == 0) {
                                        ll.setVisibility(View.GONE);
                                        return;
                                    }
                                    ll.setVisibility(View.VISIBLE);
                                    String selectedItem = sp_order_edit_list.getSelectedItem().toString();
                                    //Toast.makeText(getActivity(), "" + stringHashMap.get(selectedItem), Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError error) {
                        ProgressDialogClass.hideProgress();

                    }
                });

    }

    public void submit() {
        if (rating_bar.getRating() == 0.0) {
            Toast.makeText(getActivity(), "Please Provide Rating", Toast.LENGTH_SHORT).show();
            return;
        }
        String selectedItem = sp_order_edit_list.getSelectedItem().toString();
        String str = "" + stringHashMap.get(selectedItem);
        String[] splited = str.split("\\s+");
        Log.v("sssss",splited[0]);
        Log.v("rrrrr",""+rating_bar.getRating());
        ProgressDialogClass.showProgress(getActivity());
        AndroidNetworking.post(ConfigURL.URL_POST_FEEDBACK)
                .addBodyParameter("job_id", splited[0])
                .addBodyParameter("feedback", ""+rating_bar.getRating())
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
                                Toast.makeText(getActivity(), "Feedback Done...", Toast.LENGTH_SHORT).show();
                            } else {

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit_feedback:
                submit();
                break;
        }
    }
}
