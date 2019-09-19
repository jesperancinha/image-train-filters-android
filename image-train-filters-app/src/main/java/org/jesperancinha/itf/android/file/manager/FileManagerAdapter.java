package org.jesperancinha.itf.android.file.manager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.jesperancinha.itf.android.MainActivity;
import org.jesperancinha.itf.android.common.ChartizateThumbs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by joao on 6-2-16.
 */
public class FileManagerAdapter extends ArrayAdapter<org.jesperancinha.itf.android.file.manager.FileManagerItem> {


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

    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(id, null);
        }

        final FileManagerItem fileItem = fileList.get(position);
        final TextView fileName = (TextView) v.findViewById(org.jesperancinha.itf.android.R.id.fileName);
        final TextView fileDate = (TextView) v.findViewById(org.jesperancinha.itf.android.R.id.fileDate);
        final ImageView viewDirectory = (ImageView) v.findViewById(org.jesperancinha.itf.android.R.id.typeFolderFile);

        if (directoryManager && !fileItem.getFilename().equals("..")) {
            viewDirectory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Intent intent = new Intent(v.getContext(), MainActivity.class);
                    intent.putExtra("folderItem", fileItem);
                    final Activity activity = (Activity) FileManagerAdapter.this.context;
                    activity.setResult(Activity.RESULT_OK, intent);
                    activity.finish();
                }
            });
            viewDirectory.setBackgroundColor(Color.BLACK);
            viewDirectory.setPadding(3,3,3,3);
        }

        final ImageView imageView = (ImageView) v.findViewById(org.jesperancinha.itf.android.R.id.typeFolderFile);
        switch (fileItem.getFileType()) {
            case Folder:
                final Drawable image = ResourcesCompat.getDrawable(context.getResources(), org.jesperancinha.itf.android.R.drawable.folder, null);
                imageView.setImageDrawable(image);
                break;
            case File:
                InputStream inputStream = null;
                try {
                    inputStream = new FileInputStream(new File(fileItem.getFile().getAbsolutePath()));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }

                ChartizateThumbs.setImageThumbnail(imageView, inputStream);

                break;
        }

        fileName.setText(fileItem.getFilename());
        fileDate.setText(fileItem.getDate());
        return v;
    }
}
