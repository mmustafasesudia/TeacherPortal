package com.android.tutorapp.Profile;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.tutorapp.R;
import com.android.tutorapp.Schedule.InsertCourse;
import com.android.tutorapp.Schedule.UpdateBudget;
import com.android.tutorapp.Schedule.UpdateDay;
import com.android.tutorapp.Schedule.UpdateTime;
import com.android.tutorapp.Utills.ConfigURL;
import com.android.tutorapp.Utills.FragmentReplace;
import com.android.tutorapp.Utills.ProgressDialogClass;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.hootsuite.nachos.NachoTextView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class TutorProfileView extends Fragment {

    TextView tv_name, tv_rating, tv_qualification, tv_gender, tv_dob, tv_change_pass, tv_address;
    NachoTextView nacho_text_view_course;
    RatingBar tv_rating_tutor;
    ImageView profile_image;
    ArrayList<String> skillArrayList = new ArrayList<>();
    ImageView img_edit_profile, tv_add_course;
    String[] dob = new String[]{};
    Geocoder geocoder;
    List<Address> addresses;

    RadioButton b1, b2, b3, t1, t2, t3, t4, d1, d2, d3, d4, d5;
    TextView tv_no_day, tv_no_budget, tv_no_time;
    ImageView tv_budget, tv_time, tv_day;
    String phone;

    public TutorProfileView() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tutor_profile_view, container, false);

        getActivity().setTitle("Profile");

        tv_name = view.findViewById(R.id.tv_name);
        tv_qualification = view.findViewById(R.id.tv_qualification);
        tv_change_pass = view.findViewById(R.id.tv_change_pass);
        tv_gender = view.findViewById(R.id.tv_gender);
        tv_address = view.findViewById(R.id.tv_address);
        tv_dob = view.findViewById(R.id.tv_dob);
        tv_rating_tutor = view.findViewById(R.id.tv_rating_tutor);
        profile_image = view.findViewById(R.id.profile_image);
        img_edit_profile = view.findViewById(R.id.img_edit_profile);
        tv_add_course = view.findViewById(R.id.tv_add_course);
        nacho_text_view_course = view.findViewById(R.id.nacho_text_view_course);

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


        Bundle bundle = this.getArguments();
        if (bundle != null) {
            String type = bundle.getString("type");
            if (type.equals("view")) {
                img_edit_profile.setVisibility(View.GONE);
                tv_change_pass.setVisibility(View.GONE);
                tv_budget.setVisibility(View.GONE);
                tv_day.setVisibility(View.GONE);
                tv_time.setVisibility(View.GONE);
                tv_add_course.setVisibility(View.GONE);
            }
            phone = bundle.getString("tutorMobile");
        } else {
            phone = ConfigURL.getMobileNumber(getActivity());
        }

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


        img_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragmentName = null;
                Fragment TutorProfileEdit = new TutorProfileEdit();
                fragmentName = TutorProfileEdit;
                FragmentReplace.replaceFragment(getActivity(), fragmentName, R.id.fragment_container_drawer);

                //DateDialog();
            }
        });
        tv_change_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragmentName = null;
                Fragment ChangePassword = new ChangePassword();
                fragmentName = ChangePassword;
                FragmentReplace.replaceFragment(getActivity(), fragmentName, R.id.fragment_container_drawer);

            }
        });

        getProfile();
        getDays();
        getTime();
        getBudget();

        tv_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ConfigURL.getType(getActivity()).equals("TEACHER")) {
                    Uri navigationIntentUri = Uri.parse("google.navigation:q=" + tv_address.getText().toString());//creating intent with latlng
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, navigationIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(mapIntent);
                }
            }
        });

        tv_add_course.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragmentName = null;
                Fragment InsertCourse = new InsertCourse();
                fragmentName = InsertCourse;
                FragmentReplace.replaceFragment(getActivity(), fragmentName, R.id.fragment_container_drawer);

            }
        });

        return view;
    }

    public void getProfile() {
        ProgressDialogClass.showProgress(getActivity());
        AndroidNetworking.get(ConfigURL.URL_GET_TUTOR_PROFILE)
                .addQueryParameter("mobile_num", phone)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Toast.makeText(getActivity(),""+response,Toast.LENGTH_SHORT).show();
                        Log.v("Response", "" + response);

                        try {
                            JSONArray jsonArray = response.getJSONArray("Profile");
                            for (int i = 0; i < jsonArray.length(); i++) {

                                TutorProfile tutorProfile = new TutorProfile(jsonArray.getJSONObject(i));

                                tv_name.setText("Name : " + tutorProfile.getName());
                                tv_qualification.setText("Qualification : " + tutorProfile.getQualification());
                                tv_dob.setText("Date Of Birth : " + tutorProfile.dob);
                                tv_gender.setText("Gender : " + tutorProfile.getGender());
                                tv_address.setText("Address : " + getAddressNew(tutorProfile.getLat(), tutorProfile.getLng()));
                                /*try {
                                    tv_dob.setText("Date Of Birth : " + tutorProfile.getDob());
                                } catch (NullPointerException ex) {
                                }*/
                                /*if (tutorProfile.getDob().equals("") || tutorProfile.getDob() == null || tutorProfile.getDob() == "null") {
                                    tutorProfile.setDob("01 Jan 2018");
                                    dob = tutorProfile.getDob().split("\\s+");
                                } else
                                    dob = tutorProfile.getDob().split("\\s+");
*/
                                //Toast.makeText(getActivity(), "" + dob[0] + " " + dob[1] + " " + dob[2], Toast.LENGTH_SHORT).show();

                                float customer;

                                try {
                                    customer = Float.parseFloat(tutorProfile.getRating());
                                    tv_rating_tutor.setRating(customer);

                                } catch (NumberFormatException e) {

                                }


                                if (tutorProfile.getImage().isEmpty()) {
                                    profile_image.setImageResource(R.drawable.icon_profile_pictures);
                                } else {
                                    Picasso.get()
                                            .load(tutorProfile.getImage())
                                            .placeholder(R.drawable.icon_profile_pictures)
                                            .into(profile_image);
                                }

                                SharedPreferences preferences = getActivity().getSharedPreferences("PREFRENCE", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("NAME", "" + tutorProfile.getName());
                                editor.putString("QUALIFICATION", "" + tutorProfile.getQualification());
                                /*editor.putString("D", "" + dob[0]);
                                editor.putString("M", "" + dob[1]);
                                editor.putString("Y", "" + dob[2]);*/
                                editor.putString("DOB", "" + tutorProfile.getDob());
                                editor.putString("GENDER", "" + tutorProfile.getGender());
                                editor.putString("IMAGE", "" + tutorProfile.getImage());
                                editor.commit();

                            }
                            skillArrayList = new ArrayList<>();
                            JSONObject jsonObject = response.getJSONArray("Profile").getJSONObject(0);
                            JSONArray jsonArray1 = jsonObject.getJSONArray("Courses");
                            for (int j = 0; j < jsonArray1.length(); j++) {

                                JSONObject jsonObject1 = jsonArray1.getJSONObject(j);
                                skillArrayList.add(jsonObject1.getString("Course_Name"));
                                Log.v("TutorProfile", jsonObject1.getString("Course_Name"));

                            }
                            nacho_text_view_course.setText(skillArrayList);

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


    public void getDays() {
        AndroidNetworking.get(ConfigURL.URL_SCHEDULE_DAY)
                .addQueryParameter("user_num", phone)
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

                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });


    }

    public void getTime() {
        AndroidNetworking.get(ConfigURL.URL_SCHEDULE_TIME)
                .addQueryParameter("user_num", phone)
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
                .addQueryParameter("user_num", phone)
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

    public String getAddressNew(String lat, String lng) {
        String address = "";

        geocoder = new Geocoder(getActivity(), Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(Double.parseDouble(lat), Double.parseDouble(lng), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            e.printStackTrace();
        }
        address = addresses.get(0).getAddressLine(0);

        return address;
    }


    /*public void DateDialog() {
        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int yearOf, int monthOfYear, int dayOfMonth) {

                year = yearOf;
                month = monthOfYear;
                day = dayOfMonth;

                // Show selected date
               *//* birthdate_edt.setText(new StringBuilder().append(month + 1)
                        .append("-").append(day).append("-").append(year)
                        .append(" "));*//*

                Toast.makeText(getActivity(), "" + new StringBuilder().append(month + 1)
                        .append(" ").append(day).append(" ").append(year)
                        .append(" "), Toast.LENGTH_SHORT).show();
            }
        };
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpDialog = new DatePickerDialog(getActivity(), listener, year, month, day);
        dpDialog.show();
    }*/

}
