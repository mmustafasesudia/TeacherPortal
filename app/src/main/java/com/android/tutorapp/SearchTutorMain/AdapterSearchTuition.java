package com.android.tutorapp.SearchTutorMain;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.tutorapp.Profile.StudentOrgProfile;
import com.android.tutorapp.Profile.TutorProfileView;
import com.android.tutorapp.R;
import com.android.tutorapp.Utills.ConfigURL;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;


public class AdapterSearchTuition extends RecyclerView.Adapter<AdapterSearchTuition.MyViewHolder> {

    private Context acontext;
    private ArrayList<ModelSearchTutor> arrayList;
    private ItemClickListener clickListener;


    public interface ItemClickListener {
        void onClick(View view, int position);
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ModelSearchTutor current = arrayList.get(position);
        holder.tv_t_name.setText(current.getT_name());
        holder.tv_contact.setText(current.getT_mobile_no());
        holder.tv_t_address.setText("" + current.getT_address());
        holder.tv_distance.setText(current.getT_distance());

        String imagePath = current.getT_image();

        if (imagePath.isEmpty()) {
            holder.profile_image.setImageResource(R.drawable.icon_profile_pictures_search_screen);
        } else {
            Picasso.get()
                    .load(imagePath)
                    .placeholder(R.drawable.icon_profile_pictures_search_screen)
                    .into(holder.profile_image);
        }
        float customer;
        try {
            customer = Float.parseFloat(current.getT_rating());
            holder.ratingBar.setRating(customer);

        } catch (NumberFormatException e) {

        }
    }


    public AdapterSearchTuition(Context context, ArrayList<ModelSearchTutor> arrayList) {
        this.arrayList = arrayList;
        this.acontext = context;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_tuition, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public CardView mCardView;
        public TextView tv_t_name, tv_contact, tv_t_address, tv_distance;
        RatingBar ratingBar;
        public ImageView profile_image;
        private Button btn_send_chat;

        public MyViewHolder(View v) {
            super(v);

            mCardView = (CardView) v.findViewById(R.id.card_view_tuition_completed);
            tv_t_name = v.findViewById(R.id.tv_t_name);
            ratingBar = v.findViewById(R.id.tv_rating_tutor);
            tv_t_address = v.findViewById(R.id.tv_t_address);
            profile_image = v.findViewById(R.id.profile_image);
            tv_contact = v.findViewById(R.id.tv_contact);
            tv_distance = v.findViewById(R.id.tv_distance);
            btn_send_chat = v.findViewById(R.id.btn_send_chat);
            if (ConfigURL.getType(acontext).equals("TEACHER")) {
                btn_send_chat.setText("Send Message");
            }
            if (!ConfigURL.getType(acontext).equals("TEACHER")) {
                btn_send_chat.setText("Send Request");
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(acontext, "", Toast.LENGTH_SHORT).show();
                    ModelSearchTutor modelSearchTutor = arrayList.get(getAdapterPosition());
                    if (!ConfigURL.getType(acontext).equals("TEACHER")) {
                        Fragment fragmentName = null;
                        Fragment TutorProfileView = new TutorProfileView();
                        Bundle bundle = new Bundle();
                        bundle.putString("type", "view");
                        bundle.putString("tutorMobile", modelSearchTutor.getT_mobile_no());
                        fragmentName = TutorProfileView;
                        fragmentName.setArguments(bundle);
                        replaceFragment(fragmentName, R.id.fragment_container_drawer);
                    }
                    if (ConfigURL.getType(acontext).equals("TEACHER")) {
                        Fragment fragmentName = null;
                        Fragment StudentOrgProfile = new StudentOrgProfile();
                        Bundle bundle = new Bundle();
                        bundle.putString("type", "view");
                        bundle.putString("mobile", modelSearchTutor.getT_mobile_no());
                        fragmentName = StudentOrgProfile;
                        fragmentName.setArguments(bundle);
                        replaceFragment(fragmentName, R.id.fragment_container_drawer);
                    }
                }
            });

            btn_send_chat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ModelSearchTutor modelSearchTutor = arrayList.get(getAdapterPosition());
                    if (ConfigURL.getType(acontext).equals("TEACHER")) {
                        sendMessage(modelSearchTutor.getT_mobile_no(), "Hi, this is " + ConfigURL.getName(acontext) + "");
                    }
                    if (!ConfigURL.getType(acontext).equals("TEACHER")) {
                        sendRequest(modelSearchTutor.getT_mobile_no());
                    }
                }
            });
            //itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onClick(view, getAdapterPosition());
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public void replaceFragment(Fragment fragment, int frame) {
        String backStateName = fragment.getClass().getName();
        String fragmentTag = backStateName;

        FragmentManager manager = ((FragmentActivity) acontext).getSupportFragmentManager();
        boolean fragmentPopped = manager.popBackStackImmediate(backStateName, 0);

        if (!fragmentPopped && manager.findFragmentByTag(fragmentTag) == null) { //fragment not in back stack, create it.
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(frame, fragment, fragmentTag);
            ft.setTransition(FragmentTransaction.TRANSIT_ENTER_MASK);
            ft.addToBackStack(backStateName);
            ft.commit();
        }
    }


    public void sendMessage(String phone, String msg) {
        AndroidNetworking.get(ConfigURL.URL_SEND_MESSAGE)
                .addQueryParameter("msg", msg)
                .addQueryParameter("sender_num", ConfigURL.getMobileNumber(acontext))
                .addQueryParameter("reciever_num", phone)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        Toast.makeText(acontext, "Message Sent! ", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                    }
                });
    }

    public void sendRequest(String teacher) {
        AndroidNetworking.post(ConfigURL.URL_SEND_REQUEST)
                .addBodyParameter("student_num", ConfigURL.getMobileNumber(acontext))
                .addBodyParameter("teacher_num", teacher)
                .addBodyParameter("type", ConfigURL.getType(acontext))
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        Toast.makeText(acontext, "Message Sent! ", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                    }
                });
    }


}
