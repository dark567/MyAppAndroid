package com.dark.new_test_job.alarm.database;

/**
 * Created by dark on 21.04.2016.
 */
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;


public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "usertest.db"; // название бд
    private static final int SCHEMA = 1; // версия базы данных

    // названия столбцов
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_REZULT = "REZULT";
    public static final String COLUMN_CONTR_ID = "CONTR_ID";
    public static final String COLUMN_CONTR_NAME = "CONTR_NAME";
    public static final String COLUMN_PERSON_ID = "PERSON_ID";
    public static final String COLUMN_PERSON_NAME= "PERSON_NAME";
    public static final String COLUMN_URL = "URL";
    public static final String COLUMN_ID_guard = "guard";
    public static final String COLUMN_ID_facility = "facility";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_REQUEST = "request";
    public static final String COLUMN_TIMEOUT = "timeout";

    public static final String COLUMN_SH = "shirina";
    public static final String COLUMN_D = "dolgota";
    public static final String COLUMN_RADIUS = "radius";

    public static final String COLUMN_DATA = "DATA";

    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_NUMBER = "number";

    public static final String COLUMN_USER = "name";
    public static final String COLUMN_PASS = "pass";

    public static final String TABLE = "users"; // название таблицы в бд
    public static final String TABLEsys = "sys"; // название таблицы настроек в бд
    public static final String TABLE_number = "white_list"; // название таблицы разрешенных номеров в бд
    public static final String TABLE_p = "sys_users"; // название таблицы разрешенных номеров в бд

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + TABLE + " (" + COLUMN_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_REZULT
                + " TEXT, " + COLUMN_CONTR_ID
                + " INTEGER, " + COLUMN_CONTR_NAME
                + " TEXT, " + COLUMN_PERSON_ID
                + " INTEGER, " + COLUMN_PERSON_NAME
                + " TEXT, " + COLUMN_DATA
                + " TEXT, " + COLUMN_DESCRIPTION
                + " TEXT, " + COLUMN_REQUEST
                + " TEXT);");
        // добавление начальных данных
//        db.execSQL("INSERT INTO "+ TABLE +" (" + COLUMN_REZULT
//                + ", " + COLUMN_CONTR_NAME  + ", " + COLUMN_PERSON_NAME  + ", "
//                + COLUMN_DATA  +", " +  COLUMN_DESCRIPTION + ", " + COLUMN_REQUEST +
//                ") VALUES ('Рез.','Заказчик','Охранник ФИО', '99999', 'Описание', 'Отпр.');");

        db.execSQL("CREATE TABLE " + TABLEsys + " (" + COLUMN_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_CONTR_NAME
                + " TEXT, " + COLUMN_PERSON_NAME
                + " TEXT, " + COLUMN_ID_facility
                + " TEXT, " + COLUMN_ID_guard
                + " TEXT, " + COLUMN_URL
                + " TEXT, " + COLUMN_TIMEOUT
                + " TEXT, " + COLUMN_SH
                + " TEXT, " + COLUMN_D
                + " TEXT, " + COLUMN_RADIUS
                + " TEXT);");

        db.execSQL("CREATE TABLE " + TABLE_number + " (" + COLUMN_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_NAME
                + " TEXT, " + COLUMN_NUMBER
                + " TEXT);");


        db.execSQL("CREATE TABLE " + TABLE_p + " (" + COLUMN_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_USER
                + " TEXT, " + COLUMN_PASS
                + " TEXT);");
        // добавление начальных данных

        db.execSQL("INSERT INTO "+ TABLEsys +" (" + COLUMN_CONTR_NAME  + ", "
                + COLUMN_PERSON_NAME  + ", " + COLUMN_ID_facility + ", " + COLUMN_ID_guard   + ", " + COLUMN_URL + ", " + COLUMN_TIMEOUT
                + ", " + COLUMN_SH + ", " + COLUMN_D + ", " + COLUMN_RADIUS +
                ") VALUES ('Тестовый объект ','Тестовый охранник','5','6','inex24.com.ua', '120', '49.989653', '36.248534', '100');");

        db.execSQL("INSERT INTO "+ TABLE_number +" (" + COLUMN_NAME  + ", "
                + COLUMN_NUMBER  + ") VALUES ('Митяков Сергей', '0995548904');");


        db.execSQL("INSERT INTO "+ TABLE_p +" (" + COLUMN_USER  + ", " + COLUMN_PASS  + ") VALUES ('Admin', '7777');");
        db.execSQL("INSERT INTO "+ TABLE_p +" (" + COLUMN_USER  + ", " + COLUMN_PASS  + ") VALUES ('Head_of_security', '22');");
        db.execSQL("INSERT INTO "+ TABLE_p +" (" + COLUMN_USER  + ", " + COLUMN_PASS  + ") VALUES ('Security_guard', '1');");


    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,  int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+TABLEsys);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_number);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_p);
        onCreate(db);
    }


    public void onClear(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE);
        db.execSQL("DROP TABLE IF EXISTS "+TABLEsys);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_number);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_p);
        onCreate(db);
    }

}
