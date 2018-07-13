package com.mxfit.mentix.menu3;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.mxfit.mentix.menu3.MainActivity;

public class ClosingService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);

        MainActivity activity = MainActivity.instance;

        if (activity != null) {
            if(activity.isInTraining)
                activity.saveDataOnStop(false);
            activity.mHandler.removeCallbacksAndMessages(null);
        }
        // Destroy the service
        stopSelf();
    }


}