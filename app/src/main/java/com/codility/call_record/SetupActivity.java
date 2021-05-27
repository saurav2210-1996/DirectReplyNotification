/*
 * Copyright (C) 2019 Eugen Rădulescu <synapticwebb@gmail.com> - All rights reserved.
 *
 * You may use, distribute and modify this code only under the conditions
 * stated in the SW Call Recorder license. You should have received a copy of the
 * SW Call Recorder license along with this file. If not, please write to <synapticwebb@gmail.com>.
 */

package com.codility.call_record;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.codility.directreplynotification.R;

public class SetupActivity extends AppCompatActivity {
    private int checkResult;
    public static final String EXIT_APP = "exit_app";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setup_activity);
        checkResult = getIntent().getIntExtra(CallRecordFirstActivity.SETUP_ARGUMENT, CallRecordFirstActivity.PERMS_NOT_GRANTED & CallRecordFirstActivity.POWER_OPTIMIZED);
        insertFragment(R.id.setup_fragment_container);
    }

    protected void insertFragment(int fragmentId) {
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(fragmentId);
        if(fragment == null) {
            fragment = new SetupPowerFragment();
            fm.beginTransaction().
                    add(fragmentId, fragment).
                    commit();
        }
    }

    public int getCheckResult() {
        return checkResult;
    }


    public void cancelSetup() {
        Intent intent = new Intent();
        intent.putExtra(EXIT_APP, true);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        cancelSetup();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
