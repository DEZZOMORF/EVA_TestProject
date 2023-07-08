package test.project.eva.managers

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import test.project.eva.presentation.models.PhotoFilter

object PhotoFilterManager {

    fun photoFilterList(preview: Bitmap) =
        listOf(
            PhotoFilter(preview, "Default", null),
            PhotoFilter(preview, "Grey", greyColorMatrixFilter),
            PhotoFilter(preview, "Sepia", sepiaColorMatrixFilter),
            PhotoFilter(preview, "Invert", invertColorMatrixFilter),
            PhotoFilter(preview, "Tint", createTintFilterMatrix(200,1f )),
            PhotoFilter(preview, "Red", redColorMatrixFilter),
            PhotoFilter(preview, "Contrast", contrastColorMatrixFilter(0.3f))
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

    private val sepiaColorMatrixFilter get(): ColorMatrixColorFilter {
        val colorMatrix = ColorMatrix(
            floatArrayOf(
                0.39f, 0.769f, 0.189f, 0f, 0f,
                0.349f, 0.686f, 0.168f, 0f, 0f,
                0.272f, 0.534f, 0.131f, 0f, 0f,
                0f, 0f, 0f, 1f, 0f
            )
        )
        return ColorMatrixColorFilter(colorMatrix)
    }

    private val invertColorMatrixFilter get(): ColorMatrixColorFilter {
        val colorMatrix = ColorMatrix(
            floatArrayOf(
                -1f, 0f, 0f, 0f, 255f,
                0f, -1f, 0f, 0f, 255f,
                0f, 0f, -1f, 0f, 255f,
                0f, 0f, 0f, 1f, 0f
            )
        )
        return ColorMatrixColorFilter(colorMatrix)
    }

    private fun createTintFilterMatrix(tintColor: Int, intensity: Float): ColorMatrixColorFilter {
        val redMultiplier = Color.red(tintColor) / 255f
        val greenMultiplier = Color.green(tintColor) / 255f
        val blueMultiplier = Color.blue(tintColor) / 255f

        val colorMatrix = ColorMatrix(floatArrayOf(
            redMultiplier, 0f, 0f, 0f, 0f,
            0f, greenMultiplier, 0f, 0f, 0f,
            0f, 0f, blueMultiplier, 0f, 0f,
            0f, 0f, 0f, intensity, 0f
        ))

        return ColorMatrixColorFilter(colorMatrix)
    }
}