package org.jesperancinha.itf.android.main;

import android.app.AlertDialog;
import android.content.Intent;
import android.view.View;

import org.jesperancinha.itf.android.FileManagerActivity;
import org.jesperancinha.itf.android.config.ControlConfiguration;
import org.jesperancinha.itf.android.config.EditConfiguration;
import org.jesperancinha.itf.android.config.ImageConfiguration;

import lombok.Getter;

@Getter
public abstract class MainFragmentManagerConfiguration {

    protected abstract MainFragment getMainFragment();

    protected EditConfiguration getEditConfiguration() {
        return getMainFragment().getEditConfiguration();
    }

    protected ControlConfiguration getControlConfiguration() {
        return getMainFragment().getControlConfiguration();
    }

    protected ImageConfiguration getImageConfiguration() {
        return getMainFragment().getImageConfiguration();
    }

    public void checkButtonStart() {
        getMainFragment().checkButtonStart();
    }

    public Intent getFindFileIntent() {
        return new Intent(getMainFragment().getActivity(), FileManagerActivity.class);
    }

    protected void alertFailedUnicodeParsing() {
        new AlertDialog.Builder(getMainFragment().getActivity())
                .setTitle("Error with your code selection")
                .setMessage("Unfortunatelly this Unicode is not supported. If you want a working example, try: LATIN_EXTENDED_A")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    protected int getDestination(View view) {
        if (view == getControlConfiguration().getBtnStart()) {
            return 2;
        } else if (view == getControlConfiguration().getBtnStartEmail()) {
            return 1;
        }
        return 0;
    }
}
