package com.android.tutorapp.Others;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.tutorapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PDFViewerListFragment extends Fragment implements View.OnClickListener {


    TextView tv_1, tv_2;

    public PDFViewerListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pdfviewer_list, container, false);
        tv_1 = view.findViewById(R.id.tv_1);
        tv_2 = view.findViewById(R.id.tv_2);
        tv_1.setOnClickListener(this);
        tv_2.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), PDFViewer.class);
        switch (v.getId()) {
            case R.id.tv_1:
                intent.putExtra("book", "seo_tutorial.pdf");
                startActivity(intent);
                break;
            case R.id.tv_2:
                intent.putExtra("book", "qasim.pdf");
                startActivity(intent);
                break;
        }
    }
}
