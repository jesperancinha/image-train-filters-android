package org.jesperancinha.itf.android.main;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import org.jesperancinha.itf.android.EmailFragment;
import org.jesperancinha.itf.android.FileManagerActivity;
import org.jesperancinha.itf.android.R;
import org.jesperancinha.itf.android.SwipeAdapter;
import org.jesperancinha.itf.android.ViewFragment;
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

public abstract class ActionsMainActivity extends MainActivityManager {

    protected MainFragmentManager mainFragmentManager;

    protected void setupActivity() {
        if (checkSelfPermission(
                WRITE_EXTERNAL_STORAGE) != PERMISSION_GRANTED) {
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, 1);
        }
        setContentView(R.layout.activity_main);
        chartizatePager = findViewById(R.id.itf_pager);
        final SwipeAdapter swipeAdapter = new SwipeAdapter(getSupportFragmentManager(),
                new Fragment[]{new MainFragment(), new EmailFragment(), new ViewFragment()});
        chartizatePager.setAdapter(swipeAdapter);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.setLogo(R.mipmap.ic_launcher);
    }

    protected void setupActivity(Intent data) {
        final MainFragment mainFragment = getMainFragment();
        if (isDataPresent(data)) {
            final FileManagerItem fileManagerItem = (FileManagerItem) Objects.requireNonNull(data.getExtras()).get("fileItem");
            setSelectedInputFileAndThumbnail(fileManagerItem);
            setSelectedOutputFolder(data);
        }
        mainFragment.checkButtonStart();
    }

    public void pFindFile(View view) {
        final MainFragment mainFragment = getMainFragment();
        final Intent intent = new Intent(mainFragment.getActivity(), FileManagerActivity.class);
        intent.putExtra("directoryManager", false);
        startActivityForResult(intent, FILE_FIND);
        mainFragment.checkButtonStart();
    }

    public void pGenerateFile(View view) {
        final ControlConfiguration controlConfiguration = mainFragmentManager.getControlConfiguration();
        controlConfiguration.getBtnStart().setEnabled(false);
        controlConfiguration.getBtnStartEmail().setEnabled(false);
        controlConfiguration.getTextStatus().setText(chartizating);
        final Runnable task = createGenerateImageTask(view, getMainFragment());
        mainFragmentManager.getControlConfiguration().getTextStatus().post(task);
    }

    public void pGetBackGroundColor(View view) {
        final MainFragment mainFragment = getMainFragment();
        ColorPickerDialogBuilder
                .with(view.getContext())
                .setTitle("Choose color")
                .initialColor(Color.WHITE)
                .wheelType(ColorPickerView.WHEEL_TYPE.CIRCLE)
                .density(5)
                .setPositiveButton("ok", (dialog, selectedColor, allColors) -> {
                    mainFragmentManager.getControlConfiguration().getSvSelectedColor().setBackgroundColor(selectedColor);
                    mainFragmentManager.getControlConfiguration().getSvSelectedColor().setColor(selectedColor);
                })
                .setNegativeButton("cancel", (dialog, which) -> {
                })
                .build().show();
        mainFragment.checkButtonStart();
    }

    public void pFindOutputFolder(View view) {
        final MainFragment mainFragment = getMainFragment();
        final Intent intent = new Intent(mainFragment.getActivity(), FileManagerActivity.class);
        intent.putExtra("directoryManager", true);
        startActivityForResult(intent, FOLDER_FIND);
        mainFragment.checkButtonStart();
    }

    public void pAddOne(View view) {
        final MainFragment mainFragment = getMainFragment();
        final int currentFontSize = parseInt(mainFragmentManager.getEditConfiguration().getEditFontSize().getText().toString());
        mainFragmentManager.getEditConfiguration().getEditFontSize().setText(String.valueOf(currentFontSize + 1));
        mainFragment.checkButtonStart();
    }

    public void pMinusOne(View view) {
        final MainFragment mainFragment = getMainFragment();
        final int currentFontSize = parseInt(mainFragmentManager.getEditConfiguration().getEditFontSize().getText().toString());
        mainFragmentManager.getEditConfiguration().getEditFontSize().setText(String.valueOf(currentFontSize - 1));
        mainFragment.checkButtonStart();
    }

    public void pSendEmail(View view) {
        final MainFragment mainFragment = (MainFragment) getSupportFragmentManager().getFragments().get(0);
        final EmailFragment emailFragment = (EmailFragment) getSupportFragmentManager().getFragments().get(1);
        final String outputFileName = mainFragmentManager.getOutputFileName();
        final MailSender mailSender = MailSender.builder().mainFragment(mainFragment).emailFragment(emailFragment)
                .outputFileName(outputFileName).build();
        mailSender.sendEmail();
    }

    protected MainFragment getMainFragment() {
        if (Objects.isNull(this.mainFragmentManager)) {
            this.mainFragmentManager = new MainFragmentManager(
                    (MainFragment) getSupportFragmentManager().getFragments().get(chartizatePager.getCurrentItem()));
        }
        return mainFragmentManager.getMainFragment();
    }

    @Override
    public MainFragmentManager getMainFragmentManager() {
        return mainFragmentManager;
    }
}
