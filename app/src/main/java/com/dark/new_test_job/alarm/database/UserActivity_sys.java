package com.dark.new_test_job.alarm.database;

/**
 * Created by dark on 22.06.2016.
 */

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dark.new_test_job.R;
import com.dark.new_test_job.alarm.test_db;
import com.dark.new_test_job.alarm.test_db_sys;
import com.dark.new_test_job.phone_info.GPSTracker_new;
import com.dark.new_test_job.phone_info.MyLocationListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.util.Date;

import ru.yandex.yandexmapkit.MapController;
import ru.yandex.yandexmapkit.MapView;
import ru.yandex.yandexmapkit.OverlayManager;


public class UserActivity_sys extends ActionBarActivity {

    private LocationManager locationManager;
    private static String gpslat;
    private static String gpslng;
    private static String netlat;
    private static String netlng;

    private static String gpslat_old;
    private static String gpslng_old;
    private static String radius_old;
    private static String sys_new;
    private static String sys_new_re;

    TextView LocGPS;
    TextView LocNet;

    DatabaseHelper sqlHelper;
    SQLiteDatabase db;
    Cursor userCursor;

    public static final String APP_PREFERENCES = "mysettings";
    public String APP_PREFERENCES_NICKNAME ;//
    private String User;
    SharedPreferences mSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_sys);

        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        if (mSettings.contains(APP_PREFERENCES_NICKNAME)) {
            // выводим данные в TextView
            User = mSettings.getString(APP_PREFERENCES_NICKNAME,"");

            Log.i("test_db", "Имя user: " + mSettings.getString(APP_PREFERENCES_NICKNAME,
                    ""));
            setTitle(/*getTitle() + */" пользователь:"+User);
        }

        long userId=0;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userId = extras.getLong("id");
        }


            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container, PlaceholderFragment.newInstance(userId, User))
                        .commit();
            }


    }


    public static class PlaceholderFragment extends Fragment {

        EditText FACILITY;
        EditText GUARD;
        EditText ID_GUARD;
        EditText ID_FACILITY;
        EditText URL;
        EditText TIMEOUT;
        EditText SH;
        EditText D;
        EditText RADIUS;
        Button delButton;
        Button saveButton;
        TextView Loc_GPS;
        TextView Loc_Net;

        DatabaseHelper sqlHelper;
        SQLiteDatabase db;
        Cursor userCursor;







        public static PlaceholderFragment newInstance(long id, String user) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args=new Bundle();
            args.putLong("id", id);
            args.putString("user", user);
            fragment.setArguments(args);

            return fragment;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setRetainInstance(true);



            sqlHelper = new DatabaseHelper(getActivity());



        }
        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {



            View rootView = inflater.inflate(R.layout.fragment_user_sys, container, false);
            FACILITY = (EditText) rootView.findViewById(R.id.FACILITY);
            GUARD = (EditText) rootView.findViewById(R.id.GUARD);
            ID_GUARD = (EditText) rootView.findViewById(R.id.ID_GUARD);
            ID_FACILITY = (EditText) rootView.findViewById(R.id.ID_FACILITY);
            URL = (EditText) rootView.findViewById(R.id.URL);
            TIMEOUT = (EditText) rootView.findViewById(R.id.TIMEOUT);
            SH = (EditText) rootView.findViewById(R.id.ID_sh);
            D = (EditText) rootView.findViewById(R.id.ID_d);
            RADIUS = (EditText) rootView.findViewById(R.id.ID_r);
            delButton = (Button) rootView.findViewById(R.id.delete);
            saveButton = (Button) rootView.findViewById(R.id.save);

            Loc_GPS = (TextView) rootView.findViewById(R.id.GPS_SH);
            Loc_Net = (TextView) rootView.findViewById(R.id.GPS_D);




            final long id = getArguments() != null ? getArguments().getLong("id") : 0;

            final String User = getArguments() != null ? getArguments().getString("user") : "";


            Log.i("UserActivity_sys", "Имя User_: " + User);

            db = sqlHelper.getWritableDatabase();
            // кнопка удаления
            delButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    db.delete(DatabaseHelper.TABLEsys, "_id = ?", new String[]{String.valueOf(id)});
                    goHome();
                }
            });

            // кнопка сохранения
            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ContentValues cv = new ContentValues();

                        cv.put(DatabaseHelper.COLUMN_CONTR_NAME, FACILITY.getText().toString());
                        cv.put(DatabaseHelper.COLUMN_PERSON_NAME, GUARD.getText().toString());
                    if (User.equals("Admin")) {
                        cv.put(DatabaseHelper.COLUMN_ID_guard, ID_GUARD.getText().toString());
                        cv.put(DatabaseHelper.COLUMN_ID_facility, ID_FACILITY.getText().toString());

                        cv.put(DatabaseHelper.COLUMN_URL, URL.getText().toString());
                    }

                    cv.put(DatabaseHelper.COLUMN_TIMEOUT, TIMEOUT.getText().toString());
                    cv.put(DatabaseHelper.COLUMN_SH, SH.getText().toString());
                    cv.put(DatabaseHelper.COLUMN_D, D.getText().toString());
                    cv.put(DatabaseHelper.COLUMN_RADIUS, RADIUS.getText().toString());

                    if (id > 0) {
                        db.update(DatabaseHelper.TABLEsys, cv, DatabaseHelper.COLUMN_ID + "=" + String.valueOf(id), null);
                    } else {
                        db.insert(DatabaseHelper.TABLEsys, null, cv); //использовать для будильника
                    }
                    goHome();
                }
            });

            // если 0, то добавление
            if (id > 0) {
                delButton.setVisibility(View.GONE);// скрываем кнопку удаления
                // получаем элемент по id из бд
                userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLEsys + " where " +
                        DatabaseHelper.COLUMN_ID + "=?", new String[]{String.valueOf(id)});
                userCursor.moveToFirst();

                FACILITY.setText(userCursor.getString(1));
                GUARD.setText(userCursor.getString(2));
                ID_FACILITY.setText(userCursor.getString(3));
                ID_GUARD.setText(userCursor.getString(4));
                URL.setText(userCursor.getString(5));
                TIMEOUT.setText(userCursor.getString(6));
                SH.setText(userCursor.getString(7));

                D.setText(userCursor.getString(8));
                RADIUS.setText(userCursor.getString(9));


                try {
                    GPSTracker_new gps = new GPSTracker_new(getActivity());

                    if(gps.canGetLocation())

                    {
                        gpslat = String.format(
                                "%1$.8f", gps.getLatitude());
                        gpslng = String.format(
                                "%1$.8f",gps.getLongitude());

                        Loc_GPS.setText(gpslat);
                        Loc_Net.setText(gpslng);
                    }
                }catch (Exception e) {
                        Log.d("GPS", "0");

                        Toast toast = Toast.makeText(getActivity(),
                                "Координаты! не определены." ,
                                Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.TOP, 0, 0);
                        toast.show();
                    }



                userCursor.close();
            } else {
                // скрываем кнопку удаления
                delButton.setVisibility(View.GONE);
            }
            return rootView;
        }

        public void goHome(){
            // закрываем подключение
            db.close();
            // переход к главной activity
            Intent intent = new Intent(getActivity(), test_db_sys.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        }



    }



    public void onClickLocationSettings(View view) {
        startActivity(new Intent(
                android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
    };

    public void ShowGPS(View view) {
        sqlHelper = new DatabaseHelper(this);

        db = sqlHelper.getWritableDatabase();
        userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLEsys, null);
        userCursor.moveToFirst();

        gpslat_old = userCursor.getString(7);
        gpslng_old = userCursor.getString(8);
        radius_old = userCursor.getString(9);
        userCursor.close();

        LocGPS = (TextView) findViewById(R.id.GPS_SH);
        LocNet = (TextView) findViewById(R.id.GPS_D);


        try {
            GPSTracker_new gps = new GPSTracker_new(this);
        int status = 0;
        if(gps.canGetLocation())

        {
//             status = GooglePlayServicesUtil
//                     .isGooglePlayServicesAvailable(getApplicationContext());

   //          if (status == ConnectionResult.SUCCESS) {

            gpslat = String.format(
                    "%1$.8f", gps.getLatitude());
            gpslng = String.format(
                    "%1$.8f",gps.getLongitude());

            Double diff = ((Double.parseDouble(gpslat_old)-Double.parseDouble(gpslat))-(Double.parseDouble(gpslng_old)-Double.parseDouble(gpslng)))*100000;

            if (diff<0)
            {
                diff= diff* -1;
            };

            String re_diff = String.format("%1$.0f",diff);

            Log.d("diff ", "" + re_diff);



            Toast toast = Toast.makeText(this,
                    "Координаты! " + gpslat +" old"+ gpslat_old + "/" + gpslng + " old"+gpslng_old + " r=" + radius_old +" дистанция= "+ re_diff+ "метров ",
                    Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 10, 15);
            toast.show();


                    Log.d("GPS", LocGPS.getText().toString());
                    Log.d("GPS", LocNet.getText().toString());

            LocGPS.setText(gpslat);
            LocNet.setText(gpslng);

            AlertDialog.Builder dialog = new AlertDialog.Builder(UserActivity_sys.this);
            dialog.setTitle("Привязка");
            dialog.setMessage("Использовать это место как центр объекта?");
            dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    String sys_new = "UPDATE "+ DatabaseHelper.TABLEsys +" SET "+DatabaseHelper.COLUMN_SH + " = " + gpslat;
                    String sys_new_re = "UPDATE "+ DatabaseHelper.TABLEsys +" SET "+DatabaseHelper.COLUMN_D + " = " + gpslng;

                    Log.d("GPS", sys_new);
                    Log.d("GPS", sys_new_re);

//                    Log.d("GPS", String.format(
//                            "%1$.8f", LocGPS.getText().toString()));
//                    Log.d("GPS", String.format(
//                            "%1$.8f", LocNet.getText().toString()));


                    db.execSQL(sys_new);
                    db.execSQL(sys_new_re);


                    goHome();

                }
            });
            dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            dialog.show();

           // }

        } else {
            Log.d("GPS", "0");

            Toast toast = Toast.makeText(this,
                    "Координаты! не определены, включите GPS в настройках." ,
                    Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 0, 0);
            toast.show();
        }
        } catch (Exception e) {
            Log.d("GPS", "0");

            sqlHelper = new DatabaseHelper(this);

            db = sqlHelper.getWritableDatabase();
            userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLEsys, null);
            userCursor.moveToFirst();

            gpslat = LocGPS.getText().toString();
            gpslng = LocNet.getText().toString();


            Log.d("GPS", gpslat);
            Log.d("GPS", gpslng);

            Toast toast = Toast.makeText(this,
                    "Координаты! не определены. " + gpslat + "/" + gpslng,
                    Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 0, 0);
            toast.show();

            try {
                AlertDialog.Builder dialog = new AlertDialog.Builder(UserActivity_sys.this);
                dialog.setTitle("Привязка");
                dialog.setMessage("Использовать это место как центр объекта?");
                dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

             sys_new = "UPDATE " + DatabaseHelper.TABLEsys + " SET " + DatabaseHelper.COLUMN_SH + " = " + gpslat.replace(",",".");;
             sys_new_re = "UPDATE " + DatabaseHelper.TABLEsys + " SET " + DatabaseHelper.COLUMN_D + " = " + gpslng.replace(",",".");;

                        Log.d("GPS", sys_new);
                        Log.d("GPS", sys_new_re);



                        db.execSQL(sys_new);
                       db.execSQL(sys_new_re);


                       goHome();

                    }
                });
                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                dialog.show();

        } catch (Exception q) {

                Toast toast_1 = Toast.makeText(this,
                        "Координаты! не определены. " + sys_new + "/" + sys_new_re,
                        Toast.LENGTH_LONG);
                toast_1.setGravity(Gravity.TOP, 0, 0);
                toast_1.show();
            }


            userCursor.close();
        }
    };

    public void goHome(){
        // закрываем подключение
        db.close();
        // переход к главной activity
        Intent intent = new Intent(this, test_db_sys.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }



}