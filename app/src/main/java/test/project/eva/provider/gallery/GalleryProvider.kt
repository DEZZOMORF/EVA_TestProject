package test.project.eva.provider.gallery

import android.content.Intent
import android.graphics.Bitmap
import androidx.activity.result.ActivityResultLauncher

interface GalleryProvider {
    fun savePhoto(bitmap: Bitmap): SavePhotoState
    fun selectPictureFromGallery(selectPictureLauncher: ActivityResultLauncher<Intent>)
}