package org.jesperancinha.itf.app

import android.graphics.Bitmap
import android.graphics.Typeface
import android.view.View
import lombok.Getter
import java.io.File
import java.io.FileInputStream
import java.io.IOException

class MainFragmentManager(protected var mainFragment: MainFragment) :
    MainFragmentManMainFragmentManagerConfigurationagerConfiguration() {
    val mainView: View?
        get() = mainFragment.view
    val outputFileName: String
        get() = getEditConfiguration().getEditOutputFileName().getText().toString()

    @Throws(IOException::class)
    protected fun setUpManager(): ChartizateManager<Int, Typeface, Bitmap> {
        return ChartizateManagerBuilderImpl()
            .backgroundColor(getControlConfiguration().getSvSelectedColor().getColor())
            .densityPercentage(getEditConfiguration().getDensity().getText().toString().toInt())
            .rangePercentage(getEditConfiguration().getRange().getText().toString().toInt())
            .distributionType(ChartizateDistributionType.Linear)
            .fontName(getControlConfiguration().getSpiFontType().getSelectedItem().toString())
            .fontSize(getEditConfiguration().getEditFontSize().getText().toString().toInt())
            .block(
                Character.UnicodeBlock.forName(
                    getControlConfiguration().getSpiLanguageCode().getSelectedItem().toString()
                )
            )
            .imageFullStream(
                FileInputStream(
                    File(
                        getImageConfiguration().getCurrentSelectedFile().getFile().getAbsolutePath()
                    )
                )
            )
            .destinationImagePath(
                File(
                    getImageConfiguration().getCurrentSelectedFolder().getFile(),
                    outputFileName
                ).getAbsolutePath()
            )
            .build()
    }
}