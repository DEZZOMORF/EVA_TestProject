package test.project.eva.provider.gallery

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import dagger.hilt.android.qualifiers.ApplicationContext
import test.project.eva.R
import java.io.OutputStream
import javax.inject.Inject

class GalleryProviderImpl @Inject constructor(@ApplicationContext private val context: Context): GalleryProvider {

    override fun savePhoto(bitmap: Bitmap): SavePhotoState {
        var outputStream: OutputStream? = null
        return try {
            val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
            } else {
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            }
            val fileName = "EVA_${System.currentTimeMillis()}"
            val contentResolver = context.contentResolver
            val values = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            }

            val imageUri = contentResolver.insert(uri, values) ?: throw Exception()

            outputStream = contentResolver.openOutputStream(imageUri)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream?.flush()
            SavePhotoState.Success(SavePhotoState.SavePhotoResult(imageUri))
        } catch (e: Exception) {
            SavePhotoState.Error(e)
        } finally {
            outputStream?.close()
        }
    }

    override fun selectPictureFromGallery(selectPictureLauncher: ActivityResultLauncher<Intent>) {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        selectPictureLauncher.launch(
            Intent.createChooser(
                intent,
                context.getString(R.string.select_picture)
            )
        )
    }
}