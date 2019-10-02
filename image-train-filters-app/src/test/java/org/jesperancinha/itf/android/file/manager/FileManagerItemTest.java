package org.jesperancinha.itf.android.file.manager;


import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FileManagerItemTest {

    public static final String TEST_FILE_NAME_EXTENSION1 = "testFileName.extension";
    public static final String TEST_FILE_NAME_EXTENSION2 = "testFileName.extension2";

    @Test
    public void compareTo() {

        FileManagerItem fileManagerItem1 = FileManagerItem.builder().build();
        FileManagerItem fileManagerItem2 = FileManagerItem.builder().build();

        assertThrows(NullPointerException.class, () -> fileManagerItem1.compareTo(fileManagerItem2));
    }

    @Test
    public void compareToFirstNull() {

        FileManagerItem fileManagerItem1 = FileManagerItem.builder().build();
        FileManagerItem fileManagerItem2 = FileManagerItem.builder().filename(TEST_FILE_NAME_EXTENSION1).build();

        assertThrows(NullPointerException.class, () -> fileManagerItem1.compareTo(fileManagerItem2));
    }

    @Test
    public void compareToSecondNull() {

        FileManagerItem fileManagerItem1 = FileManagerItem.builder().filename(TEST_FILE_NAME_EXTENSION1).build();
        FileManagerItem fileManagerItem2 = FileManagerItem.builder().build();

        assertThrows(NullPointerException.class, () -> fileManagerItem1.compareTo(fileManagerItem2));
    }

    @Test
    public void compareToFirstFirst() {

        FileManagerItem fileManagerItem1 = FileManagerItem.builder().filename(TEST_FILE_NAME_EXTENSION1).build();
        FileManagerItem fileManagerItem2 = FileManagerItem.builder().filename(TEST_FILE_NAME_EXTENSION2).build();

        int result = fileManagerItem1.compareTo(fileManagerItem2);

        assertThat(result).isGreaterThan(0);
    }

    @Test
    public void compareToSecondFirst() {

        FileManagerItem fileManagerItem1 = FileManagerItem.builder().filename(TEST_FILE_NAME_EXTENSION2).build();
        FileManagerItem fileManagerItem2 = FileManagerItem.builder().filename(TEST_FILE_NAME_EXTENSION1).build();

        int result = fileManagerItem1.compareTo(fileManagerItem2);

        assertThat(result).isLessThan(0);
    }
}