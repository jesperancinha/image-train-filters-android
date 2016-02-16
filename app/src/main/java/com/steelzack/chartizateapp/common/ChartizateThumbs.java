package com.steelzack.chartizateapp.common;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.widget.ImageView;

import java.io.InputStream;

/**
 * Created by joaofilipesabinoesperancinha on 16-02-16.
 */
public class ChartizateThumbs {
    public static void setImageThumbnail(ImageView imageView, InputStream inputStream) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, options);
        imageView.setImageBitmap(ThumbnailUtils.extractThumbnail(bitmap, 50, 50));
    }
}
