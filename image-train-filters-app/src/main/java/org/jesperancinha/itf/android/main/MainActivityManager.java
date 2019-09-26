package org.jesperancinha.itf.android.main;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import org.jesperancinha.chartizate.ChartizateManager;
import org.jesperancinha.chartizate.ChartizateManagerBuilderImpl;
import org.jesperancinha.itf.android.EmailFragment;
import org.jesperancinha.itf.android.FileManagerActivity;
import org.jesperancinha.itf.android.R;
import org.jesperancinha.itf.android.SwipeAdapter;
import org.jesperancinha.itf.android.ViewFragment;
import org.jesperancinha.itf.android.common.ChartizateThumbs;
import org.jesperancinha.itf.android.config.ControlConfiguration;
import org.jesperancinha.itf.android.config.ImageConfiguration;
import org.jesperancinha.itf.android.file.manager.FileManagerItem;
import org.jesperancinha.itf.android.mail.MailSender;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Objects;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static java.lang.Integer.parseInt;
import static org.jesperancinha.chartizate.distributions.ChartizateDistributionType.Linear;
import static org.jesperancinha.itf.android.ITFConstants.FILE_FIND;
import static org.jesperancinha.itf.android.ITFConstants.FOLDER_FIND;
import static org.jesperancinha.itf.android.R.id.fileImageSourcePreview;
import static org.jesperancinha.itf.android.R.id.lblESelectedFile;
import static org.jesperancinha.itf.android.R.id.lblOutputFolder;
import static org.jesperancinha.itf.android.R.string.chartizating;

public abstract class MainActivityManager extends FragmentActivity {

    protected ViewPager chartizatePager;

    protected MainFragmentManager mainFragmentManager;

    protected void setUpMainActivity() {
        if (checkSelfPermission(
                WRITE_EXTERNAL_STORAGE) != PERMISSION_GRANTED) {
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, 1);
        }
        setContentView(R.layout.activity_main);
        chartizatePager = findViewById(R.id.itf_pager);
        final SwipeAdapter swipeAdapter = new SwipeAdapter(getSupportFragmentManager());
        chartizatePager.setAdapter(swipeAdapter);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.setLogo(R.mipmap.ic_launcher);
    }

    protected void manageOnActivityResult(Intent data) {
        final MainFragment mainFragment = getMainFragment();
        if (isDataPresent(data)) {
            final FileManagerItem fileManagerItem = (FileManagerItem) Objects.requireNonNull(data.getExtras()).get("fileItem");
            setSelectedInputFileAndThumbnail(mainFragment, fileManagerItem);
            setSelectedOutputFolder(data, mainFragment);
        }
        mainFragment.checkButtonStart();
    }

    private MainFragment getMainFragment() {
        if (Objects.isNull(this.mainFragmentManager)) {
            this.mainFragmentManager = MainFragmentManager
                    .builder().mainFragment((MainFragment) getSupportFragmentManager().getFragments().get(chartizatePager.getCurrentItem()))
                    .build();
        }
        return mainFragmentManager.getMainFragment();
    }

    private void setSelectedOutputFolder(Intent data, MainFragment mainFragment) {
        final FileManagerItem folderManagerItem = (FileManagerItem) Objects.requireNonNull(data.getExtras()).get("folderItem");
        if (folderManagerItem != null) {
            final TextView currentFile = mainFragmentManager.getMainView().findViewById(lblOutputFolder);
            currentFile.setText(folderManagerItem.getFilename());
            getImageConfiguration(mainFragment).setCurrentSelectedFolder(folderManagerItem);
        }
    }

    private void setSelectedInputFileAndThumbnail(MainFragment mainFragment, FileManagerItem fileManagerItem) {
        if (fileManagerItem != null) {
            final TextView currentFile = mainFragmentManager.getMainView().findViewById(lblESelectedFile);
            currentFile.setText(fileManagerItem.getFilename());
            getImageConfiguration(mainFragment).setCurrentSelectedFile(fileManagerItem);
            final ImageView btnImageFile = mainFragmentManager.getMainView().findViewById(fileImageSourcePreview);

            try {
                ChartizateThumbs.setImageThumbnail(btnImageFile, new FileInputStream(fileManagerItem.getFile()));
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private ImageConfiguration getImageConfiguration(MainFragment mainFragment) {
        return mainFragment.getImageConfiguration();
    }

    private boolean isDataPresent(Intent data) {
        return data != null && data.getExtras() != null;
    }

    public void pFindFile(View view) {
        final MainFragment mainFragment = getMainFragment();
        final Intent intent = new Intent(mainFragment.getActivity(), FileManagerActivity.class);
        intent.putExtra("directoryManager", false);
        startActivityForResult(intent, FILE_FIND);
        mainFragment.checkButtonStart();
    }

    public void pGenerateFile(View view) {
        final MainFragment mainFragment = processMainFragment();
        final Runnable task = createGenerateImageTask(view, mainFragment);
        mainFragmentManager.getControlConfiguration().getTextStatus().post(task);
    }

    private Runnable createGenerateImageTask(View view, MainFragment mainFragment) {
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
                .backgroundColor(mainFragmentManager.getBackground())
                .densityPercentage(mainFragmentManager.getDensity())
                .rangePercentage(mainFragmentManager.getRangePercentage())
                .distributionType(Linear)
                .fontName(mainFragmentManager.getFontName())
                .fontSize(mainFragmentManager.getFontSize())
                .block(mainFragmentManager.getBlock())
                .imageFullStream(mainFragmentManager.getImageFullStream())
                .destinationImagePath(mainFragmentManager.getDestinationImagePath())
                .build();
    }

    private MainFragment processMainFragment() {
        final MainFragment mainFragment = getMainFragment();
        final ControlConfiguration controlConfiguration = mainFragmentManager.getControlConfiguration();
        controlConfiguration.getBtnStart().setEnabled(false);
        controlConfiguration.getBtnStartEmail().setEnabled(false);
        controlConfiguration.getTextStatus().setText(chartizating);
        return mainFragment;
    }

    private void postFileGeneration(MainFragment mainFragment, View view) {
        final ControlConfiguration controlConfiguration = mainFragmentManager.getControlConfiguration();
        controlConfiguration.getTextStatus().setText(R.string.done);
        controlConfiguration.getBtnStart().post(() -> controlConfiguration.getBtnStart().setEnabled(true));
        controlConfiguration.getBtnStartEmail().post(() -> controlConfiguration.getBtnStartEmail().setEnabled(true));
        chartizatePager.setCurrentItem(getDestination(view));
        final ViewFragment viewFragment = (ViewFragment) getSupportFragmentManager().getFragments().get(2);
        final ImageView imageView = viewFragment.getImageView();
        final ImageView imageViewEmail = findViewById(R.id.imageViewGeneratedAttachment);
        final Uri uri = Uri.fromFile(new File(mainFragment.getImageConfiguration().getCurrentSelectedFolder().getFile(), mainFragmentManager.getOutputFileName()));
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
        if (view == mainFragmentManager.getControlConfiguration().getBtnStart()) {
            return 2;
        } else if (view == mainFragmentManager.getControlConfiguration().getBtnStartEmail()) {
            return 1;
        }
        return 0;
    }

    public void pGetBackGroundColor(View view) {
        final MainFragment mainFragment = getMainFragment();
        ColorPickerDialogBuilder
                .with(view.getContext())
                .setTitle("Choose color")
                .initialColor(Color.WHITE)
                .wheelType(ColorPickerView.WHEEL_TYPE.CIRCLE)
                .density(5)
                .setPositiveButton("ok", (dialog, selectedColor, allColors) -> {
                    mainFragmentManager.getControlConfiguration().getSvSelectedColor().setBackgroundColor(selectedColor);
                    mainFragmentManager.getControlConfiguration().getSvSelectedColor().setColor(selectedColor);
                })
                .setNegativeButton("cancel", (dialog, which) -> {
                })
                .build().show();
        mainFragment.checkButtonStart();
    }

    public void pFindOutputFolder(View view) {
        final MainFragment mainFragment = getMainFragment();
        final Intent intent = new Intent(mainFragment.getActivity(), FileManagerActivity.class);
        intent.putExtra("directoryManager", true);
        startActivityForResult(intent, FOLDER_FIND);
        mainFragment.checkButtonStart();
    }

    public void pAddOne(View view) {
        final MainFragment mainFragment = getMainFragment();
        final int currentFontSize = parseInt(mainFragmentManager.getEditConfiguration().getEditFontSize().getText().toString());
        mainFragmentManager.getEditConfiguration().getEditFontSize().setText(String.valueOf(currentFontSize + 1));
        mainFragment.checkButtonStart();
    }

    public void pMinusOne(View view) {
        final MainFragment mainFragment = getMainFragment();
        final int currentFontSize = parseInt(mainFragmentManager.getEditConfiguration().getEditFontSize().getText().toString());
        mainFragmentManager.getEditConfiguration().getEditFontSize().setText(String.valueOf(currentFontSize - 1));
        mainFragment.checkButtonStart();
    }

    public void pSendEmail(View view) {
        final MainFragment mainFragment = (MainFragment) getSupportFragmentManager().getFragments().get(0);
        final EmailFragment emailFragment = (EmailFragment) getSupportFragmentManager().getFragments().get(1);
        final String outputFileName = mainFragmentManager.getOutputFileName();
        final MailSender mailSender = MailSender.builder().mainFragment(mainFragment).emailFragment(emailFragment)
                .outputFileName(outputFileName).build();
        mailSender.sendEmail();
    }
}
