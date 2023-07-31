package org.jesperancinha.itf.app.config

import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import org.jesperancinha.itf.app.common.ChartizateSurfaceView


class ControlConfiguration (
    val svSelectedColor: ChartizateSurfaceView,
    val btnStart: Button,
    val btnStartEmail: Button,
    val textStatus: TextView,
    val spiFontType: Spinner,
    val spiDistribution: Spinner,
    val spiLanguageCode: Spinner,
)