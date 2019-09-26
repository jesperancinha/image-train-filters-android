package org.jesperancinha.itf.android.config;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.jesperancinha.itf.android.common.ChartizateSurfaceView;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ControlConfiguration {
    private final ChartizateSurfaceView svSelectedColor;

    private final EditText editFontSize;

    private final Button btnStart;

    private final Button btnStartEmail;

    private final TextView textStatus;

    private final Spinner spiFontType;

    private final EditText range;

    private final EditText density;

    private final EditText editOutputFileName;

    private final Spinner spiDistribution;

    private final Spinner spiLanguageCode;
}
