package com.android.tutorapp.Chat;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.tutorapp.R;
import com.android.tutorapp.Utills.ConfigURL;
import com.android.tutorapp.Utills.FragmentReplace;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.rey.material.widget.ProgressView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class ChatFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, AdapterChatFragment.ItemClickListener {


    ArrayList<Message> data;
    SwipeRefreshLayout mSwipeRefreshLayout;
    RecyclerView rv;
    Bundle saveInstanced = new Bundle();
    int i = 0;
    ProgressView circularProgressBar;

    public ChatFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.chat_activity, container, false);

        AndroidNetworking.initialize(getActivity());

        rv = (RecyclerView) view.findViewById(R.id.rv_chat_message);
        rv.setHasFixedSize(true);
        /*if (data != null) {
            Toast.makeText(getActivity(), "", Toast.LENGTH_LONG).show();
        }*/

        getActivity().setTitle("Chat");
        circularProgressBar = (ProgressView) view.findViewById(R.id.circular_progress);

        getData();
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.contentView);
        mSwipeRefreshLayout.setOnRefreshListener(this);


        return view;

    }

    public void getData() {
        circularProgressBar.start();
        // ProgressDialogClass.showProgress(getActivity());
        AndroidNetworking.get(ConfigURL.URL_GET_PERSON_CHAT)
                .addQueryParameter("sender_num", ConfigURL.getMobileNumber(getActivity()))
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        //Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();
                        refreshItems();
                        circularProgressBar.stop();
                        //  ProgressDialogClass.hideProgress();
                        data = new ArrayList<>();
                        try {
                            JSONArray jsonArray = response.getJSONArray("chat");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                String name, mobile, msg_id, msg_key, msg, time;
                                Message message = new Message(jsonArray.getJSONObject(i));

                                name = message.getName();
                                mobile = message.getMobile_no();
                                msg = message.getMessage();
                                msg_id = message.getMessage_id();
                                msg_key = message.getMessage_key();
                                time = message.getDateTime();

                                Message obj = new Message(time, msg, name, mobile, msg_id, msg_key);
                                data.add(obj);

                                Log.v("Message", "" + name + "" + mobile + "" + msg + "" + msg_id + "" + msg_key + "" + time);
                            }
                            saveInstanced.putSerializable("arrayList", data);
                            AdapterChatFragment adapter = new AdapterChatFragment(getActivity(), data);
                            rv.setAdapter(adapter);
                            adapter.setClickListener(ChatFragment.this);

                        } catch (JSONException e) {
                            refreshItems();
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        refreshItems();
                        // ProgressDialogClass.hideProgress();
                    }
                });

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
    }


    @Override
    public void onRefresh() {
        getData();
    }

    public void refreshItems() {
        onItemsLoadComplete();
    }

    public void onItemsLoadComplete() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onClick(View view, int position) {

        Fragment fragmentName = null;
        Fragment ChatRoomFragment = new ChatRoomFragment();
        Bundle bundle = new Bundle();
        bundle.putString("phone", "" + data.get(position).getMobile_no());
        bundle.putString("name", "" + data.get(position).getName());
        fragmentName = ChatRoomFragment;
        fragmentName.setArguments(bundle);
        FragmentReplace.replaceFragment(getActivity(), fragmentName, R.id.fragment_container_drawer);

    }


}