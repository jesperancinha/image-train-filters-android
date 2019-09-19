package org.jesperancinha.itf.android.common;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.SurfaceView;

/**
 * Created by joaofilipesabinoesperancinha on 16-02-16.
 */
public class ChartizateSurfaceView extends SurfaceView{
    private int color;

    public ChartizateSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
