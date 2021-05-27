package com.codility.call_record;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class CallRecordFirstActivity extends AppCompatActivity {

    private static final int SETUP_ACTIVITY = 3;
    public static final int PERMS_NOT_GRANTED = 2;
    public static final int POWER_OPTIMIZED = 4;
    public static final String SETUP_ARGUMENT = "setup_arg";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {

        checkPermission();
    }

    private void checkPermission() {

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        int permsNotGranted = 0, powerOptimized = 0;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permsNotGranted = checkPermissions() ? 0 : PERMS_NOT_GRANTED;
            if(pm != null)
                powerOptimized = pm.isIgnoringBatteryOptimizations(getPackageName()) ? 0 : POWER_OPTIMIZED;
        }
        int checkResult = permsNotGranted | powerOptimized;

        if(checkResult != 0) {
            Intent setupIntent = new Intent(this, SetupActivity.class);
            setupIntent.putExtra(SETUP_ARGUMENT, checkResult);
            startActivityForResult(setupIntent, SETUP_ACTIVITY);
        }
    }

    private boolean checkPermissions() {
        boolean phoneState = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                == PackageManager.PERMISSION_GRANTED;
        boolean recordAudio = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED;
        boolean readContacts = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                == PackageManager.PERMISSION_GRANTED;
        boolean readStorage = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
        boolean writeStorage = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
        return phoneState && recordAudio && readContacts && readStorage && writeStorage;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data); //necesar pentru că altfel nu apelează onActivityResult din fragmente:
        // https://stackoverflow.com/questions/6147884/onactivityresult-is-not-being-called-in-fragment
        if(resultCode == RESULT_OK && requestCode == SETUP_ACTIVITY) {
            if(data.getBooleanExtra(SetupActivity.EXIT_APP, true))
                finish();
        }
    }
}
