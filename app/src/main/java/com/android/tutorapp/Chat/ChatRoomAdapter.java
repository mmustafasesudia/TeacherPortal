package com.android.tutorapp.Chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.tutorapp.R;

import java.util.ArrayList;
import java.util.List;

class ChatRoomAdapter extends ArrayAdapter<ChatMessage> {

    private TextView chatText, time;
    private List<ChatMessage> chatMessageList = new ArrayList<ChatMessage>();
    public Context context;
    private String offer = "N";

    public ChatRoomAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        this.context = context;
        notifyDataSetChanged();
    }

    @Override
    public void add(ChatMessage object) {
        chatMessageList.add(object);
        super.add(object);
    }

    public int getCount() {
        return this.chatMessageList.size();
    }

    public ChatMessage getItem(int index) {
        return this.chatMessageList.get(index);
    }

    public View getView(final int position, final View convertView, ViewGroup parent) {

        final ChatMessage chatMessageObj = getItem(position);
        View row = convertView;
        LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (chatMessageObj.left) {
            row = inflater.inflate(R.layout.chat_incoming_msg, parent, false);

        } else {
            row = inflater.inflate(R.layout.chat_outgoing_msg, parent, false);

        }
        chatText = (TextView) row.findViewById(R.id.textview_message);
        time = (TextView) row.findViewById(R.id.textview_time);

        /*if msg type is offered and outgoing then go in this */

        chatText.setText(chatMessageObj.message);
        //DateFormat dateFormat = new SimpleDateFormat("h:mm a");
        time.setText(chatMessageObj.date);

        return row;
    }

}






























