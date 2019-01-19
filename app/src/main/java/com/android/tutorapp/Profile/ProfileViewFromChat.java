package com.android.tutorapp.Profile;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.tutorapp.R;
import com.android.tutorapp.Utills.ConfigURL;
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

public class ProfileViewFromChat extends AppCompatActivity {

    TextView tv_name, tv_change_pass, tv_address, tv_email;
    ImageView profile_image;
    String phone;
    Double lat, lng;
    Geocoder geocoder;
    List<Address> addresses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view_from_chat);

        getSupportActionBar().setTitle("Profile");

        tv_name = findViewById(R.id.tv_name);
        tv_email = findViewById(R.id.tv_email);
        tv_change_pass = findViewById(R.id.tv_change_pass);
        tv_address = findViewById(R.id.tv_address);
        profile_image = findViewById(R.id.profile_image);


        Intent intent = getIntent();
        if (intent != null) {
            phone = intent.getStringExtra("mobile");
            String type = intent.getStringExtra("type");
        }

        getProfile();

        tv_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ConfigURL.getType(ProfileViewFromChat.this).equals("TEACHER")) {
                    Uri navigationIntentUri = Uri.parse("google.navigation:q=" + tv_address.getText().toString());//creating intent with latlng
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, navigationIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(mapIntent);
                }
            }
        });
    }


    public void getProfile() {
        ProgressDialogClass.showProgress(ProfileViewFromChat.this);
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
                                tv_name.setText("Name : " + jsonObject.getString("Name"));
                                tv_email.setText("Email : " + jsonObject.getString("Email"));
                                lat = Double.parseDouble(jsonObject.getString("Latitude"));
                                lng = Double.parseDouble(jsonObject.getString("Longitude"));
                                address_lat_lng = getAddressNew(jsonObject.getString("Latitude"), jsonObject.getString("Longitude"));
                                tv_address.setText("Address : " + address_lat_lng);


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

        geocoder = new Geocoder(ProfileViewFromChat.this, Locale.getDefault());

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
