package com.example.finalproject0623;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

//CHECKBOX: 用系統通知 3 (三選一)(BroadcastReceiver)
public class NotifyBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent repeating_Intent = new Intent(context, NotificationOpenActivity.class);
        repeating_Intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent  = PendingIntent.getActivities(context,0, new Intent[]{repeating_Intent},PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"notifyBMI")
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.trophy)
                .setContentTitle("Remind BMI Measurement")
                .setContentText("Hey dear user, this is a bmi reminder")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(200, builder.build());
    }
}
