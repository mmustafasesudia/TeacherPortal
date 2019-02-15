package com.android.tutorapp.SearchTutorMain;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.tutorapp.R;
import com.android.tutorapp.Utills.ConfigURL;
import com.android.tutorapp.Utills.FragmentReplace;
import com.android.tutorapp.Utills.ProgressDialogClass;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.hootsuite.nachos.NachoTextView;
import com.hootsuite.nachos.chip.Chip;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.android.tutorapp.Utills.ConfigURL.Filterbudget;
import static com.android.tutorapp.Utills.ConfigURL.Filtercourse;
import static com.android.tutorapp.Utills.ConfigURL.Filterday;
import static com.android.tutorapp.Utills.ConfigURL.Filtertime;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchTutor extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, AdapterSearchTuition.ItemClickListener {


    FusedLocationProviderClient mFusedLocationClient;

    NachoTextView nachoTextView;
    String[] suggestions = new String[]{};
    ArrayList<String> stringArrayList = new ArrayList<String>();
    ArrayList<String> stringChipList = new ArrayList<String>();
    TextView tv_search;
    ArrayList<ModelSearchTutor> data;
    SwipeRefreshLayout mSwipeRefreshLayout;
    RecyclerView rv;
    Geocoder geocoder;
    List<Address> addresses;


    public SearchTutor() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_tutor, container, false);
        setHasOptionsMenu(false);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        //Bundle bundle = this.getArguments().getBundle("");

        //Filter Screen
        if (!Filtercourse.equals("")) {
            String courseFilter = "" + Filtercourse;
            String day = "" + Filterday;
            String time = "" + Filtertime;
            String budget = "" + Filterbudget;

            getListByLocationFilter(courseFilter, day, time, budget);
        } else {
//NNormal
            getListByLocation();
        }
        getActivity().setTitle("Home");

        rv = (RecyclerView) view.findViewById(R.id.rv_orders);
        rv.setHasFixedSize(true);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.contentView);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        nachoTextView = view.findViewById(R.id.nacho_text_view);
        tv_search = view.findViewById(R.id.tv_search);
        tv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragmentName = null;
                Fragment FilterFragment = new FilterFragment();
                fragmentName = FilterFragment;
                FragmentReplace.replaceFragment(getActivity(), fragmentName, R.id.fragment_container_drawer);

            }
        });
        //loadData();


        nachoTextView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (nachoTextView.getRight() - nachoTextView.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        getAllChips();
                        return true;
                    }
                }
                return false;
            }
        });


        if (ConfigURL.getType(getActivity()).equals("TEACHER")) {
            tv_search.setVisibility(View.GONE);
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // getCourseList();

    }

    public void getAllChips() {
        stringChipList.clear();
        int i = 0;
        for (Chip chip : nachoTextView.getAllChips()) {
            i++;
            // Do something with the text of each chip
            CharSequence text = chip.getText();
            stringChipList.add(text.toString());
            // Do something with the data of each chip (this data will be set if the chip was created by tapping a suggestion)
            Object data = chip.getData();
        }

        String idList = stringChipList.toString();
        String csv = idList.substring(1, idList.length() - 1).replace(", ", "','");
        String courseList = "'" + csv + "'";
        getListByLocation(String.valueOf(i), courseList);
        i = 0;
    }

    @SuppressLint("MissingPermission")
    public void getListByLocation() {

        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            loadData(location.getLatitude(), location.getLongitude());
                        }
                    }
                });
    }


    @Override
    public void onRefresh() {
        getListByLocation();
    }


    public void loadData(Double lat, Double lng) {
        ProgressDialogClass.showProgress(getActivity());
        AndroidNetworking.get(ConfigURL.URL_GET_ALL_TUTORS)
                .addQueryParameter("latitude", String.valueOf(lat))
                .addQueryParameter("longitude", String.valueOf(lng))
                .addQueryParameter("user_num", ConfigURL.getMobileNumber(getActivity()))
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        refreshItems();
                        data = new ArrayList<>();
                        Log.v("AA Response", "" + response);

                        try {
                            JSONArray jsonArray = response.getJSONArray("Tutors");
                            for (int i = 0; i < jsonArray.length(); i++) {

                                String instr, image, dist, rating, mobile, known, lat, lng;

                                ModelSearchTutor active_tuitions = new ModelSearchTutor(jsonArray.getJSONObject(i));

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                instr = active_tuitions.getT_name();
                                image = active_tuitions.getT_image();
                                dist = active_tuitions.getT_distance();
                                rating = active_tuitions.getT_rating();
                                mobile = active_tuitions.getT_mobile_no();
                                lat = active_tuitions.getT_lat();
                                lng = active_tuitions.getT_lng();

                                ModelSearchTutor obj = new ModelSearchTutor(instr, mobile, rating, image, dist,lat,lng);
                                data.add(obj);

                            }
                            AdapterSearchTuition adapter = new AdapterSearchTuition(getActivity(), data);
                            rv.setAdapter(adapter);
                            //adapter.setClickListener(SearchTutor.this);


                        } catch (JSONException e) {
                            refreshItems();
                            e.printStackTrace();
                        }
                        ProgressDialogClass.hideProgress();

                    }

                    @Override
                    public void onError(ANError anError) {
                        //refreshItems();
                        ProgressDialogClass.hideProgress();

                    }
                });

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
    }

    public void refreshItems() {
        onItemsLoadComplete();
    }

    public void onItemsLoadComplete() {
        mSwipeRefreshLayout.setRefreshing(false);
    }


    public void getListByCourses(Double lat, Double lng, String count, String course) {
        ProgressDialogClass.showProgress(getActivity());
        AndroidNetworking.get(ConfigURL.URL_GET_ALL_TUTORS)
                .addQueryParameter("latitude", String.valueOf(lat))
                .addQueryParameter("longitude", String.valueOf(lng))
                .addQueryParameter("student_num", ConfigURL.getMobileNumber(getActivity()))
                .addQueryParameter("NumOfCourse", count)
                .addQueryParameter("Course", course)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        refreshItems();
                        ProgressDialogClass.hideProgress();
                        data = new ArrayList<>();
                        Log.v("AA Response", "" + response);

                        try {
                            JSONArray jsonArray = response.getJSONArray("Tutors");
                            for (int i = 0; i < jsonArray.length(); i++) {

                                String instr, image, dist, rating, mobile;

                                ModelSearchTutor active_tuitions = new ModelSearchTutor(jsonArray.getJSONObject(i));

                                instr = active_tuitions.getT_name();
                                image = active_tuitions.getT_image();
                                dist = active_tuitions.getT_distance();
                                rating = active_tuitions.getT_rating();
                                mobile = active_tuitions.getT_mobile_no();

                                ModelSearchTutor obj = new ModelSearchTutor(instr, mobile, rating, image, dist);
                                data.add(obj);

                            }
                            AdapterSearchTuition adapter = new AdapterSearchTuition(getActivity(), data);
                            rv.setAdapter(adapter);
                            //adapter.setClickListener();


                        } catch (JSONException e) {
                            refreshItems();
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        refreshItems();
                        ProgressDialogClass.hideProgress();

                    }
                });

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
    }

    public void getListByLocation(final String count, final String course) {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations, this can be null.
                        if (location != null) {
                            getListByCourses(location.getLatitude(), location.getLongitude(), count, course);
                        }
                    }
                });
    }

    @SuppressLint("MissingPermission")
    public void getListByLocationFilter(final String course, final String day, final String time, final String budget) {
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations, this can be null.
                        if (location != null) {
                            getListBy(location.getLatitude(), location.getLongitude(), course, budget, time, day);
                        }
                    }
                });
    }

    public void getListBy(Double lat, Double lng, String course, String budget, String schedule, String day) {
        ProgressDialogClass.showProgress(getActivity());
        AndroidNetworking.get(ConfigURL.URL_GET_ALL_TUTORS_FILTER)
                .addQueryParameter("latitude", String.valueOf(lat))
                .addQueryParameter("longitude", String.valueOf(lng))
                .addQueryParameter("Course", course)
                .addQueryParameter("Budget", budget)
                .addQueryParameter("Schedule", schedule)
                .addQueryParameter("Day", day)
                .addQueryParameter("pMobile", ConfigURL.getMobileNumber(getActivity()))
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Filtercourse = "";
                        Filterday = "";
                        Filtertime = "";
                        Filterbudget = "";

                        refreshItems();
                        ProgressDialogClass.hideProgress();
                        data = new ArrayList<>();
                        Log.d("AA Response", "" + response);

                        try {
                            JSONArray jsonArray = response.getJSONArray("Tutors");
                            for (int i = 0; i < jsonArray.length(); i++) {

                                String instr, image, dist, rating, mobile, known, lat, lng;

                                ModelSearchTutor active_tuitions = new ModelSearchTutor(jsonArray.getJSONObject(i));

                                instr = active_tuitions.getT_name();
                                image = active_tuitions.getT_image();
                                dist = active_tuitions.getT_distance();
                                rating = active_tuitions.getT_rating();
                                mobile = active_tuitions.getT_mobile_no();


                                ModelSearchTutor obj = new ModelSearchTutor(instr, mobile, rating, image, dist);
                                data.add(obj);

                            }
                            AdapterSearchTuition adapter = new AdapterSearchTuition(getActivity(), data);
                            rv.setAdapter(adapter);
                            //adapter.setClickListener();


                        } catch (JSONException e) {
                            refreshItems();
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        refreshItems();
                        ProgressDialogClass.hideProgress();

                    }
                });

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
    }

    public String getAddressNew(Double lat, Double lng) {
        String address = "";

        geocoder = new Geocoder(getActivity(), Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(lat, lng, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            e.printStackTrace();
        }
        address = addresses.get(0).getAddressLine(0);

        return address;
    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void onClick(View view, int position) {

    }
}
