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

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.dark.new_test_job.MainActivity;
import com.dark.new_test_job.R;
import com.dark.new_test_job.alarm.database.Database;
import com.dark.new_test_job.alarm.database.DatabaseHelper;
import com.dark.new_test_job.alarm.preferences.AlarmPreferencesActivity;
import com.dark.new_test_job.alarm.service.AlarmServiceBroadcastReciever;
import com.dark.new_test_job.other.Sync;
import com.dark.new_test_job.other.SyncSettings;
import com.dark.new_test_job.other.SyncSettingsActivity;
import com.dark.new_test_job.parser.Get;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public abstract class BaseActivity  extends ActionBarActivity implements android.view.View.OnClickListener{
	private Alarm alarm;
	Boolean rezult = true;

	DatabaseHelper sqlHelper;
	SQLiteDatabase db;
	Cursor userCursor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		try {
	        ViewConfiguration config = ViewConfiguration.get(this);
	        Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
	        if(menuKeyField != null) {
	            menuKeyField.setAccessible(true);
	            menuKeyField.setBoolean(config, false);
	        }
	    } catch (Exception ex) {
	        // Ignore
	    }
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu, menu);

	    return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		String url = null;
		Intent intent = null;
		switch (item.getItemId()) {

		case R.id.home:
			onBackPressed();
					break;

		case R.id.menu_item_new:

			//String User = "qwerty";
			Intent newAlarmIntent = new Intent(this, AlarmPreferencesActivity.class);
		//	intent.putExtra("User", User);
			startActivity(newAlarmIntent);
			break;
		case R.id.menu_item_clearbd: /*delete*/

			try {
				Database.deleteAll();



				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			onResume();

			break;


		case R.id.menu_item_website:
			url = "http://inex24.com.ua";
			intent = new Intent(Intent.ACTION_VIEW);
			intent.setData(Uri.parse(url));
			try {
				startActivity(intent);
			} catch (ActivityNotFoundException e) {
				Toast.makeText(this, "Couldn't launch the website", Toast.LENGTH_LONG).show();
			}
			break;
		case R.id.menu_item_replication:

//			try {
//
//				Database.deleteAll();
//				TimeUnit.SECONDS.sleep(1);
//
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//

			try {

				try{
					Sync test_sync = new Sync();
					test_sync.run(this);

				}
				catch (Exception e) {}
			//replication();

            TimeUnit.SECONDS.sleep(1);
		   } catch (InterruptedException e) {
			e.printStackTrace();
		  }


			onResume();

				break;

			case R.id.synchronization_settings:

				startActivity(new Intent(this, SyncSettingsActivity.class));

				return true;

		case R.id.menu_item_report:
			/*
			url = "https://github.com/SheldonNeilson/Android-Alarm-Clock/issues";
			intent = new Intent(Intent.ACTION_VIEW);
			intent.setData(Uri.parse(url));
			try {
				startActivity(intent);
			} catch (ActivityNotFoundException e) {
				Toast.makeText(this, "Couldn't launch the bug reporting website", Toast.LENGTH_LONG).show();
			}
			*/

			Intent send = new Intent(Intent.ACTION_SENDTO);
			String uriText;

			String emailAddress = "darkmit@list.ru";
			String subject = R.string.app_name + " Bug Report";
			String body = "Debug:";
			body += "\n OS Version: " + System.getProperty("os.version") + "(" + android.os.Build.VERSION.INCREMENTAL + ")";
			body += "\n OS API Level: " + android.os.Build.VERSION.SDK_INT;
			body += "\n Device: " + android.os.Build.DEVICE;
			body += "\n Model (and Product): " + android.os.Build.MODEL + " (" + android.os.Build.PRODUCT + ")";
			body += "\n Screen Width: " + getWindow().getWindowManager().getDefaultDisplay().getWidth();
			body += "\n Screen Height: " + getWindow().getWindowManager().getDefaultDisplay().getHeight();
			body += "\n Hardware Keyboard Present: " + (getResources().getConfiguration().keyboard != Configuration.KEYBOARD_NOKEYS);

			uriText = "mailto:" + emailAddress + "?subject=" + subject + "&body=" + body;

			uriText = uriText.replace(" ", "%20");
			Uri emalUri = Uri.parse(uriText);

			send.setData(emalUri);
			startActivity(Intent.createChooser(send, "Send mail..."));

			break;
		}
		return super.onOptionsItemSelected(item);
	}

	protected void callMathAlarmScheduleService() {
		Intent mathAlarmServiceIntent = new Intent(this, AlarmServiceBroadcastReciever.class);
		sendBroadcast(mathAlarmServiceIntent, null);
	}

	public void setMathAlarm(Alarm alarm) {
		this.alarm = alarm;
	}

	public void replication() {

		sqlHelper = new DatabaseHelper(this);
		db = sqlHelper.getWritableDatabase();

		userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLEsys, null);
		userCursor.moveToFirst();

		String facility_id = userCursor.getString(3);

		String url="http://inex24.com.ua/api/timetable.php?action=get_timetable&facility="  + facility_id;

		Log.d("json", "url " + url);


		Get test = new Get();



		test.run(url,
				new Callback() {
					@Override
					public void onFailure(Call call, IOException e) {




					}
					@Override
					public void onResponse(Call call, final Response response) throws IOException {
						// ... check for failure using `isSuccessful` before proceeding

						// Read data on the worker thread
						final String responseData = response.body().string();

						// Run view-related code  on the main thread
						BaseActivity.this.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								try {
									//TextView myTextView = (TextView) findViewById(R.id.textView2);
									//myTextView.setText(responseData);
									Log.d("json", "responseData " + responseData);



								} catch (Exception e) {
									e.printStackTrace();
								}



								String jsonData = responseData;

								try {

									JSONArray jArray = new JSONArray(jsonData);

									for (int i=0 ; i < jArray.length() ; i++ ) {
										System.out.println(jArray.get(i));
										String my = jArray.get(i).toString();
										try{
											JSONObject jsonObject = new JSONObject(my);

											String id = jsonObject.getString("id");
											String day = jsonObject.getString("day");
											String time = jsonObject.getString("time");
											Log.d("json", "all " +jsonObject.toString());
											Log.d("json", "id " + id + " day " + day + " time " + time);


											setMathAlarm(new Alarm());

											alarm.setAlarmActive(true);
											alarm.setAlarmName("импортированные с сайта");

											alarm.setAlarm_id_s(id);

											alarm.setAlarmTime(time);

											switch(day){
												case "1":alarm.removeDay_my(Alarm.Day.MONDAY);
													break;
												case "2":alarm.removeDay_my(Alarm.Day.TUESDAY);
													break;
												case "3":alarm.removeDay_my(Alarm.Day.WEDNESDAY);
													break;
												case "4":alarm.removeDay_my(Alarm.Day.THURSDAY);
													break;
												case "5":alarm.removeDay_my(Alarm.Day.FRIDAY);
													break;
												case "6":alarm.removeDay_my(Alarm.Day.SATURDAY);
													break;
												case "7":alarm.removeDay_my(Alarm.Day.SUNDAY);
													break;
//                                                default:
//                                                    оператор;
//                                                    break;
											}

//											Log.d("json", "responseData " + day + alarm.getDays().toString());

											alarm.setVibrate(true);


											try{
                                                /*проверка на одинаковый id*/
												Database.init(getApplicationContext());

												final List<Alarm> alarms;

												alarms= Database.getAll();

												for (int j=0 ; j < alarms.size() ; j++ ) {

													String my_1 = alarms.get(j).toString();

													System.out.println(alarms.get(j).getAlarm_id_s());

													Log.d("json", "my_j=" + alarms.get(j).getAlarm_id_s() + " my_id=" + id);
													if(alarms.get(j).getAlarm_id_s().equals(id)){
														rezult = false;}
												}
												alarms.clear();
												/*проверка конец*/

												if (rezult) {
													Log.d("json", id + " не совпадение");

													Database.init(getApplicationContext());

													Database.create(alarm);

													onResume();

												}
												else {
													Log.d("json", id + " совпадение");
												}



											} catch (Exception e) {
												e.printStackTrace();
											}


										} catch (JSONException e){}

									}

								} catch (JSONException e) {
									e.printStackTrace();
								}



							}
						});
					}

				});
	}

}
