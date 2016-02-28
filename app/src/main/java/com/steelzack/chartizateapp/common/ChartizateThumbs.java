package com.steelzack.chartizateapp.common;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.URI;

/**
 * Created by joaofilipesabinoesperancinha on 16-02-16.
 */
public class ChartizateThumbs {
    public static void setImageThumbnail(ImageView imageView, InputStream inputStream) {
        setImageThumbnail(imageView, inputStream, 50, 50);
    }

    public static void setImageThumbnail(ImageView imageView, InputStream inputStream, int width, int heigh) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, options);
        imageView.setImageBitmap(ThumbnailUtils.extractThumbnail(bitmap, width, heigh));
    }

    public static void setImage(ImageView imageView, Uri uri)
    {
        imageView.setImageURI(uri);
    }
}
