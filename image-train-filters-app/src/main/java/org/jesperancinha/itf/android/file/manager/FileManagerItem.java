package org.jesperancinha.itf.android.file.manager;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.File;
import java.io.Serializable;

@Getter
@Builder
@AllArgsConstructor
public class FileManagerItem implements Comparable<FileManagerItem>, Serializable {

    private final String filename;
    private final String date;
    private final FileType fileType;
    private final File file;

    @Override
    public int compareTo(FileManagerItem another) {
        return another.getFilename().compareTo(this.getFilename());
    }
}
