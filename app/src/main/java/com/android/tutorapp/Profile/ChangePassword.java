package com.android.tutorapp.Profile;


import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.tutorapp.R;
import com.android.tutorapp.Utills.ConfigURL;
import com.android.tutorapp.Utills.NetworkConnectivityClass;
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
public class ChangePassword extends Fragment implements View.OnClickListener {

    Button btn_done;
    EditText et_old_password, et_new_password, et_new_confirm_password;

    public ChangePassword() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_change_password, container, false);

        getActivity().setTitle("Change Password");

        btn_done = (Button) rootView.findViewById(R.id.btn_done);
        et_old_password = (EditText) rootView.findViewById(R.id.input_old_password);
        et_new_password = (EditText) rootView.findViewById(R.id.input_new_password);
        et_new_confirm_password = (EditText) rootView.findViewById(R.id.input_new_confirm_password);
        btn_done.setOnClickListener(this);

        return rootView;
    }

    public void sendData() {
        Log.v("ChangePassword", "" + ConfigURL.getMobileNumber(getActivity()) + " " + et_old_password.getText().toString() + " " + et_new_password.getText().toString());
        ProgressDialogClass.showProgress(getActivity());
        AndroidNetworking.post(ConfigURL.URL_PROFILE_CHANGE_PASSWORD)
                .addBodyParameter("mobileNo", ConfigURL.getMobileNumber(getActivity()))
                .addBodyParameter("previous_pass", et_old_password.getText().toString())
                .addBodyParameter("uPass", et_new_password.getText().toString())
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ProgressDialogClass.hideProgress();
                        String msg = "";
                        boolean error = false;
                        try {
                            msg = response.getString("message");
                            error = response.getBoolean("error");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(getActivity(), "" + msg, Toast.LENGTH_LONG).show();

                        getActivity().onBackPressed();
                    }

                    @Override
                    public void onError(ANError anError) {
                        ProgressDialogClass.hideProgress();
                        Toast.makeText(getActivity(), "" + anError, Toast.LENGTH_LONG).show();

                    }
                });

    }

    public void submit() {

        if (et_old_password.getText().toString().isEmpty()) {
            et_old_password.setError("Old Password Cannot Be Empty");
            requestFocus(et_old_password);
            return;
        }
        if (et_new_password.getText().toString().isEmpty()) {
            et_new_password.setError("New Password Cannot Be Empty");
            requestFocus(et_new_password);
            return;
        }
        if (et_new_confirm_password.getText().toString().isEmpty()) {
            et_new_confirm_password.setError("Confirm Password Cannot Be Empty");
            requestFocus(et_new_confirm_password);
            return;
        }
        if (!et_new_password.getText().toString().equals(et_new_confirm_password.getText().toString())) {
            et_new_confirm_password.setError("Confirm Password Not Match");
            requestFocus(et_new_confirm_password);
            return;
        }

        if (NetworkConnectivityClass.isNetworkAvailable(getActivity())) {
            sendData();
        } else {
            Snackbar.make(getActivity().findViewById(android.R.id.content), "Internet Not Connected",
                    Snackbar.LENGTH_SHORT).show();
        }
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_done:
                submit();
                break;
        }
    }
}
