package org.jesperancinha.itf.android.config;

import android.widget.EditText;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


@Builder
@Getter
@AllArgsConstructor
public class EditConfiguration {

    private final EditText range;

    private final EditText density;

    private final EditText editFontSize;

    private final EditText editOutputFileName;
}
