package com.dark.new_test_job.other;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TextView;

import com.dark.new_test_job.MainActivity;
import com.dark.new_test_job.R;
import com.dark.new_test_job.alarm.Alarm;
import com.dark.new_test_job.alarm.database.Database;
import com.dark.new_test_job.alarm.database.DatabaseHelper;
import com.dark.new_test_job.parser.Get;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by dark on 11.09.2016.
 */
public class Sync {

    DatabaseHelper sqlHelper;
    SQLiteDatabase db;
       private Alarm alarm;
    Cursor userCursor;
    private String date;
    private String time;
    private String status;
    Boolean rezult = true;



    public void run(Context context) {

        sqlHelper = new DatabaseHelper(context);

        db = sqlHelper.getWritableDatabase();




        try{

           try{ isreachable();} catch (Exception e) {}


               Log.d("synchronization", "synchronization on" + status);

                synchronization();

              Log.d("synchronization", "synchronization on");

            } catch (Exception e) {
                        Log.d("synchronization", "synchronization off");
                    }

        try{

            try{ isreachable();} catch (Exception e) {}

         Database.init(context);
            replication();


            Log.d("synchronization", "replication on");

        } catch (Exception e) {
            Log.d("synchronization", "replication off");
        }







    }

    public void replication() {

        userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLEsys, null);
        userCursor.moveToFirst();

        String facility_id = userCursor.getString(3);

        String url="http://inex24.com.ua/api/timetable.php?action=get_timetable&facility="  + facility_id;


        //   Log.d("json", "url " + url);


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



                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                        try {
                            //TextView myTextView = (TextView) findViewById(R.id.textView2);
                            //  myTextView.setText(responseData);
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
                                        //  Database.init(context);
                                        final List<Alarm> alarms = Database.getAll();
                                        for (int j=0 ; j < alarms.size() ; j++ ) {



                                            System.out.println(alarms.get(j).getAlarm_id_s());

                                            Log.d("json", "my_j=" + alarms.get(j).getAlarm_id_s() + " my_id=" + id);
                                            if(alarms.get(j).getAlarm_id_s().equals(id)){
                                                rezult = false;}
                                        }
												/*проверка конец*/

                                        if (rezult) {
                                            Log.d("json", id + " не совпадение");

                                            //   Database.init(context);

                                            Database.create(alarm);

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

    public void synchronization() {



        userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLEsys, null);
        userCursor.moveToFirst();


        String id_facility = userCursor.getString(3);
        String id_guard = userCursor.getString(4);
        String URL = userCursor.getString(5);






        String query = "select * from "+ DatabaseHelper.TABLE +" where request is not '+' ORDER BY _id DESC";
        Cursor catcursor = db.rawQuery(query, null);
//        String[] contr_ = new String[catcursor.getCount()];
        String[] name_ = new String[catcursor.getCount()];
        String[] status_ = new String[catcursor.getCount()];
        String[] url_ = new String[catcursor.getCount()];
        String[] id_ = new String[catcursor.getCount()];
        String[] urlnew_ = new String[catcursor.getCount()];
        String[] urlnew_re_ = new String[catcursor.getCount()];


        int i = 0;

        //catcursor.moveToFirst();
        String name;
        String id;
        String status;
        String data;

        while (catcursor.moveToNext()) {
//            contr = catcursor.getString(catcursor
//                    .getColumnIndex(DatabaseHelper.COLUMN_CONTR_NAME));
            id = catcursor.getString(catcursor
                    .getColumnIndex(DatabaseHelper.COLUMN_ID));
            name = catcursor.getString(catcursor
                    .getColumnIndex(DatabaseHelper.COLUMN_PERSON_NAME));
            status = catcursor.getString(catcursor
                    .getColumnIndex(DatabaseHelper.COLUMN_REZULT));
            data = catcursor.getString(catcursor
                    .getColumnIndex(DatabaseHelper.COLUMN_DATA));

            //  Log.d("catname", "catname " +name);
            // id_[i] = id;
            name_[i] = name;
            status_[i] = status;

            String dtStart = data;//"2010-10-15T09:27:37Z";
            Date date_original;

            String date_now = (DateFormat.format("HH:mm:ss dd.MM.yyyy", new Date()).toString());


            SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy");
            SimpleDateFormat format_date = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat format_time = new SimpleDateFormat("HH:mm:ss");

            try {
                date_original = format.parse(dtStart);
                System.out.println(date_original);
                Log.d("date", "date " + date_original);
                date = format_date.format(date_original);
                time = format_time.format(date_original);
                Log.d("date", "date " + date);
                Log.d("date", "time " + time);

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            url_[i] = "http://" + URL + "/app/report.php?act=sync&guard=" + id_guard + "&facility=" + id_facility + "&status="+status+"&date="+date+"&time="+time;

            urlnew_[i] = "UPDATE "+ DatabaseHelper.TABLE +" SET request = '+' where "+DatabaseHelper.COLUMN_ID+"="+id;
            urlnew_re_[i] = "UPDATE "+ DatabaseHelper.TABLE +" SET description = 'update "+date_now+"' where "+DatabaseHelper.COLUMN_ID+"="+id;


            i=i+1;



        }

        catcursor.close();

        for (int j=0 ; j < status_.length ; j++ ) {
            // result = result + mice[i];
            Log.d("catname", "url_ " + url_[j]);
            Log.d("catname", "id " + urlnew_[j]);
            db.execSQL(urlnew_[j]);
            db.execSQL(urlnew_re_[j]);

            Get test_at = new Get();

            test_at.run(url_[j],
                    new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            //todo work with response, parse and etc...

                        }
                    });
        }

    }




    public void isreachable() {
        /*проверка на доступность сайта*/
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLEsys, null);
                    userCursor.moveToFirst();

                    String URL = userCursor.getString(5);

                    if (isSiteAvail("http://" + URL)) {
                        Log.e("TAG","http://" + URL + " ON");

                       status = "++";
                    }
                    else {
                        Log.e("TAG","http://" + URL + " OFF");
                        status = "--";
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    public boolean isSiteAvail(String site){
        try {
            URL url = new URL(site);
            HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
            urlc.setRequestProperty("Connection", "close");
            urlc.setConnectTimeout(5000);
            urlc.connect();
            int Code = urlc.getResponseCode();
            if (Code == HttpURLConnection.HTTP_OK) return true;
        }
        catch (MalformedURLException e) {}
        catch (IOException e) {}
        return false;
    }


/*расписание*/



    public void setMathAlarm(Alarm alarm) {
        this.alarm = alarm;
    }





}