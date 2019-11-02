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
package com.dark.new_test_job.alarm.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.dark.new_test_job.activity_2;
import com.dark.new_test_job.alarm.Alarm;

public class AlarmServiceBroadcastReciever extends BroadcastReceiver {
	private Alarm alarm;

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d("AlarmServiceBroadcastReciever", "onReceive()");


		Intent serviceIntent = new Intent(context, AlarmService/*activity_2*/.class);
		context.startService(serviceIntent);
	}

	public void setMathAlarm(Alarm alarm) {
		this.alarm = alarm;
	}

}
