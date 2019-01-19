package com.android.tutorapp.StarterPack;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.tutorapp.R;
import com.android.tutorapp.Utills.FragmentReplace;
import com.android.tutorapp.Utills.NetworkConnectivityClass;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SignUpInfoFragment extends Fragment implements View.OnClickListener {

    FusedLocationProviderClient mFusedLocationClient;

    TextView tv_sign_in;
    Button btn_next;
    String phone, reg_type;
    EditText et_input_full_name, et_input_email, et_input_phone, et_input_password, et_input_confirm_password;
    ImageView img_profile_image;

    Bitmap bitmap;
    ByteArrayOutputStream bStream;
    byte[] byteArray;

    Spinner spinner;
    String[] type = new String[]{
            "Register As", "Student/Parent", "Teacher", "Organization"
    };

    public SignUpInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_sign_up_info, container, false);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        //((AppCompatActivity) getActivity()).getSupportActionBar().show();

        //spinner = rootView.findViewById(R.id.et_type);
        et_input_full_name = (EditText) rootView.findViewById(R.id.et_input_full_name);
        et_input_email = (EditText) rootView.findViewById(R.id.et_input_email);
        et_input_phone = (EditText) rootView.findViewById(R.id.et_input_phone);
        et_input_password = (EditText) rootView.findViewById(R.id.et_input_password);
        et_input_confirm_password = (EditText) rootView.findViewById(R.id.et_input_confirm_password);
        img_profile_image = (ImageView) rootView.findViewById(R.id.img_profile_image);
        img_profile_image.setOnClickListener(this);


        tv_sign_in = (TextView) rootView.findViewById(R.id.tv_sign_in);
        btn_next = (Button) rootView.findViewById(R.id.btn_next);

        tv_sign_in.setOnClickListener(this);
        btn_next.setOnClickListener(this);


        final List<String> offer_for_month = new ArrayList<>(Arrays.asList(type));
        final ArrayAdapter<String> offer_for_month_adapter = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_spinner_item, offer_for_month);
        offer_for_month_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner = (Spinner) rootView.findViewById(R.id.et_type);
        spinner.setAdapter(offer_for_month_adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    reg_type = "Register As";
                    //Toast.makeText(getActivity(), "Please select type", Toast.LENGTH_SHORT).show();
                    return;
                } else if (i == 1) {
                    reg_type = "STUDENT";
                } else if (i == 2) {
                    reg_type = "TEACHER";
                } else if (i == 3) {
                    reg_type = "ORGANIZATION";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return rootView;
    }

    public void getLocationAndSendData() {
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations, this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            // Toast.makeText(getActivity(), "" + location.getLatitude() + "" + location.getLongitude(), Toast.LENGTH_SHORT).show();
                            checkIfNumberExist(location.getLatitude(), location.getLongitude());
                        }
                    }
                });
    }

    public void submit() {

        if (et_input_full_name.getText().toString().isEmpty()) {
            et_input_full_name.setError("Full Name Cannot Be Empty");
            requestFocus(et_input_full_name);
            return;
        }
        if (et_input_email.getText().toString().isEmpty() || !isValidEmail(et_input_email.getText().toString())) {
            et_input_email.setError("Invalid Email");
            requestFocus(et_input_email);
            return;
        }
        if (et_input_full_name.getText().toString().startsWith(" ")) {
            et_input_full_name.setError("Space In Start And End Is Not Allowed");
            requestFocus(et_input_full_name);
            return;
        }

        if (!validatePhone()) {
            return;
        }
        if (reg_type.equals("Register As")) {
            Toast.makeText(getActivity(), "Please select registration type", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!validatePassword()) {
            return;
        }
        if (!validatePasswordConfirmation()) {
            return;
        }
        if (NetworkConnectivityClass.isNetworkAvailable(getActivity())) {
            //checkIfNumberExist();
            getLocationAndSendData();
        } else {
            Snackbar.make(getActivity().findViewById(android.R.id.content), "Internet Not Connected",
                    Snackbar.LENGTH_SHORT).show();
        }
    }


    private boolean validatePassword() {
        if (et_input_password.getText().toString().trim().isEmpty()) {
            et_input_password.setError("Password Cannot Be Empty");
            requestFocus(et_input_password);
            return false;
        }
        if (et_input_password.getText().toString().length() < 7) {
            et_input_password.setError("Password Length Cannot Less Then 6");
            requestFocus(et_input_password);
            return false;
        }
        return true;
    }


    private boolean validatePasswordConfirmation() {
        if (et_input_confirm_password.getText().toString().trim().isEmpty()) {
            et_input_confirm_password.setError("Confirm Password Cannot Be Empty");
            requestFocus(et_input_confirm_password);
            return false;
        } else if (!et_input_password.getText().toString().equals(et_input_confirm_password.getText().toString())) {
            et_input_confirm_password.setError("Password Not Match");
            requestFocus(et_input_confirm_password);
            return false;
        }
        return true;
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
            et_input_phone.setError("Feild Cannot Be Empty");
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

    public void checkIfNumberExist(final Double lat, final Double lng) {
        //ProgressDialogClass.showProgress(getActivity());

        final String name = et_input_full_name.getText().toString();
        final String pass = et_input_confirm_password.getText().toString();

        // Toast.makeText(getActivity(), "" + name + "" + pass, Toast.LENGTH_SHORT).show();

        phone = et_input_phone.getText().toString();
        if (phone.length() == 10) {
            phone = "+92" + phone;
        } else {
            phone = "+92" + phone.substring(1);
        }

        if (byteArray != null) {

            Bundle bundle = new Bundle();
            bundle.putString("name", name);
            bundle.putString("phone", phone);
            bundle.putString("type", reg_type);
            bundle.putString("pass", pass);
            bundle.putString("lat", lat.toString());
            bundle.putString("lng", lng.toString());
            bundle.putByteArray("image", byteArray);
            bundle.putString("email", et_input_email.getText().toString());
            Fragment fragmentName = null;
            Fragment SignUpVerification = new SignUpVerification();
            fragmentName = SignUpVerification;
            fragmentName.setArguments(bundle);
            FragmentReplace.replaceFragment(getActivity(), fragmentName, R.id.content_frame_signin);
        } else {
            Toast.makeText(getActivity(), "Please Select Image", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_sign_in:
                Fragment fragmentName = null;
                Fragment SignInFragment = new SignInFragment();
                fragmentName = SignInFragment;
                FragmentReplace.replaceFragment(getActivity(), fragmentName, R.id.content_frame_signin);
                break;
            case R.id.btn_next:
                submit();
                break;
            case R.id.img_profile_image:
                startDialog();
                break;
        }
    }

    private void startDialog() {
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(
                getActivity());
        myAlertDialog.setTitle("Upload Pictures Option");
        myAlertDialog.setMessage("How do you want to set your picture?");

        myAlertDialog.setPositiveButton("Gallery",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(pickPhoto, 1);

                    }
                });

        myAlertDialog.setNegativeButton("Camera",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                        Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(takePicture, 0);
                    }
                });
        myAlertDialog.show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode) {
            case 0:
                if (resultCode == getActivity().RESULT_OK) {

                    bitmap = (Bitmap) imageReturnedIntent.getExtras().get("data");
                    bStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, bStream);
                    byteArray = bStream.toByteArray();
                    img_profile_image.setImageBitmap(bitmap);

                }

                break;
            case 1:
                if (resultCode == getActivity().RESULT_OK) {
                    Uri selectedImage = imageReturnedIntent.getData();
//                    img_profile_image.setImageURI(selectedImage);

                    bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    bStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, bStream);
                    byteArray = bStream.toByteArray();
                    img_profile_image.setImageBitmap(bitmap);

                }
                break;
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}