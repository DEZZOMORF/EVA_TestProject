package test.project.eva.presentation.models

import android.graphics.Bitmap
import android.graphics.ColorMatrixColorFilter

data class PhotoFilter(
    val preview: Bitmap,
    val name: String,
    val colorMatrix: ColorMatrixColorFilter?
)
