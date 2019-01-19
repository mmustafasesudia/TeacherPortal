package com.android.tutorapp.Profile;


import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.tutorapp.R;
import com.android.tutorapp.Utills.ConfigURL;
import com.android.tutorapp.Utills.ProgressDialogClass;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TutorProfileEdit extends Fragment {
    final Calendar c = Calendar.getInstance();
    EditText tv_name, tv_qualification;
    Spinner tv_gender, sp_day, sp_month, sp_year;
    String sp_day_str, sp_month_str, sp_year_str, sp_gender_str;
    Button btn_update_profile;
    TextView tv_dob;
    int _year, _day, _month;

    String[] day = new String[]{
            "Select Day", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"
    };
    String[] month = new String[]{
            "Select Month", "Jan", "Feb", "March", "April", "June", "July", "Aug", "Sep", "Oct", "Nov", "Dec"
    };
    String[] year = new String[]{
            "Select Year", "2010", "2009", "2008", "2007", "2006", "2005", "2004", "2003", "2002", "2001", "2000", "1999", "1998", "1997", "1996", "1995", "1994", "1992", "1991", "1990", "1989", "1988", "1987", "1986", "1985"
    };
    String[] gender = new String[]{
            "Select Gender", "MALE", "FEMALE"
    };

    ImageView profile_image;

    public TutorProfileEdit() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tutor_profile_edit, container, false);

        getActivity().setTitle("Profile Edit");


        tv_name = view.findViewById(R.id.tv_name);
        tv_qualification = view.findViewById(R.id.tv_qualification);

        tv_gender = view.findViewById(R.id.tv_gender);
        tv_dob = view.findViewById(R.id.tv_dob);
        tv_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateDialog();
            }
        });
       /* sp_day = view.findViewById(R.id.sp_day);
        sp_month = view.findViewById(R.id.sp_month);
        sp_year = view.findViewById(R.id.sp_year);
*/
        btn_update_profile = view.findViewById(R.id.btn_update_profile);

/*
        final List<String> day_list = new ArrayList<>(Arrays.asList(day));
        final ArrayAdapter<String> day_adaptor = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_spinner_item, day_list);
        day_adaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sp_day.setAdapter(day_adaptor);
        sp_day.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sp_day_str = day[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        final List<String> month_list = new ArrayList<>(Arrays.asList(month));
        final ArrayAdapter<String> month_adaptor = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_spinner_item, month_list);
        month_adaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sp_month.setAdapter(month_adaptor);
        sp_month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sp_month_str = month[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        final List<String> year_list = new ArrayList<>(Arrays.asList(year));
        final ArrayAdapter<String> year_adaptor = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_spinner_item, year_list);
        year_adaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sp_year.setAdapter(year_adaptor);
        sp_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sp_year_str = year[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });*/

        final List<String> gender_list = new ArrayList<>(Arrays.asList(gender));
        final ArrayAdapter<String> gender_adaptor = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_spinner_item, gender_list);
        gender_adaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        tv_gender.setAdapter(gender_adaptor);
        tv_gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sp_gender_str = gender[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        profile_image = view.findViewById(R.id.profile_image);


        tv_name.setText(ConfigURL.getName(getActivity()));
        tv_qualification.setText(ConfigURL.getQualification(getActivity()));

        /*Picasso.get()
                .load(ConfigURL.getImage(getActivity()))
                .placeholder(R.drawable.profile_pics)
                .into(profile_image);*/

        if (ConfigURL.getImage(getActivity()).isEmpty()) {
            profile_image.setImageResource(R.drawable.icon_profile_pictures);
        } else {
            Picasso.get()
                    .load(ConfigURL.getImage(getActivity()))
                    .placeholder(R.drawable.icon_profile_pictures)
                    .into(profile_image);
        }

        String gender_ = "" + ConfigURL.getGender(getActivity());
        if (gender_ == null || gender_.isEmpty() || gender_.equals("null")) {
            tv_gender.getItemAtPosition(0);
        } else {
            tv_gender.setSelection(getIndex(tv_gender, gender_));

        }
        if (ConfigURL.getDOB(getActivity()).length() > 0) {
            tv_dob.setText("" + ConfigURL.getDOB(getActivity()));
        } else {
            _year = c.get(Calendar.YEAR);
            _month = c.get(Calendar.MONTH);
            _day = c.get(Calendar.DAY_OF_MONTH);
            tv_dob.setText("" + _day + " " + month[_month] + " " + _year);
        }
        /*String date = "" + ConfigURL.getD(getActivity());
        if (date == null || date.isEmpty() || date.equals("null")) {
            sp_day.getItemAtPosition(0);
        } else {
            sp_day.setSelection(getIndex(sp_day, date));

        }
        String month = "" + ConfigURL.getM(getActivity());
        if (month == null || month.isEmpty() || month.equals("null")) {
            sp_month.getItemAtPosition(0);
        } else {
            sp_month.setSelection(getIndex(sp_month, month));

        }
        String year = "" + ConfigURL.getY(getActivity());
        if (year == null || year.isEmpty() || year.equals("null")) {
            sp_year.getItemAtPosition(0);
        } else {
            sp_year.setSelection(getIndex(sp_day, date));

        }*/


        btn_update_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv_name.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Name Cannot Be Empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (tv_qualification.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Qualification Cannot Be Empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (tv_gender.equals("Select Gender")) {
                    Toast.makeText(getActivity(), "Please Select Gender", Toast.LENGTH_SHORT).show();
                    return;
                }
                /*if (sp_day.equals("Select Day")) {
                    Toast.makeText(getActivity(), "Please Select Day", Toast.LENGTH_SHORT).show();
                    return;

                }
                if (sp_month.equals("Select Month")) {
                    Toast.makeText(getActivity(), "Please Select Month", Toast.LENGTH_SHORT).show();
                    return;

                }
                if (sp_year.equals("Select Year")) {
                    Toast.makeText(getActivity(), "Please Select Year", Toast.LENGTH_SHORT).show();
                    return;

                }*/
                updateProfile();

            }
        });
        return view;
    }

    private int getIndex(Spinner spinner, String myString) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                return i;
            }
        }
        return 0;
    }


    public void updateProfile() {
        ProgressDialogClass.showProgress(getActivity());
        AndroidNetworking.post(ConfigURL.URL_GET_TUTOR_PROFILE)
                .addBodyParameter("mobileNo", ConfigURL.getMobileNumber(getActivity()))
                .addBodyParameter("name", tv_name.getText().toString())
                .addBodyParameter("gender", sp_gender_str)
                .addBodyParameter("dob", tv_dob.getText().toString())
                .addBodyParameter("Qualification", tv_qualification.getText().toString())
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
                                SharedPreferences preferences = getActivity().getSharedPreferences("PREFRENCE", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("NAME", "" + tv_name.getText().toString());
                                editor.putString("QUALIFICATION", "" + tv_qualification.getText().toString());
                                /*editor.putString("D", "" + day);
                                editor.putString("M", "" + month);
                                editor.putString("Y", "" + year);*/
                                editor.putString("DOB", "" + tv_dob.getText().toString());
                                editor.putString("GENDER", "" + gender);
                                editor.commit();
                                getActivity().onBackPressed();
                            } else {
                                Toast.makeText(getActivity(), "" + msg, Toast.LENGTH_SHORT).show();
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

    public void DateDialog() {
        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int yearOf, int monthOfYear, int dayOfMonth) {

                _year = yearOf;
                _month = monthOfYear;
                _day = dayOfMonth;

                // Show selected date
               /* birthdate_edt.setText(new StringBuilder().append(month + 1)
                        .append("-").append(day).append("-").append(year)
                        .append(" "));*/
               /* Toast.makeText(getActivity(), "" + new StringBuilder().append(_month + 1)
                        .append(" ").append(_day).append(" ").append(_year)
                        .append(" "), Toast.LENGTH_SHORT).show();*/
                tv_dob.setText(new StringBuilder().append(_day).append(" ").append(month[_month]).append(" ").append(_year).append(" "));
            }
        };
        _year = c.get(Calendar.YEAR);
        _month = c.get(Calendar.MONTH);
        _day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpDialog = new DatePickerDialog(getActivity(), listener, _year, _month, _day);
        dpDialog.show();
    }
}
