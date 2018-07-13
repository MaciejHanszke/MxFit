package com.mxfit.mentix.menu3;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;

import com.mxfit.mentix.menu3.R;
import com.mxfit.mentix.menu3.SplashActivity;


public class NotificationAlarm {

    public static void setAlarm(Context ctx) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        boolean vibrate = false;
        boolean ringtone = false;
        if(prefs.getBoolean("switch_notifications_enable",false)) {
            if (prefs.getBoolean("switch_notifications_vibrate", false))
                vibrate = true;
            if (prefs.getBoolean("switch_notifications_ringtone", false))
                ringtone = true;
        }
        Uri sound;

        NotificationManager notificationManager;
        Intent repeating_intent;
        notificationManager = (NotificationManager) ctx
                        .getSystemService(Context.NOTIFICATION_SERVICE);
        repeating_intent = new Intent(ctx, SplashActivity.class);
        repeating_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(ctx, 1,
                    repeating_intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(ctx)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_notif)
                .setContentTitle("Training reminder")
                .setContentText("You have pending trainings")
                .setAutoCancel(true);
        if(vibrate)
            builder.setVibrate(new long[] { 0, 130, 50, 70, 50,70 });
        if(ringtone) {
            sound = Uri.parse(prefs.getString("notifications_ringtone",
                                                      "DEFAULT_SOUND"));
            builder.setSound(sound);
        }
        notificationManager.notify(1, builder.build());
    }
}
