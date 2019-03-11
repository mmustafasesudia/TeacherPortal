package com.android.tutorapp.Chat;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.tutorapp.FeedbackActivity;
import com.android.tutorapp.Profile.ProfileViewFromChat;
import com.android.tutorapp.R;
import com.android.tutorapp.Utills.ConfigURL;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.rey.material.widget.ProgressView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatRoomFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "ChatFragment";
    public ChatRoomAdapter chat_room_adapter;
    public ListView chat_room_list_view;
    private EditText chat_room_et_message;
    private Button chat_room_btn_send;
    private boolean side = false;
    String phone, name;
    ProgressView circularProgressBar;
    private boolean feedbackIsTaken = false;
    private String teacher_num = "";


    public Dialog d;


    public ChatRoomFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.chat_room_activity, container, false);

        setHasOptionsMenu(true);


        Bundle bundle = getArguments();
        if (bundle != null) {
            phone = (String) bundle.get("phone");
            if (name != null) {
                name = (String) bundle.get("name");
                getActivity().setTitle("" + name);
            } else
                getActivity().setTitle("" + phone);

        }


        circularProgressBar = view.findViewById(R.id.circular_progress);

        chat_room_btn_send = view.findViewById(R.id.chat_room_btn_send);
        chat_room_list_view = view.findViewById(R.id.chat_room_list_view);
        chat_room_et_message = view.findViewById(R.id.chat_room_et_message);
        chat_room_adapter = new ChatRoomAdapter(getActivity(), R.layout.chat_incoming_msg);
        chat_room_list_view.setAdapter(chat_room_adapter);

        viewAllMessage();


        chat_room_btn_send.setOnClickListener(this);

        chat_room_list_view.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        chat_room_list_view.setAdapter(chat_room_adapter);

        //to scroll the list view to bottom on data change
        chat_room_adapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                chat_room_list_view.setSelection(chat_room_adapter.getCount() - 1);
            }
        });

        return view;
    }

    private boolean sendNormalMessage() {
        chat_room_adapter.add(new ChatMessage(side, chat_room_et_message.getText().toString(), getCurrentTimeUsingDate()));
        sendMessage(chat_room_et_message.getText().toString());
        chat_room_et_message.setText("");
        //side = !side;
        return true;
    }

    private boolean showAllMessage(Boolean side, String msg, String date, String feedbackGiven) {
        chat_room_adapter.add(new ChatMessage(side, msg, date, feedbackGiven));
        return true;
    }

    public String getCurrentTimeUsingDate() {
        Date date = new Date();
        String strDateFormat = "MMM d, h:mm a";
        DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
        String formattedDate = dateFormat.format(date);
        System.out.println("Current time of the day using Date - 12 hour format: " + formattedDate);
        return formattedDate;
    }


    public void sendMessage(String msg) {
        AndroidNetworking.get(ConfigURL.URL_SEND_MESSAGE)
                .addQueryParameter("msg", msg)
                .addQueryParameter("sender_num", ConfigURL.getMobileNumber(getActivity()))
                .addQueryParameter("reciever_num", phone)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                    }
                });
    }

    public void viewAllMessage() {
        //Toast.makeText(getActivity(), "" + ConfigURL.getMobileNumber(getActivity()) + "   " + phone + "", Toast.LENGTH_LONG).show();
        circularProgressBar.start();
        AndroidNetworking.get(ConfigURL.URL_LIST_OF_SINGLE_CHAT)
                .addQueryParameter("user1_num", ConfigURL.getMobileNumber(getActivity()))
                .addQueryParameter("user2_num", phone)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.v("RESP", "" + response);
                        // do anything with response
                        circularProgressBar.stop();
                        try {
                            JSONArray commentsObj = response.getJSONArray("chat");
                            for (int i = 0; i < commentsObj.length(); i++) {
                                JSONObject commentObj = (JSONObject) commentsObj.get(i);

                                String senderName = commentsObj.getJSONObject(i).getString("sender");
                                String reciever = commentsObj.getJSONObject(i).getString("reciever");
                                String msg = commentsObj.getJSONObject(i).getString("message");
                                String time = commentsObj.getJSONObject(i).getString("time");
                                String senderPhoneNo = commentsObj.getJSONObject(i).getString("sender_num");
                                String msgId = commentsObj.getJSONObject(i).getString("message_id");
                                String feedbackGiven = commentsObj.getJSONObject(i).getString("feedbackexist");
                                // Log.v("HHHHHHH", "" + msg);

                                feedbackIsTaken = !feedbackGiven.equals("No");
                                if (ConfigURL.getType(getActivity()).equals("STUDENT")) {
                                    if (!senderPhoneNo.equals(ConfigURL.getMobileNumber(getActivity()))) {
                                        teacher_num = senderPhoneNo;
                                    }
                                }

                                //Number Self False
                                //Number Other True
                                if (senderPhoneNo.equals(ConfigURL.getMobileNumber(getActivity())) && !msg.equals("null")) {
                                    showAllMessage(false, msg, time, feedbackGiven);
                                } else if (!senderPhoneNo.equals(ConfigURL.getMobileNumber(getActivity())) && !msg.equals("null")) {
                                    showAllMessage(true, msg, time, feedbackGiven);
                                }
                                //TODO View chat messages

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.chat_room_btn_send:
                if (!chat_room_et_message.getText().toString().isEmpty()) {
                    sendNormalMessage();
                } else {
                    Toast.makeText(getActivity(), "Message cannot be empty", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(mMessageReceiver, new IntentFilter(ConfigURL.PUSH_NOTIFICATION));

    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(mMessageReceiver);

    }


    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Extract data included in the Intent
            String message = intent.getStringExtra("message");
            String mobile = intent.getStringExtra("mobile");

            if (phone.equals(mobile)) {
                chat_room_adapter.add(new ChatMessage(true, message, getCurrentTimeUsingDate()));
                chat_room_adapter.notifyDataSetChanged();
                //MyFirebaseMessagingService.cancelNotification(getActivity());
            }
            // Toast.makeText(getActivity(), "" + utype, Toast.LENGTH_SHORT).show();

            //do other stuff here
        }
    };


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.chat_profile, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        if (ConfigURL.getType(getActivity()).equals("TEACHER")) {
            menu.findItem(R.id.action_view_profile).setVisible(true);
            menu.findItem(R.id.action_feedback).setVisible(false);
        }
        if (ConfigURL.getType(getActivity()).equals("STUDENT")) {
            menu.findItem(R.id.action_feedback).setVisible(true);
            menu.findItem(R.id.action_view_profile).setVisible(false);
        }
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_view_profile) {

            Intent intent = new Intent(getActivity(), ProfileViewFromChat.class);
            intent.putExtra("type", "view");
            intent.putExtra("mobile", phone);
            startActivity(intent);

            return true;
        }
        if (id == R.id.action_feedback) {
            if (feedbackIsTaken) {
                Toast.makeText(getActivity(), "Feedback Already Submitted", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(getActivity(), FeedbackActivity.class);
                intent.putExtra("num", "" + teacher_num);
                startActivity(intent);
                //Toast.makeText(getActivity(), "Hello Feedback", Toast.LENGTH_SHORT).show();
            }
        }

        return super.onOptionsItemSelected(item);
    }


}