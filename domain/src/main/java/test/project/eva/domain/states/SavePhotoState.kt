package test.project.eva.domain.states

import test.project.eva.domain.models.SavePhotoResult

sealed class SavePhotoState {
    class Success(val data: SavePhotoResult): SavePhotoState()
    object Error: SavePhotoState()
}
