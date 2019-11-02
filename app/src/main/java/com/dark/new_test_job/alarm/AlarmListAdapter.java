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

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.preference.ListPreference;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.dark.new_test_job.R;
import com.dark.new_test_job.alarm.database.Database;

public class AlarmListAdapter extends BaseAdapter {

	private static final String KEY_AUTO_SYNC = "com.dark.new_test_job.KEY_AUTO_SYNC";

	SharedPreferences mSettings;

    private AlarmActivity alarmActivity;
	private List<Alarm> alarms = new ArrayList<Alarm>();

	public static final String ALARM_FIELDS[] = { Database.COLUMN_ALARM_ACTIVE,
			Database.COLUMN_ALARM_TIME, Database.COLUMN_ALARM_DAYS, Database.COLUMN_id_s };

	public AlarmListAdapter(AlarmActivity alarmActivity) {
		this.alarmActivity = alarmActivity;
//		Database.init(alarmActivity);
//		alarms = Database.getAll();

	}

	@Override
	public int getCount() {
		return alarms.size();
	}

	@Override
	public Object getItem(int position) {
		return alarms.get(position);

	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup viewGroup) {
		if (null == view)
			view = LayoutInflater.from(alarmActivity).inflate(
					R.layout.alarm_list_element, null);

		Alarm alarm = (Alarm) getItem(position);

		CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBox_alarm_active);
		TextView alarmTimeView = (TextView) view
				.findViewById(R.id.textView_alarm_time);
		TextView alarmDaysView = (TextView) view
				.findViewById(R.id.textView_alarm_days);



		if (alarm.getAlarmName().equals("update"))
{


	checkBox.setChecked(alarm.getAlarmActive());
	checkBox.setTag(position);
	checkBox.setOnClickListener(alarmActivity);


	alarmTimeView.setText(alarm.getAlarmTimeString());
    alarmDaysView.setText(alarm.getRepeatDaysString() + ", для обновления id = " + alarm.getAlarm_id_s() + ", KEY_AUTO_SYNC "
			/*+ mSettings.getString(KEY_AUTO_SYNC, "NULL")*/);

            alarmDaysView.setTextColor(Color.GRAY);
			alarmDaysView.setTypeface(Typeface.DEFAULT);
			alarmTimeView.setTextColor(Color.GRAY);
			alarmTimeView.setTypeface(Typeface.DEFAULT_BOLD);

//			checkBox.setVisibility(View.INVISIBLE);
//			alarmTimeView.setVisibility(View.INVISIBLE);
//			alarmDaysView.setVisibility(View.INVISIBLE);

	//alarms.remove(position);

	return view;
		}

		else {
			checkBox.setChecked(alarm.getAlarmActive());
			checkBox.setTag(position);
			checkBox.setOnClickListener(alarmActivity);


			alarmTimeView.setText(alarm.getAlarmTimeString());


			alarmDaysView.setText(alarm.getRepeatDaysString() + ", id = " + alarm.getAlarm_id_s());


//		TextView alarm_id_s_View = (TextView) view
//				.findViewById(R.id.textView_id_s);
//		alarm_id_s_View.setText(" id = "+alarm.getAlarm_id_s());
			return view;
		}

	}

	public List<Alarm> getMathAlarms() {
		return alarms;
	}

	public void setMathAlarms(List<Alarm> alarms) {
		this.alarms = alarms;
	}



}
