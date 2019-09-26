package org.jesperancinha.itf.android.config;

import androidx.annotation.Nullable;

import org.jesperancinha.chartizate.ChartizateFontManagerImpl;
import org.jesperancinha.chartizate.ChartizateUnicodes;
import org.jesperancinha.chartizate.distributions.ChartizateDistributionType;
import org.jesperancinha.itf.android.file.manager.FileManagerItem;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;

import static org.jesperancinha.itf.android.ITFConstants.EMPTY_SELECTION;

@Getter
@Setter
public class ImageConfiguration {

    private final ControlConfiguration controlConfiguration;
    private final EditConfiguration editConfiguration;
    private final List<String> listOfAllLanguageCode = ChartizateUnicodes.getAllUniCodeBlocksJava().stream().map(Objects::toString).collect(Collectors.toList());
    private final List<String> listOfAllDistributions = ChartizateDistributionType.getAllDistributionTypes();
    private final List<String> listOfAllFonts = ChartizateFontManagerImpl.getAllFontTypes();
    private FileManagerItem currentSelectedFile;
    private FileManagerItem currentSelectedFolder;

    public ImageConfiguration(ControlConfiguration controlConfiguration, EditConfiguration editConfiguration) {
        this.listOfAllLanguageCode.add(EMPTY_SELECTION);
        this.controlConfiguration = controlConfiguration;
        this.editConfiguration = editConfiguration;
        sortFontNames();
        sortLanguageCodes();
    }

    private void sortFontNames() {
        Collections.sort(listOfAllFonts);
    }

    private void sortLanguageCodes() {
        Collections.sort(listOfAllLanguageCode, (lhs, rhs) -> {
            Integer x = getInteger(lhs, rhs);
            if (x != null) return x;
            return lhs.compareTo(rhs);
        });
    }

    @Nullable
    private Integer getInteger(String lhs, String rhs) {
        if (lhs.equals(EMPTY_SELECTION)) {
            return -1;
        }
        if (rhs.equals(EMPTY_SELECTION)) {
            return 1;
        }
        return null;
    }

    public boolean validate() {
        return validateFile() && validateFolder() && validateRawCurrentSelectedFile() &&
                validateRawFontSize() && validateOutputFile() && validateFondType() &&
                validateAphabet() && validateDensity() && validateRange();
    }

    private boolean validateRange() {
        final String range = editConfiguration.getRange().getText().toString();
        return !range.isEmpty();
    }

    private boolean validateDensity() {
        final String density = editConfiguration.getDensity().getText().toString();
        return !density.isEmpty();
    }

    private boolean validateAphabet() {
        final String alphabet = controlConfiguration.getSpiLanguageCode().getSelectedItem().toString();
        return !(alphabet.isEmpty() || alphabet.equals(EMPTY_SELECTION));
    }

    private boolean validateFondType() {
        final String fontType = controlConfiguration.getSpiFontType().getSelectedItem().toString();
        return !fontType.isEmpty();
    }

    private boolean validateOutputFile() {
        final String outputFileName = editConfiguration.getEditOutputFileName().getText().toString();
        return !outputFileName.isEmpty();
    }

    private boolean validateRawFontSize() {
        final String rawFontSize = editConfiguration.getEditFontSize().getText().toString();
        return !rawFontSize.isEmpty();
    }

    private boolean validateRawCurrentSelectedFile() {
        final File rawCurrentSelectedFile = currentSelectedFile.getFile();
        return rawCurrentSelectedFile != null;
    }

    private boolean validateFolder() {
        if (currentSelectedFolder == null) {
            return false;
        }
        return currentSelectedFolder.getFile() != null;
    }

    private boolean validateFile() {
        return currentSelectedFile != null;
    }
}
