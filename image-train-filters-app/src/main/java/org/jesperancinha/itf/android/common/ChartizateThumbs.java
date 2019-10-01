package org.jesperancinha.itf.android.common;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.widget.ImageView;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.InputStream;

@Getter
@Builder
@AllArgsConstructor
public class ChartizateThumbs {

    private final Integer width;
    private final Integer height;
    private final ImageView imageView;

    public void setImageThumbnail(InputStream inputStream) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        final Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, options);
        imageView.setImageBitmap(ThumbnailUtils.extractThumbnail(bitmap, width, height));
    }

    public void setImage(Uri uri) {
        imageView.setImageURI(null);
        imageView.setImageURI(uri);
    }
}
