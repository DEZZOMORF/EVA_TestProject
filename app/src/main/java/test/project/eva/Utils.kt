package test.project.eva

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import androidx.camera.core.ImageProxy
import java.nio.ByteBuffer

object Utils {

    fun imageProxyToBitmap(image: ImageProxy): Bitmap {
        val planeProxy = image.planes[0]
        val buffer: ByteBuffer = planeProxy.buffer
        val bytes = ByteArray(buffer.remaining())
        buffer.get(bytes)
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }

    fun setPhotoFilter(original: Bitmap, filter: ColorMatrixColorFilter?): Bitmap {
        val bitmap = Bitmap.createBitmap(
            original.width,
            original.height, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)

        val paint = Paint()
        paint.colorFilter = filter
        canvas.drawBitmap(original, 0f, 0f, paint)

        return bitmap
    }
}