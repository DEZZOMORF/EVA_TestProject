package test.project.eva.presentation.screens.editor

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import test.project.eva.domain.states.RequestDataState
import test.project.eva.domain.usecase.GetRandomPhotoUseCase
import test.project.eva.managers.GlideManager
import test.project.eva.presentation.models.PhotoFilter
import test.project.eva.presentation.utils.UIState
import test.project.eva.provider.gallery.GalleryProvider
import test.project.eva.provider.gallery.SavePhotoState
import java.io.FileNotFoundException
import java.io.InputStream
import javax.inject.Inject

@HiltViewModel
class EditorViewModel @Inject constructor(
    private val galleryProvider: GalleryProvider,
    private val glideManager: GlideManager,
    private val getRandomPhotoUseCase: GetRandomPhotoUseCase
) : ViewModel() {

    private val _getPictureState: MutableLiveData<UIState<Drawable?>> = MutableLiveData()
    val getPictureState: LiveData<UIState<Drawable?>> = _getPictureState

    private val _savePictureState: MutableLiveData<SavePhotoState> = MutableLiveData()
    val savePictureState: LiveData<SavePhotoState> = _savePictureState

    private val _photoFilter: MutableLiveData<PhotoFilter?> = MutableLiveData()
    val photoFilter: LiveData<PhotoFilter?> = _photoFilter

    fun rememberSelectedPicture(context: Context, uri: Uri?) {
        uri?.let { pictureUri ->
            _getPictureState.value = try {
                val inputStream: InputStream? = context.contentResolver.openInputStream(pictureUri)
                val drawable = Drawable.createFromStream(inputStream, pictureUri.toString())
                UIState.Success(drawable)
            } catch (e: FileNotFoundException) {
                UIState.Error(e)
            }
        }
    }

    fun savePicture(bitmap: Bitmap): SavePhotoState {
        val result = galleryProvider.savePhoto(bitmap)
        _savePictureState.value = result
        return result
    }

    fun getSelectedPicture(): Drawable? {
        return when (val requestDataState = _getPictureState.value) {
            is UIState.Success -> requestDataState.data
            else -> null
        }
    }

    fun getRandomPhoto() {
        _getPictureState.value = UIState.Loading
        viewModelScope.launch(Dispatchers.Main) {
            val result = withContext(Dispatchers.IO) {
                getRandomPhotoUseCase.execute()
            }
            _getPictureState.value = when (result) {
                is RequestDataState.Loading -> UIState.Loading
                is RequestDataState.Error -> UIState.Error(result.exception)
                is RequestDataState.Success -> {
                    val drawable = withContext(Dispatchers.IO) {
                        glideManager.loadDrawable(result.data.imageUrl)
                    }
                    UIState.Success(drawable)
                }
            }
        }
    }

    fun setPhotoFilter(photoFilter: PhotoFilter){
        _photoFilter.value = photoFilter
    }

    fun selectPictureFromGallery(selectPictureLauncher: ActivityResultLauncher<Intent>) {
        galleryProvider.selectPictureFromGallery(selectPictureLauncher)
    }
}