package com.example.assignment_1_permissions;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.media.AudioManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    private EditText main_EDT_password;
    private MaterialButton main_BTN_submit;

    private static BatteryManager myBatteryManager; // Battery

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
        initSystemService(MainActivity.this);
        initSubmitBTN();
    }


    private void initSystemService(Context context) {
        myBatteryManager = (BatteryManager) context.getSystemService(Context.BATTERY_SERVICE);
    }

    /**
     * Check password's textField:
     * 1. if password is current battery percentage, then permission granted
     * 2. if phone is on silent mode, then permission granted
     * 3. if phone is on airplane mode, then permission granted
     * 4. if phone is on vibrate mode, then permission granted
     *  Otherwise, permission denied
     */
    private void initSubmitBTN() {
        main_BTN_submit.setOnClickListener(view -> {
            String input = main_EDT_password.getText().toString();

            if (input.isEmpty()) {
                Toast.makeText(MainActivity.this,"login failed [Please Enter a password]",Toast.LENGTH_SHORT).show();
                return;
            }

            int password = Integer.parseInt(input);

            if (getBatteryPercentage() == password) {
                Toast.makeText(MainActivity.this,"Login successfully - the battery equal to password",Toast.LENGTH_LONG).show();
            }
            // silent mode is on
            else if (isSilentModeOn()) {
                Toast.makeText(MainActivity.this,"Login successfully - silent mode is open",Toast.LENGTH_LONG).show();
            }
            // vibration mode is on
            else if (isVibrationModeOn()) {
                Toast.makeText(MainActivity.this,"Login successfully - vibrate mode is open",Toast.LENGTH_LONG).show();
            }
            // airplane mode is on
            else if (isAirplaneModeOn(MainActivity.this)) {
                Toast.makeText(MainActivity.this,"Login successfully - airplane mode is open",Toast.LENGTH_LONG).show();
            }
            // otherwise
            else {
                Toast.makeText(MainActivity.this,"Login failed",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int getBatteryPercentage() {
        return myBatteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
    }

    private boolean isSilentModeOn() {
        AudioManager am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        return am.getRingerMode() == AudioManager.RINGER_MODE_SILENT;
    }

    private boolean isAirplaneModeOn(Context context) {
        return Settings.System.getInt(context.getContentResolver(),
                Settings.Global.AIRPLANE_MODE_ON, 0) != 0;
    }

    private boolean isVibrationModeOn() {
        AudioManager am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        return am.getRingerMode() == AudioManager.RINGER_MODE_VIBRATE;
    }

    private void findViews() {
        main_EDT_password = findViewById(R.id.main_EDT_password);
        main_BTN_submit = findViewById(R.id.main_BTN_submit);
    }
}