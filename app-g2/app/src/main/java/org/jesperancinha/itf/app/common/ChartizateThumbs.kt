package org.jesperancinha.itf.app.common

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.ImageView
import java.io.InputStream
import android.media.ThumbnailUtils


class ChartizateThumbs(
    val width: Int,
     val height: Int,
    val imageView: ImageView
) {
    fun setImageThumbnail(inputStream: InputStream?) {
        val options: BitmapFactory.Options = BitmapFactory.Options()
        options.inPreferredConfig = Bitmap.Config.RGB_565
        val bitmap: Bitmap = kotlin.requireNotNull(BitmapFactory.decodeStream(inputStream, null, options))
        imageView.setImageBitmap(ThumbnailUtils.extractThumbnail(bitmap, width, height))
    }

    fun setImage(uri: Uri?) {
        imageView.setImageURI(null)
        imageView.setImageURI(uri)
    }
}