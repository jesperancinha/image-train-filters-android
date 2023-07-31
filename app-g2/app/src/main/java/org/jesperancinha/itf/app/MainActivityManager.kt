package org.jesperancinha.itf.app

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.viewpager.widget.ViewPager
import org.jesperancinha.itf.app.common.ChartizateThumbs
import org.jesperancinha.itf.app.manager.FileManagerItem
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.util.Objects

abstract class MainActivityManager : FragmentActivity() {
    protected var chartizatePager: ViewPager? = null
    protected abstract val mainFragmentManager: MainFragmentManager
    protected fun setSelectedOutputFolder(data: Intent) {
        val folderManagerItem: FileManagerItem? =
            Objects.requireNonNull<Bundle>(data.getExtras())["folderItem"] as FileManagerItem?
        if (folderManagerItem != null) {
            val currentFile: TextView =
                mainFragmentManager.getMainView().findViewById(R.id.lblOutputFolder)
            currentFile.setText(folderManagerItem.filename)
            mainFragmentManager.getImageConfiguration().setCurrentSelectedFolder(folderManagerItem)
        }
    }

    protected fun setSelectedInputFileAndThumbnail(fileManagerItem: FileManagerItem?) {
        if (fileManagerItem != null) {
            val currentFile: TextView =
                mainFragmentManager.getMainView().findViewById(R.id.lblESelectedFile)
            currentFile.setText(fileManagerItem.filename)
            mainFragmentManager.getImageConfiguration().setCurrentSelectedFile(fileManagerItem)
            val btnImageFile: ImageView =
                mainFragmentManager.getMainView().findViewById(R.id.fileImageSourcePreview)
            try {
                val chartizateThumbs: ChartizateThumbs = ChartizateThumbs.builder()
                    .width(resources.getInteger(R.integer.thumb_width))
                    .height(resources.getInteger(R.integer.thumb_height))
                    .imageView(btnImageFile)
                    .build()
                chartizateThumbs.setImageThumbnail(FileInputStream(fileManagerItem.file))
            } catch (e: FileNotFoundException) {
                throw RuntimeException(e)
            }
        }
    }

    protected fun isDataPresent(data: Intent?): Boolean {
        return data?.extras != null
    }
}