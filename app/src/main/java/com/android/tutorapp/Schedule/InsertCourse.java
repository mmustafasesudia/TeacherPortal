package com.android.tutorapp.Schedule;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.android.tutorapp.R;
import com.android.tutorapp.Utills.ConfigURL;
import com.android.tutorapp.Utills.ProgressDialogClass;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.hootsuite.nachos.NachoTextView;
import com.hootsuite.nachos.chip.Chip;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.android.tutorapp.Utills.ConfigURL.suggestions;

/**
 * A simple {@link Fragment} subclass.
 */
public class InsertCourse extends Fragment {

    ArrayList<String> stringChipList = new ArrayList<String>();
    NachoTextView nacho_text_view;
    Button btn_update_course;

    public InsertCourse() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_insert_course, container, false);
        nacho_text_view = view.findViewById(R.id.nacho_text_view);
        btn_update_course = view.findViewById(R.id.btn_update_course);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, suggestions);

        nacho_text_view = view.findViewById(R.id.nacho_text_view);
        nacho_text_view.setAdapter(adapter);

        btn_update_course.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAllChips();
            }
        });

        return view;
    }


    public void getBudget(String course) {
        ProgressDialogClass.showProgress(getActivity());
        AndroidNetworking.post(ConfigURL.URL_SCHEDULE_COURSE)
                .addBodyParameter("MobileNo", ConfigURL.getMobileNumber(getActivity()))
                .addBodyParameter("course_name", course)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Toast.makeText(getActivity(),""+response,Toast.LENGTH_SHORT).show();
                        Log.v("Response", "" + response);
                        String msg = "";
                        ProgressDialogClass.hideProgress();

                        try {
                            boolean error = response.getBoolean("error");
                            msg = response.getString("message");

                            if (!error) {
                                Toast.makeText(getActivity(), "" + msg, Toast.LENGTH_SHORT).show();
                                getActivity().onBackPressed();
                            } else {
                                Toast.makeText(getActivity(), "" + msg, Toast.LENGTH_SHORT).show();
                                getActivity().onBackPressed();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        ProgressDialogClass.hideProgress();

                    }
                });
    }

    public void getAllChips() {
        stringChipList.clear();
        for (Chip chip : nacho_text_view.getAllChips()) {
            // Do something with the text of each chip
            CharSequence text = chip.getText();
            stringChipList.add(text.toString());
            // Do something with the data of each chip (this data will be set if the chip was created by tapping a suggestion)
            Object data = chip.getData();
        }

        String idList = stringChipList.toString();
        String csv = idList.substring(1, idList.length() - 1).replace(", ", ",");
        String courseList = "" + csv + "";
        getBudget(courseList);
    }


}
