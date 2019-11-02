package com.dark.new_test_job.phone_info;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dark.new_test_job.R;
import com.dark.new_test_job.alarm.database.DatabaseHelper;

import java.lang.reflect.Method;

/**
 * Created by dark on 11.10.2016.
 */
//String phoneNumber = "";

public class CallReceiver extends BroadcastReceiver {
    String phoneNumber = "";

    DatabaseHelper sqlHelper;
    SQLiteDatabase db;
    Cursor userCursor;
    public static String TAG = "PhoneStateReceiver";
    public static Integer rezult = 0;
   // public static String TAG;


    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.NEW_OUTGOING_CALL")) {
            // Outgoing call
            phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            Log.d(TAG, "PhoneStateReceiver **Outgoing call " + phoneNumber);


            sqlHelper = new DatabaseHelper(context);

            db = sqlHelper.getReadableDatabase();

            Cursor catcursor = db.rawQuery("select * from "+ DatabaseHelper.TABLE_number +" ORDER BY _id DESC", null);

            String[] number_ = new String[catcursor.getCount()];
            String[] name_ = new String[catcursor.getCount()];
            int i = 0;

            String name;
            String number;


            while (catcursor.moveToNext()) {

                number = catcursor.getString(catcursor.getColumnIndex(DatabaseHelper.COLUMN_NUMBER));
                name = catcursor.getString(catcursor.getColumnIndex(DatabaseHelper.COLUMN_NAME));

                name_[i] = name;
                number_[i] = number;
                i=i+1;
            }
           catcursor.close();



            for (int j=0 ; j < name_.length ; j++ ) {
                // result = result + mice[i];
                Log.d("number", "ФИО " + name_[j]);
                Log.d("number", "Номер телефона" + number_[j]);



                if (phoneNumber.replace(" ","").replace(")","").replace("(","").replace("-","").equals(number_[j]))
                {rezult = 1;
                 Log.d("number", "Совпадение " + number_[j] + " = " + phoneNumber);
                }
                else {Log.d("number", "НЕ совпадение " + number_[j] + " != " + phoneNumber);}

                if (number_[j].equals("*"))
                {rezult = 1;
                    Log.d("number", "ATTENTION " + number_[j] + " разрешен доступ на все номера ");
                }



            }

            if (rezult != 1) {
            setResultData(null);

                Toast toast = Toast.makeText(context,
                        "Исходящий вызов на запрещенный номер. " +  phoneNumber /*+ " rezult" + rezult*/,
                        Toast.LENGTH_LONG);
                LinearLayout toastContainer = (LinearLayout) toast.getView();
                ImageView catImageView = new ImageView(context);
                catImageView.setImageResource(R.drawable.stop);
                toastContainer.addView(catImageView, 0);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();

                rezult = 0;

            }// Kills the outgoing call
            else
            {Log.d("number", "Отмена отмены ");

                Toast toast = Toast.makeText(context,
                        "Исходящий вызов на разрешенный номер. " +  phoneNumber  /*+ " rezult" + rezult*/,
                        Toast.LENGTH_LONG);
                LinearLayout toastContainer = (LinearLayout) toast.getView();
                ImageView catImageView = new ImageView(context);
                catImageView.setImageResource(R.drawable.start);
                toastContainer.addView(catImageView, 0);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();

                rezult = 0;

            }

        }


         else if (intent.getAction().equals("android.intent.action.PHONE_STATE")){
        Log.d(TAG, "PhoneStateReceiver **unexpected intent.action=" + intent.getAction());
            String phone_state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            if (phone_state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                //телефон звонит, получаем входящий номер
                phoneNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);


            } else if (phone_state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)){
                //телефон находится в режиме звонка (набор номера / разговор)
            } else if (phone_state.equals(TelephonyManager.EXTRA_STATE_IDLE)){
                //телефон находиться в ждущем режиме. Это событие наступает по окончанию разговора, когда мы уже знаем номер и факт звонка
            }
        }
    }



    public boolean killCall(Context context) {
        try {
            // Get the boring old TelephonyManager
            TelephonyManager telephonyManager =
                    (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

            // Get the getITelephony() method
            Class classTelephony = Class.forName(telephonyManager.getClass().getName());
            Method methodGetITelephony = classTelephony.getDeclaredMethod("getITelephony");

            // Ignore that the method is supposed to be private
            methodGetITelephony.setAccessible(true);

            // Invoke getITelephony() to get the ITelephony interface
            Object telephonyInterface = methodGetITelephony.invoke(telephonyManager);

            // Get the endCall method from ITelephony
            Class telephonyInterfaceClass =
                    Class.forName(telephonyInterface.getClass().getName());
            Method methodEndCall = telephonyInterfaceClass.getDeclaredMethod("endCall");

            // Invoke endCall()
            methodEndCall.invoke(telephonyInterface);

        } catch (Exception ex) { // Many things can go wrong with reflection calls
            Log.d("PhoneStateReceiver","PhoneStateReceiver **" + ex.toString());
            return false;
        }
        return true;
    }


}
