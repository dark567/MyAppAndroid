package com.dark.new_test_job;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.dark.new_test_job.other.Sync;
import com.dark.new_test_job.parser.Get;
import com.dark.new_test_job.phone_info.GPSTracker_new;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;


import com.dark.new_test_job.alarm.Alarm;
import com.dark.new_test_job.alarm.alert.MathProblem;
import com.dark.new_test_job.alarm.database.DatabaseHelper;
import com.dark.new_test_job.alarm.test_db;
import com.dark.new_test_job.parser.JSONParser;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * Created by dark on 22.03.2016.
 */
public class activity_2 extends Activity {

    ImageButton imageButton;
    boolean flag = true;
    DatabaseHelper sqlHelper;
    SQLiteDatabase db;
    Cursor userCursor;

    private Alarm alarm;
    private MediaPlayer mediaPlayer;
    TextView mCount;
    private TextView mTimer;

    private StringBuilder answerBuilder = new StringBuilder();

    private MathProblem mathProblem;
    private Vibrator vibrator;

    private boolean alarmActive;

    private TextView problemView;
    private TextView answerView;
    private String answerString;
    private String responseHandler;

    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    private static String url_create_product = "http://test.devcolibri.com/create_product.php";
    private static final String TAG_SUCCESS = "success";
    public final String TAG = "MyLogger";
    public static final MediaType MEDIA_TYPE_MARKDOWN
            = MediaType.parse("text/x-markdown; charset=utf-8");

    private String status;
    private String p2;
    String Desc;
    String Req;
    private LocationManager locationManager;
    TextView LocationNet;
    TextView LocationGPS;

    private static String answer;

    private static String gpslat;
    private static String gpslng;
    private static String netlat;
    private static String netlng;
    private static String gpslat_old;
    private static String gpslng_old;
    private static String radius_old;
    private static Integer re_diff_int;
    private static String GPSTracker_new_gpslat;
    private static String GPSTracker_new_gpslng ;
    private static String Description ;

    private static String finish;

    private TextView mInfoTextView;
    private ProgressBar progressBar_my;

    private String date;
    private String time;


    ProgressDialog pd;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client2;


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        final Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        setContentView(R.layout.activity_2);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        LocationGPS = (TextView) findViewById(R.id.LocationGPS);
        LocationNet = (TextView) findViewById(R.id.LocationNet);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        mTimer = (TextView) findViewById(R.id.tv);
        mInfoTextView = (TextView) findViewById(R.id.textViewInfo);
        progressBar_my = (ProgressBar) findViewById(R.id.progressBar);

        progressBar_my.setVisibility(View.INVISIBLE); // прячем кнопку




        sqlHelper = new DatabaseHelper(this);
        db = sqlHelper.getWritableDatabase();

//        String test = alarm.toString();
//
//        Log.d("my_test", "my_test activity2 " +  alarm.toString());

        userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLEsys, null);
        userCursor.moveToFirst();

        String timeout = userCursor.getString(6);

        int mytimeout= 30000;

        try {
            mytimeout = Integer.parseInt(timeout);
        } catch(NumberFormatException nfe) {
            // Handle parse error.
        }


        //  userCursor.close();

//        Toast toast = Toast.makeText(getApplicationContext(),
//                timeout,
//                Toast.LENGTH_LONG);
//        toast.show();

        //Создаем таймер обратного отсчета на 20 секунд с шагом отсчета
        //в 1 секунду (задаем значения в миллисекундах):
        new CountDownTimer(mytimeout*1000, 1000) {

            //Здесь обновляем текст счетчика обратного отсчета с каждой секундой
            public void onTick(long millisUntilFinished) {
                mTimer.setText("Осталось: "
                        + millisUntilFinished / 1000);
            }

            //Задаем действия после завершения отсчета (высвечиваем надпись "Бабах!"):
            public void onFinish() {

                mTimer.setText("Время вышло!");

                findViewById(R.id.ImageButton).setVisibility(View.INVISIBLE);

/*потом включить*/


                if (finish != "1") {

//                    CatTask catTask = new CatTask();
//                    catTask.execute("0");
//
//

                    rezult("0");



                }
                else {

                    finish = "0";
                }

                finish();
            }
        }
                .start();


       // startAlarm();  /*потом включить*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        alarmActive = true;

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                1000 * 10, 10, locationListener);
//        locationManager.requestLocationUpdates(
//                LocationManager.NETWORK_PROVIDER, 1000 * 10, 10,
//                locationListener);
        checkEnabled();
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(locationListener);
    }

    @Override
    protected void onDestroy() {
        try {
            if (vibrator != null)
                vibrator.cancel();
        } catch (Exception e) {

        }
        try {
            mediaPlayer.stop();
        } catch (Exception e) {

        }
        try {
            mediaPlayer.release();
        } catch (Exception e) {

        }
        super.onDestroy();
    }


    private LocationListener locationListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            showLocation(location);
        }

        @Override
        public void onProviderDisabled(String provider) {
            checkEnabled();
        }

        @Override
        public void onProviderEnabled(String provider) {
            checkEnabled();
            showLocation(locationManager.getLastKnownLocation(provider));
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            if (provider.equals(LocationManager.GPS_PROVIDER)) {
                LocationGPS.setText("Status: " + String.valueOf(status));
            }
            else if (provider.equals(LocationManager.NETWORK_PROVIDER)) {
                LocationNet.setText("Status: " + String.valueOf(status));
            }
        }
    };

    private void showLocation(Location location) {

        if (location == null)
            return;

        if (location.getProvider().equals(LocationManager.GPS_PROVIDER)) {


            gpslat = String.format(
                    "lat = %1$.8f",location.getLatitude());

            gpslng = String.format(
                    "lon = %1$.8f",location.getLongitude());

            LocationGPS.append(" gps: "+gpslat+","+gpslng);
            LocationNet.append(" network: off");


        }
        else if (location.getProvider().equals(
                LocationManager.NETWORK_PROVIDER)) {

            netlat = String.format(
                    "%1$.8f",location.getLatitude()); /*lat =*/

            netlng = String.format(
                    "%1$.8f",location.getLongitude()); /*lon =*/

            LocationGPS.append(" gps: off");
            LocationNet.append(" network: "+netlat+","+netlng);

        }
    }

    private String formatLocation(Location location) {
        if (location == null)
            return "";
        return String.format(
                "Coordinates: lat = %1$.8f, lon = %2$.8f, time = %3$tF %3$tT",
                location.getLatitude(), location.getLongitude(), new Date(
                        location.getTime()));
    }

    private void checkEnabled() {
        LocationGPS.setText("Enabled: "
                + locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER));
        LocationNet.setText("Enabled: "
                + locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER));
    }


    private void startAlarm() {

        Bundle bundle = this.getIntent().getExtras();
        alarm = (Alarm) bundle.getSerializable("alarm");

        this.setTitle(alarm.getAlarmName());


        if (alarm.getAlarmTonePath() != "") {
            mediaPlayer = new MediaPlayer();
            if (alarm.getVibrate()) {
                vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                long[] pattern = {1000, 200, 200, 200};
                vibrator.vibrate(pattern, 0);
            }
            try {
                mediaPlayer.setVolume(1.0f, 1.0f);
                mediaPlayer.setDataSource(this,
                        Uri.parse(alarm.getAlarmTonePath()));
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
                mediaPlayer.setLooping(true);
                mediaPlayer.prepare();
                mediaPlayer.start();

            } catch (Exception e) {
                mediaPlayer.release();
                alarmActive = false;
            }
        }

    }




    public void onClick(View v) {

        rezult("1");
        finish = "1";

        //     CatTask catTask = new CatTask();
        //      catTask.execute("1");
//


        finish(); /*потом включить*/

    }



    public void Update_Db(String rez,String desc, String req) {

                /*внесение данных в базу*/

        String date = (DateFormat.format("HH:mm:ss dd.MM.yyyy", new Date()).toString());

//        String data = (DateFormat.format("yyyy-MM-dd", new Date()).toString());
//        String time = (DateFormat.format("HH-mm-ss", new Date()).toString());

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


    public void onClick_1(View view) {

        Intent intent = new Intent(activity_2.this, test_db.class);
        startActivity(intent);
    }




    public void request(String status) throws IOException {


        userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLEsys, null);
        userCursor.moveToFirst();


        String id_facility = userCursor.getString(3);
        String id_guard = userCursor.getString(4);
        String URL = userCursor.getString(5);

        GPSTracker_new_gpslat=userCursor.getString(7);
        GPSTracker_new_gpslng=userCursor.getString(8);

        try {
            GPSTracker_new gps = new GPSTracker_new(this);
            int status_1 = 0;
            if(gps.canGetLocation())

            {
//             status_1 = GooglePlayServicesUtil
//                     .isGooglePlayServicesAvailable(getApplicationContext());
//
//             if (status_1 == ConnectionResult.SUCCESS) {

                GPSTracker_new_gpslat = String.format(
                        "%1$.8f", gps.getLatitude());
                GPSTracker_new_gpslng = String.format(
                        "%1$.8f",gps.getLongitude());
                Description = "success " ;



                // }

            } else {
                Log.d("GPS_new", "Координаты! не определены, включите GPS в настройках.");
                GPSTracker_new_gpslat=netlat;
                GPSTracker_new_gpslng=netlng;
                Description = "unclear";
            }
        } catch (Exception e) {
            Log.d("GPS_new", "Координаты! не определены.");
            GPSTracker_new_gpslat=userCursor.getString(7);
            GPSTracker_new_gpslng=userCursor.getString(8);
            Description = "fail";
        }



        // http://inex24.com.ua/app/report.php?guard=6&facility=5&status=4&latitude=50.12345&longitude=50.54321
        String myURL = "http://" + URL + "/app/report.php?guard=" + id_guard + "&facility=" + id_facility + "&status="+status
                + "&latitude="+GPSTracker_new_gpslat + "&longitude="+GPSTracker_new_gpslng + "&comment="+Description;

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

                        String res = response.body().string();
                        String resDate = response.headers().getDate("Date").toString();


                        Scanner s = new Scanner(response.toString()).useDelimiter("\\A");
                        String result = s.hasNext() ? s.next() : "";

                        Log.e("TAG", result);
                        Log.d("TAG", "response is: " + res);
                        Log.d("TAG", "response Date is: " + resDate);

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
                        Req = "-";
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
            Req = "+";
            return true;
        } else {
            Log.v("TAG", "Internet Connection Not Present");
            status = "Internet Connection Not Present";
            Req = "-";
            return false;
        }
    }


    public void rezult(String rez) {

        try{ isreachable();} catch (Exception e) {}


        try {
            if (netlat != null && netlng != null) {
                if (isOnline()) {
                    Desc = netlat + "," + netlng + "," + status;

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

                    }
                    else {
                        Req = "-";
                        Desc = gpslat + "," + gpslng + "," + status;
                    }
                }

                else {
                    if (isOnline()) {

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

            try {
                Integer qwerty = ShowGPS();
                Desc = Desc + " превышение:" + qwerty;
            } catch (Exception e) {}





            Toast toast = Toast.makeText(getApplicationContext(),
                    "Req Update_Db " + Req + " Desc"+ Desc,
                    Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 10, 15);
            toast.show();



            Update_Db(rez, Desc, Req);




        } catch (Exception e) {

        }

        try {

            if (isOnline()) {

                Toast toast = Toast.makeText(getApplicationContext(),
                        "Req request " + Req,
                        Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 10, 15);
                toast.show();

                try {

                    Integer qwerty = ShowGPS();
                    Log.d("GPS_new", "qwerty " + qwerty);
                    if (qwerty > 100)
                    {rez = "4";}
                } catch (Exception e) {}


                request(rez);

                try{Sync test_sync = new Sync();
                    test_sync.run(this);}
                catch (Exception e) {}


            }
            else {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Сайт не доступен. Данные не отправлены. местоположение:" + gpslat+"/"+gpslng+";"+netlat+"/"+netlng,
                        Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 10, 15);
                toast.show();


            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Данные НЕ отправлены на сайт. местоположение:" + gpslat+"/"+gpslng+";"+netlat+"/"+netlng,
                    Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 10, 15);
            toast.show();
            Desc = "-";
        }



    }

    public void onClick_sos(View view) {

        sqlHelper = new DatabaseHelper(this);
        db = sqlHelper.getWritableDatabase();

        userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLEsys, null);
        userCursor.moveToFirst();

        String facility_id = userCursor.getString(3);
        String id_guard = userCursor.getString(4);



        //  Log.d("facility", "facility " + facility_id + " id_guard " + id_guard);

        pd = new ProgressDialog(this);
        pd.setIndeterminateDrawable(this.getResources().getDrawable(R.drawable.red_progress)); /*прикольная фишка*/
        pd.setTitle("Информация");
        pd.setMessage("Отправка данных");

          CatTask catTask = new CatTask();
          catTask.execute(facility_id,id_guard);

        // добавляем кнопку
//        pd.setButton(Dialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//            }
        //   });
        pd.show();

    try {
        TimeUnit.SECONDS.sleep(5);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }

        finish = "1";

        //  if(pd != null)
           //  pd.dismiss();

        finish(); /*потом включить*/

    }



    private Integer ShowGPS() {



        sqlHelper = new DatabaseHelper(this);

        db = sqlHelper.getWritableDatabase();
        userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLEsys, null);
        userCursor.moveToFirst();

        gpslat_old = userCursor.getString(7);
        gpslng_old = userCursor.getString(8);
        radius_old = userCursor.getString(9);
        userCursor.close();



        GPSTracker_new gps = new GPSTracker_new(this);
        int status = 0;
        if(gps.canGetLocation())

        {
            // status = GooglePlayServicesUtil
            //         .isGooglePlayServicesAvailable(getApplicationContext());

            // if (status == ConnectionResult.SUCCESS) {

            String gpslat = String.format(
                    "%1$.8f", gps.getLatitude());
            String gpslng = String.format(
                    "%1$.8f",gps.getLongitude());

            if (gpslat == "")
            {gpslat=netlat;}
            if (gpslng == "")
            {gpslng=netlng;}

            LocationGPS.append(" gps: "+gpslat+","+gpslng);
            LocationNet.append(" gps: "+gpslat+","+gpslng);


//            if (gpslat == "")
//            {gpslat=gpslat_old;
//                Description= "Координаты не определены";}
//            if (gpslng == "")
//            {gpslng=gpslng_old;}


            Double diff = ((Double.parseDouble(gpslat_old)-Double.parseDouble(gpslat))-(Double.parseDouble(gpslng_old)-Double.parseDouble(gpslng)))*100000;

            if (diff < 50)
            {
                diff= diff* -1;
            };


            String re_diff = String.format("%1$.0f",diff);


            re_diff_int = Integer.parseInt(re_diff) - Integer.parseInt(radius_old);



            Log.d("GPS_new ", "Координаты! " + gpslat +" old"+ gpslat_old + "/" + gpslng
                    + " old"+gpslng_old + ", r=" + radius_old +", дистанция= "+ re_diff+ "метров"+ ", разница=" +re_diff_int );

//            Toast toast = Toast.makeText(this,
//                    "Координаты! " + gpslat +" old"+ gpslat_old + "/" + gpslng + " old"+gpslng_old + " r=" + radius_old +" дистанция= "+ re_diff+ "метров",
//                    Toast.LENGTH_LONG);
//            toast.setGravity(Gravity.CENTER, 0, 0);
//            toast.show();



            //}

        } else {
            Log.d("GPS_new", "Координаты! не определены, включите GPS в настройках.");



//            Toast toast = Toast.makeText(this,
//                    "Координаты! не определены, включите GPS в настройках." ,
//                    Toast.LENGTH_LONG);
//            toast.setGravity(Gravity.CENTER, 0, 0);
//            toast.show();
        }
        //String answer = "something";
        return re_diff_int;
    };


    class CatTask extends AsyncTask<String, Integer, Void> {


        //class CatTask extends AsyncTask<Void, Integer, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //  mProgressBar.setVisibility(View.VISIBLE);

            //   test_v.setText("Отправка данных....");
//            mInfoTextView.setText("Полез на крышу");
//            mStartButton.setVisibility(View.INVISIBLE);





        }

        @Override
        protected Void doInBackground(String... main){
            // protected Void doInBackground(Void... params) {

            try {
                int counter = 0;
                //for (String facility_id : main) {

                Get test_at = new Get();

                String url_1 = "http://inex24.com.ua/api/alarm.php?action=trigger&facility=" + main[0];
                String url_2 = "http://inex24.com.ua/api/alarm.php?action=trigger&facility="+ main[0]+"&guard=" + main[1];

//            Log.d("facility", "url_1 " + url_1);
//            Log.d("facility", "url_2 " + url_2);

                test_at.run(url_1,
                        new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {

                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                //todo work with response, parse and etc...
                            }
                        });

                test_at.run(url_2,
                        new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {

                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                //todo work with response, parse and etc...
                            }
                        });



              //  TimeUnit.SECONDS.sleep(1);
           // } catch (InterruptedException e) {
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            //           mInfoTextView.setText("Залез");
//            mStartButton.setVisibility(View.VISIBLE);
//            mHorizontalProgressBar.setProgress(0);
//        mProgressBar.setVisibility(View.INVISIBLE);
//
            //           test_v.setText("Данные отправлены");
       //     pd.setMessage("Данные отправлены");
//            try{
//                TimeUnit.SECONDS.sleep(1);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
          //  if(pd != null)
           //     pd.dismiss();

            //helloTextView.setVisibility(View.INVISIBLE);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);


            //  test_v.setText("Данные отправляются....");
         //   pd.setMessage("Данные отправляются....");
//            try{
//                TimeUnit.SECONDS.sleep(1);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }

        }

    }



}


