package org.jesperancinha.itf.android.config;

import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import org.jesperancinha.itf.android.R;
import org.jesperancinha.itf.android.file.manager.FileManagerItem;

import java.io.File;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static org.jesperancinha.itf.android.ITFConstants.EMPTY_SELECTION;
import static org.jesperancinha.itf.android.R.id.editDensity;
import static org.jesperancinha.itf.android.R.id.editOutputFileName;
import static org.jesperancinha.itf.android.R.id.editRange;
import static org.jesperancinha.itf.android.R.id.spiLanguageCode;

@Getter
@Setter
@NoArgsConstructor
public class ImageConfiguration {

    private View mainView;
    private FileManagerItem currentSelectedFile;
    private FileManagerItem currentSelectedFolder;

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
