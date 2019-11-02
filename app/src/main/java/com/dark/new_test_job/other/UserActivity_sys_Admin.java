package com.dark.new_test_job.other;

/**
 * Created by dark on 22.06.2016.
 */

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dark.new_test_job.R;
import com.dark.new_test_job.alarm.database.DatabaseHelper;
import com.dark.new_test_job.alarm.test_db;


public class UserActivity_sys_Admin extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_sys_admin);

        long userId=0;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userId = extras.getLong("id");
        }

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container_sys_Admin, PlaceholderFragment.newInstance(userId))
                    .commit();
        }
    }

    public static class PlaceholderFragment extends Fragment {

        TextView userBox;
        EditText passBox;
        Button saveButton;

        DatabaseHelper sqlHelper;
        SQLiteDatabase db;
        Cursor userCursor;

        public static PlaceholderFragment newInstance(long id) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args=new Bundle();
            args.putLong("id", id);
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
            View rootView = inflater.inflate(R.layout.fragment_sys_admin, container, false);
          //  TextView helloTextView3 = (TextView) findViewById(R.id.textView3);

            userBox = (TextView) rootView.findViewById(R.id.user);
            passBox = (EditText) rootView.findViewById(R.id.pass);

            saveButton = (Button) rootView.findViewById(R.id.save);

            final long id = getArguments() != null ? getArguments().getLong("id") : 0;

            db = sqlHelper.getWritableDatabase();


            // кнопка сохранения
            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ContentValues cv = new ContentValues();
                  //  cv.put(DatabaseHelper.COLUMN_USER, userBox.getText().toString());
                    if (passBox.getText().toString().equals(""))
                    {
                        Toast toast = Toast.makeText(getContext(),
                                "Пароль не зможет быть NULL",
                                Toast.LENGTH_LONG);
                        toast.show();
                    }
                    else
                    {
                        cv.put(DatabaseHelper.COLUMN_PASS, passBox.getText().toString());

                        if (id > 0) {
                            db.update(DatabaseHelper.TABLE_p, cv, DatabaseHelper.COLUMN_ID + "=" + String.valueOf(id), null);
                        } else {
                            db.insert(DatabaseHelper.TABLE_p, null, cv); //использовать для будильника
                        }
                        goHome();
                    }
                }
            });

            // если 0, то добавление
            if (id > 0) {
                // получаем элемент по id из бд
                userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLE_p + " where " +
                        DatabaseHelper.COLUMN_ID + "=?", new String[]{String.valueOf(id)});
                userCursor.moveToFirst();

                userBox.setText(" " + userCursor.getString(1));
                passBox.setText(userCursor.getString(2));


                userCursor.close();
            } else {
                // скрываем кнопку удаления
             //   delButton.setVisibility(View.GONE);
            }
            return rootView;
        }

        public void goHome(){
            // закрываем подключение
            db.close();
            // переход к главной activity
            Intent intent = new Intent(getActivity(), test_db_admin.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        }
    }
}