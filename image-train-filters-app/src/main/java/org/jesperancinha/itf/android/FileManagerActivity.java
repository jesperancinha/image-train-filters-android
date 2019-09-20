package org.jesperancinha.itf.android;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import org.jesperancinha.itf.android.file.manager.FileManagerAdapter;
import org.jesperancinha.itf.android.file.manager.FileManagerItem;
import org.jesperancinha.itf.android.file.manager.FileType;

import java.io.File;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static java.util.Collections.sort;

public class FileManagerActivity extends ListActivity {
    private FileManagerAdapter fileManagerAdapter;
    private boolean directoryManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        File currentDirectory = getApplicationContext().getExternalFilesDir("/");
        if (!currentDirectory.exists()) {
            currentDirectory = new File("/");
        }
        directoryManager = Objects.requireNonNull(getIntent().getExtras()).getBoolean("directoryManager");
        createListOfFiles(currentDirectory);

    }

    private void createListOfFiles(File directory) {
        File[] innerFiles = directory.listFiles(file -> true);
        final List<FileManagerItem> files = new ArrayList<>();
        final List<FileManagerItem> directories = new ArrayList<>();

        if (directory.getName().equalsIgnoreCase("0")) {
            List<File> innerFiles1 = Collections.singletonList(new File(directory, "DCIM"));
            if (innerFiles != null) {
                innerFiles1.addAll(Arrays.asList(innerFiles));
            }
            innerFiles = innerFiles1.toArray(new File[0]);
        }
        if (innerFiles != null) {
            for (File file : innerFiles) {
                final FileType type = file.isDirectory() ? FileType.Folder : FileType.File;
                final String extension = MimeTypeMap.getFileExtensionFromUrl(file.getAbsolutePath());
                final String mimetype = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
                final String fileFormattedDate = getDateString(file);
                switch (type) {
                    case Folder:
                        directories.add(new FileManagerItem(file.getName(), fileFormattedDate, FileType.Folder, file));
                        break;
                    case File:
                        if (mimetype.contains(getString(org.jesperancinha.itf.android.R.string.image_mime_type)) && !directoryManager) {
                            files.add(new FileManagerItem(file.getName(), fileFormattedDate, FileType.File, file));
                        }
                        break;
                }
            }
        }

        sort(directories);
        sort(files);
        final List<FileManagerItem> completeFolderList = new ArrayList<>();

        completeFolderList.add(
                new FileManagerItem("..(" + directory.getAbsolutePath() + ")",
                        getDateString(directory),
                        FileType.Folder,
                        directory.getParentFile() == null ? directory : directory.getParentFile()));

        files.addAll(directories);
        completeFolderList.addAll(files);

        fileManagerAdapter = new FileManagerAdapter(FileManagerActivity.this, org.jesperancinha.itf.android.R.layout.file_navigator, completeFolderList, directoryManager);
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
        final Intent intent = new Intent(FileManagerActivity.this, org.jesperancinha.itf.android.MainActivity.class);
        intent.putExtra("fileItem", fileManagerItem);
        setResult(
                android.app.Activity.RESULT_OK,
                intent);
        finish();
    }
}

