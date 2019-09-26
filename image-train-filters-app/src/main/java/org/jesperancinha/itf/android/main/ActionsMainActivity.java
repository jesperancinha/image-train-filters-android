package org.jesperancinha.itf.android.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import org.jesperancinha.chartizate.ChartizateManager;
import org.jesperancinha.itf.android.EmailFragment;
import org.jesperancinha.itf.android.R;
import org.jesperancinha.itf.android.SwipeAdapter;
import org.jesperancinha.itf.android.ViewFragment;
import org.jesperancinha.itf.android.common.ChartizateThumbs;
import org.jesperancinha.itf.android.config.ControlConfiguration;
import org.jesperancinha.itf.android.file.manager.FileManagerItem;
import org.jesperancinha.itf.android.mail.MailSender;

import java.io.File;
import java.util.Objects;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static java.lang.Integer.parseInt;
import static org.jesperancinha.itf.android.ITFConstants.FILE_FIND;
import static org.jesperancinha.itf.android.ITFConstants.FOLDER_FIND;
import static org.jesperancinha.itf.android.R.string.chartizating;

public abstract class ActionsMainActivity extends MainActivityManager {

    protected MainFragmentManager mainFragmentManager;

    protected void setupActivity() {
        if (checkSelfPermission(
                WRITE_EXTERNAL_STORAGE) != PERMISSION_GRANTED) {
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, 1);
        }
        setContentView(R.layout.activity_main);
        chartizatePager = findViewById(R.id.itf_pager);
        final SwipeAdapter swipeAdapter = new SwipeAdapter(getSupportFragmentManager(),
                new Fragment[]{new MainFragment(), new EmailFragment(), new ViewFragment()});
        chartizatePager.setAdapter(swipeAdapter);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.setLogo(R.mipmap.ic_launcher);
    }

    protected void setupActivity(Intent data) {
        final MainFragmentManager mainFragmentManager = getMainFragment();
        if (isDataPresent(data)) {
            final FileManagerItem fileManagerItem = (FileManagerItem) Objects.requireNonNull(data.getExtras()).get("fileItem");
            setSelectedInputFileAndThumbnail(fileManagerItem);
            setSelectedOutputFolder(data);
        }
        mainFragmentManager.checkButtonStart();
    }

    public void pFindFile(View view) {
        final MainFragmentManager mainFragment = getMainFragment();
        final Intent intent = mainFragment.getFindFileIntent();
        intent.putExtra("directoryManager", false);
        startActivityForResult(intent, FILE_FIND);
        mainFragment.checkButtonStart();
    }

    public void pGenerateFile(View view) {
        final ControlConfiguration controlConfiguration = mainFragmentManager.getControlConfiguration();
        controlConfiguration.getBtnStart().setEnabled(false);
        controlConfiguration.getBtnStartEmail().setEnabled(false);
        controlConfiguration.getTextStatus().setText(chartizating);
        final Runnable task = createGenerateImageTask(view);
        mainFragmentManager.getControlConfiguration().getTextStatus().post(task);
    }

    protected Runnable createGenerateImageTask(View view) {
        return () -> {
            try {
                final ChartizateManager<Integer, Typeface, Bitmap> manager = mainFragmentManager.setUpManager();
                manager.saveImage(manager.generateConvertedImage());
            } catch (IllegalArgumentException e) {
                mainFragmentManager.alertFailedUnicodeParsing();
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                postFileGeneration(view);
            }
        };
    }

    protected void postFileGeneration(View view) {
        final ControlConfiguration controlConfiguration = mainFragmentManager.getControlConfiguration();
        controlConfiguration.getTextStatus().setText(R.string.done);
        controlConfiguration.getBtnStart().post(() -> controlConfiguration.getBtnStart().setEnabled(true));
        controlConfiguration.getBtnStartEmail().post(() -> controlConfiguration.getBtnStartEmail().setEnabled(true));
        chartizatePager.setCurrentItem(mainFragmentManager.getDestination(view));
        final ViewFragment viewFragment = (ViewFragment) getSupportFragmentManager().getFragments().get(2);
        final ImageView imageView = viewFragment.getImageView();
        final ImageView imageViewEmail = findViewById(R.id.imageViewGeneratedAttachment);
        final Uri uri = Uri.fromFile(new File(mainFragmentManager.getImageConfiguration().getCurrentSelectedFolder().getFile(), mainFragmentManager.getOutputFileName()));
        imageView.post(() -> ChartizateThumbs.setImage(imageView, uri));
        imageViewEmail.post(() -> ChartizateThumbs.setImage(imageViewEmail, uri));
    }

    public void pGetBackGroundColor(View view) {
        final MainFragmentManager mainFragmentManager = getMainFragment();
        ColorPickerDialogBuilder
                .with(view.getContext())
                .setTitle("Choose color")
                .initialColor(Color.WHITE)
                .wheelType(ColorPickerView.WHEEL_TYPE.CIRCLE)
                .density(5)
                .setPositiveButton("ok", (dialog, selectedColor, allColors) -> {
                    this.mainFragmentManager.getControlConfiguration().getSvSelectedColor().setBackgroundColor(selectedColor);
                    this.mainFragmentManager.getControlConfiguration().getSvSelectedColor().setColor(selectedColor);
                })
                .setNegativeButton("cancel", (dialog, which) -> {
                })
                .build().show();
        mainFragmentManager.checkButtonStart();
    }

    public void pFindOutputFolder(View view) {
        final MainFragmentManager mainFragmentManager = getMainFragment();
        final Intent intent = mainFragmentManager.getFindFileIntent();
        intent.putExtra("directoryManager", true);
        startActivityForResult(intent, FOLDER_FIND);
        mainFragmentManager.checkButtonStart();
    }

    public void pAddOne(View view) {
        final MainFragmentManager mainFragmentManager = getMainFragment();
        final int currentFontSize = parseInt(this.mainFragmentManager.getEditConfiguration().getEditFontSize().getText().toString());
        this.mainFragmentManager.getEditConfiguration().getEditFontSize().setText(String.valueOf(currentFontSize + 1));
        mainFragmentManager.checkButtonStart();
    }

    public void pMinusOne(View view) {
        final MainFragmentManager mainFragmentManager = getMainFragment();
        final int currentFontSize = parseInt(this.mainFragmentManager.getEditConfiguration().getEditFontSize().getText().toString());
        this.mainFragmentManager.getEditConfiguration().getEditFontSize().setText(String.valueOf(currentFontSize - 1));
        mainFragmentManager.checkButtonStart();
    }

    public void pSendEmail(View view) {
        final MainFragment mainFragment = (MainFragment) getSupportFragmentManager().getFragments().get(0);
        final EmailFragment emailFragment = (EmailFragment) getSupportFragmentManager().getFragments().get(1);
        final String outputFileName = mainFragmentManager.getOutputFileName();
        final MailSender mailSender = MailSender.builder().mainFragment(mainFragment).emailFragment(emailFragment)
                .outputFileName(outputFileName).build();
        mailSender.sendEmail();
    }

    protected MainFragmentManager getMainFragment() {
        if (Objects.isNull(this.mainFragmentManager)) {
            this.mainFragmentManager = new MainFragmentManager(
                    (MainFragment) getSupportFragmentManager().getFragments().get(chartizatePager.getCurrentItem()));
        }
        return mainFragmentManager;
    }

    @Override
    public MainFragmentManager getMainFragmentManager() {
        return mainFragmentManager;
    }
}
