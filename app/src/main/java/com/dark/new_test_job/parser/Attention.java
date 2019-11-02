package com.dark.new_test_job.parser;

/**
 * Created by dark on 12.07.2016.
 */
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.dark.new_test_job.alarm.database.DatabaseHelper;

import java.io.IOException;
import java.util.Scanner;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Attention {

    DatabaseHelper sqlHelper;
    SQLiteDatabase db;
    Cursor userCursor;
    OkHttpClient client = new OkHttpClient();




    public void run() {

//        userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLEsys, null);
//        userCursor.moveToFirst();
//
//
//
//        String URL = userCursor.getString(5);



        //   userCursor.close();



      //  String myURL = "http://" + URL + "/test.php?guard=" + id_guard + "&facility=" + id_facility + "&status="+status;

        String myURL = "http://inex24.com.ua/app/alarm.php?action=trigger";



/*открыть потом*/
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(myURL)
                .build();

        Log.d("TAG", "myURL is: " + myURL);

        client.newCall(request)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

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

                        Log.d("TAG", "response Date is: " + resDate);
                        // Получить ответ и сделать с ним что то

                    }
                });
    }
 }

