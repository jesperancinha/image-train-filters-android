package org.jesperancinha.itf.app.common

import android.content.Context
import android.util.AttributeSet
import android.view.SurfaceView

data class ChartizateSurfaceView(val contextIn: Context, val attrsIn: AttributeSet) :
    SurfaceView(contextIn, attrsIn) {
    private val color = 0
}