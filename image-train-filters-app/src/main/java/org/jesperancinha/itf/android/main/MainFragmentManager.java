package org.jesperancinha.itf.android.main;

import android.view.View;

import org.jesperancinha.itf.android.config.ControlConfiguration;
import org.jesperancinha.itf.android.config.EditConfiguration;
import org.jesperancinha.itf.android.config.ImageConfiguration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import static java.lang.Integer.parseInt;

@Builder
@AllArgsConstructor
@Getter
public class MainFragmentManager {

    private final MainFragment mainFragment;


    public String getOutputFileName() {
        return getEditConfiguration().getEditOutputFileName().getText().toString();
    }

    public String getDestinationImagePath() {
        return new File(mainFragment.getImageConfiguration().getCurrentSelectedFolder().getFile(), getOutputFileName()).getAbsolutePath();
    }

    public FileInputStream getImageFullStream() throws FileNotFoundException {
        return new FileInputStream(
                new File(getImageConfiguration().getCurrentSelectedFile().getFile().getAbsolutePath()));
    }

    public Character.UnicodeBlock getBlock() {
        return Character.UnicodeBlock.forName(getControlConfiguration().getSpiLanguageCode().getSelectedItem().toString());
    }

    public View getMainView() {
        return mainFragment.getView();
    }

    public int getFontSize() {
        return parseInt(getEditConfiguration().getEditFontSize().getText().toString());
    }

    public String getFontName() {
        return getControlConfiguration().getSpiFontType().getSelectedItem().toString();
    }

    public int getRangePercentage() {
        return parseInt(getEditConfiguration().getRange().getText().toString());
    }

    public int getDensity() {
        return parseInt(getEditConfiguration().getDensity().getText().toString());
    }

    public int getBackground() {
        return getControlConfiguration().getSvSelectedColor().getColor();
    }

    public EditConfiguration getEditConfiguration() {
        return this.mainFragment.getEditConfiguration();
    }

    public ControlConfiguration getControlConfiguration() {
        return this.mainFragment.getControlConfiguration();
    }

    public ImageConfiguration getImageConfiguration() {
        return this.mainFragment.getImageConfiguration();
    }

}
