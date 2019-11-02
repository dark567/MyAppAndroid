package com.dark.new_test_job;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.v7.app.ActionBarActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.dark.new_test_job.alarm.Alarm;
import com.dark.new_test_job.alarm.alert.MathProblem;
import com.dark.new_test_job.alarm.database.DatabaseHelper;
import com.dark.new_test_job.alarm.database.UserActivity;
import com.dark.new_test_job.alarm.test_db;
import com.dark.new_test_job.parser.JSONParser;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.Scanner;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by dark on 19.08.2016.
 */
public class activity_3 extends ActionBarActivity {
    ListView mList;
    TextView header;
    DatabaseHelper sqlHelper;
    SQLiteDatabase db;
    Cursor userCursor;
    SimpleCursorAdapter userAdapter;



    private String status;
    private String p2;
    String Desc;
    String Req;

    private static String gpslat;
    private static String gpslng;
    private static String netlat;
    private static String netlng;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myalarm /*activity_main_db*/);

        header = (TextView)findViewById(R.id.header);

        mList = (ListView)findViewById(R.id.list);
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getApplicationContext(), UserActivity.class /*Admin_pass.class*/);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });
        sqlHelper = new DatabaseHelper(this);
    }
    @Override
    public void onResume() {
        super.onResume();






        // открываем подключение
        db = sqlHelper.getReadableDatabase();

        //получаем данные из бд
        userCursor =  db.rawQuery("select * from "+ DatabaseHelper.TABLE +" where request is not '+' ORDER BY _id DESC", null);

        String[] headers = new String[] {DatabaseHelper.COLUMN_CONTR_NAME, DatabaseHelper.COLUMN_PERSON_NAME,
                DatabaseHelper.COLUMN_REZULT, DatabaseHelper.COLUMN_DATA, DatabaseHelper.COLUMN_DESCRIPTION, DatabaseHelper.COLUMN_REQUEST};


        //String[] my = new String[] {userCursor.getString(0),userCursor.getString(1),userCursor.getString(2)};





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

                if (tv.getText().equals("0"))
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
                else if (req.getText().equals("-"))
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

//        for (int i=0 ; i < my.length ; i++ ) {
//            // result = result + mice[i];
//            Log.d("my", "my " +my[i]);}
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
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {

            Intent intent = new Intent(getApplicationContext(), UserActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.clear) {

            sqlHelper.onClear(db);
            return true;
        }
        if (id == R.id.replication) {

            Toast toast = Toast.makeText(getApplicationContext(),
                    "Репликация",
                    Toast.LENGTH_LONG);
            toast.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }













    public void Update_Db(String rez,String desc, String req) {

                /*внесение данных в базу*/

        String date = (DateFormat.format("HH:mm:ss dd.MM.yyyy, EEEE", new Date()).toString());

        try {
//            sqlHelper = new DatabaseHelper(this);
//            db = sqlHelper.getWritableDatabase();

// получаем элемент по id из бд

            userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLEsys, null);
            userCursor.moveToFirst();

            ContentValues cv = new ContentValues();

            cv.put(DatabaseHelper.COLUMN_CONTR_NAME, userCursor.getString(1));
            cv.put(DatabaseHelper.COLUMN_PERSON_NAME, userCursor.getString(2));
            cv.put(DatabaseHelper.COLUMN_REZULT, rez);
            cv.put(DatabaseHelper.COLUMN_DATA, date.toString());
            cv.put(DatabaseHelper.COLUMN_DESCRIPTION, desc);
            cv.put(DatabaseHelper.COLUMN_REQUEST, req);

            db.insert(DatabaseHelper.TABLE, null, cv);
            userCursor.close();


        } catch (Exception e) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Ошибка добавления данных в базу ",
                    Toast.LENGTH_LONG);
            toast.show();
        }

    }







    public void request(String status) throws IOException {

//        pDialog = new ProgressDialog(activity_2.this);
//        pDialog.setMessage("Добавление...");
//        pDialog.setIndeterminate(false);
//        pDialog.setCancelable(true);
//        pDialog.show();
//        pDialog.dismiss();/*выключить*/




        //sqlHelper = new DatabaseHelper(this);



        userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLEsys, null);
        userCursor.moveToFirst();


        String id_facility = userCursor.getString(3);
        String id_guard = userCursor.getString(4);
        String URL = userCursor.getString(5);



        //   userCursor.close();



        String myURL = "http://" + URL + "/test.php?guard=" + id_guard + "&facility=" + id_facility + "&status="+status;

        Toast toast = Toast.makeText(getApplicationContext(),
                "myURL " + myURL,
                Toast.LENGTH_LONG);
        toast.show();



/*открыть потом*/
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(myURL)
                .build();

        Log.d("TAG", "myURL is: " + myURL);

        client.newCall(request)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(final Call call, IOException e) {
                        // Error
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // For the example, you can show an error dialog or a toast
                                // on the main UI thread
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        //  String res = response.request().body().toString();

                        String res = response.body().string();
                        String resDate = response.headers().getDate("Date").toString();


                        Scanner s = new Scanner(response.toString()).useDelimiter("\\A");
                        String result = s.hasNext() ? s.next() : "";

                        Log.e("TAG", result);
                        Log.d("TAG", "response is: " + res);
                        Log.d("TAG", "response Date is: " + resDate);
                        // Получить ответ и сделать с ним что то

                    }
                });


        // Do something with the response.
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

                        Req = "+";
                    }
                    else {
                        Log.e("TAG","http://" + URL + " OFF");
                        Req = "+";
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }


    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        // test for connection
        if (cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().isAvailable()
                && cm.getActiveNetworkInfo().isConnected()
                ) {
            Log.v("TAG", "Internet Connection  Present");
            status = "Internet Connection  Present";
            return true;
        } else {
            Log.v("TAG", "Internet Connection Not Present");
            status = "Internet Connection Not Present";
            return false;
        }
    }








    public void rezult(String rez) {

        try {
            if (netlat != null && netlng != null) {
                if (isOnline()) {
                    Desc = netlat + "," + netlng + "," + status;
                    Req = "+";
                }
                else {
                    Desc = netlat + "," + netlng + "," + status;
                    Req = "-";
                }
            }
            else{
                if (gpslat != null && gpslng != null) {
                    if (isOnline()) {

                        Desc = gpslat + "," + gpslng + "," + status;
                        Req = "+";
                    }
                    else {
                        Req = "-";
                        Desc = gpslat + "," + gpslng + "," + status;
                    }
                }

                else {
                    if (isOnline()) {
                        Req = "+";
                        Desc = status;
                    }
                    else {
                        Req = "-";
                        Desc = status;
                    }
                }
            }

            if (isOnline()) {
                Req = "+";
            }
            else {
                Req = "-";
            }


            Update_Db(rez, Desc, Req);
        } catch (Exception e) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Ошибка записи",
                    Toast.LENGTH_LONG);
            toast.show();
        }

        try {

            if (isOnline()) {
                request(rez);
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Сайт доступен. Данные отправлены.",
                        Toast.LENGTH_LONG);
                toast.show();


            }
            else {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Сайт не доступен. Данные не отправлены.",
                        Toast.LENGTH_LONG);
                toast.show();


            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Данные НЕ отправлены на сайт",
                    Toast.LENGTH_LONG);
            toast.show();
            Desc = "-";
        }



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
}

