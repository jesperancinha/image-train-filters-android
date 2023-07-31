package org.jesperancinha.itf.app.config

import java.io.File
import java.util.Collections
import java.util.Objects
import java.util.stream.Collectors
import org.jesperancinha.chartizate.ChartizateUnicodes
import org.jesperancinha.chartizate.ChartizateFontManagerImpl
import org.jesperancinha.chartizate.distributions.ChartizateDistributionType
import org.jesperancinha.itf.app.manager.FileManagerItem
import org.jesperancinha.itf.app.ITFConstants.EMPTY_SELECTION


class ImageConfiguration(
    val controlConfiguration: ControlConfiguration,
    val editConfiguration: EditConfiguration
) {
    private val listOfAllLanguageCode: MutableList<String> =
        ChartizateUnicodes.getAllUniCodeBlocksJava().stream().map(
            Objects::toString
        ).collect(Collectors.toList())
    private val listOfAllDistributions: List<String> =
        ChartizateDistributionType.getAllDistributionTypes()
     val listOfAllFont = ChartizateFontManagerImpl.getAllFontTypes()
    private val currentSelectedFile: FileManagerItem? = null
    private val currentSelectedFolder: FileManagerItem? = null

    init {
        listOfAllLanguageCode.add(EMPTY_SELECTION)
        sortFontNames()
        sortLanguageCodes()
    }

    private fun sortFontNames() {
//        listOfAllFonts.sort()
    }

    private fun sortLanguageCodes() {
        listOfAllLanguageCode.sortWith { lhs: String, rhs: String ->
            lhs.compareTo(rhs)
        }
    }

    private fun getInteger(lhs: String, rhs: String): Int? {
        if (lhs == EMPTY_SELECTION) {
            return -1
        }
        return if (rhs == EMPTY_SELECTION) {
            1
        } else null
    }

    fun validate(): Boolean {
        return validateFile() && validateFolder() && validateRawCurrentSelectedFile() &&
                validateRawFontSize() && validateOutputFile() && validateFondType() &&
                validateAphabet() && validateDensity() && validateRange()
    }

    private fun validateRange(): Boolean {
        val range: String = editConfiguration.range.getText().toString()
        return !range.isEmpty()
    }

    private fun validateDensity(): Boolean {
        val density: String = editConfiguration.density.getText().toString()
        return !density.isEmpty()
    }

    private fun validateAphabet(): Boolean {
        val alphabet: String =
            controlConfiguration.spiLanguageCode.getSelectedItem().toString()
        return !(alphabet.isEmpty() || alphabet == EMPTY_SELECTION)
    }

    private fun validateFondType(): Boolean {
        val fontType: String = controlConfiguration.spiFontType.getSelectedItem().toString()
        return !fontType.isEmpty()
    }

    private fun validateOutputFile(): Boolean {
        val outputFileName: String = editConfiguration.editOutputFileName.getText().toString()
        return !outputFileName.isEmpty()
    }

    private fun validateRawFontSize(): Boolean {
        val rawFontSize: String = editConfiguration.editFontSize.getText().toString()
        return !rawFontSize.isEmpty()
    }

    private fun validateRawCurrentSelectedFile(): Boolean {
        val rawCurrentSelectedFile = currentSelectedFile?.file
        return rawCurrentSelectedFile != null
    }

    private fun validateFolder(): Boolean {
        return if (currentSelectedFolder == null) {
            false
        } else currentSelectedFolder.file != null
    }

    private fun validateFile(): Boolean {
        return currentSelectedFile != null
    }
}