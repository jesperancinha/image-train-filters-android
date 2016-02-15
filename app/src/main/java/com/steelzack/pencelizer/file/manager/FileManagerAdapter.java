package com.steelzack.pencelizer.file.manager;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.steelzack.pencelizer.R;

import java.util.List;

/**
 * Created by joao on 6-2-16.
 */
public class FileManagerAdapter extends ArrayAdapter<FileManagerItem>{


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
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(id, null);
        }

        final FileManagerItem fileItem = fileList.get(position);
        final TextView fileName = (TextView)v.findViewById(R.id.fileName);
        final TextView fileDate = (TextView)v.findViewById(R.id.fileDate);
        final Button btnAcceptDirectory = (Button)v.findViewById(R.id.btnAcceptFolder);
        btnAcceptDirectory.setVisibility(directoryManager ? View.VISIBLE : View.INVISIBLE);
        
        switch (fileItem.getFileType()){
            case Folder:
                final ImageView imageView = (ImageView)v.findViewById(R.id.typeFolderFile);
                Drawable image = ResourcesCompat.getDrawable(context.getResources(), R.drawable.folder, null);
                imageView.setImageDrawable(image);break;
            case File:break;
        }

        fileName.setText(fileItem.getFilename());
        fileDate.setText(fileItem.getDate());
        return v;
    }
}
