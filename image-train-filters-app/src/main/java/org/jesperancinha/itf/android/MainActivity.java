package org.jesperancinha.itf.android;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class MainActivity extends MainActivityManager {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpMainActivity();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        manageOnActivityResult(data);
    }

    @Override
    public MainActivity getContext() {
        return MainActivity.this;
    }
}
