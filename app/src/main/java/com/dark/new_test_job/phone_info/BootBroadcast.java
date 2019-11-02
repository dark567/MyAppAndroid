package com.dark.new_test_job.phone_info;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by dark on 09.10.2016.
 */
public class BootBroadcast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        try {

            Log.d("BootBroadcast_GPS", "BootBroadcast START");

        context.startService(new Intent(context, GPSTracker_new.class));

        }catch (Exception e) {
            Log.d("BootBroadcast_GPS", "BootBroadcast ERROR");

        }
    }
}