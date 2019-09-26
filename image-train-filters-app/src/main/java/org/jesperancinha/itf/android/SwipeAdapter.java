package org.jesperancinha.itf.android;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import org.jesperancinha.itf.android.main.MainFragment;

/**
 *
 */
public class SwipeAdapter extends FragmentStatePagerAdapter {

    private final Fragment[] fragments;

    public SwipeAdapter(final FragmentManager fm, final Fragment[] fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return this.fragments[position];
    }

    @Override
    public int getCount() {
        return this.fragments.length;
    }
}
