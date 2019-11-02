package com.dark.new_test_job.alarm;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.dark.new_test_job.alarm.database.Database;
import com.dark.new_test_job.alarm.database.UserActivity;
import com.dark.new_test_job.alarm.database.DatabaseHelper;
import com.dark.new_test_job.R;
import com.dark.new_test_job.other.SyncSettings;
import com.dark.new_test_job.other.SyncSettingsActivity;
import com.dark.new_test_job.parser.Get;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by dark on 22.06.2016.
 */
public class test_db extends FragmentActivity {
    ListView mList;
    TextView header;
    DatabaseHelper sqlHelper;
    SQLiteDatabase db;
    Cursor userCursor;
    SimpleCursorAdapter userAdapter;

    private String date;
    private String time;
    private String status;

    public static final String APP_PREFERENCES = "mysettings";
    public String APP_PREFERENCES_NICKNAME ;
    private String User;
    SharedPreferences mSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.myalarm /*activity_main_db*/);

        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        header = (TextView)findViewById(R.id.header);

        mList = (ListView)findViewById(R.id.list);
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (User.equals("Admin")) {
                    Intent intent = new Intent(getApplicationContext(), UserActivity.class /*Admin_pass.class*/);
                    intent.putExtra("id", id);
                    startActivity(intent);
                }else {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Извините редактирование только для разработчика.",
                            Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 10, 15);
                    toast.show();
                }
            }
        });
        sqlHelper = new DatabaseHelper(getApplicationContext());
    }
    @Override
    public void onResume() {
        super.onResume();

        if (mSettings.contains(APP_PREFERENCES_NICKNAME)) {
            // выводим данные в TextView
            User = mSettings.getString(APP_PREFERENCES_NICKNAME,"");
            setTitle(/*getTitle() + */" пользователь:"+User);
            Log.i("test_db", "Имя user: " + mSettings.getString(APP_PREFERENCES_NICKNAME,
                    ""));
        }




        // открываем подключение
        db = sqlHelper.getReadableDatabase();

        //получаем данные из бд
        userCursor =  db.rawQuery("select * from "+ DatabaseHelper.TABLE +" ORDER BY _id DESC", null);

        String[] headers = new String[] {DatabaseHelper.COLUMN_CONTR_NAME, DatabaseHelper.COLUMN_PERSON_NAME,
                DatabaseHelper.COLUMN_REZULT, DatabaseHelper.COLUMN_DATA, DatabaseHelper.COLUMN_DESCRIPTION, DatabaseHelper.COLUMN_REQUEST};



        userAdapter = new SimpleCursorAdapter(this, R.layout.row,
                userCursor, headers, new int[]{R.id.ColCOLUMN_CONTR_NAME, R.id.ColCOLUMN_PERSON_NAME,
                R.id.ColCOLUMN_REZULT,R.id.ColCOLUMN_DATA,R.id.DESCRIPTION,R.id.REQUEST}, 0){

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View row = super.getView(position, convertView, parent);
                // Here we get the textview and set the color
                TextView contr = (TextView) row.findViewById(R.id.ColCOLUMN_CONTR_NAME);
                TextView name = (TextView) row.findViewById(R.id.ColCOLUMN_PERSON_NAME);
                TextView tv = (TextView) row.findViewById(R.id.ColCOLUMN_REZULT);
                TextView data = (TextView) row.findViewById(R.id.ColCOLUMN_DATA);
                TextView desc = (TextView) row.findViewById(R.id.DESCRIPTION);
                TextView req = (TextView) row.findViewById(R.id.REQUEST);

                ImageView imageView = (ImageView) row.findViewById(R.id.signal);
                ImageView rez = (ImageView) row.findViewById(R.id.rez);


                if (data.getText().equals("99999"))
                {data.setText(" Дата");}

                if (tv.getText().equals("0") )
                {rez.setImageResource(R.drawable.sleep);
                }
                else if (tv.getText().equals("1"))
                {rez.setImageResource(R.drawable.activ);
                }



                if (tv.getText().equals("0"))
                {contr.setTextColor(Color.RED);
                    name.setTextColor(Color.RED);
                 tv.setTextColor(Color.RED);
                    data.setTextColor(Color.RED);
                }
                else if (tv.getText().equals("1"))
                {contr.setTextColor(Color.WHITE);
                    name.setTextColor(Color.WHITE);
                 tv.setTextColor(Color.WHITE);
                    data.setTextColor(Color.WHITE);;
                }

                if (req.getText().equals("+"))
                {imageView.setImageResource(R.drawable.a);
                }
                else if (req.getText().equals("-") || (req.getText().equals("")) || (req.getText() == null))
                {imageView.setImageResource(R.drawable.z);
                }

                return row;
            }
        };

//        userAdapter = new SimpleCursorAdapter(this, android.R.layout.two_line_list_item,
//                userCursor, headers, new int[]{android.R.id.text1, android.R.id.text2}, 0);

        header.setText("Найдено элементов: " + String.valueOf(userCursor.getCount()));

       // header.append("\nОБЪЕКТ "+"   ОХРАНИК "+"    Рез. "+"  ДАТА");
        mList.setAdapter(userAdapter);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        // Закрываем подключения
        db.close();
        userCursor.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.feeds, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }


        if (id == R.id.clear) {
            if (User.equals("Admin")) {
                sqlHelper.onClear(db);

                onResume();

            }else {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Извините удаление только для разработчика.",
                        Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 10, 15);
                toast.show();
            }
            return true;
        }

        if (id == R.id.replication_db) {

            try {


                if (isOnline()) {
                    try{ isreachable();} catch (Exception e) {}
                    
                    synchronization();

                    onResume();
                    Log.d("date", "Status " + status);

                }
                else {
                    Log.d("date", "Status " + status);
                }


            } catch (Exception e) {
                e.printStackTrace();
            }



            return true;
        }


        if (item.getItemId() == R.id.action_prefs) {
            if (User.equals("Admin")) {
                startActivity(new Intent(this, SyncSettingsActivity.class));
            }else {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Извините доступ только для разработчика.",
                        Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 10, 15);
                toast.show();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
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
            Log.d("catname", "id " + urlnew_re_[j]);
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

    private boolean isSiteAvail(String site){
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

    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        // test for connection
        if (cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().isAvailable()
                && cm.getActiveNetworkInfo().isConnected()
                /*&& isreachable()*/) {
            Log.v("TAG", "Internet Connection  Present");
            status = "Internet Connection  Present";
            return true;
        } else {
            Log.v("TAG", "Internet Connection Not Present");
            status = "Internet Connection Not Present";
            return false;
        }
    }

}