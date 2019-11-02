package com.dark.new_test_job.other.sync;

import android.content.ContentResolver;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

/**
 * Created by dark on 06.09.2016.
 */
public class MainSync extends FragmentActivity {
    //...
    // Constants
    // Content provider authority
    public static final String AUTHORITY = "com.example.android.datasync.provider";
    // Account
    public static final String ACCOUNT = "default_account";
    // Sync interval constants
    public static final long SECONDS_PER_MINUTE = 60L;
    public static final long SYNC_INTERVAL_IN_MINUTES = 60L;
    public static final long SYNC_INTERVAL =
            SYNC_INTERVAL_IN_MINUTES *
                    SECONDS_PER_MINUTE;
    // Global variables
    // A content resolver for accessing the provider Преобразователь содержания для доступа к поставщику
    ContentResolver mResolver;
 //   ...
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     //   ...
        // Get the content resolver for your app Получите преобразователь содержания для своего приложения
        mResolver = getContentResolver();
        /*
         * Turn on periodic syncing Включите периодическую синхронизацию
         */
//        ContentResolver.addPeriodicSync(
//                ACCOUNT,
//                AUTHORITY,
//                Bundle.EMPTY,
//                SYNC_INTERVAL);
//      //  ...
   }
   // ...
}
