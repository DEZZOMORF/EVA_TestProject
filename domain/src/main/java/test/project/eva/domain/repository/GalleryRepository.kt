package test.project.eva.domain.repository

import android.graphics.Bitmap
import test.project.eva.domain.states.SavePhotoState

interface GalleryRepository {
    fun savePhoto(bitmap: Bitmap): SavePhotoState
}