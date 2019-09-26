package org.jesperancinha.itf.android;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
