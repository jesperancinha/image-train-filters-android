package org.jesperancinha.itf.android;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import org.jesperancinha.chartizate.ChartizateFontManagerImpl;
import org.jesperancinha.chartizate.distributions.ChartizateDistributionType;
import org.jesperancinha.itf.android.common.ChartizateSurfaceView;
import org.jesperancinha.itf.android.distribution.manager.DistributionManager;
import org.jesperancinha.itf.android.file.manager.FileManagerItem;
import org.jesperancinha.itf.android.font.manager.FontManagerAdapter;
import org.jesperancinha.itf.android.language.manager.LanguageManagerAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class MainFragment extends Fragment {
    public static final int FILE_FIND = 0;
    public static final int FOLDER_FIND = 1;
    public static final String EMPTY_SELECTION = "------------------------------------------------------------------";
    private FileManagerItem currentSelectedFile = null;
    private FileManagerItem currentSelectedFolder = null;
    private List<String> listOfAllLanguageCode = ChartizateFontManagerImpl.getAllFontTypes();
    private final List<String> listOfAllDistributions = ChartizateDistributionType.getAllDistributionTypes();
    private final List<String> listOfAllFonts = ChartizateFontManagerImpl.getAllFontTypes();

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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mainView == null) {
            mainView = inflater.inflate(R.layout.content_main, container, false);
            listOfAllLanguageCode = new ArrayList<>(listOfAllLanguageCode);
            listOfAllLanguageCode.add(EMPTY_SELECTION);
            Collections.sort(listOfAllLanguageCode, (lhs, rhs) -> {
                Integer x = getInteger(lhs, rhs, EMPTY_SELECTION);
                if (x != null) return x;
                return lhs.compareTo(rhs);
            });
            Collections.sort(listOfAllFonts);

            if (Objects.requireNonNull(getActivity()).getIntent() != null && getActivity().getIntent().getExtras() != null) {
                final FileManagerItem fileManagerItem = (FileManagerItem) getActivity().getIntent().getExtras().get("fileItem");
                if (fileManagerItem != null) {
                    TextView currentFile = getMainView().findViewById(R.id.lblESelectedFile);
                    currentFile.setText(fileManagerItem.getFilename());
                    setCurrentSelectedFile(fileManagerItem);
                }

                final FileManagerItem folderManagerItem = (FileManagerItem) getActivity().getIntent().getExtras().get("folderItem");
                if (folderManagerItem != null) {
                    TextView currentFile = getMainView().findViewById(R.id.lblOutputFolder);
                    currentFile.setText(folderManagerItem.getFilename());
                    setCurrentSelectedFolder(folderManagerItem);
                }
            }

            final Spinner spiLanguageCode = getMainView().findViewById(R.id.spiLanguageCode);
            final LanguageManagerAdapter dataAdapter = new LanguageManagerAdapter(
                    getActivity(),
                    android.R.layout.simple_spinner_item, listOfAllLanguageCode
            );
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spiLanguageCode.setAdapter(dataAdapter);
            spiLanguageCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    checkButtonStart();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }

            });

            final Spinner spiDistribution = getMainView().findViewById(R.id.spiDistribution);
            final DistributionManager distributionDataAdapter = new DistributionManager(
                    getActivity(),
                    android.R.layout.simple_spinner_item, listOfAllDistributions
            );
            distributionDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spiDistribution.setAdapter(distributionDataAdapter);


            final Spinner spiFontType = getMainView().findViewById(R.id.spiFontType);
            final FontManagerAdapter fontManagerAdapter = new FontManagerAdapter(
                    getActivity(),
                    android.R.layout.simple_spinner_item, listOfAllFonts
            );
            distributionDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spiFontType.setAdapter(fontManagerAdapter);

            svSelectedColor = getMainView().findViewById(R.id.svSelectedColor);
            svSelectedColor.setColor(Color.BLACK);
            getSvSelectedColor().setBackgroundColor(Color.BLACK);

            spiDistribution.setEnabled(false);

            editFontSize = getMainView().findViewById(R.id.editFontSize);

            btnStart = getMainView().findViewById(R.id.btnStart);
            btnStartEmail = getMainView().findViewById(R.id.btnStartAndEmail);
            getBtnStart().setEnabled(false);
            getBtnStartEmail().setEnabled(false);

            final EditText editFileName = getMainView().findViewById(R.id.editOutputFileName);
            editFileName.setOnEditorActionListener((v, actionId, event) -> {
                checkButtonStart();
                return true;
            });

            textStatus = getMainView().findViewById(R.id.textStatus);


            final EditText density = getMainView().findViewById(R.id.editDensity);
            density.setOnKeyListener((v, keyCode, event) -> {
                checkButtonStart();
                return false;
            });

            final EditText range = getMainView().findViewById(R.id.editRange);
            range.setOnKeyListener((v, keyCode, event) -> {
                checkButtonStart();
                return false;
            });
        }
        return getMainView();
    }

    @Nullable
    public Integer getInteger(String lhs, String rhs, String code) {
        if (lhs.equals(code)) {
            return -1;
        }
        if (rhs.equals(code)) {
            return 1;
        }
        return null;
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
        final String outputFileName = ((EditText) getMainView().findViewById(org.jesperancinha.itf.android.R.id.editOutputFileName)).getText().toString();
        if (outputFileName.isEmpty()) {
            return false;
        }
        final String fontType = ((Spinner) getMainView().findViewById(R.id.spiFontType)).getSelectedItem().toString();
        if (fontType.isEmpty()) {
            return false;
        }
        final String alphabet = ((Spinner) getMainView().findViewById(R.id.spiLanguageCode)).getSelectedItem().toString();
        if (alphabet.isEmpty() || alphabet.equals(EMPTY_SELECTION)) {
            return false;
        }

        final String density = ((EditText) getMainView().findViewById(R.id.editDensity)).getText().toString();
        if (density.isEmpty()) {
            return false;
        }

        final String range = ((EditText) getMainView().findViewById(R.id.editRange)).getText().toString();
        return !range.isEmpty();
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
