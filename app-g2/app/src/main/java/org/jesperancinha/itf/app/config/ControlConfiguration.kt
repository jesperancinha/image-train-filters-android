package org.jesperancinha.itf.app.config

import android.widget.Button
import android.widget.Spinner
import android.widget.TextView

class ControlConfiguration (
    val svSelectedColor: org.jesperancinha.itf.app.common.ChartizateSurfaceView,
    val btnStart: Button,
    val btnStartEmail: Button,
    val textStatus: TextView,
    val spiFontType: Spinner,
    val spiDistribution: Spinner,
    val spiLanguageCode: Spinner,
)