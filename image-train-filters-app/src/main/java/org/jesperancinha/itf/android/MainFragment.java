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
import org.jesperancinha.chartizate.ChartizateUnicodes;
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
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static org.jesperancinha.itf.android.R.id.editDensity;
import static org.jesperancinha.itf.android.R.id.editOutputFileName;
import static org.jesperancinha.itf.android.R.id.editRange;
import static org.jesperancinha.itf.android.R.id.spiLanguageCode;

public class MainFragment extends Fragment {
    public static final int FILE_FIND = 0;
    public static final int FOLDER_FIND = 1;
    public static final String EMPTY_SELECTION = "------------------------------------------------------------------";
    private FileManagerItem currentSelectedFile = null;
    private FileManagerItem currentSelectedFolder = null;
    private List<String> listOfAllLanguageCode = ChartizateUnicodes.getAllUniCodeBlocksJava().stream().map(Objects::toString).collect(Collectors.toList());
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
        if (isNull(mainView)) {
            assignControls(inflater, container);
            sortLanguageCodes();
            sortFontNames();
            populateControls();
            setSelectedColor();
            setUpFilenameEditor();
            setUpDensityEditor();
            setUpRangeEditor();
        }
        return getMainView();
    }

    private void populateControls() {
        populateLanguageSpinner();
        populateDistributionSpinner();
        populateFontTypeSpinner();
    }

    private void assignControls(LayoutInflater inflater, ViewGroup container) {
        this.listOfAllLanguageCode = new ArrayList<>(listOfAllLanguageCode);
        this.listOfAllLanguageCode.add(EMPTY_SELECTION);
        this.mainView = inflater.inflate(R.layout.content_main, container, false);
        this.editFontSize = this.mainView.findViewById(R.id.editFontSize);
        this.btnStart = this.mainView.findViewById(R.id.btnStart);
        this.btnStartEmail = this.mainView.findViewById(R.id.btnStartAndEmail);
        this.textStatus = this.mainView.findViewById(R.id.textStatus);
        this.btnStart.setEnabled(false);
        this.btnStartEmail.setEnabled(false);
    }

    private void sortFontNames() {
        Collections.sort(listOfAllFonts);
        if (Objects.requireNonNull(getActivity()).getIntent() != null && getActivity().getIntent().getExtras() != null) {
            setSelectedFile();
            setSelectedFolder();
        }
    }

    private void sortLanguageCodes() {
        Collections.sort(listOfAllLanguageCode, (lhs, rhs) -> {
            Integer x = getInteger(lhs, rhs, EMPTY_SELECTION);
            if (x != null) return x;
            return lhs.compareTo(rhs);
        });
    }

    private void setUpRangeEditor() {
        final EditText range = getMainView().findViewById(editRange);
        range.setOnKeyListener((v, keyCode, event) -> {
            checkButtonStart();
            return false;
        });
    }

    private void setUpDensityEditor() {
        final EditText density = this.mainView.findViewById(editDensity);
        density.setOnKeyListener((v, keyCode, event) -> {
            checkButtonStart();
            return false;
        });
    }

    private void setUpFilenameEditor() {
        final EditText editFileName = this.mainView.findViewById(editOutputFileName);
        editFileName.setOnEditorActionListener((v, actionId, event) -> {
            checkButtonStart();
            return true;
        });
    }

    private void setSelectedColor() {
        svSelectedColor = this.mainView.findViewById(R.id.svSelectedColor);
        svSelectedColor.setColor(Color.BLACK);
        getSvSelectedColor().setBackgroundColor(Color.BLACK);
    }

    private void populateFontTypeSpinner() {
        final Spinner spiFontType = this.mainView.findViewById(R.id.spiFontType);
        final FontManagerAdapter fontManagerAdapter = new FontManagerAdapter(
                getActivity(),
                android.R.layout.simple_spinner_item, listOfAllFonts
        );
        spiFontType.setAdapter(fontManagerAdapter);
    }

    private void populateDistributionSpinner() {
        final Spinner spiDistribution = this.mainView.findViewById(R.id.spiDistribution);
        final DistributionManager distributionDataAdapter = new DistributionManager(
                getActivity(),
                android.R.layout.simple_spinner_item, listOfAllDistributions
        );
        spiDistribution.setAdapter(distributionDataAdapter);
        spiDistribution.setEnabled(false);
        distributionDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    private void populateLanguageSpinner() {
        final Spinner spiLanguageCode = this.mainView.findViewById(R.id.spiLanguageCode);
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
    }

    private void setSelectedFolder() {
        final FileManagerItem folderManagerItem = (FileManagerItem) getActivity().getIntent().getExtras().get("folderItem");
        if (folderManagerItem != null) {
            TextView currentFile = this.mainView.findViewById(R.id.lblOutputFolder);
            currentFile.setText(folderManagerItem.getFilename());
            setCurrentSelectedFolder(folderManagerItem);
        }
    }

    private void setSelectedFile() {
        final FileManagerItem fileManagerItem = (FileManagerItem) getActivity().getIntent().getExtras().get("fileItem");
        if (fileManagerItem != null) {
            TextView currentFile = this.mainView.findViewById(R.id.lblESelectedFile);
            currentFile.setText(fileManagerItem.getFilename());
            setCurrentSelectedFile(fileManagerItem);
        }
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
        return validateFile() && validateFolder() && validateRawCurrentSelectedFile() &&
                validateRawFontSize() && validateOutputFile() && validateFondType() &&
                validateAphabet() && validateDensity() && validateRange();
    }

    private boolean validateRange() {
        final String range = ((EditText) this.mainView.findViewById(editRange)).getText().toString();
        return !range.isEmpty();
    }

    private boolean validateDensity() {
        final String density = ((EditText) this.mainView.findViewById(editDensity)).getText().toString();
        return !density.isEmpty();
    }

    private boolean validateAphabet() {
        final String alphabet = ((Spinner) this.mainView.findViewById(spiLanguageCode)).getSelectedItem().toString();
        return !(alphabet.isEmpty() || alphabet.equals(EMPTY_SELECTION));
    }

    private boolean validateFondType() {
        final String fontType = ((Spinner) this.mainView.findViewById(R.id.spiFontType)).getSelectedItem().toString();
        return !fontType.isEmpty();
    }

    private boolean validateOutputFile() {
        final String outputFileName = ((EditText) this.mainView.findViewById(editOutputFileName)).getText().toString();
        return !outputFileName.isEmpty();
    }

    private boolean validateRawFontSize() {
        final String rawFontSize = ((EditText) this.mainView.findViewById(R.id.editFontSize)).getText().toString();
        return !rawFontSize.isEmpty();
    }

    private boolean validateRawCurrentSelectedFile() {
        final File rawCurrentSelectedFile = getCurrentSelectedFile().getFile();
        return rawCurrentSelectedFile != null;
    }

    private boolean validateFolder() {
        if (getCurrentSelectedFolder() == null) {
            return false;
        }
        return getCurrentSelectedFolder().getFile() != null;
    }

    private boolean validateFile() {
        return getCurrentSelectedFile() != null;
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
