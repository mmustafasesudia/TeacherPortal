package com.android.tutorapp.SearchTutorMain;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.tutorapp.R;
import com.hootsuite.nachos.NachoTextView;
import com.hootsuite.nachos.chip.Chip;

import java.util.ArrayList;

import static com.android.tutorapp.Utills.ConfigURL.Filterbudget;
import static com.android.tutorapp.Utills.ConfigURL.Filtercourse;
import static com.android.tutorapp.Utills.ConfigURL.Filterday;
import static com.android.tutorapp.Utills.ConfigURL.Filtertime;
import static com.android.tutorapp.Utills.ConfigURL.suggestions;


/**
 * A simple {@link Fragment} subclass.
 */
public class FilterFragment extends Fragment {

    NachoTextView nacho_text_view;
    RadioButton b1, b2, b3, t1, t2, t3, t4, d1, d2, d3, d4, d5;
    Button btn_cancel, btn_okay;
    ArrayList<String> stringChipList = new ArrayList<String>();


    public FilterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_filter, container, false);

        getActivity().setTitle("Filter");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, suggestions);

        nacho_text_view = view.findViewById(R.id.nacho_text_view);
        nacho_text_view.setAdapter(adapter);


        b1 = view.findViewById(R.id.b1);
        b2 = view.findViewById(R.id.b2);
        b3 = view.findViewById(R.id.b3);

        t1 = view.findViewById(R.id.t1);
        t2 = view.findViewById(R.id.t2);
        t3 = view.findViewById(R.id.t3);
        t4 = view.findViewById(R.id.t4);

        d1 = view.findViewById(R.id.d2);
        d2 = view.findViewById(R.id.d2);
        d3 = view.findViewById(R.id.d3);
        d4 = view.findViewById(R.id.d4);
        d5 = view.findViewById(R.id.d5);

        btn_cancel = view.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        btn_okay = view.findViewById(R.id.btn_ok);
        btn_okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendData();
            }
        });


        return view;
    }

    public void sendData() {
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
        String budget = "";
        if (b1.isChecked()) {
            budget = b1.getText().toString();
        } else if (b2.isChecked()) {
            budget = b2.getText().toString();
        } else if (b3.isChecked()) {
            budget = b3.getText().toString();
        }
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
        String course = nacho_text_view.getText().toString();
        if (course == null || course.isEmpty() || course.equals("null")) {
            Toast.makeText(getActivity(), "Course cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if (time != null || day != null || budget != null) {
            // Toast.makeText(getActivity(), " " + time + " " + day + " " + budget, Toast.LENGTH_SHORT).show();
        }
        Filtercourse = "" + getAllChips();
        Filtertime = "" + time;
        Filterday = "" + day;
        Filterbudget = "" + budget;
        getActivity().onBackPressed();
    }

    public String getAllChips() {
        stringChipList.clear();
        int i = 0;
        for (Chip chip : nacho_text_view.getAllChips()) {
            i++;
            // Do something with the text of each chip
            CharSequence text = chip.getText();
            stringChipList.add(text.toString());
            // Do something with the data of each chip (this data will be set if the chip was created by tapping a suggestion)
            Object data = chip.getData();
        }

        String idList = stringChipList.toString();
        String csv = idList.substring(1, idList.length() - 1).replace(", ", "','");
        return csv;
    }

}
