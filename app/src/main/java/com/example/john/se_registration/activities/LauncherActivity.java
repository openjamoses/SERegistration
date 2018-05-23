package com.example.john.se_registration.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.john.se_registration.MainActivity;
import com.example.john.se_registration.R;
import com.example.john.se_registration.utils.Constants;

public class LauncherActivity extends AppCompatActivity {

    private static final int CAMERA_PERMISSION = 100;
    private Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.CAMERA)
                 != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.CAMERA},CAMERA_PERMISSION);
        }else {
            Intent intent = new Intent(context, MainActivity.class);
            startActivity(intent);
        }
    }
}
