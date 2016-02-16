package com.steelzack.chartizateapp.file.manager;

import java.io.File;
import java.io.Serializable;

/**
 * Created by joao on 6-2-16.
 */
public class FileManagerItem implements Comparable<FileManagerItem>, Serializable  {

    private final String filename;

    final String date;

    final FileType fileType;

    final File file;

    public FileManagerItem(String filename, String date, FileType fileType, File file) {
        this.filename = filename;
        this.date = date;
        this.fileType = fileType;
        this.file = file;
    }

    @Override
    public int compareTo(FileManagerItem another) {
        return another.getFilename().compareTo(this.getFilename());
    }

    public String getFilename() {
        return filename;
    }

    public FileType getFileType() {
        return fileType;
    }

    public String getDate() {
        return date;
    }

    public File getFile() {
        return file;
    }
}
