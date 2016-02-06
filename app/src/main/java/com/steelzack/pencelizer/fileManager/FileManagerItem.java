package com.steelzack.pencelizer.fileManager;

import java.io.File;

/**
 * Created by joao on 6-2-16.
 */
public class FileManagerItem implements Comparable<FileManagerItem> {

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
}
