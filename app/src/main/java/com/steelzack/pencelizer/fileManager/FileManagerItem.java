package com.steelzack.pencelizer.fileManager;

/**
 * Created by joao on 6-2-16.
 */
public class FileManagerItem {

    final String filename;

    final String date;

    final FileType fileType;

    public FileManagerItem(String filename, String date, FileType fileType) {
        this.filename = filename;
        this.date = date;
        this.fileType = fileType;
    }
}
