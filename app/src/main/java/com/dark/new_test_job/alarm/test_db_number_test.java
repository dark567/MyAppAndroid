package com.dark.new_test_job.alarm;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.dark.new_test_job.MyFragment.MyFragmentA;
import com.dark.new_test_job.MyFragment.MyFragmentB;
import com.dark.new_test_job.MyFragment.MyFragmentC;
import com.dark.new_test_job.R;
import com.dark.new_test_job.alarm.database.DatabaseHelper;
import com.dark.new_test_job.alarm.database.UserActivity_number;
import com.dark.new_test_job.other.SyncSettingsActivity;

import java.util.ArrayList;

/**
 * Created by dark on 16.10.2016.
 */
public class test_db_number_test  extends AppCompatActivity implements android.support.v7.app.ActionBar.TabListener {

        final String LOG_TAG = "myLogs";
    public static final String APP_PREFERENCES = "mysettings";

    public static final String WHITE_LIST = "Список разрешенных исходящих";
    public static final String BLACK_PROGRAM = "Список запрещенных приложений";
    public static final String DESCRIPTION = "Описание";

    public String APP_PREFERENCES_NICKNAME ;
    private String User;
    SharedPreferences mSettings;

    ListView mList;
    TextView header;
    DatabaseHelper sqlHelper;
    SQLiteDatabase db;
    Cursor userCursor;
    SimpleCursorAdapter userAdapter;
    String test;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);



            setContentView(R.layout.myalarm);

            sqlHelper = new DatabaseHelper(getApplicationContext());
            db = sqlHelper.getReadableDatabase();

            mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

            if (mSettings.contains(APP_PREFERENCES_NICKNAME)) {
                // выводим данные в TextView
                User = mSettings.getString(APP_PREFERENCES_NICKNAME,"");
                setTitle(/*getTitle() + */" пользователь:"+User);
                Log.i("test_db_number", "Имя user: " + mSettings.getString(APP_PREFERENCES_NICKNAME,
                        ""));
            }

            header = (TextView)findViewById(R.id.header);
            mList = (ListView)findViewById(R.id.list);



            android.support.v7.app.ActionBar bar = getSupportActionBar();



            bar.setNavigationMode(android.support.v7.app.ActionBar.NAVIGATION_MODE_TABS);
            bar.setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_TITLE);
            android.support.v7.app.ActionBar.Tab tab = bar.newTab();
            tab.setText(WHITE_LIST);
            tab.setTabListener(this);
            bar.addTab(tab);

            tab = bar.newTab();
            tab.setText(BLACK_PROGRAM);
            tab.setTabListener(this);
            bar.addTab(tab);

            tab = bar.newTab();
            tab.setText(DESCRIPTION);
            tab.setTabListener(this);
            bar.addTab(tab);

/*потом поэксперементировать*/
//            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//            toolbar.setNavigationIcon(R.drawable.stop);
//            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    finish();
//                }
//            });



            mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    if (mSettings.contains(APP_PREFERENCES_NICKNAME)) {
                        // выводим данные в TextView
                        User = mSettings.getString(APP_PREFERENCES_NICKNAME,"");
                        setTitle(/*getTitle() + */" пользователь:"+User);
                        Log.i("test_db_number", "Имя user: " + mSettings.getString(APP_PREFERENCES_NICKNAME,
                                ""));
                    }

                    if (User.equals("Admin") || User.equals("Head_of_security")) {
                        Intent intent = new Intent(getApplicationContext(), UserActivity_number.class);
                        intent.putExtra("id", id);
                        startActivity(intent);
                    }
                    else {
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Извините редактирование только для разработчика или начальника охраны.",
                                Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 10, 15);
                        toast.show();
                    }
                }
            });


        }

    @Override
    public void onResume() {
        super.onResume();

//        if (mSettings.contains(APP_PREFERENCES_NICKNAME)) {
//            // выводим данные в TextView
//            User = mSettings.getString(APP_PREFERENCES_NICKNAME,"");
//            setTitle(/*getTitle() + */" пользователь:"+User);
//            Log.i("test_db_number", "Имя user: " + mSettings.getString(APP_PREFERENCES_NICKNAME,
//                    ""));
//        }
//
        //получаем данные из бд
//        userCursor =  db.rawQuery("select * from "+ DatabaseHelper.TABLE_number +" ORDER BY _id DESC", null);
//
//        String[] headers = new String[] {DatabaseHelper.COLUMN_NAME, DatabaseHelper.COLUMN_NUMBER};
//
//        userAdapter = new SimpleCursorAdapter(this, R.layout.row_number,
//                userCursor, headers, new int[]{R.id.T_NAME, R.id.T_NUMBER}, 0){
//
//            @Override
//            public View getView(int position, View convertView, ViewGroup parent) {
//                View row = super.getView(position, convertView, parent);
//                // Here we get the textview and set the color
//
//                TextView name = (TextView) row.findViewById(R.id.T_NAME);
//                TextView number = (TextView) row.findViewById(R.id.T_NUMBER);
//
////                if (tv.getText().equals("1"))
////                {rez.setImageResource(R.drawable.activ);
////                }
////
//                return row;
//            }
//        };
//
//
//        mList.setAdapter(userAdapter);


       // header.setText(test);

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
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_test, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();

            if (item.getItemId() == android.R.id.home) {
                onBackPressed();
                return true;
            }


            //noinspection SimplifiableIfStatement
            if (id == R.id.action_settings) {
                return true;
            }

            if (id == R.id.menu_item_new) {
                if (User.equals("Admin") || User.equals("Head_of_security")) {

                    Intent intent = new Intent(getApplicationContext(), UserActivity_number.class);

                    startActivity(intent);
                }else {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Извините добавление только для разработчика или начальника охраны.",
                            Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 10, 15);
                    toast.show();
                }


                return true;
            }

            return super.onOptionsItemSelected(item);
        }



        @Override
        public void onTabSelected(android.support.v7.app.ActionBar.Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction) {
            Log.d(LOG_TAG, "selected tab: " + tab.getText());

            if (tab.getText().equals(WHITE_LIST))
            {header.setText("1");
                test = "1231";
            mList.setVisibility(View.VISIBLE);

                //получаем данные из бд
                userCursor =  db.rawQuery("select * from "+ DatabaseHelper.TABLE_number +" ORDER BY _id DESC", null);

                String[] headers = new String[] {DatabaseHelper.COLUMN_NAME, DatabaseHelper.COLUMN_NUMBER};

                userAdapter = new SimpleCursorAdapter(this, R.layout.row_number,
                        userCursor, headers, new int[]{R.id.T_NAME, R.id.T_NUMBER}, 0){

                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View row = super.getView(position, convertView, parent);
                        // Here we get the textview and set the color

                        TextView name = (TextView) row.findViewById(R.id.T_NAME);
                        TextView number = (TextView) row.findViewById(R.id.T_NUMBER);

//                if (tv.getText().equals("1"))
//                {rez.setImageResource(R.drawable.activ);
//                }
//
                        return row;
                    }
                };

                header.setText("Найдено номеров: " + String.valueOf(userCursor.getCount()));
                mList.setAdapter(userAdapter);
//
            }

            if (tab.getText().equals(BLACK_PROGRAM))
            {header.setText("список приложений запрещенных для запуска (в работе)");

              mList.setVisibility(View.INVISIBLE);
            }

            if (tab.getText().equals(DESCRIPTION))
            {header.setText("что то о описании");
                mList.setVisibility(View.INVISIBLE);
                }
        }

        @Override
        public void onTabUnselected(android.support.v7.app.ActionBar.Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction) {
            Log.d(LOG_TAG, "unselected tab: " + tab.getText());
        }

        @Override
        public void onTabReselected(android.support.v7.app.ActionBar.Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction) {
            Log.d(LOG_TAG, "reselected tab: " + tab.getText());
        }
    }


