package com.android.tutorapp.StarterPack;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.tutorapp.Others.Drawer;
import com.android.tutorapp.R;
import com.android.tutorapp.Utills.ConfigURL;
import com.android.tutorapp.Utills.FragmentReplace;
import com.android.tutorapp.Utills.NetworkConnectivityClass;
import com.android.tutorapp.Utills.ProgressDialogClass;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SignInFragment extends Fragment implements View.OnClickListener {

    TextView tv_new_account, tv_forgot_pass;
    EditText et_input_phone, et_input_password, et_input_email;
    String phone, pass;
    Button btn_signin;
    String LOGIN = "active";

    public SignInFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_sign_in, container, false);

        // ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        if (ConfigURL.getType(getActivity()).length() > 0) {
            Intent intent = new Intent(getActivity(), Drawer.class);
            startActivity(intent);
            getActivity().finish();
        }

        et_input_phone = (EditText) rootView.findViewById(R.id.et_input_phone);
        et_input_password = (EditText) rootView.findViewById(R.id.et_input_password);
        et_input_email = (EditText) rootView.findViewById(R.id.et_input_email);
        tv_new_account = (TextView) rootView.findViewById(R.id.tv_new_account);
        tv_forgot_pass = (TextView) rootView.findViewById(R.id.tv_forgot_pass);
        btn_signin = (Button) rootView.findViewById(R.id.btn_signin);

        tv_new_account.setOnClickListener(this);
        tv_forgot_pass.setOnClickListener(this);
        btn_signin.setOnClickListener(this);

        return rootView;
    }

    public void submit() {

        if (et_input_email.getText().toString().isEmpty() || !isValidEmail(et_input_email.getText().toString())) {
            et_input_email.setError("Invalid Email");
            requestFocus(et_input_email);
            return;
        }
        if (et_input_password.getText().toString().isEmpty()) {
            et_input_password.setError("Password Cannot Be Empty");
            requestFocus(et_input_password);
            return;
        }

        if (NetworkConnectivityClass.isNetworkAvailable(getActivity())) {
            sendData();
        } else {
            Snackbar.make(getActivity().findViewById(android.R.id.content), "Internet Not Connected",
                    Snackbar.LENGTH_SHORT).show();
        }
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean validatePhone() {
        if (!et_input_phone.getText().toString().matches("[0][3][0-9]{9}|[3][0-9]{9}")) {
            et_input_phone.setError("Invalid Phone Number");
            requestFocus(et_input_phone);
            return false;
        } else if (et_input_phone.getText().toString().length() < 10) {
            et_input_phone.setError("Invalid Length");
            requestFocus(et_input_phone);
            return false;
        } else if (et_input_phone.getText().toString().trim().isEmpty()) {
            et_input_phone.setError("Phone Number Cannot Be Empty");
            requestFocus(et_input_phone);
            return false;
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    public void sendData() {

        ProgressDialogClass.showProgress(getActivity());

        pass = et_input_password.getText().toString();

        AndroidNetworking.post(ConfigURL.URL_LOGIN_STUDENT)
                .addBodyParameter("email", et_input_email.getText().toString())
                .addBodyParameter("password", pass)
                .addBodyParameter("fcmKey", FirebaseInstanceId.getInstance().getToken())
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Toast.makeText(getActivity(), "" + response, Toast.LENGTH_SHORT).show();
                        ProgressDialogClass.hideProgress();
                        /*
                        {
                          [
                          ]
                        }
                        */

                        String pName = "", req_Type = "";
                        try

                        {
                            String msg = response.getString("message");
                            boolean error = false;

                            error = response.getBoolean("error");

                            if (!error) {

                                JSONArray jsonArray = response.getJSONArray("user");
                                User workerDetail = new User(jsonArray.getJSONObject(0));
                                pName = workerDetail.getUname();
                                req_Type = workerDetail.getUtype();
                                phone = workerDetail.getUmobile();

                                Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getActivity(), Drawer.class);
                                startActivity(intent);

                                SharedPreferences preferences = getActivity().getSharedPreferences("PREFRENCE", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("LOGIN", LOGIN);
                                editor.putString("PHONE", phone);
                                editor.putString("EMAIL", et_input_email.getText().toString());
                                editor.putString("NAME", pName);
                                editor.putString("TYPE", req_Type);
                                editor.commit();

                            }
                            if (error) {
                                ProgressDialogClass.hideProgress();
                                Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
                            }
                        } catch (
                                JSONException e)

                        {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        ProgressDialogClass.hideProgress();
                        Log.v("Sign In", "" + error);
                    }
                });
    }

    @Override
    public void onClick(View view) {

        Fragment fragmentName = null;
        Fragment SignUpInfoFragment = new SignUpInfoFragment();
        Fragment ForgotPassword = new ForgotPassword();

        switch (view.getId()) {
            case R.id.btn_signin:
                submit();
                break;
            case R.id.tv_forgot_pass:
                fragmentName = ForgotPassword;
                FragmentReplace.replaceFragment(getActivity(), fragmentName, R.id.content_frame_signin);
                break;
            case R.id.tv_new_account:
                fragmentName = SignUpInfoFragment;
                FragmentReplace.replaceFragment(getActivity(), fragmentName, R.id.content_frame_signin);
                break;
        }
    }
}
