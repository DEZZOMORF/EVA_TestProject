package test.project.eva.presentation.screens.camera

import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageProxy
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import test.project.eva.Utils
import test.project.eva.domain.usecase.SavePhotoUseCase
import test.project.eva.presentation.models.PhotoFilter
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
    private val savePhotoUseCase: SavePhotoUseCase
) : ViewModel() {

    private val _cameraSelector: MutableLiveData<CameraSelector> = MutableLiveData(CameraSelector.DEFAULT_BACK_CAMERA)
    val cameraSelector get() = _cameraSelector

    private val _selectedPhotoFilter: MutableLiveData<PhotoFilter?> = MutableLiveData(null)
    val selectedPhotoFilter get() = _selectedPhotoFilter

    fun changeFrontBackCamera() {
        _cameraSelector.value = when (_cameraSelector.value) {
            CameraSelector.DEFAULT_FRONT_CAMERA -> CameraSelector.DEFAULT_BACK_CAMERA
            CameraSelector.DEFAULT_BACK_CAMERA -> CameraSelector.DEFAULT_FRONT_CAMERA
            else -> CameraSelector.DEFAULT_BACK_CAMERA
        }
    }

    fun setSelectedPhotoFilter(photoFilter: PhotoFilter) {
        _selectedPhotoFilter.value = photoFilter
    }

    fun takePhoto(imageCapture: ImageCapture) {
        val imgCaptureExecutor: ExecutorService = Executors.newSingleThreadExecutor()
        imageCapture.takePicture(imgCaptureExecutor, object :
            ImageCapture.OnImageCapturedCallback() {
            override fun onCaptureSuccess(image: ImageProxy) {
                val bitmap = Utils.imageProxyToBitmap(image)
                val bitmapWithFilter = Utils.setPhotoFilter(bitmap, _selectedPhotoFilter.value?.colorMatrix)
                savePhotoUseCase.execute(bitmapWithFilter)
                super.onCaptureSuccess(image)
            }
        })
    }
}