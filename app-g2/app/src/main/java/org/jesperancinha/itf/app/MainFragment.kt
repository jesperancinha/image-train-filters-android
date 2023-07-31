package org.jesperancinha.itf.app

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import org.jesperancinha.itf.app.R.id.btnStart
import org.jesperancinha.itf.app.R.id.btnStartAndEmail
import org.jesperancinha.itf.app.R.id.svSelectedColor
import org.jesperancinha.itf.app.config.ControlConfiguration
import java.util.Objects

class MainFragment : Fragment() {
    private var mainView: View? = null
    lateinit var imageConfiguration: ImageConfiguration
    lateinit var controlConfiguration: ControlConfiguration
    lateinit var editConfiguration: EditConfiguration
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        if (Objects.isNull(mainView)) {
            assignControls(inflater, container)
            setSelectedFolders()
            populateControls()
            setSelectedColor()
            setUpFilenameEditor()
            setUpDensityEditor()
            setUpRangeEditor()
        }
        return mainView
    }

    private fun setSelectedFolders() {
        val intent: Intent? = requireActivity().intent
        if (intent != null && intent.getExtras() != null) {
            setSelectedFile()
            setSelectedFolder()
        }
    }

    private fun populateControls() {
        populateLanguageSpinner()
        populateDistributionSpinner()
        populateFontTypeSpinner()
    }

    private fun assignControls(inflater: LayoutInflater, container: ViewGroup?) {
        mainView = inflater.inflate(R.layout.content_main, container, false)
        controlConfiguration = ControlConfiguration(
            svSelectedColor=  mainView.findViewById(svSelectedColor),
            btnStart = mainView.findViewById(btnStart),
            btnStartEmail = mainView.findViewById(btnStartAndEmail),
            textStatus = mainView.findViewById(textStatus),
            spiFontType  = mainView.findViewById(spiFontType),
            spiDistribution =  mainView.findViewById(spiDistribution),
            spiLanguageCode = mainView.findViewById(spiLanguageCode))
        editConfiguration = EditConfiguration(
            editFontSize = mainView.findViewById(editFontSize),
            density = mainView.findViewById(editDensity),
            range = mainView.findViewById(editRange),
            editOutputFileName = mainView.findViewById(editOutputFileName))
        imageConfiguration = ImageConfiguration(controlConfiguration, editConfiguration)
        controlConfiguration.getBtnStart().setEnabled(false)
        controlConfiguration.getBtnStartEmail().setEnabled(false)
    }

    private fun setUpRangeEditor() {
        editConfiguration.getRange().setOnKeyListener { v, keyCode, event ->
            checkButtonStart()
            false
        }
    }

    private fun setUpDensityEditor() {
        editConfiguration.getDensity().setOnKeyListener { v, keyCode, event ->
            checkButtonStart()
            false
        }
    }

    private fun setUpFilenameEditor() {
        editConfiguration.getEditOutputFileName().setOnEditorActionListener { v, actionId, event ->
            checkButtonStart()
            true
        }
    }

    private fun setSelectedColor() {
        val svSelectedColor: ChartizateSurfaceView = controlConfiguration.getSvSelectedColor()
        svSelectedColor.setColor(Color.BLACK)
        svSelectedColor.setBackgroundColor(Color.BLACK)
    }

    private fun populateFontTypeSpinner() {
        val fontManagerAdapter = FontManagerAdapter(
            activity,
            android.R.layout.simple_spinner_item, imageConfiguration.getListOfAllFonts()
        )
        controlConfiguration.getSpiFontType().setAdapter(fontManagerAdapter)
    }

    private fun populateDistributionSpinner() {
        val distributionDataAdapter = DistributionManager(
            activity,
            android.R.layout.simple_spinner_item, imageConfiguration.getListOfAllDistributions()
        )
        val spiDistribution: Spinner = controlConfiguration.getSpiDistribution()
        spiDistribution.setAdapter(distributionDataAdapter)
        spiDistribution.setEnabled(false)
        distributionDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    }

    private fun populateLanguageSpinner() {
        val dataAdapter = LanguageManagerAdapter(
            activity,
            android.R.layout.simple_spinner_item, imageConfiguration.getListOfAllLanguageCode()
        )
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val spiLanguageCode: Spinner = controlConfiguration.getSpiLanguageCode()
        spiLanguageCode.setAdapter(dataAdapter)
        spiLanguageCode.setOnItemSelectedListener(object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                checkButtonStart()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })
    }

    private fun setSelectedFolder() {
        val bundle = requireActivity().intent.extras
        if (bundle != null) {
            val folderManagerItem: FileManagerItem? = bundle["folderItem"] as FileManagerItem?
            if (folderManagerItem != null) {
                val currentFile = mainView.findViewById<TextView>(R.id.lblOutputFolder)
                currentFile.setText(folderManagerItem.getFilename())
                imageConfiguration.setCurrentSelectedFolder(folderManagerItem)
            }
        }
    }

    private fun setSelectedFile() {
        val bundle = requireActivity().intent.extras
        if (bundle != null) {
            val fileManagerItem: FileManagerItem? = bundle["fileItem"] as FileManagerItem?
            if (fileManagerItem != null) {
                val currentFile = mainView.findViewById<TextView>(R.id.lblESelectedFile)
                currentFile.setText(fileManagerItem.getFilename())
                imageConfiguration.setCurrentSelectedFile(fileManagerItem)
            }
        }
    }

    fun checkButtonStart() {
        val validate: Boolean = imageConfiguration.validate()
        controlConfiguration.getBtnStart().setEnabled(validate)
        controlConfiguration.getBtnStartEmail().setEnabled(validate)
    }

    fun getControlConfiguration(): ControlConfiguration? {
        return controlConfiguration
    }

    fun getEditConfiguration(): EditConfiguration? {
        return editConfiguration
    }

    fun getImageConfiguration(): ImageConfiguration? {
        return imageConfiguration
    }
}