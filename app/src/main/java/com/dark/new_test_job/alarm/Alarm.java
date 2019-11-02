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

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.util.Log;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import com.dark.new_test_job.activity_2;
import com.dark.new_test_job.alarm.alert.AlarmAlertBroadcastReciever;

public class Alarm implements Serializable {





	public enum Difficulty{
		EASY,
		MEDIUM,
		HARD;
		
		@Override
		public String toString() {
			switch(this.ordinal()){
				case 0:
					return "Легко";
				case 1:
					return "Средне";
				case 2:
					return "Сильно";
			}
			return super.toString();
		}
	}

	public enum Difficulty_my{
		VERY_HARD,
		HARD,
		MEDIUM,
		EASY;

		@Override
		public String toString() {
			switch(this.ordinal()){
				case 0:
					return "1 час";
				case 1:
					return "2 часа";
				case 2:
					return "3 часа";
				case 3:
					return "4 часа";
			}
			return super.toString();
		}
	}
	
	public enum Day{
		SUNDAY,
		MONDAY,
		TUESDAY,
		WEDNESDAY,
		THURSDAY,
		FRIDAY,
		SATURDAY;

		@Override
		public String toString() {
			switch(this.ordinal()){
				case 0:
					return "Воскресенье";
				case 1:
					return "Понедельник";
				case 2:
					return "Вторник";
				case 3:
					return "Среда";
				case 4:
					return "Четверг";
				case 5:
					return "Пятница";
				case 6:
					return "Суббота";
			}
			return super.toString();
		}
		
	}




	private static final long serialVersionUID = 8699489847426803789L;
	private int id;
	private Boolean alarmActive = true;
	private Calendar alarmTime = Calendar.getInstance();
	private Day[] days = {Day.MONDAY, Day.TUESDAY, Day.WEDNESDAY, Day.THURSDAY, Day.FRIDAY, Day.SATURDAY, Day.SUNDAY};

	private String alarmTonePath = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM).toString();
	private Boolean vibrate = true;
	private String alarmName = "Проверка";
	private String alarmOWN = "";
	private String _id_s = "";
	private Boolean alarmRepeate = false;
	private Difficulty difficulty = Difficulty.EASY;
	private Difficulty_my difficulty_my = Difficulty_my.HARD;
	public Alarm() {

	}

//	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
//		out.defaultWriteObject();
//		out.writeObject(getAlarmToneUri().getEncodedPath());
//	}

//	private void readObject(java.io.ObjectInputStream in) throws IOException {
//		try {
//			in.defaultReadObject();
//			this.setAlarmToneUri(Uri.parse(in.readObject().toString()));
//		} catch (ClassNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}		
//	}

	/**
	 * @return the alarmActive
	 */
	public Boolean getAlarmActive() {
		return alarmActive;
	}

	public Boolean getalarmRepeate() {
		return alarmRepeate;
	}

	/**
	 * @param alarmActive
	 *            the alarmActive to set
	 */
	public void setAlarmActive(Boolean alarmActive) {
		this.alarmActive = alarmActive;
	}

	public void setalarmRepeate(Boolean alarmRepeate) {
		this.alarmRepeate = alarmRepeate;
	}

	/**
	 * @return the alarmTime
	 */
	public Calendar getAlarmTime() {
		if (alarmTime.before(Calendar.getInstance()))
			alarmTime.add(Calendar.DAY_OF_MONTH, 1);
		while(!Arrays.asList(getDays()).contains(Day.values()[alarmTime.get(Calendar.DAY_OF_WEEK)-1])){
			alarmTime.add(Calendar.DAY_OF_MONTH, 1);
		}
		return alarmTime;
	}

	/**
	 * @return the alarmTime
	 */
	public String getAlarmTimeString() {

		String time = "";
		if (alarmTime.get(Calendar.HOUR_OF_DAY) <= 9)
			time += "0";
		time += String.valueOf(alarmTime.get(Calendar.HOUR_OF_DAY));
		time += ":";

		if (alarmTime.get(Calendar.MINUTE) <= 9)
			time += "0";
		time += String.valueOf(alarmTime.get(Calendar.MINUTE));

		return time;
	}

	/**
	 * @param alarmTime
	 *            the alarmTime to set
	 */
	public void setAlarmTime(Calendar alarmTime) {
		this.alarmTime = alarmTime;
	}

	/**
	 * @param alarmTime
	 *            the alarmTime to set
	 */
	public void setAlarmTime(String alarmTime) {

		String[] timePieces = alarmTime.split(":");

		Calendar newAlarmTime = Calendar.getInstance();
		newAlarmTime.set(Calendar.HOUR_OF_DAY,
				Integer.parseInt(timePieces[0]));
		newAlarmTime.set(Calendar.MINUTE, Integer.parseInt(timePieces[1]));
		newAlarmTime.set(Calendar.SECOND, 0);
		setAlarmTime(newAlarmTime);		
	}

	/**
	 * @return the repeatDays
	 */
	public Day[] getDays() {
		return days;
	}



	/**
	 * @param set
	 *            the repeatDays to set
	 */
	public void setDays(Day[] days) {
		this.days = days;
	}

	public void addDay(Day day){
		boolean contains = false;
		for(Day d : getDays())
			if(d.equals(day))
				contains = true;
		if(!contains){
			List<Day> result = new LinkedList<Day>();
			for(Day d : getDays())
				result.add(d);
			result.add(day);
			setDays(result.toArray(new Day[result.size()]));
		}
	}
	
	public void removeDay(Day day) {
	    
		List<Day> result = new LinkedList<Day>();
	    for(Day d : getDays())
	        if(!d.equals(day))
	            result.add(d);
	    setDays(result.toArray(new Day[result.size()]));
	}

	public void removeDay_my(Day day) {

		List<Day> result = new LinkedList<Day>();
		for(Day d : getDays())
			if(d.equals(day))
				result.add(d);
		setDays(result.toArray(new Day[result.size()]));
	}
	
	/**
	 * @return the alarmTonePath
	 */
	public String getAlarmTonePath() {
		return alarmTonePath;
	}

	/**
	 * @param alarmTonePath the alarmTonePath to set
	 */
	public void setAlarmTonePath(String alarmTonePath) {
		this.alarmTonePath = alarmTonePath;
	}
	
	/**
	 * @return the vibrate
	 */
	public Boolean getVibrate() {
		return vibrate;
	}

	/**
	 * @param vibrate
	 *            the vibrate to set
	 */
	public void setVibrate(Boolean vibrate) {
		this.vibrate = vibrate;
	}

	/**
	 * @return the alarmName
	 */
	public String getAlarmName() {
		return alarmName;
	}

	public String getAlarmOWN() {

		//alarmOWN = "777";
		return alarmOWN;
	}

	public String getAlarm_id_s() {
		return _id_s;
	}

	/**
	 * @param alarmName
	 *            the alarmName to set
	 */
	public void setAlarmName(String alarmName) {
		this.alarmName = alarmName;
	}

	public void setAlarmOWN(String alarmOWN) {
		//alarmOWN = "666";
		this.alarmOWN= alarmOWN;
	}

	public void setAlarm_id_s(String _id_s) {
		this._id_s = _id_s;
	}


	public Difficulty getDifficulty() {
		return difficulty;
	}

	public Difficulty_my getDifficulty_my() {
		return difficulty_my;
	}

	public void setDifficulty(Difficulty difficulty) {
		this.difficulty = difficulty;
	}

	public void setDifficulty_my(Difficulty_my difficulty_my) {
		this.difficulty_my = difficulty_my;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRepeatDaysString() {
		StringBuilder daysStringBuilder = new StringBuilder();
		if(getDays().length == Day.values().length){
			daysStringBuilder.append("Каждый день");
		}else{
			Arrays.sort(getDays(), new Comparator<Day>() {
				@Override
				public int compare(Day lhs, Day rhs) {

					return lhs.ordinal() - rhs.ordinal();
				}
			});
			for(Day d : getDays()){
				switch(d){
				case TUESDAY:
				case THURSDAY:
//					daysStringBuilder.append(d.toString().substring(0, 4));
//					break;
					default:
						daysStringBuilder.append(d.toString().substring(0, 4));	/*длина сокращенного дня*/
						break;
				}				
				daysStringBuilder.append(',');
			}
			daysStringBuilder.setLength(daysStringBuilder.length()-1);
		}
			
		return daysStringBuilder.toString();
	}

	public void schedule(Context context) {
		setAlarmActive(true);
		
		Intent myIntent = new Intent(context, AlarmAlertBroadcastReciever.class);
		myIntent.putExtra("alarm", this);

		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, myIntent, PendingIntent.FLAG_CANCEL_CURRENT);

		AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

		alarmManager.set(AlarmManager.RTC_WAKEUP, getAlarmTime().getTimeInMillis(), pendingIntent);
	}
	
	public String getTimeUntilNextAlarmMessage(){
		long timeDifference = getAlarmTime().getTimeInMillis() - System.currentTimeMillis();
		long days = timeDifference / (1000 * 60 * 60 * 24);
		long hours = timeDifference / (1000 * 60 * 60) - (days * 24);
		long minutes = timeDifference / (1000 * 60) - (days * 24 * 60) - (hours * 60);
		long seconds = timeDifference / (1000) - (days * 24 * 60 * 60) - (hours * 60 * 60) - (minutes * 60);
		String alert = "Сигнал сработает через ";
		if (days > 0) {
			alert += String.format(
					"%d дней, %d часов, %d минут и %d секунд", days,
					hours, minutes, seconds);
		} else {
			if (hours > 0) {
				alert += String.format("%d часов, %d минут и %d секунд",
						hours, minutes, seconds);
			} else {
				if (minutes > 0) {
					alert += String.format("%d минут, %d секунд", minutes,
							seconds);
				} else {
					alert += String.format("%d секунд", seconds);
				}
			}
		}
		return alert;
	}
}
