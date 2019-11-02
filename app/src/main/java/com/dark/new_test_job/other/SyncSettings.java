/*
 * Copyright 2012-2014 Daniel Serdyukov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dark.new_test_job.other;

import android.accounts.Account;
import android.content.ContentResolver;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.text.TextUtils;
import android.util.Log;

import com.dark.new_test_job.R;

import com.dark.new_test_job.other.AppDelegate;
//import com.elegion.newsfeed.R;

/**
 * @author Daniel Serdyukov
 */
public class SyncSettings extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String KEY_AUTO_SYNC = "com.dark.new_test_job.KEY_AUTO_SYNC";

    private static final String KEY_AUTO_SYNC_INTERVAL = "com.dark.new_test_job.KEY_AUTO_SYNC_INTERVAL";

    public static final String ACCOUNT = "default_account";
    public static final String AUTHORITY = "com.example.android.datasync.provider";
    public static final long SECONDS_PER_MINUTE = 60L;
    public static final long SYNC_INTERVAL_IN_MINUTES = 60L;
    public static final long SYNC_INTERVAL = SYNC_INTERVAL_IN_MINUTES * SECONDS_PER_MINUTE;
    public static Account sAccount;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.sync_prefs);
        final ListPreference interval = (ListPreference) getPreferenceManager()
                .findPreference(KEY_AUTO_SYNC_INTERVAL);
        interval.setSummary(interval.getEntry());


        if (sAccount == null) {
            sAccount = new Account(getString(R.string.news), ACCOUNT);
        }


    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        getPreferenceManager().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
        if (TextUtils.equals(KEY_AUTO_SYNC, key)) {
            if (prefs.getBoolean(key, false)) {
                final long interval = Long.parseLong(prefs.getString(
                        KEY_AUTO_SYNC_INTERVAL,
                        getString(R.string.auto_sync_interval_default)
                ));

                Log.d("interval", "interval " + interval);
               // ContentResolver.addPeriodicSync(AppDelegate.sAccount, AppDelegate.AUTHORITY, Bundle.EMPTY, interval);
            } else {
               // Log.d("interval", "interval " + interval);
               // ContentResolver.removePeriodicSync(AppDelegate.sAccount, AppDelegate.AUTHORITY, new Bundle());
            }
        } else if (TextUtils.equals(KEY_AUTO_SYNC_INTERVAL, key)) {
            final ListPreference interval = (ListPreference) getPreferenceManager().findPreference(key);
            interval.setSummary(interval.getEntry());

            Log.d("interval", "interval " + interval);
            ContentResolver.addPeriodicSync(
                    sAccount,
                    AUTHORITY,
                    Bundle.EMPTY,
                    SYNC_INTERVAL);


        }
    }

}