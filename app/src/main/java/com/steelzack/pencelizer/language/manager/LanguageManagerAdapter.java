package com.steelzack.pencelizer.language.manager;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by joao on 7-2-16.
 */
public class LanguageManagerAdapter extends ArrayAdapter<Character.UnicodeBlock> {
    private final Context contex;
    private final List<Character.UnicodeBlock> listOfCodeLanguages;
    private final int id;

    public LanguageManagerAdapter(Context context, int resource, List<Character.UnicodeBlock> listOfCodeLanguages) {
        super(context, resource, listOfCodeLanguages);

        this.contex = context;
        this.id = resource;
        this.listOfCodeLanguages = listOfCodeLanguages;
    }
}
