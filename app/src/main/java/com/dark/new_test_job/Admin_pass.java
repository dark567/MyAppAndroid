package com.dark.new_test_job;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.media.audiofx.AudioEffect;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.support.design.widget.NavigationView;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dark.new_test_job.R;
import com.dark.new_test_job.alarm.AlarmActivity;


/**
 * Created by dark on 22.03.2016.
 */
public class Admin_pass extends Activity {
    private TextView mHelloTextView;
    private EditText mNameEditText;
    private EditText etFName;
    private EditText etLName;



    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

//        mHelloTextView = (TextView) findViewById(R.id.textView3);
//        mNameEditText = (EditText) findViewById(R.id.editText3);



        setContentView(R.layout.admin_pass);
    }


    public void onClick_5(View view) {
        TextView helloTextView = (TextView) findViewById(R.id.textView3);
        TextView helloTextView4 = (TextView) findViewById(R.id.textView4);
        EditText mNameEditText = (EditText) findViewById(R.id.editText3);
        EditText mNameEditText4 = (EditText) findViewById(R.id.editText4);

        helloTextView.setText("логин, " + mNameEditText.getText());
        helloTextView4.setText("пароль, " + mNameEditText4.getText());

        etFName = (EditText) findViewById(R.id.editText3);
        etLName = (EditText) findViewById(R.id.editText4);

        helloTextView.setEnabled(false);


        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("login", etFName.getText().toString());
        intent.putExtra("pass", etLName.getText().toString());



        String pass = "z";
        String etLNamepass = etLName.getText().toString();

        if ( etLNamepass.equals(pass) /*etLName == pass*/) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Пароль верный! ",
                    Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            LinearLayout toastContainer = (LinearLayout) toast.getView();
            ImageView catImageView = new ImageView(getApplicationContext());
            catImageView.setImageResource(R.mipmap.inex);
            toastContainer.addView(catImageView, 0);
            toast.show();
            startActivity(intent);

//            Button button1 = (Button)findViewById(R.id.button);
//           // button.setVisibility(View.VISIBLE);
//            button1.setEnabled(true);

        }
        else {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Пароль НЕ верный! "+etLNamepass+"/"+pass,
                    Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            LinearLayout toastContainer = (LinearLayout) toast.getView();
            ImageView catImageView = new ImageView(getApplicationContext());
            catImageView.setImageResource(R.mipmap.inex);
            toastContainer.addView(catImageView, 0);
            toast.show();

//            Button button1 = (Button)findViewById(R.id.button);
//           // button.setVisibility(View.INVISIBLE);
//            button1.setEnabled(false);
        }






    }

    public void onClick_6(View view) {
        Intent intent = new Intent(Admin_pass.this, MainActivity.class);
        startActivity(intent);


    }

}