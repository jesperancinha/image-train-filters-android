package org.jesperancinha.itf.android.main;

import org.jesperancinha.itf.android.config.ControlConfiguration;
import org.jesperancinha.itf.android.config.EditConfiguration;
import org.jesperancinha.itf.android.config.ImageConfiguration;

public abstract class MainFragmentManagerConfiguration {

    protected MainFragment mainFragment;

    public MainFragmentManagerConfiguration(MainFragment mainFragment) {
        this.mainFragment = mainFragment;
    }

    protected EditConfiguration getEditConfiguration() {
        return this.mainFragment.getEditConfiguration();
    }

    protected ControlConfiguration getControlConfiguration() {
        return this.mainFragment.getControlConfiguration();
    }

    protected ImageConfiguration getImageConfiguration() {
        return this.mainFragment.getImageConfiguration();
    }
}
