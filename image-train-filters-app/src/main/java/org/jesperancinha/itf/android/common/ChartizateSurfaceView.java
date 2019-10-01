package org.jesperancinha.itf.android.common;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceView;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChartizateSurfaceView extends SurfaceView {
    private int color;

    public ChartizateSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}
