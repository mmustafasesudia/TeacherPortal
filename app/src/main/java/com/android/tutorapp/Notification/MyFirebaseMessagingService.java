package com.android.tutorapp.Notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.android.tutorapp.Others.Drawer;
import com.android.tutorapp.R;
import com.android.tutorapp.Utills.ConfigURL;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

public class MyFirebaseMessagingService extends FirebaseMessagingService {


    private static final String TAG = "FCM Service";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.v(TAG, "From: " + remoteMessage.getData().toString());
        try {
            JSONObject notificationObject = new JSONObject(remoteMessage.getData().toString());
            addNotification(notificationObject.getJSONObject("data"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    static void updateMyActivity(Context context, String message, String mobile, String msg_id) {

        Intent intent = new Intent(ConfigURL.PUSH_NOTIFICATION);
        intent.putExtra("message", message);
        intent.putExtra("mobile", mobile);
        intent.putExtra("msg_id", msg_id);
        context.sendBroadcast(intent);
    }

    public static void cancelNotification(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(0);
    }

    public void addNotification(JSONObject notificationObject) throws JSONException {


        NotificationManager mNotificationManager;

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(getApplicationContext(), "notify_001");
        Intent ii = new Intent(getApplicationContext(), Drawer.class);

        String msg, sender_num, msg_id, type,time;
        msg_id = notificationObject.getString("msg_id");
        msg = notificationObject.getString("message");
        sender_num = notificationObject.getString("sender_num");
        time = notificationObject.getString("time");


        ii.putExtra("msg_id", "" + msg_id);
        ii.putExtra("message", "" + msg);
        ii.putExtra("sender_num", "" + sender_num);
        ii.putExtra("time", "" + time);

        Log.v("From : ", "Message : " + msg + " Request Id : " + msg_id + " Sender Number : " + sender_num);

        //PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, ii, 0,PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        ii,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher_round);
        mBuilder.setContentTitle(notificationObject.getString("senderName"));
        mBuilder.setContentText(notificationObject.getString("message"));
        mBuilder.setPriority(Notification.PRIORITY_MAX);
        // mBuilder.setStyle(bigText);

        mBuilder.setSound(Settings.System.DEFAULT_RINGTONE_URI);

        mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("notify_001",
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            mNotificationManager.createNotificationChannel(channel);
        }

        mNotificationManager.notify(0, mBuilder.build());

        updateMyActivity(this, msg, sender_num, msg_id);
    }
}
