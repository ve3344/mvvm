package me.lwb.mvvm.network

sealed class RequestStatus<out T> {
    companion object {
        fun <T> loading(loadingMessage: String): RequestStatus<T> = Loading(loadingMessage)
        fun <T> error(error: Throwable): RequestStatus<T> = Error(error)
        fun <T> result(response: IResponse<T>): RequestStatus<T> = response.run {
            if (succeed) Success(data) else Fail(status, msg ?: "")
        }
    }

    data class Loading(val loadingMessage: String) : RequestStatus<Nothing>()
    data class Success<out T>(val data: T) : RequestStatus<T>()
    data class Fail(val code: String, val msg: String) : RequestStatus<Nothing>()
    data class Error(val error: Throwable) : RequestStatus<Nothing>()

}



