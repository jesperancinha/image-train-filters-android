package com.steelzack.chartizateapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;
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
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int FILE_FIND = 0;

    public static final int FOLDER_FIND = 1;

    private FileManagerItem currentSelectedFile = null;

    private FileManagerItem currentSelectedFolder = null;

    List<String> listOfAllLanguageCode = ChartizateFontManager.getAllUniCodeBlockStringsJava7();

    List<String> listOfAllDistributions = ChartizateFontManager.getAllDistributionTypes();

    final List<String> listOfAllFonts = ChartizateFontManagerImpl.getAllFontTypes();
    private SurfaceView svSelectedColor;
    private EditText editFontSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.steelzack.chartizateapp.R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(com.steelzack.chartizateapp.R.id.toolbar);
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
                currentSelectedFolder= folderManagerItem;
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

        svSelectedColor = (SurfaceView) findViewById(com.steelzack.chartizateapp.R.id.svSelectedColor);

        spiDistribution.setEnabled(false);

        editFontSize = (EditText)findViewById(com.steelzack.chartizateapp.R.id.editFontSize);
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

        //noinspection SimplifiableIfStatement
        if (id == com.steelzack.chartizateapp.R.id.action_settings) {
            return true;
        }

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
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .build()
                .show();
    }

    public void pFindFile(View view) {
        final Intent intent = new Intent(MainActivity.this, FileManagerActivity.class);
        intent.putExtra("directoryManager", false);
        startActivityForResult(intent, FILE_FIND);
    }

    public void pFindOutputFolder(View view) {
        final Intent intent = new Intent(MainActivity.this, FileManagerActivity.class);
        intent.putExtra("directoryManager", true);
        startActivityForResult(intent, FOLDER_FIND);
    }

    public void pAddOne(View view) {
        int currentFontSize = Integer.parseInt(editFontSize.getText().toString());
        editFontSize.setText(String.valueOf(currentFontSize + 1));
    }

    public void pMinusOne(View view) {
        int currentFontSize = Integer.parseInt(editFontSize.getText().toString());
        editFontSize.setText(String.valueOf(currentFontSize - 1));
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
                final ImageView btnImageFile = (ImageView)findViewById(com.steelzack.chartizateapp.R.id.fileImageSourcePreview);

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
                currentSelectedFolder= folderManagerItem;
            }
        }
    }

    public void pGenerateFile(View view) throws IOException {
        final InputStream imageFullStream = new FileInputStream(new File(currentSelectedFile.getFile().getAbsolutePath()));
        final String outputFileName =((EditText)findViewById(com.steelzack.chartizateapp.R.id.editOutputFileName)).getText().toString();

        final ChartizateManagerImpl manager = new ChartizateManagerImpl( //
                Color.BLACK, //
                50, //
                10, //
                ChartizateDistributionType.Linear, //
                "Sans", //
                5, //
                Character.UnicodeBlock.LATIN_EXTENDED_A, //
                imageFullStream, //
                new File(currentSelectedFolder.getFile(), outputFileName).getAbsolutePath() //
        );
        manager.generateConvertedImage();
    }
}
