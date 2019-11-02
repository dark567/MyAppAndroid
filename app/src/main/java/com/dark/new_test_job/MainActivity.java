package com.dark.new_test_job;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dark.new_test_job.alarm.Alarm;
import com.dark.new_test_job.alarm.AlarmActivity;
import com.dark.new_test_job.alarm.AlarmListAdapter;
import com.dark.new_test_job.alarm.database.Database;
import com.dark.new_test_job.alarm.database.DatabaseHelper;
import com.dark.new_test_job.alarm.test_db_number;
import com.dark.new_test_job.alarm.test_db;
import com.dark.new_test_job.alarm.test_db_number_test;
import com.dark.new_test_job.alarm.test_db_sys;

import com.dark.new_test_job.other.Sync;
import com.dark.new_test_job.other.SyncSettingsActivity;
import com.dark.new_test_job.other.Update;
import com.dark.new_test_job.other.test_db_admin;
import com.dark.new_test_job.parser.Get;
import com.dark.new_test_job.phone_info.*;
import com.dark.new_test_job.qr.QKActivity;
import com.dark.new_test_job.qr.newqr;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener


{
    // это будет именем файла настроек
    public static final String APP_PREFERENCES = "mysettings";
    public String APP_PREFERENCES_NICKNAME ;//
//    private String User;
    SharedPreferences mSettings;

    private static final int NOTIFY_ID = 101;
    private String pass  = "1";
    private TextView text;
    private TelephonyManager manager;
    private TextView mQuestionTextView;
    DatabaseHelper sqlHelper;
    SQLiteDatabase db;
    private Alarm alarm;
    private ProgressBar mProgressBar;

    private String date;
    private String time;

    Boolean rezult = true;

    Cursor userCursor;

    private String status;

    private Alarm.Day[] days = {Alarm.Day.MONDAY, Alarm.Day.TUESDAY, Alarm.Day.WEDNESDAY, Alarm.Day.THURSDAY, Alarm.Day.FRIDAY, Alarm.Day.SATURDAY, Alarm.Day.SUNDAY};



    ImageButton newButton;
    ListView mathAlarmListView;
    AlarmListAdapter alarmListAdapter;
    private static String s;

    ProgressDialog pd;




    private String USER;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setEnabled(false);

        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.INVISIBLE);

        sqlHelper = new DatabaseHelper(this);
        db = sqlHelper.getWritableDatabase();

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



    }

    public void onDestroy() {
        moveTaskToBack(true);

        SharedPreferences.Editor e = mSettings.edit();
        e.putString(APP_PREFERENCES_NICKNAME, USER);
        setTitle(/*getTitle() + */" пользователь:"+USER);
        e.commit();

        super.onDestroy();

        System.runFinalizersOnExit(true);
        System.exit(0);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

            getMenuInflater().inflate(R.menu.main_menu, menu);

        return true;
    }


    // Восстановление состояния деятельности
    @Override
    public void onResume() {

        super.onResume();

//        sqlHelper = new DatabaseHelper(this);
//        db = sqlHelper.getWritableDatabase();

//        String pass = "7777";
//
//        userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLE_p + " where pass = " + pass, null);
//        userCursor.moveToFirst();
//
//        TextView helloTextView = (TextView) findViewById(R.id.textView3);
//
//        String USER = userCursor.getString(1);
//        String PASS = userCursor.getString(2);
//
//        helloTextView.setText(USER + ':' + PASS);




        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Проверка");
        dialog.setMessage("Введите пароль");
        dialog.setCancelable(false);
        dialog.setIcon(R.drawable.logo);
        final EditText input = new EditText(this);

        input.setInputType(InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_VARIATION_PASSWORD);
        input.setSelection(input.getText().length());

        dialog.setView(input);
        dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                String ePass = input.getText().toString();
                SharedPreferences.Editor e = mSettings.edit();
                try {
                userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLE_p + " where pass = " + ePass, null);
                userCursor.moveToFirst();
                TextView helloTextView3 = (TextView) findViewById(R.id.textView3);

                    if (userCursor.getCount() > 0) {
                        USER = userCursor.getString(1);
                        String PASS = userCursor.getString(2);
                        helloTextView3.setText(USER + ':' + PASS);
                    }
                    else {
                     if (ePass.toString().equals("23041980"))
                     {
                         USER = "Admin";
                     }
                        else
                     {
                         e.putString(APP_PREFERENCES_NICKNAME, "");
                         e.commit();

                         dialog.dismiss();
                         onDestroy();
                     }
                    }

                    e.putString(APP_PREFERENCES_NICKNAME, USER);
                    e.commit();
                    dialog.dismiss();
                    setTitle(/*getTitle() + */" USER:"+USER);

                   // UPDATE(); -- пока отключим возможность обновления и проверки новой версии

                } catch (Exception q) {

                    dialog.dismiss();
                    onDestroy();

                }

                      TextView helloTextView = (TextView) findViewById(R.id.textView2);
                      ImageButton imageButton = (ImageButton) findViewById(R.id.allarmButton);




//                if (USER == null) {
//                    switch(ePass) {
//                          case "1":
//                              USER = "Security_guard";
//                              e.putString(APP_PREFERENCES_NICKNAME, USER);
//                              e.commit();
//
//                              helloTextView.setText(input.getText().toString());
//                              helloTextView.append("Пароль Верный");
//                              dialog.dismiss();
//                              setTitle(/*getTitle() + */" пользователь:"+USER);
//
//
//                              imageButton.setVisibility(View.VISIBLE);
//
//                              break;
//                          case "22":
//                              USER = "Head_of_security";
//                              e.putString(APP_PREFERENCES_NICKNAME, USER);
//                              e.commit();
//
//                              helloTextView.setText(input.getText().toString());
//                              helloTextView.setText("Пароль Верный");
//                              dialog.dismiss();
//                              setTitle(/*getTitle() + */" пользователь:"+USER);
//
//                              imageButton.setVisibility(View.INVISIBLE);
//                              break;
//                          case "7777":
//                              USER = "Admin";
//                              e.putString(APP_PREFERENCES_NICKNAME, USER);
//                              e.commit();
//
//                              helloTextView.setText(input.getText().toString());
//                              helloTextView.setText("Пароль Верный");
//                              dialog.dismiss();
//                              setTitle(/*getTitle() + */" пользователь:"+USER);
//                              imageButton.setVisibility(View.INVISIBLE);
//                              break;
//                          default:
//
//                              dialog.dismiss();
//                              onDestroy(); //потом включить
//                              break;
//                    }
//                }


                      if (mSettings.contains(APP_PREFERENCES_NICKNAME)) {
                          // выводим данные в TextView
                          helloTextView.setText(mSettings.getString(APP_PREFERENCES_NICKNAME,
                                  ""));
                          Log.i("Share", "Имя user: " + mSettings.getString(APP_PREFERENCES_NICKNAME,
                                  ""));
                      }


//                  }
//                   else {
//                     // dialog.cancel();
//                      dialog.dismiss();
//                      onDestroy(); //потом включить
//                  }
             // }
//              catch (Exception e) {
//                  dialog.dismiss();
//                  onDestroy(); //потом включить
//                 // System.out.println("Необязательный блок, но раз уже написан, то выполнятся будет не зависимо от того было исключение или нет");
//              }
            }
        });

        dialog.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                onDestroy();
            }
        });

//        if (input.getText().toString().equals(""))
//        {
//            // Здесь код, если EditText пуст
//           // dialog.dismiss();
//            onDestroy();
//        }


        dialog.show();


    }


    public void onClick_1(View view) {
        TextView helloTextView = (TextView)findViewById(R.id.textView2);
        helloTextView.setText("переход к вводу пароля");


        Bundle extras = getIntent().getExtras();
        helloTextView.append("login/pass ");
        helloTextView.append(extras.getString("login"));
        helloTextView.append(extras.getString("pass"));

           /*if (sting1 == null) {} */
      //  String pass = "z";
        String etLName = getIntent().getExtras().getString("pass");

        if (etLName != null){
        if ( etLName.equals(pass) /*etLName == pass*/) {


        }
        else {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Пароль НЕ верный! "+etLName+"/"+pass,
                    Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            LinearLayout toastContainer = (LinearLayout) toast.getView();
            ImageView catImageView = new ImageView(getApplicationContext());
            catImageView.setImageResource(R.drawable.logo);
            toastContainer.addView(catImageView, 0);
            toast.show();
        }}

    }

    public void onClick_2(View view) {


        TextView helloTextView = (TextView)findViewById(R.id.textView2);

        manager = (TelephonyManager)getSystemService(
                Context.TELEPHONY_SERVICE);

        helloTextView.append("\nCallState:\t" +
                convertCallStateToString(manager.getCallState()));
//        text.append("\nDevice ID:\t" +
//        		manager.getDeviceId());
//		text.append("\nDevice Software Version:\t" +
//        		manager.getDeviceSoftwareVersion());
//        text.append("\nLine1 Number:\t" +
//        		manager.getLine1Number());
        helloTextView.append("\nNetwork Type:\t" +
                convertNetworkTypeToString(manager.getNetworkType()));
        helloTextView.append("\nNetwork Country ISO:\t" +
                manager.getNetworkCountryIso());
        helloTextView.append("\nNetwork Operator:\t" +
                manager.getNetworkOperator());
        helloTextView.append("\nNetwork Operator Name:\t" +
                manager.getNetworkOperatorName());
//		text.append("\nPhone Type:\t" +
//        		convertPhoneTypeToString(manager.getPhoneType()));
        helloTextView.append("\nData Activity:\t" +
                convertDataActivityToString(manager.getDataActivity()));
        helloTextView.append("\nData State:\t" +
                convertDataConnStateToString(manager.getDataState()));
//		text.append("\nSubscriber ID:\t" +
//        		manager.getSubscriberId());
//		text.append("\nVoice Mail Alpha Tag:\t" +
//        		manager.getVoiceMailAlphaTag());
//        text.append("\nVoice Mail Number:\t" +
//        		manager.getVoiceMailNumber());
        helloTextView.append("\nIcc Card:\t" +
                manager.hasIccCard());
        helloTextView.append("\nNetwork Roaming:\t" +
                manager.isNetworkRoaming());

//            GsmCellLocation gsmCell = (GsmCellLocation)manager.getCellLocation();
//            if (gsmCell != null) {
//                text.append("\nGSM Cell Location:");
//        	text.append("\n\tCID:\t" + gsmCell.getCid());
//        	text.append("\n\tLAC:\t" + gsmCell.getLac());
//            }
    }

    private String convertCallStateToString(int callState) {
        switch (callState) {
            case TelephonyManager.CALL_STATE_IDLE:
                return "IDLE";
            case TelephonyManager.CALL_STATE_OFFHOOK:
                return "OFFHOOK";
            case TelephonyManager.CALL_STATE_RINGING:
                return "RINGING";
            default:
                return "Not defined";
        }
    }

    private String convertNetworkTypeToString(int networkType) {
        switch (networkType) {
            case TelephonyManager.NETWORK_TYPE_1xRTT:
                return "1xRTT";
            case TelephonyManager.NETWORK_TYPE_CDMA:
                return "CDMA";
            case TelephonyManager.NETWORK_TYPE_EDGE:
                return "EDGE";
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                return "EVDO revision 0";
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                return "EVDO revision A";
            //case TelephonyManager.NETWORK_TYPE_EVDO_B:
            //    return "EVDO revision B";
            case TelephonyManager.NETWORK_TYPE_GPRS:
                return "GPRS";
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                return "HSDPA";
            case TelephonyManager.NETWORK_TYPE_HSPA:
                return "HSPA";
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                return "HSUPA";
            //case TelephonyManager.NETWORK_TYPE_IDEN:
            //    return "iDen";
            case TelephonyManager.NETWORK_TYPE_UMTS:
                return "UMTS";
            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                return "Unknown";
            default:
                return "Not defined";
        }


    }

    private String convertDataActivityToString(int dataActivity) {
        switch (dataActivity) {
            case TelephonyManager.DATA_ACTIVITY_DORMANT:
                return "Dormant";
            case TelephonyManager.DATA_ACTIVITY_IN:
                return "In";
            case TelephonyManager.DATA_ACTIVITY_INOUT:
                return "In-out";
            case TelephonyManager.DATA_ACTIVITY_NONE:
                return "None";
            case TelephonyManager.DATA_ACTIVITY_OUT:
                return "Out";
            default:
                return "Not defined";
        }
    }

    private String convertDataConnStateToString(int dataConnState) {
        switch (dataConnState) {
            case TelephonyManager.DATA_CONNECTED:
                return "Data connected";
            case TelephonyManager.DATA_CONNECTING:
                return "Data connecting";
            case TelephonyManager.DATA_DISCONNECTED:
                return "Data suspended";
            case TelephonyManager.DATA_SUSPENDED:
                return "Data suspended";
            default:
                return "Not defined";
        }
    }

    private String convertPhoneTypeToString(int phoneType) {
        switch (phoneType) {
            case TelephonyManager.PHONE_TYPE_GSM:
                return "GSM";
            case TelephonyManager.PHONE_TYPE_CDMA:
                return "CDMA";
            case TelephonyManager.PHONE_TYPE_NONE:
                return "NONE";
            default:
                return "Not defined";
        }
    }




    public void onClick_3(View view) {

        Intent intent = new Intent(MainActivity.this, AlarmActivity.class/*AboutActivity.class*/);
        startActivity(intent);

    }


    public void on_alarm (View view) {

        Toast toast = Toast.makeText(getApplicationContext(),
                "тревога тревога. волк украл зайчат",
                Toast.LENGTH_LONG);
        toast.show();

    }


    public void onClick_4(View view) {Context context = getApplicationContext();
        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context,
                0, notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);
        Resources res = context.getResources();
        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentIntent(contentIntent)

                .setSmallIcon(R.drawable.logo)
// большая картинка
                .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.logo))
//.setTicker(res.getString(R.string.warning)) // текст в строке состояния
                .setTicker("Последнее китайское предупреждение!")
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
//.setContentTitle(res.getString(R.string.notifytitle)) // Заголовок уведомления
                .setContentTitle("Напоминание")
//.setContentText(res.getString(R.string.notifytext))
                .setContentText("Предупреждение .... текст ...."); // Текст уведомленимя
// Notification notification = builder.getNotification(); // до API 16
        Notification notification = builder.build();
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFY_ID, notification);
        notification.defaults = Notification.DEFAULT_SOUND |
                Notification.DEFAULT_VIBRATE;
    }

    public void onClick_5(View view) {

        Intent intent = new Intent(MainActivity.this, Admin_pass.class);
        startActivity(intent);

//        Database.init(MainActivity.this);
//
//        final List<Alarm> alarms = Database.getAll();
//        alarmListAdapter.setMathAlarms(alarms);

//        runOnUiThread(new Runnable() {
//            public void run() {
//                // reload content
//                MainActivity.this.alarmListAdapter.notifyDataSetChanged();
//                if (alarms.size() < 0) {
//                    findViewById(android.R.id.empty).setVisibility(View.INVISIBLE);
//                } else {
//                    findViewById(android.R.id.empty).setVisibility(View.VISIBLE);
//                }
//            }
//        });

    }


    public void onClick_6(View v) {

//        MyLocationListener.SetUpLocationListener(this);

//        mQuestionTextView = (TextView)findViewById(R.id.textView2);
//
//        mQuestionTextView.setText("координаты");
//
//        String kord = MyLocationListener.imHere.toString();
//
//
//        if (kord != null) {
//            mQuestionTextView.append(kord);
//        }
//        else {mQuestionTextView.append("NULL");
//        }
//        Toast.makeText(MainActivity.this,
//                "321",
//                Toast.LENGTH_SHORT).show();




// Пока ничего не делает, но скоро будет!

        Intent intent = new Intent(MainActivity.this, GpsTreker.class);
        startActivity(intent);
    }


    public void onClick_my(View view) {

        if (USER.equals("Admin")) {

             Intent intent = new Intent(MainActivity.this, activity_2.class);
             startActivity(intent);

        }
      //  Intent intent = new Intent(MainActivity.this, activity_2.class);
      // startActivity(intent);

    }

    public void onClick_alarm(View view) {

        sqlHelper = new DatabaseHelper(this);
        db = sqlHelper.getWritableDatabase();

        userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLEsys, null);
        userCursor.moveToFirst();

        String facility_id = userCursor.getString(3);
        String id_guard = userCursor.getString(4);

        CatTask catTask = new CatTask();
        catTask.execute(facility_id, id_guard);

        pd = new ProgressDialog(this);
        // pd.setIndeterminateDrawable(this.getResources().getDrawable(R.drawable.red_progress)); /*прикольная фишка*/
        // pd.setTitle("Title");
        pd.setMessage("Отправка данных");


        pd.show();

    }

    public void onPutSettings(View v){

//        SharedPreferences.Editor editor = mSettings.edit();
//        editor.putString(APP_PREFERENCES_NAME, "Васька");
//        editor.apply();

//        Set<String> catnames = new HashSet<String>();
//        catnames.add("Мурзик");
//        catnames.add("Барсик");
//        catnames.add("Рыжик");
//        SharedPreferences.Editor e = mSettings.edit();
//        e.putStringSet("strSetKey", catnames);
//        e.apply();
    }

    // считываем имена котов обратно
    public void onShowSettings(View v)
    {
//        if(mSettings.contains(APP_PREFERENCES_NAME)) {
//            Log.i("Share", "Имя кота: " + mSettings.getString(APP_PREFERENCES_NAME, ""));
//
//
//
//
//            //nicknameText.setText(mSettings.getString(APP_PREFERENCES_NAME, ""));
//        }
//
//
//        Set<String> ret = mSettings.getStringSet("strSetKey", new HashSet<String>());
//        for(String r : ret) {
//            Log.i("Share", "Имя кота: " + r);
      //  }
    }







    public boolean click1(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Intent intent = new Intent(MainActivity.this, AboutActivity.class);


        TextView helloTextView = (TextView)findViewById(R.id.textView2);
        helloTextView.setText("Hello, РРРР!");
        startActivity(intent);

        switch (id) {
            case R.id.action_cat1:
                startActivity(intent);
                return true;
            case R.id.action_cat2:
                startActivity(intent);
                return true;
            case R.id.action_cat3:
                startActivity(intent);
                return true;
            case R.id.action_cat4:
            {//создаем и отображаем текстовое уведомление
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Пораааа заняться делом!",
                        Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                LinearLayout toastContainer = (LinearLayout) toast.getView();
                ImageView catImageView = new ImageView(getApplicationContext());
                catImageView.setImageResource(R.drawable.logo);
                toastContainer.addView(catImageView, 0);
                toast.show();}
            return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == R.id.action_add) {
//            new AddFeedPopup().show(getFragmentManager(), AddFeedPopup.class.getName());
//            return true;
//        } else if (item.getItemId() == R.id.action_prefs) {
//            startActivity(new Intent(this, SyncSettingsActivity.class));
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

     switch (id) {

         case R.id.action_cat1:
             if (USER.equals("Admin")) {

                 // sqlHelper = new DatabaseHelper((MainActivity) getApplicationContext());
                 db = sqlHelper.getReadableDatabase();
                 sqlHelper.onClear(db);

                 Database.deleteAll();

             }else {
                 Toast toast = Toast.makeText(getApplicationContext(),
                         "Извините удаление разрешено только для разработчика.",
                         Toast.LENGTH_LONG);
                 toast.setGravity(Gravity.CENTER, 10, 15);
                 toast.show();
             }
             return true;
         case R.id.action_cat2: /*синхронизация*/
             if (USER.equals("Admin") || USER.equals("Head_of_security")) {

                 if (isOnline()) {
                     try {
                         isreachable();
                     } catch (Exception e) {
                     }

//                    synchronization();
                     Sync test_sync = new Sync();

                     test_sync.run(this);


                     Log.d("date", "Status " + status);

                 } else {
                     Log.d("date", "Status " + status);
                 }

             }
             else {
                 Toast toast = Toast.makeText(getApplicationContext(),
                         "Извините доступ только для разработчика или начальника охраны.",
                         Toast.LENGTH_LONG);
                 toast.setGravity(Gravity.CENTER, 10, 15);
                 toast.show();
             }
             return true;

         case R.id.action_cat3: /*обновление расписания*/

             if (USER.equals("Admin") || USER.equals("Head_of_security")) {

                 if (isOnline()) {
                     try {
                         isreachable();
                     } catch (Exception e) {
                     }

                     replication();


                     Log.d("date", "Status " + status);

                 } else {
                     Log.d("date", "Status " + status);
                 }
             }
             else {
                 Toast toast = Toast.makeText(getApplicationContext(),
                         "Извините доступ только для разработчика или начальника охраны.",
                         Toast.LENGTH_LONG);
                 toast.setGravity(Gravity.CENTER, 10, 15);
                 toast.show();
             }

             return true;

         case R.id.action_cat4: /*обновление расписания*/

//               Log.d("facility", "KEY_AUTO_SYNC " + KEY_AUTO_SYNC);
//
//               Log.d("facility", "KEY_AUTO_SYNC_INTERVAL " + KEY_AUTO_SYNC_INTERVAL);
//               Log.d("facility", "KEY_AUTO_SYNC_INTERVAL " + R.string.auto_sync_interval);
//
//                final long interval = Long.parseLong((
//                               KEY_AUTO_SYNC_INTERVAL,
//                               getString(R.string.auto_sync_interval_default)
//                       );


             return true;

         case R.id.action_cat_attention: /*тревога*/

             sqlHelper = new DatabaseHelper(this);
             db = sqlHelper.getWritableDatabase();

             userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLEsys, null);
             userCursor.moveToFirst();

             String facility_id = userCursor.getString(3);
             String id_guard = userCursor.getString(4);

             CatTask catTask = new CatTask();
             catTask.execute(facility_id, id_guard);

             pd = new ProgressDialog(this);
             // pd.setIndeterminateDrawable(this.getResources().getDrawable(R.drawable.red_progress)); /*прикольная фишка*/
             // pd.setTitle("Title");
             pd.setMessage("Отправка данных");


             pd.show();


             return true;

         case R.id.action_settings:
             if (USER.equals("Admin")) {

                 startActivity(new Intent(this, SyncSettingsActivity.class));
             }
             else {
                 Toast toast = Toast.makeText(getApplicationContext(),
                         "Извините доступ только для разработчика.",
                         Toast.LENGTH_LONG);
                 toast.setGravity(Gravity.CENTER, 10, 15);
                 toast.show();
             }

             return true;

         case R.id.about:

             //сервисы
//               ActivityManager am = (ActivityManager) this
//                       .getSystemService(ACTIVITY_SERVICE);
//               List<ActivityManager.RunningServiceInfo> rs = am.getRunningServices(50);
//
//               for (int i = 0; i < rs.size(); i++) {
//                   ActivityManager.RunningServiceInfo rsi = rs.get(i);
//                   Log.i("Service", "Process " + rsi.process + " with component "
//                           + rsi.service.getClassName());
//               }
             if (USER.equals("Admin") || USER.equals("Head_of_security")) {
             Intent activity_AboutActivity = new Intent(MainActivity.this, AboutActivity.class);
             startActivity(activity_AboutActivity);
             }else {
                 Toast toast = Toast.makeText(getApplicationContext(),
                         "Извините доступ только для разработчика или начальника охраны.",
                         Toast.LENGTH_LONG);
                 toast.setGravity(Gravity.CENTER, 10, 15);
                 toast.show();
             }

             return true;
         default:
             return super.onOptionsItemSelected(item);
     }

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action

            //Intent intent = new Intent(MainActivity.this, QKActivity.class);
            Intent intent = new Intent(MainActivity.this, newqr.class);
            startActivity(intent);



        } else if (id == R.id.nav_gallery) {
            if (USER.equals("Admin") || USER.equals("Head_of_security")) {
                Intent intent = new Intent(MainActivity.this, test_db.class/*AboutActivity.class*/);
                startActivity(intent);
            }else {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Извините, доступ только для разработчика или начальника охраны.",
                        Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 10, 15);
                toast.show();
            }

        } else if (id == R.id.nav_slideshow) {
            if (USER.equals("Admin") || USER.equals("Head_of_security")) {
                Intent intent = new Intent(MainActivity.this, GpsTreker.class);
                startActivity(intent);
            }else {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Извините, доступ только для разработчика или начальника охраны.",
                        Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 10, 15);
                toast.show();
            }

        }else if (id == R.id.list_number) {

                Intent intent = new Intent(MainActivity.this, test_db_number_test.class);
                startActivity(intent);


        }else if (id == R.id.dic_qr) {

        Intent intent = new Intent(MainActivity.this, test_db_number_test.class);
        startActivity(intent);


        }else if (id == R.id.nav_manage) {
            if (USER.equals("Admin") || USER.equals("Head_of_security")) {
                Intent intent = new Intent(MainActivity.this, test_db_sys.class);
                startActivity(intent);
            }else {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Извините, доступ только для разработчика или начальника охраны.",
                        Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 10, 15);
                toast.show();
            }
        }
         else if (id == R.id.nav_send) {
            if (USER.equals("Admin") || USER.equals("Head_of_security")) {
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
            }else {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Извините, доступ только для разработчика или начальника охраны.",
                        Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 10, 15);
                toast.show();
            }

        }
        else if (id == R.id.alarm) {
            if (USER.equals("Admin") || USER.equals("Head_of_security")) {
                Intent intent = new Intent(MainActivity.this, AlarmActivity.class);
                startActivity(intent);
            }
        }
        else if (id == R.id.phone_info) {
            if (USER.equals("Admin") || USER.equals("Head_of_security")) {
                Intent intent = new Intent(MainActivity.this, PhoneInfoActivity.class);
                startActivity(intent);
            }else {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Извините, доступ только для разработчика или начальника охраны.",
                        Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 10, 15);
                toast.show();
            }
        }
        else if (id == R.id.nav_pass) {
            if (USER.equals("Admin")) {
                Intent intent = new Intent(MainActivity.this, test_db_admin.class);
                startActivity(intent);
            }else {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Извините, доступ только для разработчика",
                        Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 10, 15);
                toast.show();
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setMathAlarm(Alarm alarm) {
        this.alarm = alarm;
    }


    public void replication() {   /*синхронизация расписания*/

        sqlHelper = new DatabaseHelper(this);
        db = sqlHelper.getWritableDatabase();

        userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLEsys, null);
        userCursor.moveToFirst();

        String facility_id = userCursor.getString(3);

        Get test = new Get();

        test.run("http://inex24.com.ua/api/timetable.php?action=get_timetable&facility=" + facility_id,
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
                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    TextView myTextView = (TextView) findViewById(R.id.textView2);
                                    myTextView.setText(responseData);
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

                                                    Database.init(getApplicationContext());

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

            i=i+1;



        }

        catcursor.close();

        for (int j=0 ; j < status_.length ; j++ ) {
            // result = result + mice[i];
            Log.d("catname", "url_ " + url_[j]);
            Log.d("catname", "id " + urlnew_[j]);
            db.execSQL(urlnew_[j]);

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



                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
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
            pd.setMessage("Данные отправлены");
            try{
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//
            pd.dismiss();

            //helloTextView.setVisibility(View.INVISIBLE);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);


            //  test_v.setText("Данные отправляются....");
            pd.setMessage("Данные отправляются....");
            try{
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

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

    public boolean isOnline() {
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


    public void ShowGPS(View view) {

        GPSTracker_new gps = new GPSTracker_new(this);
        int status = 0;
        if(gps.canGetLocation())

        {
           // status = GooglePlayServicesUtil
           //         .isGooglePlayServicesAvailable(getApplicationContext());

           // if (status == ConnectionResult.SUCCESS) {

        String gpslat = String.format(
                "lat = %1$.6f", gps.getLatitude());
        String gpslng = String.format(
                "lon = %1$.6f",gps.getLongitude());

             Toast toast = Toast.makeText(this,
                    "Координаты! "+gpslat+"/"+gpslng ,
                    Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();


                Log.d("GPS ", "" + gpslat + "-"
                        + gpslng);
 //}

            } else {
            Log.d("GPS", "0");

            Toast toast = Toast.makeText(this,
                    "Координаты! не определены" ,
                    Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            }

    };


    public void UPDATE() {

        Intent notificationIntent = new Intent(this, Update.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this,
                0, notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);
        Resources res = this.getResources();
        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentIntent(contentIntent)

                .setSmallIcon(R.drawable.logo)
// большая картинка
                .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.logo))
//.setTicker(res.getString(R.string.warning)) // текст в строке состояния
             //   .setTicker("Последнее китайское предупреждение!")
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
//.setContentTitle(res.getString(R.string.notifytitle)) // Заголовок уведомления
                .setContentTitle("Обновление...")
//.setContentText(res.getString(R.string.notifytext))
                .setContentText("Есть новая версия. Необходимо обновить программу."); // Текст уведомленимя
// Notification notification = builder.getNotification(); // до API 16
        Notification notification = builder.build();
        NotificationManager notificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFY_ID, notification);
        notification.defaults = Notification.DEFAULT_SOUND |
                Notification.DEFAULT_VIBRATE;

    }


}


