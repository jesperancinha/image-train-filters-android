package com.steelzack.chartizateapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.steelzack.chartizate.ChartizateManagerImpl;
import com.steelzack.chartizate.distributions.ChartizateDistributionType;
import com.steelzack.chartizateapp.common.ChartizateThumbs;
import com.steelzack.chartizateapp.file.manager.FileManagerItem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends FragmentActivity {

    private ViewPager chartizatePager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        chartizatePager = (ViewPager) findViewById(R.id.chartizate_pager);
        final SwipeAdapter swipeAdapter = new SwipeAdapter(getSupportFragmentManager());
        chartizatePager.setAdapter(swipeAdapter);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.setLogo(R.mipmap.ic_launcher);
    }

    public void pFindFile(View view) {
        final MainFragment mainFragment = (MainFragment) getSupportFragmentManager().getFragments().get(chartizatePager.getCurrentItem());
        final Intent intent = new Intent(mainFragment.getActivity(), FileManagerActivity.class);
        intent.putExtra("directoryManager", false);
        startActivityForResult(intent, mainFragment.FILE_FIND);
        mainFragment.checkButtonStart();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final MainFragment mainFragment = (MainFragment) getSupportFragmentManager().getFragments().get(chartizatePager.getCurrentItem());


        if (data != null && data.getExtras() != null) {
            final FileManagerItem fileManagerItem = (FileManagerItem) data.getExtras().get("fileItem");
            if (fileManagerItem != null) {
                final TextView currentFile = (TextView) mainFragment.getMainView().findViewById(com.steelzack.chartizateapp.R.id.lblESelectedFile);
                currentFile.setText(fileManagerItem.getFilename());
                mainFragment.setCurrentSelectedFile(fileManagerItem);
                final ImageView btnImageFile = (ImageView) mainFragment.getMainView().findViewById(com.steelzack.chartizateapp.R.id.fileImageSourcePreview);

                try {
                    ChartizateThumbs.setImageThumbnail(btnImageFile, new FileInputStream(fileManagerItem.getFile()));
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }

            final FileManagerItem folderManagerItem = (FileManagerItem) data.getExtras().get("folderItem");
            if (folderManagerItem != null) {
                final TextView currentFile = (TextView) mainFragment.getMainView().findViewById(com.steelzack.chartizateapp.R.id.lblOutputFolder);
                currentFile.setText(folderManagerItem.getFilename());
                mainFragment.setCurrentSelectedFolder(folderManagerItem);
            }
        }
        mainFragment.checkButtonStart();
    }

    public void pGenerateFile(View view) throws IOException {
        final MainFragment mainFragment = (MainFragment) getSupportFragmentManager().getFragments().get(chartizatePager.getCurrentItem());
        mainFragment.getBtnStart().setEnabled(false);
        mainFragment.getBtnStartEmail().setEnabled(false);
        mainFragment.getTextStatus().setText("Please wait while chartizating...");
                    final String outputFileName = ((EditText) mainFragment.getMainView().findViewById(R.id.editOutputFileName)).getText().toString();
        final Runnable task = new Runnable() {
            @Override
            public void run() {
                try {
                    final File rawCurrehtSelectedFile = mainFragment.getCurrentSelectedFile().getFile();
                    final String rawFontSize = ((EditText) mainFragment.getMainView().findViewById(R.id.editFontSize)).getText().toString();
                    final String fontType = ((Spinner) mainFragment.getMainView().findViewById(R.id.spiFontType)).getSelectedItem().toString();
                    final String alphabet = ((Spinner) mainFragment.getMainView().findViewById(R.id.spiLanguageCode)).getSelectedItem().toString();
                    final Integer dennsity = Integer.parseInt(((EditText) mainFragment.getMainView().findViewById(R.id.editDensity)).getText().toString());
                    final Integer range = Integer.parseInt(((EditText) mainFragment.getMainView().findViewById(R.id.editRange)).getText().toString());

                    final InputStream imageFullStream = new FileInputStream(new File(rawCurrehtSelectedFile.getAbsolutePath()));

                    final Integer fontSize = Integer.parseInt(rawFontSize);
                    final int svSelectedColorColor = mainFragment.getSvSelectedColor().getColor();

                    try {
                        final ChartizateManagerImpl manager = new ChartizateManagerImpl( //
                                svSelectedColorColor, //
                                dennsity, //
                                range, //
                                ChartizateDistributionType.Linear, //
                                fontType, //
                                fontSize, //
                                Character.UnicodeBlock.forName(alphabet), //
                                imageFullStream, //
                                new File(mainFragment.getCurrentSelectedFolder().getFile(), outputFileName).getAbsolutePath() //
                        );
                        manager.generateConvertedImage();
                    } catch (IllegalArgumentException e) {
                        new AlertDialog.Builder(mainFragment.getActivity())
                                .setTitle("Error with your code selection")
                                .setMessage("Unfortunatelly this Unicode is not supported. If you want a working example, try: LATIN_EXTENDED_A")
                                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                                    @Override
                                    public void onCancel(DialogInterface dialog) {

                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    mainFragment.getTextStatus().setText("Done!");
                    mainFragment.getBtnStart().post(new Runnable() {
                        @Override
                        public void run() {
                            mainFragment.getBtnStart().setEnabled(true);
                            mainFragment.getBtnStartEmail().setEnabled(true);
                            chartizatePager.setCurrentItem(2);
                            final ImageView imageView = (ImageView) findViewById(R.id.imageViewGenerated);
                            final ImageView imageViewEmail = (ImageView) findViewById(R.id.imageViewGeneratedAttachment);
                            final Uri uri = Uri.fromFile(new File(mainFragment.getCurrentSelectedFolder().getFile(), outputFileName));
                            ChartizateThumbs.setImage( //
                                    imageView, //
                                    uri //
                            );
                            ChartizateThumbs.setImage( //
                                    imageViewEmail, //
                                    uri //
                            );
                        }
                    });
                }
            }
        };
        mainFragment.getTextStatus().post(task);
    }

    public void pGetBackGroundColor(View view) {
        final MainFragment mainFragment = (MainFragment) getSupportFragmentManager().getFragments().get(chartizatePager.getCurrentItem());
        ColorPickerDialogBuilder
                .with(view.getContext())
                .setTitle("Choose color")
                .initialColor(Color.WHITE)
                .wheelType(ColorPickerView.WHEEL_TYPE.CIRCLE)
                .density(5)
                .setOnColorSelectedListener(new OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int selectedColor) {
                        // toast("onColorSelected: 0x" + Integer.toHexString(selectedColor));

                    }
                })
                .setPositiveButton("ok", new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                        mainFragment.getSvSelectedColor().setBackgroundColor(selectedColor);
                        mainFragment.getSvSelectedColor().setColor(selectedColor);
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .build().show();
        mainFragment.checkButtonStart();
    }

    public void pFindOutputFolder(View view) {
        final MainFragment mainFragment = (MainFragment) getSupportFragmentManager().getFragments().get(chartizatePager.getCurrentItem());
        final Intent intent = new Intent(mainFragment.getActivity(), FileManagerActivity.class);
        intent.putExtra("directoryManager", true);
        startActivityForResult(intent, mainFragment.FOLDER_FIND);
        mainFragment.checkButtonStart();
    }

    public void pAddOne(View view) {
        final MainFragment mainFragment = (MainFragment) getSupportFragmentManager().getFragments().get(chartizatePager.getCurrentItem());
        int currentFontSize = Integer.parseInt(mainFragment.getEditFontSize().getText().toString());
        mainFragment.getEditFontSize().setText(String.valueOf(currentFontSize + 1));
        mainFragment.checkButtonStart();
    }

    public void pMinusOne(View view) {
        final MainFragment mainFragment = (MainFragment) getSupportFragmentManager().getFragments().get(chartizatePager.getCurrentItem());
        int currentFontSize = Integer.parseInt(mainFragment.getEditFontSize().getText().toString());
        mainFragment.getEditFontSize().setText(String.valueOf(currentFontSize - 1));
        mainFragment.checkButtonStart();
    }
}
