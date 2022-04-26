package com.example.easyteamup.Backend;
import static android.app.Notification.DEFAULT_SOUND;
import static android.app.Notification.DEFAULT_VIBRATE;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.widget.RemoteViews;

import com.example.easyteamup.R;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FirebaseMessageReceiver extends FirebaseMessagingService {

    private FirebaseOperations fops;
    private int notificationCount;

    public void onCreate() {
        fops = new FirebaseOperations(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //create notification channels
            NotificationChannel channel = new NotificationChannel("main-channel", "All Notifications", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("All notifications for the EasyTeamUp App.");
            channel.setShowBadge(true);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        notificationCount = 1;
    }


    @Override
    public void
    onMessageReceived(RemoteMessage remoteMessage){
        System.out.println("Notification Received!");
        if(remoteMessage.getNotification() != null){
            showNotification(remoteMessage.getNotification().getTitle(),
                    remoteMessage.getNotification().getBody());
        }
    }

    @Override
    public void onNewToken(String token) {
        fops.updateAuthenticatedUserToken(token);
    }


    private RemoteViews getCustomDesign(String title, String message){
        RemoteViews remoteViews = new RemoteViews(getApplicationContext().getPackageName(),
                R.layout.notification);
        remoteViews.setTextViewText(R.id.title, title);
        remoteViews.setTextViewText(R.id.message, message);
        remoteViews.setImageViewResource(R.id.icon, R.drawable.clock);
        return remoteViews;
    }

    public void showNotification(String title, String message) {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "main-channel")
                .setSmallIcon(R.drawable.clock)
                .setContentTitle(title)
                .setContentText(message)
                .setDefaults(DEFAULT_SOUND | DEFAULT_VIBRATE)
                .setPriority(NotificationCompat.PRIORITY_MAX);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);

// notificationId is a unique int for each notification that you must define
        notificationManagerCompat.notify(notificationCount++, builder.build());
    }


}

//    public void showNotification(String title, String message){
//        System.out.println("title: " + title);
//        System.out.println("message: " + message);
//        Intent intent = new Intent(this, LoginActivity.class);
//        String channel_id = "notification channel";
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(
//                this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(
//                getApplicationContext(), channel_id)
//                .setSmallIcon(R.drawable.clock)
//                .setAutoCancel(true)
//                .setVibrate(new long[]{1111, 1111, 1111, 1111})
//                .setOnlyAlertOnce(true)
//                .setContentIntent(pendingIntent);
//
//        if (Build.VERSION.SDK_INT
//                >= Build.VERSION_CODES.JELLY_BEAN) {
//            builder = builder.setContent(
//                    getCustomDesign(title, message));
//        }
//        else {
//            builder = builder.setContentTitle(title)
//                    .setContentText(message)
//                    .setSmallIcon(R.drawable.clock);
//        }
//
//        NotificationManager notificationManager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        if (Build.VERSION.SDK_INT
//                >= Build.VERSION_CODES.O) {
//            NotificationChannel notificationChannel
//                    = new NotificationChannel(
//                    channel_id, "web_app",
//                    NotificationManager.IMPORTANCE_HIGH);
//            notificationManager.createNotificationChannel(
//                    notificationChannel);
//        }
//
//        notificationManager.notify(0, builder.build());
//    }