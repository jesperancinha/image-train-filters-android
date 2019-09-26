package org.jesperancinha.itf.android.main;

import android.view.View;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import lombok.Getter;

import static java.lang.Integer.parseInt;


@Getter
public class MainFragmentManager extends MainFragmentManagerConfiguration {

    protected MainFragment mainFragment;

    public MainFragmentManager(MainFragment mainFragment) {
        this.mainFragment = mainFragment;
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

    public String getOutputFileName() {
        return getEditConfiguration().getEditOutputFileName().getText().toString();
    }

    public String getDestinationImagePath() {
        return new File(getImageConfiguration().getCurrentSelectedFolder().getFile(), getOutputFileName()).getAbsolutePath();
    }

}
