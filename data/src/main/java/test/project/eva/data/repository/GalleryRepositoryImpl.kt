package test.project.eva.data.repository

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.provider.MediaStore
import test.project.eva.domain.models.SavePhotoResult
import test.project.eva.domain.states.SavePhotoState
import test.project.eva.domain.repository.GalleryRepository
import java.io.OutputStream

class GalleryRepositoryImpl(private val context: Context): GalleryRepository {

    override fun savePhoto(bitmap: Bitmap): SavePhotoState {
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

        val imageUri = contentResolver.insert(uri, values) ?: return  SavePhotoState.Error

        var outputStream: OutputStream? = null
        return try {
            outputStream = contentResolver.openOutputStream(imageUri)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream?.flush()
            SavePhotoState.Success(SavePhotoResult(imageUri))
        } catch (e: Exception) {
            e.printStackTrace()
            SavePhotoState.Error
        } finally {
            outputStream?.close()
        }
    }
}