package com.android.tutorapp.StarterPack;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.tutorapp.Others.Drawer;
import com.android.tutorapp.R;
import com.android.tutorapp.Utills.ConfigURL;
import com.android.tutorapp.Utills.NetworkConnectivityClass;
import com.android.tutorapp.Utills.ProgressDialogClass;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;


public class SignUpVerification extends Fragment implements View.OnClickListener {

    String name, email, password, phone, type, lat, lng;
    TextView countDown;
    Button verify, resend;
    //SMS Authentication Using FireBase
    EditText mVerificationField;
    private FirebaseAuth mAuth;
    String mVerificationId;
    TextView textView;
    Context context;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    String LOGIN = "active";
    ImageView img_back;

    byte[] byteArray;
    Bitmap bitmap;

    private static final String TAG = "PhoneAuthActivity";


    public SignUpVerification() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.signup_verfication_fragment, container, false);

        /*Bundle bundle = new Bundle();
        bundle.putString("name", name);
        bundle.putString("phone", phone);
        bundle.putString("type", reg_type);
        bundle.putString("pass", pass);
        bundle.putString("lat", lat.toString());
        bundle.putString("lng", lng.toString());
        bundle.putString("email", et_input_email.getText().toString());
        */

        Bundle bundle = getArguments();
        if (bundle != null) {
            name = (String) bundle.get("name");
            phone = (String) bundle.get("phone");
            type = (String) bundle.get("type");
            password = (String) bundle.get("pass");
            lat = (String) bundle.get("lat");
            lng = (String) bundle.get("lng");
            email = (String) bundle.get("email");
            byteArray = getArguments().getByteArray("image");

        }


        mVerificationField = rootView.findViewById(R.id.input_pin_code);
        countDown = rootView.findViewById(R.id.countDown);
        verify = rootView.findViewById(R.id.btn_verify);
        resend = rootView.findViewById(R.id.btn_resend);


        verify.setOnClickListener(this);
        resend.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                Log.d(TAG, "onVerificationCompleted:" + credential);
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.w(TAG, "onVerificationFailed", e);
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    //Invalid Phone Number
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    Snackbar.make(getActivity().findViewById(android.R.id.content), "Quota exceeded.",
                            Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                Log.d(TAG, "onCodeSent:" + verificationId);
                mVerificationId = verificationId;
                mResendToken = token;
            }
        };

        countDown.setVisibility(View.VISIBLE);
        verify.setVisibility(View.VISIBLE);
        startPhoneNumberVerification(phone);

        return rootView;
    }


    //Firebase Methods

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = task.getResult().getUser();

                            if (NetworkConnectivityClass.isNetworkAvailable(getActivity())) {
                                try {
                                    sendData();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Snackbar.make(getActivity().findViewById(android.R.id.content), "Internet Not Connected",
                                        Snackbar.LENGTH_SHORT).show();
                            }
                            FirebaseAuth.getInstance().signOut();

                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                mVerificationField.setError("Invalid code.");
                            }
                        }
                    }
                });
    }


    private void startPhoneNumberVerification(String phoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                getActivity(),               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks

        timerStart(resend, verify);

    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        try {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
            signInWithPhoneAuthCredential(credential);
        } catch (NullPointerException ex) {

        }
    }

    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                getActivity(),               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseApp.initializeApp(getActivity());
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            /*startActivity(new Intent(getActivity(), DrawerMainActivity.class));
            getActivity().finish();*/
        }
    }

    public void timerStart(final Button visible, final Button gone) {
        new CountDownTimer(120000, 1000) {

            public void onTick(long millisUntilFinished) {
                countDown.setText("Didn't Received Code Resend in: " + String.format("%d min, %d sec",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }

            public void onFinish() {
                countDown.setVisibility(View.GONE);
                gone.setVisibility(View.GONE);
                countDown.setVisibility(View.GONE);
                visible.setVisibility(View.VISIBLE);
            }
        }.start();
    }


    public void sendData() throws IOException {
        bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        ProgressDialogClass.showProgress(getActivity());
        AndroidNetworking.upload(ConfigURL.URL_IS_STUDENT_REGISTERED)
                .addMultipartParameter("name", name)
                .addMultipartParameter("mobile_num", phone)
                .addMultipartParameter("type", type)
                .addMultipartParameter("password", password)
                .addMultipartParameter("latitude", lat)
                .addMultipartParameter("longitude", lng)
                .addMultipartParameter("email", email)
                .addMultipartParameter("fcm", FirebaseInstanceId.getInstance().getToken())
                .addMultipartFile("photo", returnFile(bitmap))
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ProgressDialogClass.hideProgress();

                        //Toast.makeText(getActivity(), "" + name + " " + phone + " " + type + " " + password + " " + lat.toString() + " " + " " + lng.toString() + " " + email + " ", Toast.LENGTH_LONG).show();
                        try {
                            String msg = response.getString("message");
                            boolean error = false;
                            error = response.getBoolean("error");
                            if (error) {
                                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                            }
                            if (!error) {
                                Intent intent = new Intent(getActivity(), Drawer.class);
                                startActivity(intent);
                                SharedPreferences preferences = getActivity().getSharedPreferences("PREFRENCE", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("PHONE", phone);
                                editor.putString("EMAIL", email);
                                editor.putString("NAME", name);
                                editor.putString("TYPE", type);
                                editor.commit();
                                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        FirebaseAuth.getInstance().signOut();

                    }

                    @Override
                    public void onError(ANError error) {
                        ProgressDialogClass.hideProgress();
                        FirebaseAuth.getInstance().signOut();
                        Toast.makeText(getActivity(), "" + error, Toast.LENGTH_LONG).show();
                    }
                });
    }


    public File returnFile(Bitmap bmp) throws IOException {
        File f = new File(getActivity().getCacheDir(), phone + ".png");
        f.createNewFile();
        //Convert bitmap to byte array
        Bitmap bitmap = bmp;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
        byte[] bitmapdata = bos.toByteArray();
        //write the bytes in file
        FileOutputStream fos = new FileOutputStream(f);
        fos.write(bitmapdata);
        fos.flush();
        fos.close();
        return f;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_verify:
                String code = mVerificationField.getText().toString();
                if (TextUtils.isEmpty(code)) {
                    mVerificationField.setError("Cannot be empty.");
                    return;
                }
                Log.d("Firebase",""+code);
                verifyPhoneNumberWithCode(mVerificationId, code);

                break;
            case R.id.btn_resend:
                countDown.setVisibility(View.VISIBLE);
                verify.setVisibility(View.VISIBLE);
                resend.setVisibility(View.GONE);
                timerStart(resend, verify);
                resendVerificationCode(phone, mResendToken);
                break;
        }
    }
}