package org.jesperancinha.itf.android.main;

import android.content.Intent;
import android.net.Uri;

import org.jesperancinha.itf.android.file.manager.FileManagerItem;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MainActivityManagerTest {

    private final MainActivityManager mainActivityManager = mock(MainActivityManager.class, Mockito.CALLS_REAL_METHODS);

    @Test
    public void getMainFragmentManager() {
        final MainFragmentManager mainFragmentManager = mainActivityManager.getMainFragmentManager();
        assertThat(mainFragmentManager).isNull();

    }

    @Test
    public void setSelectedOutputFolder() {
        final Intent mockIntent = mock(Intent.class);

        assertThrows(NullPointerException.class, () ->
                mainActivityManager.setSelectedOutputFolder(mockIntent));
    }

    @Test
    public void setSelectedInputFileAndThumbnail() {
        final FileManagerItem fileManagerItem = mock(FileManagerItem.class);

        assertThrows(NullPointerException.class, () ->
                mainActivityManager.setSelectedInputFileAndThumbnail(fileManagerItem));
    }

    @Test
    public void isDataPresentFalseNone() {
        final Intent mockIntent = mock(Intent.class);

        final boolean dataPresent = mainActivityManager.isDataPresent(mockIntent);

        assertThat(dataPresent).isFalse();
    }

    @Test
    public void isDataPresentFalseNull() {
        final boolean dataPresent = mainActivityManager.isDataPresent(null);

        assertThat(dataPresent).isFalse();
    }

    @Test
    public void isDataPresent() {
        final Intent mockIntent = mock(Intent.class);
        when(mockIntent.getData()).thenReturn(mock(Uri.class));

        final boolean dataPresent = mainActivityManager.isDataPresent(mockIntent);

        assertThat(dataPresent).isFalse();
    }


}