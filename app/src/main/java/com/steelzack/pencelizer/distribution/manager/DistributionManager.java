package com.steelzack.pencelizer.distribution.manager;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by joaofilipesabinoesperancinha on 08-02-16.
 */
public class DistributionManager extends ArrayAdapter<String> {
    public DistributionManager(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
    }
}
