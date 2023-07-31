package org.jesperancinha.itf.app

import android.Manifest.permission
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import org.jesperancinha.itf.app.ITFConstants.FILE_FIND
import java.io.File
import java.util.Objects

abstract class ActionsMainActivity : MainActivityManager() {
    protected fun setupActivity() {
        if (checkSelfPermission(
                permission.WRITE_EXTERNAL_STORAGE
            ) !== PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf<String>(
                    permission.WRITE_EXTERNAL_STORAGE,
                    permission.READ_EXTERNAL_STORAGE
                ), 1
            )
        }
        setContentView(R.layout.activity_main)
        chartizatePager = findViewById(R.id.itf_pager)
        val swipeAdapter = SwipeAdapter(
            getSupportFragmentManager(),
            arrayOf<Fragment>(MainFragment(), EmailFragment(), ViewFragment())
        )
        chartizatePager.setAdapter(swipeAdapter)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        toolbar.setTitle(R.string.app_name)
        toolbar.setLogo(R.mipmap.ic_launcher)
    }

    protected fun setupActivity(data: Intent) {
        val mainFragmentManager: MainFragmentManager? = mainFragment
        if (isDataPresent(data)) {
            val fileManagerItem: FileManagerItem? =
                Objects.requireNonNull<Bundle>(data.getExtras())["fileItem"] as FileManagerItem?
            setSelectedInputFileAndThumbnail(fileManagerItem)
            setSelectedOutputFolder(data)
        }
        mainFragmentManager.checkButtonStart()
    }

    fun pFindFile(view: View?) {
        val mainFragment: MainFragmentManager? = mainFragment
        val intent: Intent = mainFragment.getFindFileIntent()
        intent.putExtra("directoryManager", false)
        startActivityForResult(intent, FILE_FIND)
        mainFragment.checkButtonStart()
    }

    fun pGenerateFile(view: View?) {
        val controlConfiguration: ControlConfiguration =
            mainFragmentManager.getControlConfiguration()
        controlConfiguration.getBtnStart().setEnabled(false)
        controlConfiguration.getBtnStartEmail().setEnabled(false)
        controlConfiguration.getTextStatus().setText(chartizating)
        val task = createGenerateImageTask(view)
        mainFragmentManager.getControlConfiguration().getTextStatus().post(task)
    }

    protected fun createGenerateImageTask(view: View?): Runnable {
        return Runnable {
            try {
                val manager: ChartizateManager<Int, Typeface, Bitmap> =
                    mainFragmentManager.setUpManager()
                manager.saveImage(manager.generateConvertedImage())
            } catch (e: IllegalArgumentException) {
                mainFragmentManager.alertFailedUnicodeParsing()
            } catch (e: Exception) {
                throw RuntimeException(e)
            } finally {
                postFileGeneration(view)
            }
        }
    }

    protected fun postFileGeneration(view: View?) {
        val controlConfiguration: ControlConfiguration =
            mainFragmentManager.getControlConfiguration()
        controlConfiguration.getTextStatus().setText(R.string.done)
        controlConfiguration.getBtnStart()
            .post { controlConfiguration.getBtnStart().setEnabled(true) }
        controlConfiguration.getBtnStartEmail()
            .post { controlConfiguration.getBtnStartEmail().setEnabled(true) }
        chartizatePager.setCurrentItem(mainFragmentManager.getDestination(view))
        val viewFragment: ViewFragment =
            getSupportFragmentManager().getFragments().get(2) as ViewFragment
        val imageView: ImageView = viewFragment.getImageView()
        val imageViewEmail: ImageView = findViewById(R.id.imageViewGeneratedAttachment)
        val uri = Uri.fromFile(
            File(
                mainFragmentManager.getImageConfiguration().getCurrentSelectedFolder().getFile(),
                mainFragmentManager.getOutputFileName()
            )
        )
        imageView.post { createThumb(imageView).setImage(uri) }
        imageViewEmail.post { createThumb(imageViewEmail).setImage(uri) }
    }

    private fun createThumb(imageView: ImageView): ChartizateThumbs {
        return ChartizateThumbs.builder()
            .width(getResources().getInteger(R.integer.thumb_width))
            .height(getResources().getInteger(R.integer.thumb_height))
            .imageView(imageView)
            .build()
    }

    fun pGetBackGroundColor(view: View) {
        val mainFragmentManager: MainFragmentManager? = mainFragment
        ColorPickerDialogBuilder
            .with(view.context)
            .setTitle("Choose color")
            .initialColor(Color.WHITE)
            .wheelType(ColorPickerView.WHEEL_TYPE.CIRCLE)
            .density(5)
            .setPositiveButton("ok") { dialog, selectedColor, allColors ->
                this.mainFragmentManager.getControlConfiguration().getSvSelectedColor()
                    .setBackgroundColor(selectedColor)
                this.mainFragmentManager.getControlConfiguration().getSvSelectedColor()
                    .setColor(selectedColor)
            }
            .setNegativeButton("cancel") { dialog, which -> }
            .build().show()
        mainFragmentManager.checkButtonStart()
    }

    fun pFindOutputFolder(view: View?) {
        val mainFragmentManager: MainFragmentManager? = mainFragment
        val intent: Intent = mainFragmentManager.getFindFileIntent()
        intent.putExtra("directoryManager", true)
        startActivityForResult(intent, FOLDER_FIND)
        mainFragmentManager.checkButtonStart()
    }

    fun pAddOne(view: View?) {
        val mainFragmentManager: MainFragmentManager? = mainFragment
        val currentFontSize: Int =
            this.mainFragmentManager.getEditConfiguration().getEditFontSize().getText().toString()
                .toInt()
        this.mainFragmentManager.getEditConfiguration().getEditFontSize()
            .setText((currentFontSize + 1).toString())
        mainFragmentManager.checkButtonStart()
    }

    fun pMinusOne(view: View?) {
        val mainFragmentManager: MainFragmentManager? = mainFragment
        val currentFontSize: Int =
            this.mainFragmentManager.getEditConfiguration().getEditFontSize().getText().toString()
                .toInt()
        this.mainFragmentManager.getEditConfiguration().getEditFontSize()
            .setText((currentFontSize - 1).toString())
        mainFragmentManager.checkButtonStart()
    }

    fun pSendEmail(view: View?) {
        val mainFragment: MainFragment =
            getSupportFragmentManager().getFragments().get(0) as MainFragment
        val emailFragment: EmailFragment =
            getSupportFragmentManager().getFragments().get(1) as EmailFragment
        val outputFileName: String = mainFragmentManager.getOutputFileName()
        val mailSender: MailSender =
            MailSender.builder().mainFragment(mainFragment).emailFragment(emailFragment)
                .outputFileName(outputFileName).build()
        mailSender.sendEmail()
    }

    protected val mainFragment: MainFragmentManager?
        protected get() {
            if (Objects.isNull(mainFragmentManager)) {
                mainFragmentManager = MainFragmentManager(
                    getSupportFragmentManager().getFragments()
                        .get(chartizatePager.getCurrentItem()) as MainFragment
                )
            }
            return mainFragmentManager
        }

    fun getMainFragmentManager(): MainFragmentManager? {
        return mainFragmentManager
    }
}