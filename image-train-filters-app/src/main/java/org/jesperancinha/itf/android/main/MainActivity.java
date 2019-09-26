package org.jesperancinha.itf.android.main;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends ActionsMainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActivity();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        setupActivity(data);
    }

}
