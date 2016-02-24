package com.steelzack.chartizateapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.steelzack.chartizate.ChartizateFontManager;
import com.steelzack.chartizate.ChartizateFontManagerImpl;
import com.steelzack.chartizate.ChartizateManagerImpl;
import com.steelzack.chartizate.distributions.ChartizateDistributionType;
import com.steelzack.chartizateapp.common.ChartizateSurfaceView;
import com.steelzack.chartizateapp.common.ChartizateThumbs;
import com.steelzack.chartizateapp.distribution.manager.DistributionManager;
import com.steelzack.chartizateapp.file.manager.FileManagerItem;
import com.steelzack.chartizateapp.font.manager.FontManagerAdapter;
import com.steelzack.chartizateapp.language.manager.LanguageManagerAdapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int FILE_FIND = 0;

    public static final int FOLDER_FIND = 1;

    private FileManagerItem currentSelectedFile = null;

    private FileManagerItem currentSelectedFolder = null;

    List<String> listOfAllLanguageCode = ChartizateFontManager.getAllUniCodeBlockStringsJava7();

    List<String> listOfAllDistributions = ChartizateFontManager.getAllDistributionTypes();

    final List<String> listOfAllFonts = ChartizateFontManagerImpl.getAllFontTypes();
    private ChartizateSurfaceView svSelectedColor;
    private EditText editFontSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Collections.sort(listOfAllLanguageCode);

        setContentView(com.steelzack.chartizateapp.R.layout.activity_main);
        final Toolbar toolbar = (Toolbar) findViewById(com.steelzack.chartizateapp.R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getIntent() != null && getIntent().getExtras() != null) {
            final FileManagerItem fileManagerItem = (FileManagerItem) getIntent().getExtras().get("fileItem");
            if (fileManagerItem != null) {
                TextView currentFile = (TextView) findViewById(com.steelzack.chartizateapp.R.id.lblESelectedFile);
                currentFile.setText(fileManagerItem.getFilename());
                currentSelectedFile = fileManagerItem;
            }

            final FileManagerItem folderManagerItem = (FileManagerItem) getIntent().getExtras().get("folderItem");
            if (folderManagerItem != null) {
                TextView currentFile = (TextView) findViewById(com.steelzack.chartizateapp.R.id.lblOutputFolder);
                currentFile.setText(folderManagerItem.getFilename());
                currentSelectedFolder = folderManagerItem;
            }
        }

        final Spinner spiLanguageCode = (Spinner) findViewById(com.steelzack.chartizateapp.R.id.spiLanguageCode);
        final LanguageManagerAdapter dataAdapter = new LanguageManagerAdapter( //
                this, //
                android.R.layout.simple_spinner_item, listOfAllLanguageCode //
        );
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spiLanguageCode.setAdapter(dataAdapter);

        final Spinner spiDistribution = (Spinner) findViewById(com.steelzack.chartizateapp.R.id.spiDistribution);
        final DistributionManager distributionDataAdapter = new DistributionManager( //
                this, //
                android.R.layout.simple_spinner_item, listOfAllDistributions //
        );
        distributionDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spiDistribution.setAdapter(distributionDataAdapter);


        final Spinner spiFontType = (Spinner) findViewById(com.steelzack.chartizateapp.R.id.spiFontType);
        final FontManagerAdapter fontManagerAdapter = new FontManagerAdapter( //
                this, //
                android.R.layout.simple_spinner_item, listOfAllFonts //
        );
        distributionDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spiFontType.setAdapter(fontManagerAdapter);

        svSelectedColor = (ChartizateSurfaceView) findViewById(com.steelzack.chartizateapp.R.id.svSelectedColor);

        spiDistribution.setEnabled(false);

        editFontSize = (EditText) findViewById(com.steelzack.chartizateapp.R.id.editFontSize);

        final Button btnStartGeneration = (Button) findViewById(R.id.btnStart);
        btnStartGeneration.setEnabled(validate());

        final EditText editFileName = (EditText) findViewById(R.id.editOutputFileName);
        editFileName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                final Button btnStartGeneration = (Button) findViewById(R.id.btnStart);
                btnStartGeneration.setEnabled(validate());
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(com.steelzack.chartizateapp.R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    public void pGetBackGroundColor(View view) {
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
                        svSelectedColor.setBackgroundColor(selectedColor);
                        svSelectedColor.setColor(selectedColor);
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .build().show();
        final Button btnStartGeneration = (Button) findViewById(R.id.btnStart);
        btnStartGeneration.setEnabled(validate());
    }

    public void pFindFile(View view) {
        final Intent intent = new Intent(MainActivity.this, FileManagerActivity.class);
        intent.putExtra("directoryManager", false);
        startActivityForResult(intent, FILE_FIND);
        final Button btnStartGeneration = (Button) findViewById(R.id.btnStart);
        btnStartGeneration.setEnabled(validate());
    }

    public void pFindOutputFolder(View view) {
        final Intent intent = new Intent(MainActivity.this, FileManagerActivity.class);
        intent.putExtra("directoryManager", true);
        startActivityForResult(intent, FOLDER_FIND);
        final Button btnStartGeneration = (Button) findViewById(R.id.btnStart);
        btnStartGeneration.setEnabled(validate());
    }

    public void pAddOne(View view) {
        int currentFontSize = Integer.parseInt(editFontSize.getText().toString());
        editFontSize.setText(String.valueOf(currentFontSize + 1));
        final Button btnStartGeneration = (Button) findViewById(R.id.btnStart);
        btnStartGeneration.setEnabled(validate());
    }

    public void pMinusOne(View view) {
        int currentFontSize = Integer.parseInt(editFontSize.getText().toString());
        editFontSize.setText(String.valueOf(currentFontSize - 1));
        final Button btnStartGeneration = (Button) findViewById(R.id.btnStart);
        btnStartGeneration.setEnabled(validate());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null && data.getExtras() != null) {
            final FileManagerItem fileManagerItem = (FileManagerItem) data.getExtras().get("fileItem");
            if (fileManagerItem != null) {
                final TextView currentFile = (TextView) findViewById(com.steelzack.chartizateapp.R.id.lblESelectedFile);
                currentFile.setText(fileManagerItem.getFilename());
                currentSelectedFile = fileManagerItem;
                final ImageView btnImageFile = (ImageView) findViewById(com.steelzack.chartizateapp.R.id.fileImageSourcePreview);

                try {
                    ChartizateThumbs.setImageThumbnail(btnImageFile, new FileInputStream(fileManagerItem.getFile()));
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }

            final FileManagerItem folderManagerItem = (FileManagerItem) data.getExtras().get("folderItem");
            if (folderManagerItem != null) {
                final TextView currentFile = (TextView) findViewById(com.steelzack.chartizateapp.R.id.lblOutputFolder);
                currentFile.setText(folderManagerItem.getFilename());
                currentSelectedFolder = folderManagerItem;
            }
        }
        final Button btnStartGeneration = (Button) findViewById(R.id.btnStart);
        btnStartGeneration.setEnabled(validate());
    }

    public boolean validate() {
        if (currentSelectedFile == null) {
            return false;
        }
        if (currentSelectedFolder == null) {
            return false;
        }
        if (currentSelectedFolder.getFile() == null) {
            return false;
        }


        final File rawCurrehtSelectedFile = currentSelectedFile.getFile();
        if (rawCurrehtSelectedFile == null) {
            return false;
        }
        final String rawFontSize = ((EditText) findViewById(R.id.editFontSize)).getText().toString();
        if (rawFontSize.isEmpty()) {
            return false;
        }
        final String outputFileName = ((EditText) findViewById(com.steelzack.chartizateapp.R.id.editOutputFileName)).getText().toString();
        if (outputFileName.isEmpty()) {
            return false;
        }
        final String fontType = ((Spinner) findViewById(R.id.spiFontType)).getSelectedItem().toString();
        if (fontType.isEmpty()) {
            return false;
        }
        final String alphabet = ((Spinner) findViewById(R.id.spiLanguageCode)).getSelectedItem().toString();
        if (alphabet.isEmpty()) {
            return false;
        }

        return true;
    }


    public void pGenerateFile(View view) throws IOException {
        final File rawCurrehtSelectedFile = currentSelectedFile.getFile();
        final String rawFontSize = ((EditText) findViewById(R.id.editFontSize)).getText().toString();
        final String outputFileName = ((EditText) findViewById(com.steelzack.chartizateapp.R.id.editOutputFileName)).getText().toString();
        final String fontType = ((Spinner) findViewById(R.id.spiFontType)).getSelectedItem().toString();
        final String alphabet = ((Spinner) findViewById(R.id.spiLanguageCode)).getSelectedItem().toString();

        final InputStream imageFullStream = new FileInputStream(new File(rawCurrehtSelectedFile.getAbsolutePath()));

        final Integer fontSize = Integer.parseInt(rawFontSize);
        final int svSelectedColorColor = svSelectedColor.getColor();

        try {
            final ChartizateManagerImpl manager = new ChartizateManagerImpl( //
                    svSelectedColorColor, //
                    50, //
                    10, //
                    ChartizateDistributionType.Linear, //
                    fontType, //
                    fontSize, //
                    Character.UnicodeBlock.forName(alphabet), //
                    imageFullStream, //
                    new File(currentSelectedFolder.getFile(), outputFileName).getAbsolutePath() //
            );
            manager.generateConvertedImage();
        } catch (IllegalArgumentException e) {
            new AlertDialog.Builder(MainActivity.this)
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
    }
}
