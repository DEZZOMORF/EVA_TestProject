package test.project.eva.managers

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import test.project.eva.R
import test.project.eva.presentation.models.PhotoFilter

object PhotoFilterManager {

    val photoFilterList =
        listOf(
            PhotoFilter(R.color.white, "Default", null),
            PhotoFilter(R.color.grey, "Grey", greyColorMatrixFilter),
            PhotoFilter(R.color.red, "Red", redColorMatrixFilter),
            PhotoFilter(R.color.black, "Contrast1", contrastColorMatrixFilter(0.3f)),
            PhotoFilter(R.color.green, "Green", greenColorMatrixFilter),
            PhotoFilter(R.color.black, "Contrast2", contrastColorMatrixFilter(0.6f)),
            PhotoFilter(R.color.blue, "Blue", blueColorMatrixFilter),
            PhotoFilter(R.color.black, "Contrast3", contrastColorMatrixFilter(0.9f)),
        )

    private val defaultColorMatrixFilter get(): ColorMatrixColorFilter {
        val colorMatrix = ColorMatrix()
        return ColorMatrixColorFilter(colorMatrix)
    }

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