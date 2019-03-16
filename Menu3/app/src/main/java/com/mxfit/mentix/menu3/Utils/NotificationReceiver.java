package com.mxfit.mentix.menu3.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class NotificationReceiver extends BroadcastReceiver {
    public static final String ACTION_ALARM_RECEIVER = "ACTION_ALARM_RECEIVER";
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            if (ACTION_ALARM_RECEIVER.equals(intent.getAction())) {
                NotificationAlarm.setAlarm(context);
            }
            if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
                NotificationAlarm.setAlarm(context);
            }
        }

    }
}
