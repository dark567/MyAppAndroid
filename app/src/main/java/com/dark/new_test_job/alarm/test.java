package com.dark.new_test_job.alarm;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.ListView;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;
import android.widget.SimpleCursorAdapter;

import com.dark.new_test_job.Admin_pass;
import com.dark.new_test_job.R;
import com.dark.new_test_job.alarm.database.DatabaseHelper;
import com.dark.new_test_job.alarm.database.Database;
import com.dark.new_test_job.alarm.database.UserActivity;

/**
 * Created by dark on 21.04.2016.
 */
public class test extends ActionBarActivity {

    private static final String DATABASE_NAME = "userstore.db"; // название бд

    ListView mList;
    ListView mSYSList;
    TextView header;
    TextView header1;
    Database sqlHelper1;
    DatabaseHelper sqlHelper;
    SQLiteDatabase db;
    SQLiteDatabase db1;
    Cursor userCursor;
    Cursor userSysCursor;
    SimpleCursorAdapter userSysAdapter;
    SimpleCursorAdapter userAdapter;
    String space;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myalarm);

        header = (TextView)findViewById(R.id.header);

        mList = (ListView)findViewById(R.id.list);
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


            }
        });


        mSYSList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

               Intent intent = new Intent(getApplicationContext(),/*Admin_pass.class*/ UserActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

         sqlHelper = new DatabaseHelper(getApplicationContext());

    }
    @Override
    public void onResume() {
        super.onResume();
        // открываем подключение
        db = sqlHelper.getReadableDatabase();



        db1 = sqlHelper1.getReadableDatabase();
        //получаем данные из бд

        try {
        userCursor =  db1.rawQuery("select * from "+ Database.ALARM_TABLE, null);
        String[] headers = new String[] {Database.COLUMN_ALARM_NAME, Database.COLUMN_ALARM_TIME};
        userAdapter = new SimpleCursorAdapter(this, android.R.layout.two_line_list_item,
                userCursor, headers, new int[]{android.R.id.text1, android.R.id.text2}, 0);
        header.setText("Найдено элементов: " + String.valueOf(userCursor.getCount()));
        mList.setAdapter(userAdapter);
        }
        catch (Exception e) {
            System.out.println("ошибка в базе Database.ALARM_TABLE");
            header1.append("ошибка в базе Database.ALARM_TABLE");
        }


try {


    userSysCursor = db.rawQuery("select * from " + DatabaseHelper.TABLE, null);
    String[] headersSys = new String[]{DatabaseHelper.COLUMN_CONTR_NAME, DatabaseHelper.COLUMN_DATA};
    userSysAdapter = new SimpleCursorAdapter(this, android.R.layout.two_line_list_item,
            userSysCursor, headersSys, new int[]{android.R.id.text1, android.R.id.text2}, 0);
    header1.append("Найдено элементов: " + String.valueOf(userSysAdapter.getCount()));
    mSYSList.setAdapter(userSysAdapter);
}

    catch (Exception e) {
        System.out.println("ошибка в базе DatabaseHelper.TABLE");
        header1.append("ошибка в базе DatabaseHelper.TABLE");
              }}

    @Override
    public void onDestroy(){
        super.onDestroy();
        // Закрываем подключения
        db.close();
        userCursor.close();
        userSysCursor.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}