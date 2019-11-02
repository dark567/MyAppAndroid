package com.dark.new_test_job.alarm;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.format.DateFormat;
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

import com.dark.new_test_job.MyFragment.MyFragmentA;
import com.dark.new_test_job.MyFragment.MyFragmentB;
import com.dark.new_test_job.MyFragment.MyFragmentC;
import com.dark.new_test_job.R;
import com.dark.new_test_job.alarm.database.DatabaseHelper;
import com.dark.new_test_job.alarm.database.UserActivity;
import com.dark.new_test_job.alarm.database.UserActivity_number;
import com.dark.new_test_job.other.SyncSettingsActivity;
import com.dark.new_test_job.parser.Get;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by dark on 11.10.2016.
 */
public class test_db_number extends FragmentActivity {
    ListView mList;
    TextView header;
    DatabaseHelper sqlHelper;
    SQLiteDatabase db;
    Cursor userCursor;
    SimpleCursorAdapter userAdapter;

    private String date;
    private String time;
    private String status;

    public static final String APP_PREFERENCES = "mysettings";
    public String APP_PREFERENCES_NICKNAME ;
    private String User;
    SharedPreferences mSettings;


    private final static String SENT = "SENT_SMS_ACTION",
            DELIVERED = "DELIVERED_SMS_ACTION", ISNULL = "Entered, not all data";


    ViewPager mViewPager;
    TabsAdapter mTabsAdapter;
    TextView textContacts;

    final String LOG_TAG = "myLogs";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //getActionBar().setDisplayHomeAsUpEnabled(true);


        sqlHelper = new DatabaseHelper(getApplicationContext());

//        mViewPager = new ViewPager(this);
//        mViewPager.setId(R.id.pager);
//        setContentView(mViewPager);
//
//
//               header = (TextView)findViewById(R.id.header);
//
//
//
//
//        final ActionBar bar = getActionBar();
//        // bar.setHomeButtonEnabled(true);
//        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
//        bar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);
//
//
//
//
//        mTabsAdapter = new TabsAdapter(this, mViewPager);
//        mTabsAdapter.addTab(bar.newTab().setText("Разрешенные исходящий"),
//                MyFragmentA.class, null);
//        mTabsAdapter.addTab(bar.newTab().setText("Разрешенные входящие"),
//                MyFragmentB.class, null);
//        mTabsAdapter.addTab(bar.newTab().setText("Описание"),
//                MyFragmentC.class, null);
//
//        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
//
//        if (savedInstanceState != null) {
//            bar.setSelectedNavigationItem(savedInstanceState.getInt("tab", 0));
//        }
//
//        mList = (ListView)findViewById(R.id.list_a);



        getActionBar().setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.myalarm);



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
        sqlHelper = new DatabaseHelper(getApplicationContext());



    }


    @Override
    public void onResume() {
        super.onResume();

        if (mSettings.contains(APP_PREFERENCES_NICKNAME)) {
            // выводим данные в TextView
            User = mSettings.getString(APP_PREFERENCES_NICKNAME,"");
            setTitle(/*getTitle() + */" пользователь:"+User);
            Log.i("test_db_number", "Имя user: " + mSettings.getString(APP_PREFERENCES_NICKNAME,
                    ""));
        }



        // открываем подключение
        db = sqlHelper.getReadableDatabase();

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
//                TextView data = (TextView) row.findViewById(R.id.ColCOLUMN_DATA);
//                TextView desc = (TextView) row.findViewById(R.id.DESCRIPTION);
//                TextView req = (TextView) row.findViewById(R.id.REQUEST);
//
//                ImageView imageView = (ImageView) row.findViewById(R.id.signal);
//                ImageView rez = (ImageView) row.findViewById(R.id.rez);
//
//
//                if (data.getText().equals("99999"))
//                {data.setText(" Дата");}
//
//                if (tv.getText().equals("0") )
//                {rez.setImageResource(R.drawable.sleep);
//                }
//                else if (tv.getText().equals("1"))
//                {rez.setImageResource(R.drawable.activ);
//                }
//
//
//
//                if (tv.getText().equals("0"))
//                {contr.setTextColor(Color.RED);
//                    name.setTextColor(Color.RED);
//                    tv.setTextColor(Color.RED);
//                    data.setTextColor(Color.RED);
//                }
//                else if (tv.getText().equals("1"))
//                {contr.setTextColor(Color.WHITE);
//                    name.setTextColor(Color.WHITE);
//                    tv.setTextColor(Color.WHITE);
//                    data.setTextColor(Color.WHITE);;
//                }
//
//                if (req.getText().equals("+"))
//                {imageView.setImageResource(R.drawable.a);
//                }
//                else if (req.getText().equals("-") || (req.getText().equals("")) || (req.getText() == null))
//                {imageView.setImageResource(R.drawable.z);
//                }

                return row;
            }
        };

//        userAdapter = new SimpleCursorAdapter(this, android.R.layout.two_line_list_item,
//                userCursor, headers, new int[]{android.R.id.text1, android.R.id.text2}, 0);

     //   header.setText("Найдено элементов: " + String.valueOf(userCursor.getCount()));

        // header.append("\nОБЪЕКТ "+"   ОХРАНИК "+"    Рез. "+"  ДАТА");
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

        menu.findItem(R.id.replication_db).setVisible(false);
        menu.findItem(R.id.action_prefs).setVisible(false);
        menu.findItem(R.id.clear).setVisible(false);

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

            if (User.equals("Admin") || User.equals("Head_of_security")) {

            sqlHelper.onClear(db);

            onResume();
            }else {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Извините редактирование только для разработчика или начальника охраны.",
                        Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 10, 15);
                toast.show();
            }


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


        if (item.getItemId() == R.id.action_prefs) {
            if (User.equals("Admin") || User.equals("Head_of_security")) {
                startActivity(new Intent(this, SyncSettingsActivity.class));
            }else {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Извините редактирование только для разработчика или начальника охраны.",
                        Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 10, 15);
                toast.show();
            }

                return true;

        }
        return super.onOptionsItemSelected(item);
    }


    public static class TabsAdapter extends FragmentPagerAdapter
            implements ActionBar.TabListener, ViewPager.OnPageChangeListener {

        private final Context mContext;
        private final ActionBar mActionBar;
        private final ViewPager mViewPager;
        private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();

        static final class TabInfo {
            private final Class<?> clss;
            private final Bundle args;

            TabInfo(Class<?> _class, Bundle _args) {
                clss = _class;
                args = _args;
            }
        }

        public TabsAdapter(FragmentActivity activity, ViewPager pager) {
            super(activity.getSupportFragmentManager());
            mContext = activity;
            mActionBar = activity.getActionBar();
            mViewPager = pager;
            mViewPager.setAdapter(this);
            mViewPager.setOnPageChangeListener(this);
        }

        public void addTab(ActionBar.Tab tab, Class<?> clss, Bundle args) {
            TabInfo info = new TabInfo(clss, args);
            tab.setTag(info);
            tab.setTabListener(this);
            mTabs.add(info);
            mActionBar.addTab(tab);
            notifyDataSetChanged();
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageSelected(int position) {
            // TODO Auto-generated method stub
            mActionBar.setSelectedNavigationItem(position);
        }

        @Override
        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
            Object tag = tab.getTag();
            for (int i=0; i<mTabs.size(); i++) {
                if (mTabs.get(i) == tag) {
                    mViewPager.setCurrentItem(i);
                }
            }
        }

        @Override
        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
            // TODO Auto-generated method stub

        }

        @Override
        public Fragment getItem(int position) {
            TabInfo info = mTabs.get(position);
            return Fragment.instantiate(mContext, info.clss.getName(), info.args);
        }

        @Override
        public int getCount() {
            return mTabs.size();
        }

    }



}