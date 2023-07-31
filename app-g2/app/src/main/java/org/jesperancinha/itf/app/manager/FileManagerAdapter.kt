package org.jesperancinha.itf.app.manager

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import org.jesperancinha.itf.android.R.drawable.folder
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.InputStream
import java.util.Objects

class FileManagerAdapter(
    private val context: Context,
    private val id: Int,
    private val fileList: List<FileManagerItem>,
    private val directoryManager: Boolean
) : ArrayAdapter<FileManagerItem?>(
    context, id, fileList
) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val v = getView(convertView)
        val fileItem = fileList[position]
        val fileName = v.findViewById<TextView>(R.id.fileName)
        val fileDate = v.findViewById<TextView>(R.id.fileDate)
        val viewDirectory = v.findViewById<ImageView>(typeFolderFile)
        if (isNotBackFolder(fileItem)) {
            assignFileListenersAndSettings(fileItem, viewDirectory)
        }
        val imageView = v.findViewById<ImageView>(typeFolderFile)
        assignSystemItemIcons(fileItem, imageView)
        fileName.setText(fileItem.getFilename())
        fileDate.setText(fileItem.getDate())
        return v
    }

    private fun assignSystemItemIcons(fileItem: FileManagerItem, imageView: ImageView) {
        when (fileItem.getFileType()) {
            Folder -> {
                val image: Drawable = ResourcesCompat.getDrawable(
                    context.resources, folder, null
                )
                imageView.setImageDrawable(image)
            }

            File -> {
                val inputStream: InputStream
                try {
                    inputStream = FileInputStream(File(fileItem.file.getAbsolutePath()))
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                    throw RuntimeException(e)
                }
                createChartizateThumbs(imageView).setImageThumbnail(inputStream)
            }
        }
    }

    private fun createChartizateThumbs(imageView: ImageView): ChartizateThumbs {
        return ChartizateThumbs.builder()
            .width(context.resources.getInteger(R.integer.thumb_width))
            .height(context.resources.getInteger(R.integer.thumb_height))
            .imageView(imageView)
            .build()
    }

    private fun assignFileListenersAndSettings(
        fileItem: FileManagerItem,
        viewDirectory: ImageView
    ) {
        viewDirectory.setOnClickListener { v1: View ->
            val intent = Intent(v1.context, MainActivity::class.java)
            intent.putExtra("folderItem", fileItem)
            val activity = context as Activity
            activity.setResult(Activity.RESULT_OK, intent)
            activity.finish()
        }
        viewDirectory.setBackgroundColor(Color.BLACK)
        viewDirectory.setPadding(
            COMMON_ICON_PADDING,
            COMMON_ICON_PADDING,
            COMMON_ICON_PADDING,
            COMMON_ICON_PADDING
        )
    }

    private fun isNotBackFolder(fileItem: FileManagerItem): Boolean {
        return directoryManager && !fileItem.getFilename().equals("..")
    }

    private fun getView(convertView: View?): View {
        var v = convertView
        if (v == null) {
            val vi: LayoutInflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            v = Objects.requireNonNull<LayoutInflater>(vi).inflate(id, null)
        }
        return v
    }

    companion object {
        const val COMMON_ICON_PADDING = 3
    }
}