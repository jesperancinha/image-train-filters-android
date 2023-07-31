package org.jesperancinha.itf.app.common

import android.content.Context
import android.util.AttributeSet
import android.view.SurfaceView

data class ChartizateSurfaceView(val context: Context, val attrs: AttributeSet) :
    SurfaceView(context, attrs) {
    private val color = 0
}