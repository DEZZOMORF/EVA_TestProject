package test.project.eva.provider.gallery

import android.net.Uri

sealed class SavePhotoState {
    class Success(val data: SavePhotoResult): SavePhotoState()
    class Error(val error: Exception) : SavePhotoState()

    data class SavePhotoResult(
        val imageUri: Uri
    )
}
