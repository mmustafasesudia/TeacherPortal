package com.android.tutorapp.Others;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.tutorapp.Chat.ChatFragment;
import com.android.tutorapp.Chat.ChatRoomFragment;
import com.android.tutorapp.JobPost;
import com.android.tutorapp.Notification.MyFirebaseMessagingService;
import com.android.tutorapp.Profile.StudentOrgProfile;
import com.android.tutorapp.Profile.TutorProfileView;
import com.android.tutorapp.R;
import com.android.tutorapp.SearchTutorMain.SearchTutor;
import com.android.tutorapp.StarterPack.SignInSignUpActivity;
import com.android.tutorapp.Utills.ConfigURL;
import com.android.tutorapp.Utills.FragmentReplace;
import com.android.tutorapp.Utills.ProgressDialogClass;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class Drawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    NavigationView navigationView;
    TextView tv_nav_head_name, tv_nav_mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        tv_nav_head_name = navigationView.getHeaderView(0).findViewById(R.id.tv_name);
        tv_nav_head_name.setText(ConfigURL.getName(this));

        tv_nav_mobile = navigationView.getHeaderView(0).findViewById(R.id.tv_mobile);
        tv_nav_mobile.setText(ConfigURL.getMobileNumber(this));

        //NOtification get Intent Data
        Intent intent = getIntent();
        String msg_id = "" + intent.getStringExtra("msg_id");
        String message = "" + intent.getStringExtra("message");
        String sender_num = "" + intent.getStringExtra("sender_num");

        Log.v("From ", " " + msg_id + " " + message + " " + sender_num);

        if (message == null || message.isEmpty() || message.equals("null")) {
            Fragment fragmentName = null;
            Fragment SearchTutor = new SearchTutor();
            fragmentName = SearchTutor;
            FragmentReplace.replaceFragment(Drawer.this, fragmentName, R.id.fragment_container_drawer);
            MyFirebaseMessagingService.cancelNotification(this);

        } else if (message != "null" && !message.equals("You have a request")) {
            Fragment fragmentName = null;
            Fragment ChatRoomFragment = new ChatRoomFragment();
            Bundle bundle = new Bundle();
            bundle.putString("phone", "" + sender_num);
            fragmentName = ChatRoomFragment;
            fragmentName.setArguments(bundle);
            FragmentReplace.replaceFragment(this, fragmentName, R.id.fragment_container_drawer);
            MyFirebaseMessagingService.cancelNotification(this);
        } else if (message != "null" && message.equals("You have a request")) {
            Fragment fragmentName = null;
            Fragment RequestRecieved = new RequestRecieved();
            Bundle bundle = new Bundle();
            bundle.putString("msg_id", "" + msg_id);
            fragmentName = RequestRecieved;
            fragmentName.setArguments(bundle);
            FragmentReplace.replaceFragment(this, fragmentName, R.id.fragment_container_drawer);
            MyFirebaseMessagingService.cancelNotification(this);

        } else if (message != "null" && message.equals("Your request accepted")) {
            //sender_num
            sendMessage("This is " + ConfigURL.getName(this), sender_num);
        }

        if (ConfigURL.getType(this).equals("STUDENT")) {
            hideItem();
        }
        /*if (ConfigURL.getType(this).equals("ORGANIZATION")) {
            hideItemPostJobs();
        }*/

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Fragment fragmentName = null;
            Fragment SearchTutor = new SearchTutor();
            fragmentName = SearchTutor;
            FragmentReplace.replaceFragment(Drawer.this, fragmentName, R.id.fragment_container_drawer);

        } else if (id == R.id.nav_chat) {
            Fragment fragmentName = null;
            Fragment ChatFragment = new ChatFragment();
            fragmentName = ChatFragment;
            FragmentReplace.replaceFragment(Drawer.this, fragmentName, R.id.fragment_container_drawer);

        } else if (id == R.id.nav_profile) {
            if (ConfigURL.getType(this).equals("TEACHER")) {
                Fragment fragmentName = null;
                Fragment TutorProfileView = new TutorProfileView();
                fragmentName = TutorProfileView;
                FragmentReplace.replaceFragment(Drawer.this, fragmentName, R.id.fragment_container_drawer);
            }
            if (!ConfigURL.getType(this).equals("TEACHER")) {
                Fragment fragmentName = null;
                Fragment StudentOrgProfile = new StudentOrgProfile();
                fragmentName = StudentOrgProfile;
                FragmentReplace.replaceFragment(Drawer.this, fragmentName, R.id.fragment_container_drawer);
            }
        } else if (id == R.id.nav_logout) {
            try {
                FirebaseInstanceId.getInstance().deleteInstanceId();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Intent intent = new Intent(Drawer.this, SignInSignUpActivity.class);
            intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TASK | intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            ConfigURL.clearshareprefrence(this);
            finish();

        } else if (id == R.id.nav_pdf_view) {
            if (ConfigURL.getType(this).equals("STUDENT")) {
               /* Intent intent = new Intent(Drawer.this, PDFViewer.class);
                startActivity(intent);*/
            }
            Fragment fragmentName = null;
            Fragment PDFViewerListFragment = new PDFViewerListFragment();
            fragmentName = PDFViewerListFragment;
            FragmentReplace.replaceFragment(Drawer.this, fragmentName, R.id.fragment_container_drawer);
        } else if (id == R.id.nav_post_jobs) {
            if (ConfigURL.getType(this).equals("STUDENT")) {
               /* Intent intent = new Intent(Drawer.this, PDFViewer.class);
                startActivity(intent);*/
            }
            Fragment fragmentName = null;
            Fragment PDFViewerListFragment = new JobPost();
            fragmentName = PDFViewerListFragment;
            FragmentReplace.replaceFragment(Drawer.this, fragmentName, R.id.fragment_container_drawer);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void sendMessage(String msg, final String reciever_num) {
        ProgressDialogClass.showProgress(Drawer.this);
        AndroidNetworking.get(ConfigURL.URL_SEND_MESSAGE)
                .addQueryParameter("msg", msg)
                .addQueryParameter("sender_num", ConfigURL.getMobileNumber(Drawer.this))
                .addQueryParameter("reciever_num", reciever_num)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ProgressDialogClass.hideProgress();
                        try {
                            if (!response.getBoolean("error")) {
                                Fragment fragmentName = null;
                                Fragment ChatRoomFragment = new ChatRoomFragment();
                                Bundle bundle = new Bundle();
                                bundle.putString("phone", "" + reciever_num);
                                fragmentName = ChatRoomFragment;
                                fragmentName.setArguments(bundle);
                                FragmentReplace.replaceFragment(Drawer.this, fragmentName, R.id.fragment_container_drawer);
                            } else {
                                Fragment fragmentName = null;
                                Fragment SearchTutor = new SearchTutor();
                                fragmentName = SearchTutor;
                                FragmentReplace.replaceFragment(Drawer.this, fragmentName, R.id.fragment_container_drawer);
                                MyFirebaseMessagingService.cancelNotification(Drawer.this);
                                Toast.makeText(Drawer.this, "Request Failed", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        ProgressDialogClass.hideProgress();

                    }
                });
    }

    private void hideItem() {
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(R.id.nav_pdf_view).setVisible(true);
    }

    private void hideItemPostJobs() {
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(R.id.nav_post_jobs).setVisible(true);
    }
}
