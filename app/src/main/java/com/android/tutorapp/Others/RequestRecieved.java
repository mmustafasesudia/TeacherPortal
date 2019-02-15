package com.android.tutorapp.Others;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.tutorapp.R;
import com.android.tutorapp.SearchTutorMain.SearchTutor;
import com.android.tutorapp.Utills.ConfigURL;
import com.android.tutorapp.Utills.ProgressDialogClass;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 */
public class RequestRecieved extends Fragment {


    Button btn_accept, btn_decline;
    String msg_id = "", message = "";
    TextView tv_text;

    public RequestRecieved() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_request_recieved, container, false);
        btn_accept = view.findViewById(R.id.btn_accept);
        btn_decline = view.findViewById(R.id.btn_decline);
        tv_text = view.findViewById(R.id.tv_text);
        Bundle bundle = getArguments();
        if (bundle != null) {
            msg_id = (String) bundle.get("msg_id");
            message = (String) bundle.get("message");
            tv_text.setText("" + message);

            getActivity().setTitle("New Request");
        }

        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ConfigURL.getType(getActivity()).equals("ORGANIZATION"))
                    sendAcceptJobRequest(msg_id);
                else
                    sendAcceptRequest(msg_id);
            }
        });
        btn_decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragmentName = null;
                Fragment SearchTutor = new SearchTutor();
                fragmentName = SearchTutor;
                replaceFragment(getActivity(), fragmentName, R.id.fragment_container_drawer);

            }
        });
        return view;
    }


    public void sendAcceptRequest(String req_id) {
        ProgressDialogClass.showProgress(getActivity());
        AndroidNetworking.post(ConfigURL.URL_STUDENT_REQUEST_ACCEPT)
                .addBodyParameter("request_id", req_id)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ProgressDialogClass.hideProgress();
                        try {
                            if (!response.getBoolean("error")) {
                                Fragment fragmentName = null;
                                Fragment SearchTutor = new SearchTutor();
                                fragmentName = SearchTutor;
                                replaceFragment(getActivity(), fragmentName, R.id.fragment_container_drawer);

                            } else {
                                Toast.makeText(getActivity(), "", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        Toast.makeText(getActivity(), "", Toast.LENGTH_LONG).show();
                        ProgressDialogClass.hideProgress();
                    }
                });
    }

    public void sendAcceptJobRequest(String req_id) {
        ProgressDialogClass.showProgress(getActivity());
        AndroidNetworking.post(ConfigURL.URL_REQUEST_ACCEPT)
                .addBodyParameter("job_id", req_id)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ProgressDialogClass.hideProgress();
                        try {
                            if (!response.getBoolean("error")) {
                                Fragment fragmentName = null;
                                Fragment SearchTutor = new SearchTutor();
                                fragmentName = SearchTutor;
                                replaceFragment(getActivity(), fragmentName, R.id.fragment_container_drawer);

                            } else {
                                Toast.makeText(getActivity(), "", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        Toast.makeText(getActivity(), "", Toast.LENGTH_LONG).show();
                        ProgressDialogClass.hideProgress();
                    }
                });
    }

    public void replaceFragment(FragmentActivity context, Fragment fragment, int frame) {
        String backStateName = fragment.getClass().getName();
        String fragmentTag = backStateName;

        FragmentManager manager = context.getSupportFragmentManager();
        boolean fragmentPopped = manager.popBackStackImmediate(backStateName, 0);

        if (!fragmentPopped && manager.findFragmentByTag(fragmentTag) == null) { //fragment not in back stack, create it.
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(frame, fragment, fragmentTag);
            //ft.setTransition(FragmentTransaction.TRANSIT_ENTER_MASK);
            //ft.addToBackStack(backStateName);
            ft.commit();
        }
    }
}
