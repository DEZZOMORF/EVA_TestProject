package test.project.eva.presentation.models

import android.graphics.Bitmap
import android.graphics.ColorMatrixColorFilter
import test.project.eva.Utils

data class PhotoFilter(
    private val previewRaw: Bitmap,
    val name: String,
    val colorMatrix: ColorMatrixColorFilter?
) {
    val preview: Bitmap get() = Utils.setPhotoFilter(previewRaw, colorMatrix)
}
