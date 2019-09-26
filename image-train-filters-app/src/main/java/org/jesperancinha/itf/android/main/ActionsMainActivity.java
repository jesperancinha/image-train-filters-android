package org.jesperancinha.itf.android.main;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import org.jesperancinha.itf.android.EmailFragment;
import org.jesperancinha.itf.android.FileManagerActivity;
import org.jesperancinha.itf.android.config.ControlConfiguration;
import org.jesperancinha.itf.android.mail.MailSender;

import static java.lang.Integer.parseInt;
import static org.jesperancinha.itf.android.ITFConstants.FILE_FIND;
import static org.jesperancinha.itf.android.ITFConstants.FOLDER_FIND;
import static org.jesperancinha.itf.android.R.string.chartizating;

public abstract class ActionsMainActivity extends MainActivityManager {

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
}
