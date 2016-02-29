package com.steelzack.chartizateapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.steelzack.chartizate.ChartizateFontManager;
import com.steelzack.chartizate.ChartizateFontManagerImpl;
import com.steelzack.chartizateapp.common.ChartizateSurfaceView;
import com.steelzack.chartizateapp.distribution.manager.DistributionManager;
import com.steelzack.chartizateapp.file.manager.FileManagerItem;
import com.steelzack.chartizateapp.font.manager.FontManagerAdapter;
import com.steelzack.chartizateapp.language.manager.LanguageManagerAdapter;

import java.io.File;
import java.util.Collections;
import java.util.List;

/**
 * Created by joao on 28-2-16.
 */
public class MainFragment extends Fragment {
    public static final int FILE_FIND = 0;

    public static final int FOLDER_FIND = 1;

    private FileManagerItem currentSelectedFile = null;

    private FileManagerItem currentSelectedFolder = null;

    final List<String> listOfAllLanguageCode = ChartizateFontManager.getAllUniCodeBlockStringsJava7();

    final List<String> listOfAllDistributions = ChartizateFontManager.getAllDistributionTypes();

    final List<String> listOfAllFonts = ChartizateFontManagerImpl.getAllFontTypes();

    private ChartizateSurfaceView svSelectedColor;

    private EditText editFontSize;

    private Button btnStart;

    private Button btnStartEmail;

    private TextView textStatus;

    private View mainView;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mainView == null) {
            mainView = inflater.inflate(R.layout.content_main, container, false);
            Collections.sort(listOfAllLanguageCode);
            Collections.sort(listOfAllFonts);

            if (getActivity().getIntent() != null && getActivity().getIntent().getExtras() != null) {
                final FileManagerItem fileManagerItem = (FileManagerItem) getActivity().getIntent().getExtras().get("fileItem");
                if (fileManagerItem != null) {
                    TextView currentFile = (TextView) getMainView().findViewById(com.steelzack.chartizateapp.R.id.lblESelectedFile);
                    currentFile.setText(fileManagerItem.getFilename());
                    setCurrentSelectedFile(fileManagerItem);
                }

                final FileManagerItem folderManagerItem = (FileManagerItem) getActivity().getIntent().getExtras().get("folderItem");
                if (folderManagerItem != null) {
                    TextView currentFile = (TextView) getMainView().findViewById(com.steelzack.chartizateapp.R.id.lblOutputFolder);
                    currentFile.setText(folderManagerItem.getFilename());
                    setCurrentSelectedFolder(folderManagerItem);
                }
            }

            final Spinner spiLanguageCode = (Spinner) getMainView().findViewById(com.steelzack.chartizateapp.R.id.spiLanguageCode);
            final LanguageManagerAdapter dataAdapter = new LanguageManagerAdapter( //
                    getActivity(), //
                    android.R.layout.simple_spinner_item, listOfAllLanguageCode //
            );
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spiLanguageCode.setAdapter(dataAdapter);

            final Spinner spiDistribution = (Spinner) getMainView().findViewById(com.steelzack.chartizateapp.R.id.spiDistribution);
            final DistributionManager distributionDataAdapter = new DistributionManager( //
                    getActivity(), //
                    android.R.layout.simple_spinner_item, listOfAllDistributions //
            );
            distributionDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spiDistribution.setAdapter(distributionDataAdapter);


            final Spinner spiFontType = (Spinner) getMainView().findViewById(com.steelzack.chartizateapp.R.id.spiFontType);
            final FontManagerAdapter fontManagerAdapter = new FontManagerAdapter( //
                    getActivity(), //
                    android.R.layout.simple_spinner_item, listOfAllFonts //
            );
            distributionDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spiFontType.setAdapter(fontManagerAdapter);

            svSelectedColor = (ChartizateSurfaceView) getMainView().findViewById(com.steelzack.chartizateapp.R.id.svSelectedColor);

            spiDistribution.setEnabled(false);

            editFontSize = (EditText) getMainView().findViewById(com.steelzack.chartizateapp.R.id.editFontSize);

            btnStart = (Button) getMainView().findViewById(R.id.btnStart);
            btnStartEmail = (Button) getMainView().findViewById(R.id.btnStartAndEmail);
            getBtnStart().setEnabled(false);
            getBtnStartEmail().setEnabled(false);

            final EditText editFileName = (EditText) getMainView().findViewById(R.id.editOutputFileName);
            editFileName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    checkButtonStart();
                    return true;
                }
            });

            textStatus = (TextView) getMainView().findViewById(R.id.textStatus);


            final EditText density = ((EditText) getMainView().findViewById(R.id.editDensity));
            density.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    checkButtonStart();
                    return false;
                }
            });

            final EditText range = ((EditText) getMainView().findViewById(R.id.editRange));
            range.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    checkButtonStart();
                    return false;
                }
            });
        }
        return getMainView();
    }

    public void checkButtonStart() {
        boolean validate = validate();
        getBtnStart().setEnabled(validate);
        getBtnStartEmail().setEnabled(validate);
    }

    public boolean validate() {
        if (getCurrentSelectedFile() == null) {
            return false;
        }
        if (getCurrentSelectedFolder() == null) {
            return false;
        }
        if (getCurrentSelectedFolder().getFile() == null) {
            return false;
        }


        final File rawCurrehtSelectedFile = getCurrentSelectedFile().getFile();
        if (rawCurrehtSelectedFile == null) {
            return false;
        }
        final String rawFontSize = ((EditText) getMainView().findViewById(R.id.editFontSize)).getText().toString();
        if (rawFontSize.isEmpty()) {
            return false;
        }
        final String outputFileName = ((EditText) getMainView().findViewById(com.steelzack.chartizateapp.R.id.editOutputFileName)).getText().toString();
        if (outputFileName.isEmpty()) {
            return false;
        }
        final String fontType = ((Spinner) getMainView().findViewById(R.id.spiFontType)).getSelectedItem().toString();
        if (fontType.isEmpty()) {
            return false;
        }
        final String alphabet = ((Spinner) getMainView().findViewById(R.id.spiLanguageCode)).getSelectedItem().toString();
        if (alphabet.isEmpty()) {
            return false;
        }

        final String density = ((EditText) getMainView().findViewById(R.id.editDensity)).getText().toString();
        if (density.isEmpty()) {
            return false;
        }

        final String range = ((EditText) getMainView().findViewById(R.id.editRange)).getText().toString();
        if (range.isEmpty()) {
            return false;
        }
        return true;
    }

    public View getMainView() {
        return mainView;
    }

    public FileManagerItem getCurrentSelectedFile() {
        return currentSelectedFile;
    }

    public void setCurrentSelectedFile(FileManagerItem currentSelectedFile) {
        this.currentSelectedFile = currentSelectedFile;
    }

    public void setCurrentSelectedFolder(FileManagerItem currentSelectedFolder) {
        this.currentSelectedFolder = currentSelectedFolder;
    }

    public Button getBtnStart() {
        return btnStart;
    }

    public Button getBtnStartEmail() {
        return btnStartEmail;
    }

    public TextView getTextStatus() {
        return textStatus;
    }

    public ChartizateSurfaceView getSvSelectedColor() {
        return svSelectedColor;
    }

    public FileManagerItem getCurrentSelectedFolder() {
        return currentSelectedFolder;
    }

    public EditText getEditFontSize() {
        return editFontSize;
    }
}
