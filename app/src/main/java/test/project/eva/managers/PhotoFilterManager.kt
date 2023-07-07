package test.project.eva.managers

import android.graphics.Bitmap
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import test.project.eva.Utils
import test.project.eva.presentation.models.PhotoFilter

object PhotoFilterManager {

    fun photoFilterList(preview: Bitmap) =
        listOf(
            PhotoFilter(preview, "Default", null),
            PhotoFilter(Utils.setPhotoFilter(preview, greyColorMatrixFilter), "Grey", greyColorMatrixFilter),
            PhotoFilter(Utils.setPhotoFilter(preview, redColorMatrixFilter), "Red", redColorMatrixFilter),
            PhotoFilter(Utils.setPhotoFilter(preview, contrastColorMatrixFilter(0.3f)), "Contrast1", contrastColorMatrixFilter(0.3f)),
            PhotoFilter(Utils.setPhotoFilter(preview, greenColorMatrixFilter), "Green", greenColorMatrixFilter),
            PhotoFilter(Utils.setPhotoFilter(preview, contrastColorMatrixFilter(0.6f)), "Contrast2", contrastColorMatrixFilter(0.6f)),
            PhotoFilter(Utils.setPhotoFilter(preview, blueColorMatrixFilter), "Blue", blueColorMatrixFilter),
            PhotoFilter(Utils.setPhotoFilter(preview, contrastColorMatrixFilter(0.9f)), "Contrast3", contrastColorMatrixFilter(0.9f)),
        )

    private val greyColorMatrixFilter get(): ColorMatrixColorFilter {
        val colorMatrix = ColorMatrix()
        colorMatrix.setSaturation(0f)
        return ColorMatrixColorFilter(colorMatrix)
    }

    private val redColorMatrixFilter get(): ColorMatrixColorFilter {
        val colorMatrix = ColorMatrix(
            floatArrayOf(
                1f, 0f, 0f, 0f, 160f,
                0f, 1f, 0f, 0f, 0f,
                0f, 0f, 1f, 0f, 0f,
                0f, 0f, 0f, 1f, 0f
            )
        )
        return ColorMatrixColorFilter(colorMatrix)
    }

    private val greenColorMatrixFilter get(): ColorMatrixColorFilter {
        val colorMatrix = ColorMatrix(
            floatArrayOf(
                1f, 0f, 0f, 0f, 0f,
                0f, 1f, 0f, 0f, 100f,
                0f, 0f, 1f, 0f, 0f,
                0f, 0f, 0f, 1f, 0f
            )
        )
        return ColorMatrixColorFilter(colorMatrix)
    }

    private val blueColorMatrixFilter get(): ColorMatrixColorFilter {
        val colorMatrix = ColorMatrix(
            floatArrayOf(
                1f, 0f, 0f, 0f, 0f,
                0f, 1f, 0f, 0f, 0f,
                0f, 0f, 1f, 0f, 160f,
                0f, 0f, 0f, 1f, 0f
            )
        )
        return ColorMatrixColorFilter(colorMatrix)
    }

    private fun contrastColorMatrixFilter(contrast: Float): ColorMatrixColorFilter {
        val matrix = ColorMatrix()

        val scale = contrast + 1f
        val translate = (-0.5f * scale + 0.5f) * 255f

        matrix.set(floatArrayOf(
            scale, 0f, 0f, 0f, translate,
            0f, scale, 0f, 0f, translate,
            0f, 0f, scale, 0f, translate,
            0f, 0f, 0f, 1f, 0f
        ))

        return ColorMatrixColorFilter(matrix)
    }
}