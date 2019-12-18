package com.ayra.pdamsurakarta.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.ayra.pdamsurakarta.R;
import com.ayra.pdamsurakarta.manager.SharedPreferencesManager;
import com.ayra.pdamsurakarta.ui.LoginActivity;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    // Object
    SharedPreferencesManager preferencesManager = SharedPreferencesManager.getInstance(this);
    NotificationManager notificationManager;
    Intent intent;
    NotificationCompat.Builder builder;


    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(
                instanceIdResult -> preferencesManager.saveTokenFCM(instanceIdResult.getToken()));
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String dataUser = preferencesManager.loadUserPass();
        Log.d("TAG", "onMessageReceived: " + dataUser);
        intent = new Intent(this, LoginActivity.class);
        intent.putExtra("id", remoteMessage.getData().get("id"));
        intent.putExtra("notif", "login");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Decode Icon From Drawable to Notification
        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.logo);

        builder = new NotificationCompat.Builder(this);
        builder.setPriority(Notification.PRIORITY_DEFAULT)
                .setContentTitle(remoteMessage.getData().get("title"))
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setColor(getResources().getColor(R.color.colorPrimary))
                .setSmallIcon(R.drawable.logo, 2)
                .setLargeIcon(largeIcon)
                .setContentText(remoteMessage.getData().get("description"))
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setContentIntent(pendingIntent)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(remoteMessage.getData().get("description")))
                .setAutoCancel(true);
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel("PROMOTION", "Promotion", NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.enableLights(true);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription("promotion and discount");

            builder.setChannelId("PROMOTION");
            notificationManager.createNotificationChannel(notificationChannel);
        }

        if (notificationManager != null) {
            notificationManager.notify(0, builder.build());
        }
    }
}
