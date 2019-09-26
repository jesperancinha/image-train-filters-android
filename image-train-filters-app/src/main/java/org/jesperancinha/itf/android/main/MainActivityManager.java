package org.jesperancinha.itf.android.main;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import org.jesperancinha.chartizate.ChartizateManager;
import org.jesperancinha.chartizate.ChartizateManagerBuilderImpl;
import org.jesperancinha.itf.android.R;
import org.jesperancinha.itf.android.ViewFragment;
import org.jesperancinha.itf.android.common.ChartizateThumbs;
import org.jesperancinha.itf.android.config.ControlConfiguration;
import org.jesperancinha.itf.android.file.manager.FileManagerItem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Objects;

import static org.jesperancinha.chartizate.distributions.ChartizateDistributionType.Linear;
import static org.jesperancinha.itf.android.R.id.fileImageSourcePreview;
import static org.jesperancinha.itf.android.R.id.lblESelectedFile;
import static org.jesperancinha.itf.android.R.id.lblOutputFolder;

public abstract class MainActivityManager extends FragmentActivity {

    protected ViewPager chartizatePager;

    protected abstract MainFragment getMainFragment();

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

    protected Runnable createGenerateImageTask(View view, MainFragment mainFragment) {
        return () -> {
            try {
                final ChartizateManager<Integer, Typeface, Bitmap> manager = setUpManager();
                manager.saveImage(manager.generateConvertedImage());
            } catch (IllegalArgumentException e) {
                alertFailedUnicodeParsing(mainFragment);
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                postFileGeneration(mainFragment, view);
            }
        };
    }

    private ChartizateManager<Integer, Typeface, Bitmap> setUpManager() throws java.io.IOException {
        return new ChartizateManagerBuilderImpl()
                .backgroundColor(getMainFragmentManager().getBackground())
                .densityPercentage(getMainFragmentManager().getDensity())
                .rangePercentage(getMainFragmentManager().getRangePercentage())
                .distributionType(Linear)
                .fontName(getMainFragmentManager().getFontName())
                .fontSize(getMainFragmentManager().getFontSize())
                .block(getMainFragmentManager().getBlock())
                .imageFullStream(getMainFragmentManager().getImageFullStream())
                .destinationImagePath(getMainFragmentManager().getDestinationImagePath())
                .build();
    }

    private void postFileGeneration(MainFragment mainFragment, View view) {
        final ControlConfiguration controlConfiguration = getMainFragmentManager().getControlConfiguration();
        controlConfiguration.getTextStatus().setText(R.string.done);
        controlConfiguration.getBtnStart().post(() -> controlConfiguration.getBtnStart().setEnabled(true));
        controlConfiguration.getBtnStartEmail().post(() -> controlConfiguration.getBtnStartEmail().setEnabled(true));
        chartizatePager.setCurrentItem(getDestination(view));
        final ViewFragment viewFragment = (ViewFragment) getSupportFragmentManager().getFragments().get(2);
        final ImageView imageView = viewFragment.getImageView();
        final ImageView imageViewEmail = findViewById(R.id.imageViewGeneratedAttachment);
        final Uri uri = Uri.fromFile(new File(mainFragment.getImageConfiguration().getCurrentSelectedFolder().getFile(), getMainFragmentManager().getOutputFileName()));
        imageView.post(() -> ChartizateThumbs.setImage(imageView, uri));
        imageViewEmail.post(() -> ChartizateThumbs.setImage(imageViewEmail, uri));
    }

    private void alertFailedUnicodeParsing(MainFragment mainFragment) {
        new AlertDialog.Builder(mainFragment.getActivity())
                .setTitle("Error with your code selection")
                .setMessage("Unfortunatelly this Unicode is not supported. If you want a working example, try: LATIN_EXTENDED_A")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private int getDestination(View view) {
        if (view == getMainFragmentManager().getControlConfiguration().getBtnStart()) {
            return 2;
        } else if (view == getMainFragmentManager().getControlConfiguration().getBtnStartEmail()) {
            return 1;
        }
        return 0;
    }
}
