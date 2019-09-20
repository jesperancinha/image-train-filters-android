package org.jesperancinha.itf.android;

import android.app.AlertDialog;
import android.content.Intent;
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
import java.io.InputStream;

import static org.jesperancinha.chartizate.distributions.ChartizateDistributionType.Linear;

public class MainActivity extends FragmentActivity {

    private ViewPager chartizatePager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        chartizatePager = findViewById(R.id.itf_pager);
        final SwipeAdapter swipeAdapter = new SwipeAdapter(getSupportFragmentManager());
        chartizatePager.setAdapter(swipeAdapter);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.setLogo(R.mipmap.ic_launcher);
    }

    public void pFindFile(View view) {
        final MainFragment mainFragment = (MainFragment) getSupportFragmentManager().getFragments().get(chartizatePager.getCurrentItem());
        final Intent intent = new Intent(mainFragment.getActivity(), FileManagerActivity.class);
        intent.putExtra("directoryManager", false);
        startActivityForResult(intent, MainFragment.FILE_FIND);
        mainFragment.checkButtonStart();
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

    public void pGenerateFile(View view) {
        final MainFragment mainFragment = (MainFragment) getSupportFragmentManager().getFragments().get(chartizatePager.getCurrentItem());
        final int destinationWindow = getDestination(view, mainFragment);

        mainFragment.getBtnStart().setEnabled(false);
        mainFragment.getBtnStartEmail().setEnabled(false);
        mainFragment.getTextStatus().setText("Please wait while chartizating...");
        final String outputFileName = ((EditText) mainFragment.getMainView().findViewById(R.id.editOutputFileName)).getText().toString();
        final Runnable task = () -> {
            try {
                final File rawCurrehtSelectedFile = mainFragment.getCurrentSelectedFile().getFile();
                final String rawFontSize = ((EditText) mainFragment.getMainView().findViewById(R.id.editFontSize)).getText().toString();
                final String fontType = ((Spinner) mainFragment.getMainView().findViewById(R.id.spiFontType)).getSelectedItem().toString();
                final String alphabet = ((Spinner) mainFragment.getMainView().findViewById(R.id.spiLanguageCode)).getSelectedItem().toString();
                final int dennsity = Integer.parseInt(((EditText) mainFragment.getMainView().findViewById(R.id.editDensity)).getText().toString());
                final int range = Integer.parseInt(((EditText) mainFragment.getMainView().findViewById(R.id.editRange)).getText().toString());

                final InputStream imageFullStream = new FileInputStream(new File(rawCurrehtSelectedFile.getAbsolutePath()));

                final int fontSize = Integer.parseInt(rawFontSize);
                final int svSelectedColorColor = mainFragment.getSvSelectedColor().getColor();

                try {
                    final ChartizateManager<Integer, Typeface, Bitmap> manager =
                            new ChartizateManagerBuilderImpl()
                                    .backgroundColor(svSelectedColorColor)
                                    .densityPercentage(dennsity)
                                    .rangePercentage(range)
                                    .distributionType(Linear)
                                    .fontName(fontType)
                                    .fontSize(fontSize)
                                    .block(Character.UnicodeBlock.forName(alphabet))
                                    .imageFullStream(imageFullStream)
                                    .destinationImagePath(new File(mainFragment.getCurrentSelectedFolder().getFile(), outputFileName).getAbsolutePath())
                                    .build();
                    manager.generateConvertedImage();
                } catch (IllegalArgumentException e) {
                    new AlertDialog.Builder(mainFragment.getActivity())
                            .setTitle("Error with your code selection")
                            .setMessage("Unfortunatelly this Unicode is not supported. If you want a working example, try: LATIN_EXTENDED_A")
                            .setOnCancelListener(dialog -> {
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                mainFragment.getTextStatus().setText("Done!");
                mainFragment.getBtnStart().post(() -> mainFragment.getBtnStart().setEnabled(true));
                mainFragment.getBtnStartEmail().post(() -> mainFragment.getBtnStartEmail().setEnabled(true));
                chartizatePager.setCurrentItem(destinationWindow);
                final ViewFragment viewFragment = (ViewFragment) getSupportFragmentManager().getFragments().get(2);
                final ImageView imageView = viewFragment.getImageView();
                final ImageView imageViewEmail = findViewById(R.id.imageViewGeneratedAttachment);
                final Uri uri = Uri.fromFile(new File(mainFragment.getCurrentSelectedFolder().getFile(), outputFileName));
                imageView.post(() -> ChartizateThumbs.setImage(
                        imageView,
                        uri
                ));
                imageViewEmail.post(() -> ChartizateThumbs.setImage(
                        imageViewEmail,
                        uri
                ));
            }
        };
        mainFragment.getTextStatus().post(task);
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
        final String outputFileName = ((EditText) mainFragment.getMainView().findViewById(R.id.editOutputFileName)).getText().toString();
        final Uri uri = Uri.fromFile(new File(mainFragment.getCurrentSelectedFolder().getFile(), outputFileName));


        Log.i("Send email", "");
        String[] TO = emailFragment.getTextTo().getText().toString().split(",");
        String[] CC = emailFragment.getTextCC().getText().toString().split(",");
        String[] BCC = emailFragment.getTextBCC().getText().toString().split(",");
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_BCC, BCC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Generated by Chartizate " + BuildConfig.VERSION_NAME.toString());
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
