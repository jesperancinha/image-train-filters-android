package com.steelzack.pencelizer.fileManager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by joao on 6-2-16.
 */
public class FileManagerAdapter extends ArrayAdapter<FileManagerItem>{


    private final Context context;
    private final int id;
    private final List<FileManagerItem> fileList;

    public FileManagerAdapter(Context context, int resource, List<FileManagerItem> objects) {
        super(context, resource, objects);

        this.context =context;
        this.id = resource;
        this.fileList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(id, null);
        }

        FileManagerItem fileItem = fileList.get(position);

        return super.getView(position, convertView, parent);
    }
}
