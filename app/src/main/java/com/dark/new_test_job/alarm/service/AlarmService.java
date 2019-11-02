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

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.IBinder;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.SimpleCursorAdapter;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;


import com.dark.new_test_job.activity_2;
import com.dark.new_test_job.alarm.Alarm;
import com.dark.new_test_job.alarm.alert.AlarmAlertBroadcastReciever;
import com.dark.new_test_job.alarm.database.Database;
import com.dark.new_test_job.alarm.database.DatabaseHelper;
import com.dark.new_test_job.parser.Get;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AlarmService extends Service {

//	DatabaseHelper sqlHelper;
//	SQLiteDatabase db;
//	Cursor userCursor;
//	SimpleCursorAdapter userAdapter;
//
//	private String date;
//	private String time;
//	private String status;

	@Override
	public IBinder onBind(Intent intent) {

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Service#onCreate()
	 */
	@Override
	public void onCreate() {
		Log.d(this.getClass().getSimpleName(), "onCreate()");
		Log.d("service_my", this.getClass().getSimpleName());



		super.onCreate();		
	}

	private Alarm getNext(){
		Set<Alarm> alarmQueue = new TreeSet<Alarm>(new Comparator<Alarm>() {
			@Override
			public int compare(Alarm lhs, Alarm rhs) {
				int result = 0;
				long diff = lhs.getAlarmTime().getTimeInMillis() - rhs.getAlarmTime().getTimeInMillis();				
				if(diff>0){
					return 1;
				}else if (diff < 0){
					return -1;
				}
				return result;
			}
		});
				
		Database.init(getApplicationContext());
		List<Alarm> alarms = Database.getAll();
		
		for(Alarm alarm : alarms){
			if(alarm.getAlarmActive())
				alarmQueue.add(alarm);
		}
		if(alarmQueue.iterator().hasNext()){
			return alarmQueue.iterator().next();
		}else{
			return null;
		}
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Service#onDestroy()
	 */
	@Override
	public void onDestroy() {
		Database.deactivate();
		super.onDestroy();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Service#onStartCommand(android.content.Intent, int, int)
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(this.getClass().getSimpleName(), "onStartCommand()");
		Alarm alarm = getNext();
		if(null != alarm){
			alarm.schedule(getApplicationContext());
			Log.d(this.getClass().getSimpleName(), alarm.getTimeUntilNextAlarmMessage());





			
		}else{


			Intent myIntent = new Intent(getApplicationContext(), AlarmAlertBroadcastReciever.class);
			myIntent.putExtra("alarm", new Alarm());





			PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, myIntent, PendingIntent.FLAG_CANCEL_CURRENT);
			AlarmManager alarmManager = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
			
			alarmManager.cancel(pendingIntent);
		}
		return START_NOT_STICKY;
	}





}
