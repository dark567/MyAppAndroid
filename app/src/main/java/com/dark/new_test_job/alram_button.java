package com.dark.new_test_job;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dark.new_test_job.alarm.database.DatabaseHelper;
import com.dark.new_test_job.parser.Get;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.logging.Handler;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by dark on 22.08.2016.
 */
public class alram_button extends Activity {
    DatabaseHelper sqlHelper;
    SQLiteDatabase db;
    Cursor userCursor;
    TextView  test_v;
    private ProgressBar mProgressBar;

    ProgressDialog pd;



    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);


        setContentView(R.layout.alarm_button);


        test_v = (TextView) findViewById(R.id.tV2);
        mProgressBar = (ProgressBar) findViewById(R.id.alarm_progress);
        mProgressBar.setVisibility(View.INVISIBLE);
    }

//    public void onClick_alarm(View view) {
//
//
//        sqlHelper = new DatabaseHelper(this);
//        db = sqlHelper.getWritableDatabase();
//
//
//
//        userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLEsys, null);
//        userCursor.moveToFirst();
//
//        String facility = userCursor.getString(3);
//        String URL = userCursor.getString(5);
//
//          //http://inex24.com.ua/api/alarm.php?action=trigger&facility=1
//       // String myURL = "http://" + URL + "/app/alarm.php?action=trigger"; /*измененно для кода охраника*/
//        String myURL = "http://" + URL + "/api/alarm.php?action=trigger&facility=" + facility;
//        Log.d("TAG", "myURL is: " + myURL);
//
//
//
//        Toast toast = Toast.makeText(getApplicationContext(),
//                "myURL " + myURL,
//                Toast.LENGTH_LONG);
//        toast.show();
//
//
//
//        Get test_at = new Get();
//
//        test_at.run(myURL,
//                new Callback() {
//                    @Override
//                    public void onFailure(Call call, IOException e) {
//
//                        }
//
//                    @Override
//                    public void onResponse(Call call,Response response) throws IOException {
//                        //todo work with response, parse and etc...
//                        // ... check for failure using `isSuccessful` before proceeding
//
//                    }
//                });
//
//
//           userCursor.close();
//
//
//
//    }

    public void onClick_alarm(View view) {
        sqlHelper = new DatabaseHelper(this);
        db = sqlHelper.getWritableDatabase();

        userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLEsys, null);
        userCursor.moveToFirst();

        String facility_id = userCursor.getString(3);
        String id_guard = userCursor.getString(4);

        CatTask catTask = new CatTask();
        catTask.execute(facility_id,id_guard);

      //  Log.d("facility", "facility " + facility_id + " id_guard " + id_guard);

        pd = new ProgressDialog(this);
       // pd.setIndeterminateDrawable(this.getResources().getDrawable(R.drawable.red_progress)); /*прикольная фишка*/
        // pd.setTitle("Title");
        pd.setMessage("Отправка данных");
        // добавляем кнопку
//        pd.setButton(Dialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//            }
     //   });
        pd.show();
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


}