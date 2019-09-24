package org.jesperancinha.itf.android;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import org.jesperancinha.chartizate.ChartizateManager;
import org.jesperancinha.chartizate.ChartizateManagerBuilderImpl;
import org.jesperancinha.itf.android.common.ChartizateThumbs;
import org.jesperancinha.itf.android.file.manager.FileManagerItem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static org.jesperancinha.chartizate.distributions.ChartizateDistributionType.Linear;

public class MainActivity extends FragmentActivity {

    private ViewPager chartizatePager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (checkSelfPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
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
        final MainFragment mainFragment = (MainFragment) getSupportFragmentManager().getFragments().get(chartizatePager.getCurrentItem());


        if (data != null && data.getExtras() != null) {
            final FileManagerItem fileManagerItem = (FileManagerItem) data.getExtras().get("fileItem");
            if (fileManagerItem != null) {
                final TextView currentFile = mainFragment.getMainView().findViewById(R.id.lblESelectedFile);
                currentFile.setText(fileManagerItem.getFilename());
                mainFragment.setCurrentSelectedFile(fileManagerItem);
                final ImageView btnImageFile = mainFragment.getMainView().findViewById(R.id.fileImageSourcePreview);

                try {
                    ChartizateThumbs.setImageThumbnail(btnImageFile, new FileInputStream(fileManagerItem.getFile()));
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }

            final FileManagerItem folderManagerItem = (FileManagerItem) data.getExtras().get("folderItem");
            if (folderManagerItem != null) {
                final TextView currentFile = mainFragment.getMainView().findViewById(R.id.lblOutputFolder);
                currentFile.setText(folderManagerItem.getFilename());
                mainFragment.setCurrentSelectedFolder(folderManagerItem);
            }
        }
        mainFragment.checkButtonStart();
    }

    public void pFindFile(View view) {
        final MainFragment mainFragment = (MainFragment) getSupportFragmentManager().getFragments().get(chartizatePager.getCurrentItem());
        final Intent intent = new Intent(mainFragment.getActivity(), FileManagerActivity.class);
        intent.putExtra("directoryManager", false);
        startActivityForResult(intent, MainFragment.FILE_FIND);
        mainFragment.checkButtonStart();
    }

    public void pGenerateFile(View view) {
        final MainFragment mainFragment = processMainFragment();
        final Runnable task = createGenerateImageTask(view, mainFragment);
        mainFragment.getTextStatus().post(task);
    }

    private Runnable createGenerateImageTask(View view, MainFragment mainFragment) {
        return () -> {
            try {
                final ChartizateManager<Integer, Typeface, Bitmap> manager = setUpManager(mainFragment);
                manager.saveImage(manager.generateConvertedImage());
            } catch (IllegalArgumentException e) {
                alertFailedUnicodeParsing(mainFragment);
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                postFileGeneration(mainFragment, getDestination(view, mainFragment), getOutputFileName(mainFragment));
            }
        };
    }

    private ChartizateManager<Integer, Typeface, Bitmap> setUpManager(MainFragment mainFragment) throws java.io.IOException {
        return new ChartizateManagerBuilderImpl()
                .backgroundColor(getBackground(mainFragment))
                .densityPercentage(getDensity(mainFragment))
                .rangePercentage(getRangePercentage(mainFragment))
                .distributionType(Linear)
                .fontName(getFontName(mainFragment))
                .fontSize(getFontSize(mainFragment))
                .block(getBlock(mainFragment))
                .imageFullStream(getImageFullStream(mainFragment))
                .destinationImagePath(getDestinationImagePath(mainFragment, getOutputFileName(mainFragment)))
                .build();
    }

    private MainFragment processMainFragment() {
        final MainFragment mainFragment = (MainFragment) getSupportFragmentManager().getFragments().get(chartizatePager.getCurrentItem());
        mainFragment.getBtnStart().setEnabled(false);
        mainFragment.getBtnStartEmail().setEnabled(false);
        mainFragment.getTextStatus().setText(R.string.chartizating);
        return mainFragment;
    }

    private String getOutputFileName(MainFragment mainFragment) {
        return ((EditText) mainFragment.getMainView().findViewById(R.id.editOutputFileName)).getText().toString();
    }

    private String getDestinationImagePath(MainFragment mainFragment, String outputFileName) {
        return new File(mainFragment.getCurrentSelectedFolder().getFile(), outputFileName).getAbsolutePath();
    }

    private FileInputStream getImageFullStream(MainFragment mainFragment) throws FileNotFoundException {
        return new FileInputStream(
                new File(mainFragment.getCurrentSelectedFile().getFile().getAbsolutePath()));
    }

    private Character.UnicodeBlock getBlock(MainFragment mainFragment) {
        return Character.UnicodeBlock.forName(((Spinner) mainFragment.getMainView().findViewById(R.id.spiLanguageCode)).getSelectedItem().toString());
    }

    private int getFontSize(MainFragment mainFragment) {
        return Integer.parseInt(((EditText) mainFragment.getMainView().findViewById(R.id.editFontSize)).getText().toString());
    }

    private String getFontName(MainFragment mainFragment) {
        return ((Spinner) mainFragment.getMainView().findViewById(R.id.spiFontType)).getSelectedItem().toString();
    }

    private int getRangePercentage(MainFragment mainFragment) {
        return Integer.parseInt(((EditText) mainFragment.getMainView().findViewById(R.id.editRange)).getText().toString());
    }

    private int getDensity(MainFragment mainFragment) {
        return Integer.parseInt(((EditText) mainFragment.getMainView().findViewById(R.id.editDensity)).getText().toString());
    }

    private int getBackground(MainFragment mainFragment) {
        return mainFragment.getSvSelectedColor().getColor();
    }

    private void postFileGeneration(MainFragment mainFragment, int destinationWindow, String outputFileName) {
        mainFragment.getTextStatus().setText(R.string.done);
        mainFragment.getBtnStart().post(() -> mainFragment.getBtnStart().setEnabled(true));
        mainFragment.getBtnStartEmail().post(() -> mainFragment.getBtnStartEmail().setEnabled(true));
        chartizatePager.setCurrentItem(destinationWindow);
        final ViewFragment viewFragment = (ViewFragment) getSupportFragmentManager().getFragments().get(2);
        final ImageView imageView = viewFragment.getImageView();
        final ImageView imageViewEmail = findViewById(R.id.imageViewGeneratedAttachment);
        final Uri uri = getOutputFileUrl(mainFragment, outputFileName);
        imageView.post(() -> ChartizateThumbs.setImage(imageView, uri));
        imageViewEmail.post(() -> ChartizateThumbs.setImage(imageViewEmail, uri));
    }

    private Uri getOutputFileUrl(MainFragment mainFragment, String outputFileName) {
        return Uri.fromFile(new File(mainFragment.getCurrentSelectedFolder().getFile(), outputFileName));
    }

    private void alertFailedUnicodeParsing(MainFragment mainFragment) {
        new AlertDialog.Builder(mainFragment.getActivity())
                .setTitle("Error with your code selection")
                .setMessage("Unfortunatelly this Unicode is not supported. If you want a working example, try: LATIN_EXTENDED_A")
                .setOnCancelListener(dialog -> {
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private int getDestination(View view, MainFragment mainFragment) {
        if (view == mainFragment.getBtnStart()) {
            return 2;
        } else if (view == mainFragment.getBtnStartEmail()) {
            return 1;
        }
        return 0;
    }

    public void pGetBackGroundColor(View view) {
        final MainFragment mainFragment = (MainFragment) getSupportFragmentManager().getFragments().get(chartizatePager.getCurrentItem());
        ColorPickerDialogBuilder
                .with(view.getContext())
                .setTitle("Choose color")
                .initialColor(Color.WHITE)
                .wheelType(ColorPickerView.WHEEL_TYPE.CIRCLE)
                .density(5)
                .setOnColorSelectedListener(selectedColor -> System.out.println("WOW")
//                        toast("onColorSelected: 0x" + Integer.toHexString(selectedColor))
                )
                .setPositiveButton("ok", (dialog, selectedColor, allColors) -> {
                    mainFragment.getSvSelectedColor().setBackgroundColor(selectedColor);
                    mainFragment.getSvSelectedColor().setColor(selectedColor);
                })
                .setNegativeButton("cancel", (dialog, which) -> {
                })
                .build().show();
        mainFragment.checkButtonStart();
    }

    public void pFindOutputFolder(View view) {
        final MainFragment mainFragment = (MainFragment) getSupportFragmentManager().getFragments().get(chartizatePager.getCurrentItem());
        final Intent intent = new Intent(mainFragment.getActivity(), FileManagerActivity.class);
        intent.putExtra("directoryManager", true);
        startActivityForResult(intent, MainFragment.FOLDER_FIND);
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

    public void pSendEmail(View view) {
        final MainFragment mainFragment = (MainFragment) getSupportFragmentManager().getFragments().get(0);
        final EmailFragment emailFragment = (EmailFragment) getSupportFragmentManager().getFragments().get(1);
        final String outputFileName = getOutputFileName(mainFragment);
        final Uri uri = Uri.fromFile(new File(mainFragment.getCurrentSelectedFolder().getFile(), outputFileName));


        Log.i(getString(R.string.sendEmail), "");
        String[] TO = emailFragment.getTextTo().getText().toString().split(getResources().getString(R.string.comma));
        String[] CC = emailFragment.getTextCC().getText().toString().split(getResources().getString(R.string.comma));
        String[] BCC = emailFragment.getTextBCC().getText().toString().split(getResources().getString(R.string.comma));
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setDataAndType(Uri.parse("mailto:"), "text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_BCC, BCC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Generated by Chartizate " + BuildConfig.VERSION_NAME);
        emailIntent.putExtra(Intent.EXTRA_TEXT, emailFragment.getTextEmail().getText().toString());
        emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            Log.i("Mail sent!", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(MainActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }
}
