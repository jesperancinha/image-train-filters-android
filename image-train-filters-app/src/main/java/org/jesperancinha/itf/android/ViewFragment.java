package org.jesperancinha.itf.android;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ViewFragment extends Fragment {

    private View mainView = null;

    private ImageView imageView = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mainView == null) {
            mainView = inflater.inflate(R.layout.content_view, container, false);
            imageView = mainView.findViewById(R.id.imageViewGenerated);
        }
        return mainView;
    }

    public ImageView getImageView() {
        return imageView;
    }
}
