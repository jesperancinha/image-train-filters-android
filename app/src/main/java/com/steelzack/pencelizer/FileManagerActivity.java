package com.steelzack.pencelizer;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.steelzack.pencelizer.fileManager.FileManagerAdapter;
import com.steelzack.pencelizer.fileManager.FileManagerItem;
import com.steelzack.pencelizer.fileManager.FileType;

import java.io.File;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.activation.MimetypesFileTypeMap;

import static com.steelzack.pencelizer.fileManager.FileType.File;
import static com.steelzack.pencelizer.fileManager.FileType.Folder;
import static java.util.Collections.*;

/**
 * Created by joao on 6-2-16.
 */
public class FileManagerActivity extends ListActivity{
    private File currentDirectory;
    private FileManagerAdapter fileManagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentDirectory = new File("/sdcard");
        if (!currentDirectory.exists()) {
            currentDirectory = new File("/");
        }
        createListOfFiles(currentDirectory);
    }

    private void createListOfFiles(File directory){
        final File[] innerFiles = directory.listFiles();
        final List<FileManagerItem> files = new ArrayList<>();
        final List<FileManagerItem> directories = new ArrayList<>();

        if(innerFiles != null) {
            for (File file : innerFiles) {
                final FileType type = file.isDirectory() ? Folder : File;
                final String mimetype = new MimetypesFileTypeMap().getContentType(file);
                final String fileFormattedDate = getDateString(file);
                switch (type) {
                    case Folder:
                        directories.add(new FileManagerItem(file.getName(), fileFormattedDate, Folder, file));
                        break;
                    case File:
                        if (mimetype.contains(getString(R.string.image_mime_type))) {
                            files.add(new FileManagerItem(file.getName(), fileFormattedDate, File, file));
                        }
                        break;
                }
            }
        }

        sort(directories);
        sort(files);
        List<FileManagerItem> completeFolderList =  new ArrayList<>();
        if(directory.getParentFile() != null) {
            completeFolderList.add(new FileManagerItem("..", getDateString(directory), Folder, directory.getParentFile()));
        }
        files.addAll(directories);
        completeFolderList.addAll(files);

        fileManagerAdapter = new FileManagerAdapter(FileManagerActivity.this, R.layout.file_navigator, completeFolderList);
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

        if(fileItem.getFileType() == Folder)
        {
            createListOfFiles(fileItem.getFile());

        }   else if(fileItem.getFileType() == File)
        {
            pBackToMain(fileItem);
        }
    }

    public void pBackToMain(FileManagerItem fileManagerItem) {
        final Intent intent = new Intent(FileManagerActivity.this, MainActivity.class);
        intent.putExtra("fileItem", fileManagerItem);
        startActivity(intent);
    }
}

