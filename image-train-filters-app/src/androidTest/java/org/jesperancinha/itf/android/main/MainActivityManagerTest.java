package org.jesperancinha.itf.android.main;

import android.content.Intent;
import android.net.Uri;

import org.jesperancinha.itf.android.file.manager.FileManagerItem;
import org.junit.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MainActivityManagerTest {

    private final MainActivityManager mainActivityManager = mock(MainActivityManager.class, Mockito.CALLS_REAL_METHODS);

    @Test
    public void getMainFragmentManager() {
        final MainFragmentManager mainFragmentManager = mainActivityManager.getMainFragmentManager();
        assertThat(mainFragmentManager).isNull();

    }

    @Test(expected = NullPointerException.class)
    public void setSelectedOutputFolder() {
        final Intent mockIntent = mock(Intent.class);
        mainActivityManager.setSelectedOutputFolder(mockIntent);

    }

    @Test(expected = NullPointerException.class)
    public void setSelectedInputFileAndThumbnail() {
        final FileManagerItem fileManagerItem = mock(FileManagerItem.class);
        mainActivityManager.setSelectedInputFileAndThumbnail(fileManagerItem);
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