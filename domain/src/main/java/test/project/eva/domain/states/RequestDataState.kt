package test.project.eva.domain.states

sealed class RequestDataState<out R> {
    data class Success<out T>(val data: T) : RequestDataState<T>()
    data class Error(val exception: Exception) : RequestDataState<Nothing>()
    object Loading : RequestDataState<Nothing>()
}