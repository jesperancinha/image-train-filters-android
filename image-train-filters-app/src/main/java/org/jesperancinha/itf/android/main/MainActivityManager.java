package org.jesperancinha.itf.android.main;

import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import org.jesperancinha.itf.android.common.ChartizateThumbs;
import org.jesperancinha.itf.android.file.manager.FileManagerItem;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Objects;

import static org.jesperancinha.itf.android.R.id.fileImageSourcePreview;
import static org.jesperancinha.itf.android.R.id.lblESelectedFile;
import static org.jesperancinha.itf.android.R.id.lblOutputFolder;

public abstract class MainActivityManager extends FragmentActivity {

    protected ViewPager chartizatePager;

    protected abstract MainFragmentManager getMainFragmentManager();

    protected void setSelectedOutputFolder(Intent data) {
        final FileManagerItem folderManagerItem = (FileManagerItem) Objects.requireNonNull(data.getExtras()).get("folderItem");
        if (folderManagerItem != null) {
            final TextView currentFile = getMainFragmentManager().getMainView().findViewById(lblOutputFolder);
            currentFile.setText(folderManagerItem.getFilename());
            getMainFragmentManager().getImageConfiguration().setCurrentSelectedFolder(folderManagerItem);
        }
    }

    protected void setSelectedInputFileAndThumbnail(FileManagerItem fileManagerItem) {
        if (fileManagerItem != null) {
            final TextView currentFile = getMainFragmentManager().getMainView().findViewById(lblESelectedFile);
            currentFile.setText(fileManagerItem.getFilename());
            getMainFragmentManager().getImageConfiguration().setCurrentSelectedFile(fileManagerItem);
            final ImageView btnImageFile = getMainFragmentManager().getMainView().findViewById(fileImageSourcePreview);

            try {
                ChartizateThumbs.setImageThumbnail(btnImageFile, new FileInputStream(fileManagerItem.getFile()));
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }


    protected boolean isDataPresent(Intent data) {
        return data != null && data.getExtras() != null;
    }


}
