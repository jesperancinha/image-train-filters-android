package com.steelzack.chartizateapp;

import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by joao on 28-2-16.
 */
public class ViewFragment extends Fragment {

    private View mainView = null;

    private ImageView imageView = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mainView == null) {
            mainView = inflater.inflate(R.layout.content_view, container, false);
            imageView = (ImageView) mainView.findViewById(R.id.imageViewGenerated);
        }
        return mainView;
    }

    public ImageView getImageView() {
        return imageView;
    }
}
