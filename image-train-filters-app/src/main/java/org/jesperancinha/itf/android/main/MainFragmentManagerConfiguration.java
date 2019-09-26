package org.jesperancinha.itf.android.main;

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
}
