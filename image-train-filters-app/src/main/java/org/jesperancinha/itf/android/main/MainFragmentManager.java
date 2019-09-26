package org.jesperancinha.itf.android.main;

import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.view.View;

import org.jesperancinha.chartizate.ChartizateManager;
import org.jesperancinha.chartizate.ChartizateManagerBuilderImpl;

import java.io.File;
import java.io.FileInputStream;

import lombok.Getter;

import static java.lang.Integer.parseInt;
import static org.jesperancinha.chartizate.distributions.ChartizateDistributionType.Linear;


@Getter
public class MainFragmentManager extends MainFragmentManagerConfiguration {

    protected MainFragment mainFragment;

    public MainFragmentManager(MainFragment mainFragment) {
        this.mainFragment = mainFragment;
    }

    public View getMainView() {
        return mainFragment.getView();
    }

    public String getOutputFileName() {
        return getEditConfiguration().getEditOutputFileName().getText().toString();
    }


    protected ChartizateManager<Integer, Typeface, Bitmap> setUpManager() throws java.io.IOException {
        return new ChartizateManagerBuilderImpl()
                .backgroundColor(getControlConfiguration().getSvSelectedColor().getColor())
                .densityPercentage(parseInt(getEditConfiguration().getDensity().getText().toString()))
                .rangePercentage(parseInt(getEditConfiguration().getRange().getText().toString()))
                .distributionType(Linear)
                .fontName(getControlConfiguration().getSpiFontType().getSelectedItem().toString())
                .fontSize(parseInt(getEditConfiguration().getEditFontSize().getText().toString()))
                .block(Character.UnicodeBlock.forName(getControlConfiguration().getSpiLanguageCode().getSelectedItem().toString()))
                .imageFullStream(new FileInputStream(new File(getImageConfiguration().getCurrentSelectedFile().getFile().getAbsolutePath())))
                .destinationImagePath(new File(getImageConfiguration().getCurrentSelectedFolder().getFile(), getOutputFileName()).getAbsolutePath())
                .build();
    }
}
