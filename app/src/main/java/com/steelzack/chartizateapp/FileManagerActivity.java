package com.steelzack.chartizateapp;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ListView;

import com.steelzack.chartizateapp.file.manager.FileManagerAdapter;
import com.steelzack.chartizateapp.file.manager.FileManagerItem;
import com.steelzack.chartizateapp.file.manager.FileType;

import java.io.File;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.activation.MimetypesFileTypeMap;

import static java.util.Collections.sort;

/**
 * Created by joao on 6-2-16.
 */
public class FileManagerActivity extends ListActivity {
    private File currentDirectory;
    private FileManagerAdapter fileManagerAdapter;
    private boolean directoryManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentDirectory = new File("/sdcard");
        if (!currentDirectory.exists()) {
            currentDirectory = new File("/");
        }
        directoryManager = getIntent().getExtras().getBoolean("directoryManager");
        createListOfFiles(currentDirectory);

    }

    private void createListOfFiles(File directory) {
        final File[] innerFiles = directory.listFiles();
        final List<FileManagerItem> files = new ArrayList<>();
        final List<FileManagerItem> directories = new ArrayList<>();

        if (innerFiles != null) {
            for (File file : innerFiles) {
                final FileType type = file.isDirectory() ? FileType.Folder : FileType.File;
                final String mimetype = new MimetypesFileTypeMap().getContentType(file);
                final String fileFormattedDate = getDateString(file);
                switch (type) {
                    case Folder:
                        directories.add(new FileManagerItem(file.getName(), fileFormattedDate, FileType.Folder, file));
                        break;
                    case File:
                        if (mimetype.contains(getString(com.steelzack.chartizateapp.R.string.image_mime_type)) && !directoryManager) {
                            files.add(new FileManagerItem(file.getName(), fileFormattedDate, FileType.File, file));
                        }
                        break;
                }
            }
        }

        sort(directories);
        sort(files);
        List<FileManagerItem> completeFolderList = new ArrayList<>();
        if (directory.getParentFile() != null) {
            completeFolderList.add(new FileManagerItem("..", getDateString(directory), FileType.Folder, directory.getParentFile()));
        }
        files.addAll(directories);
        completeFolderList.addAll(files);

        fileManagerAdapter = new FileManagerAdapter(FileManagerActivity.this, com.steelzack.chartizateapp.R.layout.file_navigator, completeFolderList, directoryManager);
        this.setListAdapter(fileManagerAdapter);
    }

    @NonNull
    private String getDateString(File file) {
        final Date lastModDate = new Date(file.lastModified());
        final DateFormat formater = DateFormat.getDateTimeInstance();
        return formater.format(lastModDate);
    }


    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        final FileManagerItem fileItem = fileManagerAdapter.getItem(position);

        if (fileItem.getFileType() == FileType.Folder) {
            createListOfFiles(fileItem.getFile());

        } else if (fileItem.getFileType() == FileType.File) {
            pBackToMain(fileItem);
        }
    }

    public void pBackToMain(FileManagerItem fileManagerItem) {
        final Intent intent = new Intent(FileManagerActivity.this, MainActivity.class);
        intent.putExtra("fileItem", fileManagerItem);
        setResult(
                android.app.Activity.RESULT_OK,
                intent);
        finish();
    }
}

