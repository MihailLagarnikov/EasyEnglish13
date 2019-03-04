package ru.lagarnikov.easyenglish13.notification;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import androidx.core.app.NotificationCompat;
import ru.lagarnikov.easyenglish13.MainActivity;
import ru.lagarnikov.easyenglish13.R;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //Get notification manager to manage/send notifications


        //Intent to invoke app when click on notification.
        //In this sample, we want to start/launch this sample app when user clicks on notification
        Intent intentToRepeat = new Intent(context, MainActivity.class);
        //set flag to restart/relaunch the app
        intentToRepeat.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        //Pending intent to handle launch of Activity in intent above
        PendingIntent pendingIntent =
                PendingIntent.getActivity(context, NotificationHelper.ALARM_TYPE_RTC, intentToRepeat, PendingIntent.FLAG_UPDATE_CURRENT);

        //Build notification
        Notification repeatedNotification = buildLocalNotification(context, pendingIntent).build();

        //Send local notification
        NotificationHelper.getNotificationManager(context).notify(NotificationHelper.ALARM_TYPE_RTC, repeatedNotification);
    }

    public NotificationCompat.Builder buildLocalNotification(Context context, PendingIntent pendingIntent) {

        /*
        // Create PendingIntent
        val resultIntent:Intent = Intent(context, MainActivity::class.java)
        val resultPendingIntent = PendingIntent.getActivity(
            context, 0, resultIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

// Create Notification
        val builder = NotificationCompat.Builder(context)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Title")
            .setContentText("Notification text")
            .setContentIntent(resultPendingIntent)
            .setAutoCancel(true)

        val notification = builder.build()

// Show Notification
        val notificationManager = context!!.getSystemService(NOTIFICATION_SERVICE) as NotificationManager?
        notificationManager!!.notify(1, notification)*/

        //Define sound URI
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        NotificationCompat.Builder builder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                        .setContentIntent(pendingIntent)
                        .setSmallIcon(R.drawable.health_aa)
                        .setContentTitle("Let's start the lessons in English")
                        .setAutoCancel(true)
                        .setSound(soundUri);

        return builder;
    }
}
