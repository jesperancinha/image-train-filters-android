package com.steelzack.chartizateapp.font.manager;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by joao on 10-2-16.
 */
public class FontManagerAdapter extends ArrayAdapter<String>{
    public FontManagerAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
    }
}
