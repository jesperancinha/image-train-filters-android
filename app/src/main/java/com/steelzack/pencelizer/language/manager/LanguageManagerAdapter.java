package com.steelzack.pencelizer.language.manager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.steelzack.pencelizer.R;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by joao on 7-2-16.
 */
public class LanguageManagerAdapter extends ArrayAdapter<String> {
    public LanguageManagerAdapter(Context context, int resource, List<String> listOfCodeLanguages) {
        super(context, resource, listOfCodeLanguages);
    }
}
