package com.steelzack.chartizateapp.language.manager;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by joao on 7-2-16.
 */
public class LanguageManagerAdapter extends ArrayAdapter<String> {
    public LanguageManagerAdapter(Context context, int resource, List<String> listOfCodeLanguages) {
        super(context, resource, listOfCodeLanguages);
    }
}
