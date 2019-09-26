package org.jesperancinha.itf.android.main;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.widget.Toolbar;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import org.jesperancinha.itf.android.EmailFragment;
import org.jesperancinha.itf.android.FileManagerActivity;
import org.jesperancinha.itf.android.R;
import org.jesperancinha.itf.android.SwipeAdapter;
import org.jesperancinha.itf.android.config.ControlConfiguration;
import org.jesperancinha.itf.android.file.manager.FileManagerItem;
import org.jesperancinha.itf.android.mail.MailSender;

import java.util.Objects;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static java.lang.Integer.parseInt;
import static org.jesperancinha.itf.android.ITFConstants.FILE_FIND;
import static org.jesperancinha.itf.android.ITFConstants.FOLDER_FIND;
import static org.jesperancinha.itf.android.R.string.chartizating;

public class MainActivity extends ActionsMainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (checkSelfPermission(
                WRITE_EXTERNAL_STORAGE) != PERMISSION_GRANTED) {
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, 1);
        }
        setContentView(R.layout.activity_main);
        chartizatePager = findViewById(R.id.itf_pager);
        final SwipeAdapter swipeAdapter = new SwipeAdapter(getSupportFragmentManager());
        chartizatePager.setAdapter(swipeAdapter);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.setLogo(R.mipmap.ic_launcher);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final MainFragment mainFragment = getMainFragment();
        if (isDataPresent(data)) {
            final FileManagerItem fileManagerItem = (FileManagerItem) Objects.requireNonNull(data.getExtras()).get("fileItem");
            setSelectedInputFileAndThumbnail(fileManagerItem);
            setSelectedOutputFolder(data);
        }
        mainFragment.checkButtonStart();
    }

}
