package org.jesperancinha.itf.android;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

/**
 *
 */
public class SwipeAdapter extends FragmentStatePagerAdapter {

    private final FragmentManager fm;

    private MainFragment mainFragment = null;

    private ViewFragment viewFragment = null;

    private EmailFragment emailFragment = null;

    public SwipeAdapter(FragmentManager fm) {
        super(fm);
        this.fm = fm;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                if (mainFragment == null) {
                    fragment = new MainFragment();
                    mainFragment = (MainFragment) fragment;
                } else {
                    fragment = mainFragment;
                }
                break;
            case 1:
                if (emailFragment == null) {
                    fragment = new EmailFragment();
                    emailFragment = (EmailFragment) fragment;
                } else {
                    fragment = emailFragment;
                }
                break;
            case 2:
                if (viewFragment == null) {
                    fragment = new ViewFragment();
                    viewFragment = (ViewFragment) fragment;
                } else {
                    fragment = viewFragment;
                }
                break;
            default:
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
