package test.project.eva.presentation.utils

sealed class UIState<out R> {
    data class Success<out T>(val data: T) : UIState<T>()
    data class Error(val exception: Exception) : UIState<Nothing>()
    object Loading : UIState<Nothing>()
}