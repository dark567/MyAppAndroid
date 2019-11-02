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
package com.dark.new_test_job.alarm;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Gravity;
import android.view.HapticFeedbackConstants;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.dark.new_test_job.R;
import com.dark.new_test_job.alarm.database.Database;
import com.dark.new_test_job.alarm.preferences.AlarmPreferencesActivity;
import com.dark.new_test_job.parser.Get;
import com.dark.new_test_job.alarm.Alarm;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AlarmActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener{

	ImageButton newButton;
	ListView mathAlarmListView;
	AlarmListAdapter alarmListAdapter;
	private SwipeRefreshLayout mSwipeRefreshLayout;

	public static final String APP_PREFERENCES = "mysettings";
	public String APP_PREFERENCES_NICKNAME ;
	private String User;
	SharedPreferences mSettings;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);


		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.alarm_activity);


		mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

		mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh);
		mSwipeRefreshLayout.setOnRefreshListener(this);
		// делаем повеселее
		mSwipeRefreshLayout.setColorSchemeColors(R.color.blue, R.color.green, R.color.yellow, R.color.red);

		mathAlarmListView = (ListView) findViewById(android.R.id.list);
		mathAlarmListView.setLongClickable(true);
		mathAlarmListView.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
				view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
				final Alarm alarm = (Alarm) alarmListAdapter.getItem(position);
				Builder dialog = new AlertDialog.Builder(AlarmActivity.this);
				dialog.setTitle("Удалить");
				dialog.setMessage("Удалить этот сигнал?");
				dialog.setPositiveButton("Ok", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

						if (User.equals("Admin")) {

							Database.init(AlarmActivity.this);
							Database.deleteEntry(alarm);
							AlarmActivity.this.callMathAlarmScheduleService();

							updateAlarmList();

						}else {
							Toast toast = Toast.makeText(getApplicationContext(),
									"Извините удаление только для разработчика.",
									Toast.LENGTH_LONG);
							toast.setGravity(Gravity.CENTER, 10, 15);
							toast.show();
						}




					}
				});
				dialog.setNegativeButton("Cancel", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});

				dialog.show();

				return true;
			}
		});

		callMathAlarmScheduleService();

		alarmListAdapter = new AlarmListAdapter(this);
		this.mathAlarmListView.setAdapter(alarmListAdapter);
		mathAlarmListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
				if (User.equals("Admin") || User.equals("Head_of_security")) {

					v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);

					Alarm alarm = (Alarm) alarmListAdapter.getItem(position);
					Intent intent = new Intent(AlarmActivity.this, AlarmPreferencesActivity.class);
					intent.putExtra("alarm", alarm);
					startActivity(intent);

				}else {
					Toast toast = Toast.makeText(getApplicationContext(),
							"Извините, редактирование только для разработчика и нач. охраны.",
							Toast.LENGTH_LONG);
					toast.setGravity(Gravity.CENTER, 10, 15);
					toast.show();
				}



			}

		});
	}

	@Override
	public void onRefresh() {
		// говорим о том, что собираемся начать
		Toast.makeText(this, R.string.refresh_started, Toast.LENGTH_SHORT).show();
		// начинаем показывать прогресс
		mSwipeRefreshLayout.setRefreshing(true);
		// ждем 3 секунды и прячем прогресс



		//replication();
		mSwipeRefreshLayout.postDelayed(new Runnable() {
			@Override
			public void run() {


				try {

					replication();
					TimeUnit.SECONDS.sleep(0);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}


				onResume();

				mSwipeRefreshLayout.setRefreshing(false);
				// говорим о том, что собираемся закончить
				Toast.makeText(AlarmActivity.this, R.string.refresh_finished, Toast.LENGTH_SHORT).show();
			}
		}, 3000);




	//	AlarmActivity.this.alarmListAdapter.notifyDataSetChanged();


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {


		boolean result = super.onCreateOptionsMenu(menu);		
		menu.findItem(R.id.menu_item_save).setVisible(false);
		menu.findItem(R.id.menu_item_delete).setVisible(false);
		menu.findItem(R.id.menu_item_replication).setVisible(true);
	    return result;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			onBackPressed();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
		
	@Override
	protected void onPause() {
		// setListAdapter(null);
		Database.deactivate();
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (mSettings.contains(APP_PREFERENCES_NICKNAME)) {
			// выводим данные в TextView
			User = mSettings.getString(APP_PREFERENCES_NICKNAME,"");
			setTitle(/*getTitle() + */" пользователь:"+User);
			Log.i("test_db", "Имя user: " + mSettings.getString(APP_PREFERENCES_NICKNAME,
					""));
		}

		updateAlarmList();
	}
	
	public void updateAlarmList(){
		Database.init(AlarmActivity.this);
		final List<Alarm> alarms = Database.getAll();
		alarmListAdapter.setMathAlarms(alarms);
		
		runOnUiThread(new Runnable() {
			public void run() {
				// reload content			
				AlarmActivity.this.alarmListAdapter.notifyDataSetChanged();				
				if(alarms.size() > 0){
					findViewById(android.R.id.empty).setVisibility(View.INVISIBLE);
				}else{
					findViewById(android.R.id.empty).setVisibility(View.VISIBLE);
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.checkBox_alarm_active) {
			CheckBox checkBox = (CheckBox) v;
			Alarm alarm = (Alarm) alarmListAdapter.getItem((Integer) checkBox.getTag());
			alarm.setAlarmActive(checkBox.isChecked());
			Database.update(alarm);
			AlarmActivity.this.callMathAlarmScheduleService();
			if (checkBox.isChecked()) {
				Toast.makeText(AlarmActivity.this, alarm.getTimeUntilNextAlarmMessage(), Toast.LENGTH_LONG).show();
			}
		}

	}

}