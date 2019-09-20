package org.jesperancinha.itf.android.font.manager;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

public class FontManagerAdapter extends ArrayAdapter<String>{
    public FontManagerAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
    }
}
