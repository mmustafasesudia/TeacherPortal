package com.android.tutorapp.Profile;


import android.content.Intent;
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
import android.widget.TextView;

import com.android.tutorapp.R;
import com.android.tutorapp.Utills.ConfigURL;
import com.android.tutorapp.Utills.FragmentReplace;
import com.android.tutorapp.Utills.ProgressDialogClass;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class StudentOrgProfile extends Fragment {


    TextView tv_name, tv_change_pass, tv_address, tv_email;
    ImageView profile_image;
    String phone;
    Double lat, lng;
    Geocoder geocoder;
    List<Address> addresses;


    public StudentOrgProfile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_student_org_profile, container, false);


        tv_name = view.findViewById(R.id.tv_name);
        tv_email = view.findViewById(R.id.tv_email);
        tv_change_pass = view.findViewById(R.id.tv_change_pass);
        tv_address = view.findViewById(R.id.tv_address);
        profile_image = view.findViewById(R.id.profile_image);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            String type = bundle.getString("type");
            if (type.equals("view")) {
                tv_change_pass.setVisibility(View.GONE);
            }
            phone = bundle.getString("mobile");
        } else {
            phone = ConfigURL.getMobileNumber(getActivity());
        }


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
        //sahred preference save
        tv_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ConfigURL.getType(getActivity()).equals("TEACHER")) {
                    Uri navigationIntentUri = Uri.parse("google.navigation:q=" + tv_address.getText().toString());//creating intent with latlng
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, navigationIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(mapIntent);
                }
            }
        });
        return view;
    }

    public void getProfile() {
        ProgressDialogClass.showProgress(getActivity());
        AndroidNetworking.get(ConfigURL.URL_STUDENT_ORG_PROFILE_VIEW)
                .addQueryParameter("mobile_num", phone)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Toast.makeText(getActivity(),""+response,Toast.LENGTH_SHORT).show();
                        Log.v("Response", "" + response);
                        String address_lat_lng = "";

                        try {
                            JSONArray jsonArray = response.getJSONArray("Profile");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                tv_name.setText(jsonObject.getString("Name"));
                                tv_email.setText(jsonObject.getString("Email"));
                                lat = Double.parseDouble(jsonObject.getString("Latitude"));
                                lng = Double.parseDouble(jsonObject.getString("Longitude"));

                                try {
                                    address_lat_lng = getAddressNew(jsonObject.getString("Latitude"), jsonObject.getString("Longitude"));
                                    tv_address.setText("" + address_lat_lng);
                                } catch (NullPointerException ex) {

                                }

                                if (jsonObject.getString("Image").isEmpty()) {
                                    profile_image.setImageResource(R.drawable.icon_profile_pictures);
                                } else {
                                    Picasso.get()
                                            .load(jsonObject.getString("Image"))
                                            .placeholder(R.drawable.icon_profile_pictures)
                                            .into(profile_image);
                                }
                            }

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

    public String getAddressNew(String lat, String lng) {
        String address = "";

        geocoder = new Geocoder(getActivity(), Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(Double.parseDouble(lat), Double.parseDouble(lng), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            address = addresses.get(0).getAddressLine(0);
        } catch (IndexOutOfBoundsException ex) {
        }

        return address;
    }


}
