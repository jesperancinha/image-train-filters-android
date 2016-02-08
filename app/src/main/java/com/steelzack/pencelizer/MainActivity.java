package com.steelzack.pencelizer;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Spinner;
import android.widget.TextView;

import com.steelzack.pencelizer.distribution.manager.DistributionManager;
import com.steelzack.pencelizer.file.manager.FileManagerItem;
import com.steelzack.pencelizer.language.manager.LanguageManagerAdapter;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FileManagerItem currentSelectedFile = null;

    final List<String> listOfAllLanguageCode = PencelizerUtils.getAllUniCodeBlockStringsJava7();
    final List<String> listOfAllDistributions = PencelizerUtils.getAllDistributionTypes();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        if (getIntent() != null && getIntent().getExtras() != null) {
            final FileManagerItem fileManagerItem = (FileManagerItem) getIntent().getExtras().get("fileItem");
            if (fileManagerItem != null) {
                TextView currentFile = (TextView) findViewById(R.id.lblESelectedFile);
                currentFile.setText(fileManagerItem.getFilename());
                currentSelectedFile = fileManagerItem;
            }
        }

        final Spinner spiLanguageCode = (Spinner) findViewById(R.id.spiLanguageCode);
        final LanguageManagerAdapter dataAdapter = new LanguageManagerAdapter( //
                this, //
                android.R.layout.simple_spinner_item, listOfAllLanguageCode //
        );
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spiLanguageCode.setAdapter(dataAdapter);

        final Spinner spiDistribution = (Spinner) findViewById(R.id.spiDistribution);
        final DistributionManager distributionDataAdapter = new DistributionManager( //
                this, //
                android.R.layout.simple_spinner_item, listOfAllDistributions //
        );
        distributionDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spiDistribution.setAdapter(distributionDataAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void pFindFile(View view) {
        final Intent intent = new Intent(MainActivity.this, FileManagerActivity.class);
        startActivity(intent);
    }
}