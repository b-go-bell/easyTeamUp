package com.example.easyteamup.Backend;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.widget.RemoteViews;

import com.example.easyteamup.R;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FirebaseMessageReceiver extends FirebaseMessagingService {

    FirebaseOperations fops;

    public void onCreate() {
        fops = new FirebaseOperations(this);
    }


    @Override
    public void
    onMessageReceived(RemoteMessage remoteMessage){
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
        System.out.println("title: " + title);
        System.out.println("message: " + message);

        CharSequence name = "test channel";
        String description = "description channel";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel("test-id", name, importance);
        channel.setDescription(description);
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "test-id")
                .setSmallIcon(R.drawable.clock)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);

// notificationId is a unique int for each notification that you must define
        notificationManagerCompat.notify(69, builder.build());
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