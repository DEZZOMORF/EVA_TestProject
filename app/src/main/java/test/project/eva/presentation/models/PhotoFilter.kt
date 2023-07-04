package test.project.eva.presentation.models

import android.graphics.ColorMatrixColorFilter
import androidx.annotation.ColorRes

data class PhotoFilter(
    @ColorRes
    val color: Int,
    val name: String,
    val colorMatrix: ColorMatrixColorFilter?
)
