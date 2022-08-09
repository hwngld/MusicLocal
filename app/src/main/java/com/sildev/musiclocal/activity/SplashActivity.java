package com.sildev.musiclocal.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.sildev.musiclocal.R;


public class SplashActivity extends AppCompatActivity {
    private static final String PERMISSIONS = Manifest.permission.READ_EXTERNAL_STORAGE;
    private static final int REQUEST_PERMISSION_READ_STORAGE = 1122;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        checkPermission();

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void checkPermission() {
        if (checkSelfPermission(PERMISSIONS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION_READ_STORAGE);
        } else {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();

        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_READ_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            } else {
                Toast.makeText(SplashActivity.this, "Require permission Storage", Toast.LENGTH_SHORT).show();
            }
        }
    }
}