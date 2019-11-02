package com.dark.new_test_job.other;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.dark.new_test_job.R;
import com.dark.new_test_job.alarm.database.DatabaseHelper;
import com.dark.new_test_job.alarm.database.UserActivity;

/**
 * Created by dark on 22.11.2016.
 */
public class Update extends FragmentActivity {

    public static final String APP_PREFERENCES = "mysettings";
    public String APP_PREFERENCES_NICKNAME ;
    private String User;
    SharedPreferences mSettings;

    TextView header;

    DatabaseHelper sqlHelper;
    SQLiteDatabase db;
    Cursor userCursor;
    SimpleCursorAdapter userAdapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

      //  getActionBar().setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.update /*activity_main_db*/);

        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        header = (TextView)findViewById(R.id.textView5);


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


        };

//        userAdapter = new SimpleCursorAdapter(this, android.R.layout.two_line_list_item,
//                userCursor, headers, new int[]{android.R.id.text1, android.R.id.text2}, 0);

        header.setText("Найдено элементов: " + String.valueOf(userCursor.getCount()));

        // header.append("\nОБЪЕКТ "+"   ОХРАНИК "+"    Рез. "+"  ДАТА");
       // mList.setAdapter(userAdapter);
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

//            try {
//
//
//                if (isOnline()) {
//                    try{ isreachable();} catch (Exception e) {}
//
//                    synchronization();
//
//                    onResume();
//                    Log.d("date", "Status " + status);
//
//                }
//                else {
//                    Log.d("date", "Status " + status);
//                }
//
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }



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

}
