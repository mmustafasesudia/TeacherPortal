package com.android.tutorapp.Chat;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.tutorapp.R;

import java.util.ArrayList;

public class AdapterChatFragment extends RecyclerView.Adapter<AdapterChatFragment.MyViewHolder> {

    public final Context acontext;
    private ArrayList<Message> arrayList;
    private AdapterChatFragment.ItemClickListener clickListener;


    public interface ItemClickListener {
        void onClick(View view, int position);
    }

    public void setClickListener(AdapterChatFragment.ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public AdapterChatFragment(Context context, ArrayList<Message> arrayList) {
        this.arrayList = arrayList;
        acontext = context;
    }

    @Override
    public AdapterChatFragment.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_room_card_item, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Message current = arrayList.get(position);
        holder.tv_name_person.setText(current.getName());
        holder.tv_message_person.setText(current.getMessage());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public CardView mCardView;
        public TextView tv_name_person, tv_message_person;


        public MyViewHolder(View v) {
            super(v);
            mCardView = (CardView) v.findViewById(R.id.card_view_chat_room);
            tv_name_person = (TextView) v.findViewById(R.id.tv_name);
            tv_message_person = (TextView) v.findViewById(R.id.tv_message);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (clickListener != null) clickListener.onClick(v, getAdapterPosition());

        }
    }

}
