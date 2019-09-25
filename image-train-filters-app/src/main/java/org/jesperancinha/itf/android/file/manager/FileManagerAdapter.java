package org.jesperancinha.itf.android.file.manager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import org.jesperancinha.itf.android.MainActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

import static org.jesperancinha.itf.android.R.drawable.folder;
import static org.jesperancinha.itf.android.common.ChartizateThumbs.setImageThumbnail;

public class FileManagerAdapter extends ArrayAdapter<org.jesperancinha.itf.android.file.manager.FileManagerItem> {
    public static final int COMMON_ICON_PADDING = 3;
    private final Context context;
    private final int id;
    private final List<FileManagerItem> fileList;
    private final boolean directoryManager;

    public FileManagerAdapter(Context context, int resource, List<FileManagerItem> objects, boolean directoryManager) {
        super(context, resource, objects);

        this.context = context;
        this.id = resource;
        this.fileList = objects;
        this.directoryManager = directoryManager;
    }

    @NonNull
    @Override
    public View getView(int position, final View convertView, @NonNull ViewGroup parent) {
        final View v = getView(convertView);
        final FileManagerItem fileItem = fileList.get(position);
        final TextView fileName = v.findViewById(org.jesperancinha.itf.android.R.id.fileName);
        final TextView fileDate = v.findViewById(org.jesperancinha.itf.android.R.id.fileDate);
        final ImageView viewDirectory = v.findViewById(org.jesperancinha.itf.android.R.id.typeFolderFile);
        if (isNotBackFolder(fileItem)) {
            assignFileListenersAndSettings(fileItem, viewDirectory);
        }
        final ImageView imageView = v.findViewById(org.jesperancinha.itf.android.R.id.typeFolderFile);
        assignSystemItemIcons(fileItem, imageView);
        fileName.setText(fileItem.getFilename());
        fileDate.setText(fileItem.getDate());
        return v;
    }

    private void assignSystemItemIcons(FileManagerItem fileItem, ImageView imageView) {
        switch (fileItem.getFileType()) {
            case Folder:
                final Drawable image = ResourcesCompat.getDrawable(context.getResources(), folder, null);
                imageView.setImageDrawable(image);
                break;
            case File:
                InputStream inputStream;
                try {
                    inputStream = new FileInputStream(new File(fileItem.getFile().getAbsolutePath()));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
                setImageThumbnail(imageView, inputStream);
                break;
        }
    }

    private void assignFileListenersAndSettings(FileManagerItem fileItem, ImageView viewDirectory) {
        viewDirectory.setOnClickListener(v1 -> {
            final Intent intent = new Intent(v1.getContext(), MainActivity.class);
            intent.putExtra("folderItem", fileItem);
            final Activity activity = (Activity) FileManagerAdapter.this.context;
            activity.setResult(Activity.RESULT_OK, intent);
            activity.finish();
        });
        viewDirectory.setBackgroundColor(Color.BLACK);
        viewDirectory.setPadding(COMMON_ICON_PADDING, COMMON_ICON_PADDING, COMMON_ICON_PADDING, COMMON_ICON_PADDING);
    }

    private boolean isNotBackFolder(FileManagerItem fileItem) {
        return directoryManager && !fileItem.getFilename().equals("..");
    }

    private View getView(View convertView) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = Objects.requireNonNull(vi).inflate(id, null);
        }
        return v;
    }
}
