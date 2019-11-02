package com.dark.new_test_job.alarm.database;

/**
 * Created by dark on 22.06.2016.
 */

        import android.app.Activity;
        import android.content.ContentValues;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.support.v7.app.ActionBarActivity;
        import android.support.v4.app.Fragment;
        import android.os.Bundle;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Button;
        import android.widget.EditText;
        import android.content.Intent;

        import com.dark.new_test_job.MainActivity;
        import com.dark.new_test_job.R;

        import com.dark.new_test_job.alarm.test_db;


public class UserActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        long userId=0;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userId = extras.getLong("id");
        }

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, PlaceholderFragment.newInstance(userId))
                    .commit();
        }
    }

    public static class PlaceholderFragment extends Fragment {

        EditText nameBox;
        EditText yearBox;
        EditText REZULTBox;
        EditText DATABox;
        EditText DESCRIPTION;
        Button delButton;
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
            View rootView = inflater.inflate(R.layout.fragment_user, container, false);
            nameBox = (EditText) rootView.findViewById(R.id.name_contr);
            yearBox = (EditText) rootView.findViewById(R.id.PERSON_NAME);
            REZULTBox = (EditText) rootView.findViewById(R.id.REZULT);
            DATABox = (EditText) rootView.findViewById(R.id.DATA);
            DESCRIPTION = (EditText) rootView.findViewById(R.id.DESCRIPTION);
            delButton = (Button) rootView.findViewById(R.id.delete);
            saveButton = (Button) rootView.findViewById(R.id.save);

            final long id = getArguments() != null ? getArguments().getLong("id") : 0;

            db = sqlHelper.getWritableDatabase();
            // кнопка удаления
            delButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    db.delete(DatabaseHelper.TABLE, "_id = ?", new String[]{String.valueOf(id)});
                    goHome();
                }
            });

            // кнопка сохранения
            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ContentValues cv = new ContentValues();
                    cv.put(DatabaseHelper.COLUMN_CONTR_NAME, nameBox.getText().toString());
                    cv.put(DatabaseHelper.COLUMN_PERSON_NAME, yearBox.getText().toString());
                    cv.put(DatabaseHelper.COLUMN_REZULT, REZULTBox.getText().toString());
                    cv.put(DatabaseHelper.COLUMN_DATA, DATABox.getText().toString());
                    cv.put(DatabaseHelper.COLUMN_DESCRIPTION, DESCRIPTION.getText().toString());

                    if (id > 0) {
                        db.update(DatabaseHelper.TABLE, cv, DatabaseHelper.COLUMN_ID + "=" + String.valueOf(id), null);
                    } else {
                        db.insert(DatabaseHelper.TABLE, null, cv); //использовать для будильника
                    }
                    goHome();
                }
            });

            // если 0, то добавление
            if (id > 0) {
                // получаем элемент по id из бд
                userCursor = db.rawQuery("select * from " + DatabaseHelper.TABLE + " where " +
                        DatabaseHelper.COLUMN_ID + "=?", new String[]{String.valueOf(id)});
                userCursor.moveToFirst();

                nameBox.setText(userCursor.getString(3));
               yearBox.setText(userCursor.getString(5));
                REZULTBox.setText(userCursor.getString(1));
               DATABox.setText(userCursor.getString(6));
                DESCRIPTION.setText(userCursor.getString(7));

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
            Intent intent = new Intent(getActivity(), test_db.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        }
    }
}