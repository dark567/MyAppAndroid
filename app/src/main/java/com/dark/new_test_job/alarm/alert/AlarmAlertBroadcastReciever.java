/* Copyright 2014 Sheldon Neilson www.neilson.co.za
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.dark.new_test_job.alarm.alert;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;

import com.dark.new_test_job.activity_2;
import com.dark.new_test_job.alarm.Alarm;
import com.dark.new_test_job.alarm.database.DatabaseHelper;
import com.dark.new_test_job.alarm.service.AlarmServiceBroadcastReciever;
import com.dark.new_test_job.other.Sync;

import java.net.HttpURLConnection;
import java.net.URL;

public class AlarmAlertBroadcastReciever extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent mathAlarmServiceIntent = new Intent(
				context,
				AlarmServiceBroadcastReciever.class);
		context.sendBroadcast(mathAlarmServiceIntent, null);
		
		StaticWakeLock.lockOn(context);
		Bundle bundle = intent.getExtras();
		final Alarm alarm = (Alarm) bundle.getSerializable("alarm");



		Intent mathAlarmAlertActivityIntent;

//		Log.d("my_test", "my_test null BroadcastReceiver " +  alarm.getTimeUntilNextAlarmMessage());
//		Log.d("my_test", "my_test null BroadcastReceiver " +  alarm.getAlarmName());



		if(alarm.getAlarmName().equals("update")) {

//			Log.d("my_test", "my_test null Запуск моего ");


				try{
					Sync test_sync = new Sync();
					test_sync.run(context);

				}
				catch (Exception e) {}



		}
		else
		{

			mathAlarmAlertActivityIntent = new Intent(context, activity_2.class /*AlarmAlertActivity.class*/);
			mathAlarmAlertActivityIntent.putExtra("alarm", alarm);
			mathAlarmAlertActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(mathAlarmAlertActivityIntent);

		}
	}


}
