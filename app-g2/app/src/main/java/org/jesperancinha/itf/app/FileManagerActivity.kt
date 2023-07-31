package org.jesperancinha.itf.app

import android.app.Activity
import android.app.ListActivity
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.ListView
import org.jesperancinha.itf.android.R.layout.file_navigator
import org.jesperancinha.itf.app.manager.FileManagerAdapter
import org.jesperancinha.itf.app.manager.FileManagerItem
import org.jesperancinha.itf.app.manager.FileType
import java.io.File
import java.text.DateFormat
import java.util.Collections
import java.util.Date
import java.util.Objects

class FileManagerActivity : ListActivity() {
    private var fileManagerAdapter: FileManagerAdapter? = null
    private var directoryManager = false
    protected override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var currentDirectory = Environment.getExternalStorageDirectory()
        if (!Objects.requireNonNull(currentDirectory).exists()) {
            currentDirectory = File("/")
        }
        directoryManager =
            Objects.requireNonNull<Bundle>(getIntent().getExtras()).getBoolean("directoryManager")
        createListOfFiles(currentDirectory)
    }

    protected override fun onListItemClick(l: ListView, v: View, position: Int, id: Long) {
        super.onListItemClick(l, v, position, id)
        val fileItem: FileManagerItem = fileManagerAdapter.getItem(position)
        if (Objects.requireNonNull<FileManagerItem>(fileItem).fileType === FileType.Folder) {
            createListOfFiles(fileItem.file())
        } else if (fileItem.fileType === FileType.File) {
            pBackToMain(fileItem)
        }
    }

    private fun createListOfFiles(directory: File) {
        val innerFiles = directory.listFiles()
        val files: MutableList<FileManagerItem> = ArrayList<FileManagerItem>()
        val directories: MutableList<FileManagerItem> = ArrayList<FileManagerItem>()
        if (innerFiles != null) {
            for (file in innerFiles) {
                val type: FileType = if (file.isDirectory) FileType.Folder else FileType.File
                val extension: String = MimeTypeMap.getFileExtensionFromUrl(file.absolutePath)
                val mimetype: String =
                    MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
                val fileFormattedDate = getDateString(file)
                when (type) {
                    FileType.Folder -> directories.add(
                        FileManagerItem(
                            file.name,
                            fileFormattedDate,
                            FileType.Folder,
                            file
                        )
                    )

                    FileType.File -> if (Objects.nonNull(mimetype) && Objects.requireNonNull<String>(mimetype)
                            .contains(getString(org.jesperancinha.itf.app.R.string.image_mime_type)) && !directoryManager
                    ) {
                        files.add(
                            FileManagerItem(
                                file.name,
                                fileFormattedDate,
                                FileType.File,
                                file
                            )
                        )
                    }
                }
            }
        }
        Collections.sort(directories)
        Collections.sort(files)
        val completeFolderList: MutableList<FileManagerItem> = ArrayList<FileManagerItem>()
        completeFolderList.add(
            FileManagerItem(
                "..(" + directory.absolutePath + ")",
                getDateString(directory),
                FileType.Folder,
                if (directory.parentFile == null) directory else directory.parentFile
            )
        )
        files.addAll(directories)
        completeFolderList.addAll(files)
        fileManagerAdapter = FileManagerAdapter(
            this@FileManagerActivity,
            file_navigator,
            completeFolderList,
            directoryManager
        )
        this.setListAdapter(fileManagerAdapter)
    }

    private fun getDateString(file: File): String {
        val lastModDate = Date(file.lastModified())
        val formater = DateFormat.getDateTimeInstance()
        return formater.format(lastModDate)
    }

    private fun pBackToMain(fileManagerItem: FileManagerItem) {
        val intent = Intent(this@FileManagerActivity, MainActivity::class.java)
        intent.putExtra("fileItem", fileManagerItem)
        setResult(
            Activity.RESULT_OK,
            intent
        )
        finish()
    }
}