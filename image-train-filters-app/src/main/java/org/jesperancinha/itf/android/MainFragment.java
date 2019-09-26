package org.jesperancinha.itf.android;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import org.jesperancinha.itf.android.common.ChartizateSurfaceView;
import org.jesperancinha.itf.android.config.ControlConfiguration;
import org.jesperancinha.itf.android.config.ImageConfiguration;
import org.jesperancinha.itf.android.distribution.manager.DistributionManager;
import org.jesperancinha.itf.android.file.manager.FileManagerItem;
import org.jesperancinha.itf.android.font.manager.FontManagerAdapter;
import org.jesperancinha.itf.android.language.manager.LanguageManagerAdapter;

import java.util.Objects;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;
import static org.jesperancinha.itf.android.R.id.editDensity;
import static org.jesperancinha.itf.android.R.id.editOutputFileName;
import static org.jesperancinha.itf.android.R.id.editRange;

public class MainFragment extends Fragment {

    private View mainView;

    private final ImageConfiguration imageConfiguration;

    private ControlConfiguration controlConfiguration;

    public MainFragment() {
        this.imageConfiguration = new ImageConfiguration();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (isNull(mainView)) {
            assignControls(inflater, container);
            setSelectedFolders();
            populateControls();
            setSelectedColor();
            setUpFilenameEditor();
            setUpDensityEditor();
            setUpRangeEditor();
        }
        return mainView;
    }

    private void setSelectedFolders() {
        if (requireNonNull(getActivity()).getIntent() != null && getActivity().getIntent().getExtras() != null) {
            setSelectedFile();
            setSelectedFolder();
        }
    }

    private void populateControls() {
        populateLanguageSpinner();
        populateDistributionSpinner();
        populateFontTypeSpinner();
    }

    private void assignControls(LayoutInflater inflater, ViewGroup container) {
        controlConfiguration = ControlConfiguration.builder()
                .editFontSize(this.mainView.findViewById(R.id.editFontSize))
                .btnStart(this.mainView.findViewById(R.id.btnStart))
                .btnStartEmail(this.mainView.findViewById(R.id.btnStartAndEmail))
                .textStatus(this.mainView.findViewById(R.id.textStatus))
                .svSelectedColor(this.mainView.findViewById(R.id.svSelectedColor)).build();
        this.mainView = inflater.inflate(R.layout.content_main, container, false);
        this.controlConfiguration.getBtnStart().setEnabled(false);
        this.controlConfiguration.getBtnStartEmail().setEnabled(false);
    }

    private void setUpRangeEditor() {
        final EditText range = mainView.findViewById(editRange);
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
        final ChartizateSurfaceView svSelectedColor = controlConfiguration.getSvSelectedColor();
        svSelectedColor.setColor(Color.BLACK);
        svSelectedColor.setBackgroundColor(Color.BLACK);
    }

    private void populateFontTypeSpinner() {
        final Spinner spiFontType = this.mainView.findViewById(R.id.spiFontType);
        final FontManagerAdapter fontManagerAdapter = new FontManagerAdapter(
                getActivity(),
                android.R.layout.simple_spinner_item, imageConfiguration.getListOfAllFonts()
        );
        spiFontType.setAdapter(fontManagerAdapter);
    }

    private void populateDistributionSpinner() {
        final Spinner spiDistribution = this.mainView.findViewById(R.id.spiDistribution);
        final DistributionManager distributionDataAdapter = new DistributionManager(
                getActivity(),
                android.R.layout.simple_spinner_item, imageConfiguration.getListOfAllDistributions()
        );
        spiDistribution.setAdapter(distributionDataAdapter);
        spiDistribution.setEnabled(false);
        distributionDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    private void populateLanguageSpinner() {
        final Spinner spiLanguageCode = this.mainView.findViewById(R.id.spiLanguageCode);
        final LanguageManagerAdapter dataAdapter = new LanguageManagerAdapter(
                getActivity(),
                android.R.layout.simple_spinner_item, imageConfiguration.getListOfAllLanguageCode()
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
        final FileManagerItem folderManagerItem = (FileManagerItem) Objects.requireNonNull(requireNonNull(getActivity()).getIntent().getExtras()).get("folderItem");
        if (folderManagerItem != null) {
            TextView currentFile = this.mainView.findViewById(R.id.lblOutputFolder);
            currentFile.setText(folderManagerItem.getFilename());
            imageConfiguration.setCurrentSelectedFolder(folderManagerItem);
        }
    }

    private void setSelectedFile() {
        final FileManagerItem fileManagerItem = (FileManagerItem) requireNonNull(requireNonNull(getActivity()).getIntent().getExtras()).get("fileItem");
        if (fileManagerItem != null) {
            TextView currentFile = this.mainView.findViewById(R.id.lblESelectedFile);
            currentFile.setText(fileManagerItem.getFilename());
            imageConfiguration.setCurrentSelectedFile(fileManagerItem);
        }
    }


    public void checkButtonStart() {
        imageConfiguration.setMainView(mainView);
        boolean validate = imageConfiguration.validate();
        controlConfiguration.getBtnStart().setEnabled(validate);
        controlConfiguration.getBtnStartEmail().setEnabled(validate);
    }


    public ImageConfiguration getImageConfiguration() {
        return imageConfiguration;
    }

    public ControlConfiguration getControlConfiguration() {
        return controlConfiguration;
    }
}
