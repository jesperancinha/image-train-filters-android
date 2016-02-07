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
public class LanguageManagerAdapter extends ArrayAdapter<Character.UnicodeBlock> {
    private final Context context;
    private final List<Character.UnicodeBlock> listOfCodeLanguages;
    private final int id;

    public LanguageManagerAdapter(Context context, int resource, List<Character.UnicodeBlock> listOfCodeLanguages) {
        super(context, resource, listOfCodeLanguages);

        this.context = context;
        this.id = resource;
        this.listOfCodeLanguages = listOfCodeLanguages;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(id, null);
        }

        TextView lblUnicode = (TextView) v.findViewById(R.id.lblUnicode);
        lblUnicode.setText(listOfCodeLanguages.get(position).toString());
        return v;
    }
}
