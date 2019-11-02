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
package com.dark.new_test_job.alarm.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.dark.new_test_job.MainActivity;
import com.dark.new_test_job.R;
import com.dark.new_test_job.alarm.Alarm;
import com.dark.new_test_job.alarm.preferences.AlarmPreference.Type;

public class AlarmPreferenceListAdapter extends BaseAdapter implements Serializable{

	private Context context;
	private Alarm alarm;
	private List<AlarmPreference> preferences = new ArrayList<AlarmPreference>();
	private final String[] repeatDays = {"Воскресенье","Понедельник","Вторник","Среда","Четверг","Пятница","Суббота"};
	private final String[] alarmDifficulties = {"Легко","Средне","Сильно"};
	private final String[] repeatHOURS = {"1 час","2 часа","3 часа","4 часа"};
	
	private String[] alarmTones;
	private String[] alarmTonePaths;

	public static final String APP_PREFERENCES = "mysettings";
	public String APP_PREFERENCES_NICKNAME ;
	private String User;
	SharedPreferences mSettings;
	
	public AlarmPreferenceListAdapter(Context context, Alarm alarm) {
		setContext(context);



		
//		(new Runnable(){
//
//			@Override
//			public void run() {
				Log.d("AlarmPreferenceListAdapter", "Loading Ringtones...");
				
				RingtoneManager ringtoneMgr = new RingtoneManager(getContext());
				
				ringtoneMgr.setType(RingtoneManager.TYPE_ALARM);
				
				Cursor alarmsCursor = ringtoneMgr.getCursor();
				
				alarmTones = new String[alarmsCursor.getCount()+1];
				alarmTones[0] = "Silent"; 
				alarmTonePaths = new String[alarmsCursor.getCount()+1];
				alarmTonePaths[0] = "";
				
				if (alarmsCursor.moveToFirst()) {		    			
					do {
						alarmTones[alarmsCursor.getPosition()+1] = ringtoneMgr.getRingtone(alarmsCursor.getPosition()).getTitle(getContext());
						alarmTonePaths[alarmsCursor.getPosition()+1] = ringtoneMgr.getRingtoneUri(alarmsCursor.getPosition()).toString();
					}while(alarmsCursor.moveToNext());					
				}
				Log.d("AlarmPreferenceListAdapter", "Finished Loading " + alarmTones.length + " Ringtones.");
				alarmsCursor.close();
//				
//			}
//			
//		}).run();
//		
	    setMathAlarm(alarm);		
	}

	@Override
	public int getCount() {
		return preferences.size();
	}

	@Override
	public Object getItem(int position) {
		return preferences.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}



	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		AlarmPreference alarmPreference = (AlarmPreference) getItem(position);
		LayoutInflater layoutInflater = LayoutInflater.from(getContext());

		switch (alarmPreference.getType()) {
		case BOOLEAN:
			if(null == convertView || convertView.getId() != android.R.layout.simple_list_item_checked)
			convertView = layoutInflater.inflate(android.R.layout.simple_list_item_checked, null);

			CheckedTextView checkedTextView = (CheckedTextView) convertView.findViewById(android.R.id.text1);
			checkedTextView.setText(alarmPreference.getTitle());
			checkedTextView.setChecked((Boolean) alarmPreference.getValue());
			break;
		case INTEGER:
		case STRING:
		case LIST:
		case MULTIPLE_LIST:
		case TIME:
		case MULTIPLE_LIST_R:
		case OWN:
		default:
			if(null == convertView || convertView.getId() != android.R.layout.simple_list_item_2)
			convertView = layoutInflater.inflate(android.R.layout.simple_list_item_2, null);
			
			TextView text1 = (TextView) convertView.findViewById(android.R.id.text1);
			text1.setTextSize(18);
			text1.setText(alarmPreference.getTitle());
			
			TextView text2 = (TextView) convertView.findViewById(android.R.id.text2);
			text2.setText(alarmPreference.getSummary());
			break;
		}

		return convertView;
	}

	public Alarm getMathAlarm() {		
		for(AlarmPreference preference : preferences){
			switch(preference.getKey()){
				case ALARM_ACTIVE:
					alarm.setAlarmActive((Boolean) preference.getValue());
					break;
				case ALARM_NAME:
					alarm.setAlarmName((String) preference.getValue());
					break;
				case ALARM_TIME:
					alarm.setAlarmTime((String) preference.getValue());
					break;
				case ALARM_DIFFICULTY:
					alarm.setDifficulty(Alarm.Difficulty.valueOf((String)preference.getValue()));
					break;
				case ALARM_TONE:
					alarm.setAlarmTonePath((String) preference.getValue());
					break;
				case ALARM_VIBRATE:
					alarm.setVibrate((Boolean) preference.getValue());
					break;
				case ALARM_REPEAT:
					alarm.setalarmRepeate((Boolean) preference.getValue());
					break;
				case ALARM_my:
					alarm.setAlarmActive((Boolean) preference.getValue());
					break;
			}
		}
				
		return alarm;
	}

	public void setMathAlarm(Alarm alarm) {
		this.alarm = alarm;


//		mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
//
//		if (mSettings.contains(APP_PREFERENCES_NICKNAME)) {
//			// выводим данные в TextView
//			User = mSettings.getString(APP_PREFERENCES_NICKNAME,"");
//			//setTitle(/*getTitle() + */" пользователь:"+User);
//			Log.i("test_db", "Имя user: " + mSettings.getString(APP_PREFERENCES_NICKNAME,
//					""));
//		}
//
//		if (User.toString() == null) {
//			User = "000";
//		}


		preferences.clear();
		preferences.add(new AlarmPreference(AlarmPreference.Key.ALARM_ACTIVE,"Активность", null, null, alarm.getAlarmActive(),Type.BOOLEAN));
		preferences.add(new AlarmPreference(AlarmPreference.Key.ALARM_NAME, "Метка",alarm.getAlarmName(), null, alarm.getAlarmName(), Type.STRING));
		preferences.add(new AlarmPreference(AlarmPreference.Key.ALARM_TIME, "Установить время",alarm.getAlarmTimeString(), null, alarm.getAlarmTime(), Type.TIME));
		preferences.add(new AlarmPreference(AlarmPreference.Key.ALARM_REPEAT, "Повтор",alarm.getRepeatDaysString(), repeatDays, alarm.getDays(),Type.MULTIPLE_LIST));
		preferences.add(new AlarmPreference(AlarmPreference.Key.ALARM_DIFFICULTY,"Громкость", alarm.getDifficulty().toString(), alarmDifficulties, alarm.getDifficulty(), Type.LIST));

		Uri alarmToneUri = Uri.parse(alarm.getAlarmTonePath());
			Ringtone alarmTone = RingtoneManager.getRingtone(getContext(), alarmToneUri);
		
		if(alarmTone instanceof Ringtone && !alarm.getAlarmTonePath().equalsIgnoreCase("")){
			preferences.add(new AlarmPreference(AlarmPreference.Key.ALARM_TONE, "Рингтон", alarmTone.getTitle(getContext()),alarmTones, alarm.getAlarmTonePath(), Type.LIST));
		}else{
			preferences.add(new AlarmPreference(AlarmPreference.Key.ALARM_TONE, "Рингтон", getAlarmTones()[0],alarmTones, null, Type.LIST));
		}
		
		preferences.add(new AlarmPreference(AlarmPreference.Key.ALARM_VIBRATE, "Вибрация",null, null, alarm.getVibrate(), Type.BOOLEAN));

		preferences.add(new AlarmPreference(AlarmPreference.Key.ALARM_my,"Цикличность Вкл/Выкл", null, null, alarm.getalarmRepeate(),Type.BOOLEAN));
        preferences.add(new AlarmPreference(AlarmPreference.Key.ALARM_REPEAT_HOURS, "Цикличность обходов",alarm.getDifficulty_my().toString(), repeatHOURS, alarm.getDifficulty_my(), Type.MULTIPLE_LIST_R));
		preferences.add(new AlarmPreference(AlarmPreference.Key.ALARM_OWN, "Владелец",alarm.getAlarmOWN(), null, alarm.getAlarmOWN(), Type.OWN));

//		preferences.add(new AlarmPreference(AlarmPreference.Key.ALARM_TIME_FROM, "Начиная с ...",alarm.getAlarmTimeString(), null, alarm.getAlarmTime(), Type.TIME_FROM));
//        preferences.add(new AlarmPreference(AlarmPreference.Key.ALARM_TIME_TO, "Заканчивая в ...",alarm.getAlarmTimeString(), null, alarm.getAlarmTime(), Type.TIME /*TIME_TO*/));

	}

	
	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public String[] getRepeatDays() {
		return repeatDays;
	}

	public String[] getAlarmDifficulties() {
		return alarmDifficulties;
	}

	public String[] getAlarmTones() {
		return alarmTones;
	}

	public String[] getAlarmTonePaths() {
		return alarmTonePaths;
	}

}
