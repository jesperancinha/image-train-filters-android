package org.jesperancinha.itf.android.main;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import org.jesperancinha.itf.android.R;
import org.jesperancinha.itf.android.common.ChartizateSurfaceView;
import org.jesperancinha.itf.android.config.ControlConfiguration;
import org.jesperancinha.itf.android.config.EditConfiguration;
import org.jesperancinha.itf.android.config.ImageConfiguration;
import org.jesperancinha.itf.android.distribution.manager.DistributionManager;
import org.jesperancinha.itf.android.file.manager.FileManagerItem;
import org.jesperancinha.itf.android.font.manager.FontManagerAdapter;
import org.jesperancinha.itf.android.language.manager.LanguageManagerAdapter;

import java.util.Objects;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;
import static org.jesperancinha.itf.android.R.id.btnStart;
import static org.jesperancinha.itf.android.R.id.btnStartAndEmail;
import static org.jesperancinha.itf.android.R.id.editDensity;
import static org.jesperancinha.itf.android.R.id.editFontSize;
import static org.jesperancinha.itf.android.R.id.editOutputFileName;
import static org.jesperancinha.itf.android.R.id.editRange;
import static org.jesperancinha.itf.android.R.id.spiDistribution;
import static org.jesperancinha.itf.android.R.id.spiFontType;
import static org.jesperancinha.itf.android.R.id.spiLanguageCode;
import static org.jesperancinha.itf.android.R.id.svSelectedColor;
import static org.jesperancinha.itf.android.R.id.textStatus;

public class MainFragment extends Fragment {

    private View mainView;

    private ImageConfiguration imageConfiguration;

    private ControlConfiguration controlConfiguration;

    private EditConfiguration editConfiguration;

    public MainFragment() {
        //
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
        this.mainView = inflater.inflate(R.layout.content_main, container, false);
        controlConfiguration = ControlConfiguration.builder()
                .btnStart(this.mainView.findViewById(btnStart))
                .btnStartEmail(this.mainView.findViewById(btnStartAndEmail))
                .textStatus(this.mainView.findViewById(textStatus))
                .svSelectedColor(this.mainView.findViewById(svSelectedColor))
                .spiFontType(this.mainView.findViewById(spiFontType))
                .spiDistribution(this.mainView.findViewById(spiDistribution))
                .spiLanguageCode(this.mainView.findViewById(spiLanguageCode))
                .build();
        editConfiguration = EditConfiguration.builder()
                .editFontSize(this.mainView.findViewById(editFontSize))
                .density(this.mainView.findViewById(editDensity))
                .range(mainView.findViewById(editRange))
                .editOutputFileName(this.mainView.findViewById(editOutputFileName))
                .build();
        this.imageConfiguration = new ImageConfiguration(controlConfiguration, editConfiguration);
        this.controlConfiguration.getBtnStart().setEnabled(false);
        this.controlConfiguration.getBtnStartEmail().setEnabled(false);
    }

    private void setUpRangeEditor() {
        this.editConfiguration.getRange().setOnKeyListener((v, keyCode, event) -> {
            checkButtonStart();
            return false;
        });
    }

    private void setUpDensityEditor() {
        this.editConfiguration.getDensity().setOnKeyListener((v, keyCode, event) -> {
            checkButtonStart();
            return false;
        });
    }

    private void setUpFilenameEditor() {
        this.editConfiguration.getEditOutputFileName().setOnEditorActionListener((v, actionId, event) -> {
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
        final FontManagerAdapter fontManagerAdapter = new FontManagerAdapter(
                getActivity(),
                android.R.layout.simple_spinner_item, imageConfiguration.getListOfAllFonts()
        );
        controlConfiguration.getSpiFontType().setAdapter(fontManagerAdapter);
    }

    private void populateDistributionSpinner() {
        final DistributionManager distributionDataAdapter = new DistributionManager(
                getActivity(),
                android.R.layout.simple_spinner_item, imageConfiguration.getListOfAllDistributions()
        );
        final Spinner spiDistribution = controlConfiguration.getSpiDistribution();
        spiDistribution.setAdapter(distributionDataAdapter);
        spiDistribution.setEnabled(false);
        distributionDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    private void populateLanguageSpinner() {
        final LanguageManagerAdapter dataAdapter = new LanguageManagerAdapter(
                getActivity(),
                android.R.layout.simple_spinner_item, imageConfiguration.getListOfAllLanguageCode()
        );
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        final Spinner spiLanguageCode = controlConfiguration.getSpiLanguageCode();
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
        boolean validate = imageConfiguration.validate();
        controlConfiguration.getBtnStart().setEnabled(validate);
        controlConfiguration.getBtnStartEmail().setEnabled(validate);
    }

    public ControlConfiguration getControlConfiguration() {
        return controlConfiguration;
    }

    public EditConfiguration getEditConfiguration() {
        return editConfiguration;
    }

    public ImageConfiguration getImageConfiguration() {
        return imageConfiguration;
    }
}
