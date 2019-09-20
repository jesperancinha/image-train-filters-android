package org.jesperancinha.itf.android.language.manager;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

public class LanguageManagerAdapter extends ArrayAdapter<String> {
    public LanguageManagerAdapter(Context context, int resource, List<String> listOfCodeLanguages) {
        super(context, resource, listOfCodeLanguages);
    }
}
