package test.project.eva.managers

import android.content.Context
import android.graphics.drawable.Drawable
import com.bumptech.glide.Glide
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class GlideManager @Inject constructor(@ApplicationContext private val context: Context) {

    fun loadDrawable(url: String?): Drawable {
        return Glide
            .with(context)
            .asDrawable()
            .load(url)
            .submit()
            .get()
    }
}