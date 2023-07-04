package test.project.eva.presentation.screens.editor

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import test.project.eva.domain.states.RequestDataState
import test.project.eva.domain.states.SavePhotoState
import test.project.eva.domain.usecase.GetRandomPhotoUseCase
import test.project.eva.domain.usecase.SavePhotoUseCase
import test.project.eva.presentation.models.PhotoFilter
import java.io.FileNotFoundException
import java.io.InputStream
import javax.inject.Inject

@HiltViewModel
class EditorViewModel @Inject constructor(
    private val savePhotoUseCase: SavePhotoUseCase,
    private val getRandomPhotoUseCase: GetRandomPhotoUseCase
) : ViewModel() {

    private val _getPictureState: MutableLiveData<RequestDataState<Drawable?>> = MutableLiveData()
    val getPictureState: LiveData<RequestDataState<Drawable?>> = _getPictureState

    private val _savePictureState: MutableLiveData<SavePhotoState> = MutableLiveData()
    val savePictureState: LiveData<SavePhotoState> = _savePictureState

    private val _photoFilter: MutableLiveData<PhotoFilter?> = MutableLiveData()
    val photoFilter: LiveData<PhotoFilter?> = _photoFilter

    fun rememberSelectedPicture(context: Context, uri: Uri?) {
        uri?.let { pictureUri ->
            _getPictureState.value = try {
                val inputStream: InputStream? = context.contentResolver.openInputStream(pictureUri)
                val drawable = Drawable.createFromStream(inputStream, pictureUri.toString())
                RequestDataState.Success(drawable)
            } catch (e: FileNotFoundException) {
                RequestDataState.Error(e)
            }
        }
    }

    fun savePicture(bitmap: Bitmap): SavePhotoState {
        val result = savePhotoUseCase.execute(bitmap)
        _savePictureState.value = result
        return result
    }

    fun getSelectedPicture(): Drawable? {
        return when (val requestDataState = _getPictureState.value) {
            is RequestDataState.Success -> requestDataState.data
            else -> null
        }
    }

    fun getRandomPhoto() {
        _getPictureState.value = RequestDataState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val result = getRandomPhotoUseCase.execute()
            withContext(Dispatchers.Main) {
                _getPictureState.value = when (result) {
                    is RequestDataState.Loading -> RequestDataState.Loading
                    is RequestDataState.Success -> RequestDataState.Success(result.data.drawable)
                    is RequestDataState.Error -> RequestDataState.Error(result.exception)
                }
            }
        }
    }

    fun setPhotoFilter(photoFilter: PhotoFilter){
        _photoFilter.value = photoFilter
    }
}