package org.jesperancinha.itf.android.distribution.manager;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

public class DistributionManager extends ArrayAdapter<String> {
    public DistributionManager(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
    }
}
