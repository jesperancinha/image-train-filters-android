package com.steelzack.chartizateapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 *
 */
public class SwipeAdapter extends FragmentStatePagerAdapter {

    public SwipeAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position){
            case 0: fragment = new MainFragment(); break;
            case 1: fragment = new EmailFragment(); break;
            case 2: fragment = new ViewFragment(); break;
            default:break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
