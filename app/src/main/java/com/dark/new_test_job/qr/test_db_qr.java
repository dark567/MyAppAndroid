package com.dark.new_test_job.qr;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.dark.new_test_job.R;
import com.dark.new_test_job.alarm.database.DatabaseHelper;
import com.dark.new_test_job.alarm.database.UserActivity_sys;

/**
 * Created by dark on 22.06.2016.
 */
public class test_db_qr extends FragmentActivity {
    ListView mList;
    TextView header;
    DatabaseHelper sqlHelper;
    SQLiteDatabase db;
    Cursor userCursor;
    SimpleCursorAdapter userAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.myalarm /*activity_main_db*/);

        header = (TextView)findViewById(R.id.header);

        mList = (ListView)findViewById(R.id.list);
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getApplicationContext(), UserActivity_sys.class /*Admin_pass.class*/);
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

        //получаем данные из бд
        userCursor =  db.rawQuery("select * from "+ DatabaseHelper.TABLEsys, null);

        String[] headers = new String[] {DatabaseHelper.COLUMN_CONTR_NAME, DatabaseHelper.COLUMN_PERSON_NAME,
                DatabaseHelper.COLUMN_ID_facility, DatabaseHelper.COLUMN_ID_guard, DatabaseHelper.COLUMN_URL,
                DatabaseHelper.COLUMN_TIMEOUT, DatabaseHelper.COLUMN_SH, DatabaseHelper.COLUMN_D, DatabaseHelper.COLUMN_RADIUS};


        userAdapter = new SimpleCursorAdapter(this, R.layout.row_sys,
                userCursor, headers, new int[]{R.id.COLUMN_CONTR_NAME, R.id.COLUMN_PERSON_NAME,
                R.id.COLUMN_CONTR_NAME_id,R.id.COLUMN_PERSON_NAME_id,R.id.URL,R.id.TIMEOUT,R.id.SH,R.id.D,R.id.RADIUS}, 0);

        // header.setText("Объект   "+"   Охраник "+" ID объекта "+"  ID охранника"+"  URL сайта");
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
        menu.findItem(R.id.clear).setVisible(false);
        menu.findItem(R.id.replication_db).setVisible(false);
        menu.findItem(R.id.action_prefs).setVisible(false);
        menu.findItem(R.id.menu_item_new).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }
    /*потому что вторая запись не нужна но если потом... пригодится ... не удаляю*/
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }

//    @Override /*потому что вторая запись не нужна но если потом... пригодится ... не удаляю*/
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        int id = item.getItemId();
//
//        if (id == R.id.action_settings) {
//
//            Intent intent = new Intent(getApplicationContext(), UserActivity_sys.class);
//            startActivity(intent);
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }



}