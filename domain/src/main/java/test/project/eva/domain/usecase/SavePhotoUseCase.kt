package test.project.eva.domain.usecase

import android.graphics.Bitmap
import test.project.eva.domain.states.SavePhotoState
import test.project.eva.domain.repository.GalleryRepository

class SavePhotoUseCase(private val repository: GalleryRepository) {
    fun execute(bitMap: Bitmap): SavePhotoState {
        return repository.savePhoto(bitMap)
    }
}