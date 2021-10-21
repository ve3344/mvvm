package me.lwb.mvvm.ext

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.FlowCollector
import me.lwb.mvvm.network.IResponse
import me.lwb.mvvm.network.RequestStatus

fun <T> RequestStatus<T>.listen(
    onSuccess: ((data: T) -> Unit)? = null,
    onFail: ((code: String, message: String?) -> Unit)? = null,
    onError: ((error: Throwable) -> Unit)? = null,
    onLoading: ((message: String) -> Unit)? = null
): RequestStatus<T> {
    when (this) {
        is RequestStatus.Loading -> onLoading?.invoke(loadingMessage)
        is RequestStatus.Success -> onSuccess?.invoke(data)
        is RequestStatus.Fail -> onFail?.invoke(code, msg)
        is RequestStatus.Error -> onError?.invoke(error)
    }
    return this
}

fun <T> RequestStatus<T>.onSuccess(onSuccess: ((data: T) -> Unit)): RequestStatus<T> {
    if (this is RequestStatus.Success) {
        onSuccess(data)
    }
    return this
}

fun <T> RequestStatus<T>.onFail(onFail: ((code: String, message: String?) -> Unit)): RequestStatus<T> {
    if (this is RequestStatus.Fail) {
        onFail(code, msg)
    }
    return this
}

fun <T> RequestStatus<T>.onLoading(onLoading: ((message: String) -> Unit)): RequestStatus<T> {
    if (this is RequestStatus.Fail) {
        onLoading(msg)
    }
    return this
}

fun <T> RequestStatus<T>.onError(onError: ((error: Throwable) -> Unit)): RequestStatus<T> {
    if (this is RequestStatus.Error) {
        onError(error)
    }
    return this
}


suspend fun <T, R : IResponse<T>> extractStatus(
    status: MutableLiveData<RequestStatus<T>>,
    loadingMessage: String? = null,
    block: (suspend () -> R)
): R {
    return runCatching {
        loadingMessage?.let { status.value = RequestStatus.loading(loadingMessage) }
        block()
    }.onSuccess {
        status.value = RequestStatus.result(it)
    }.onFailure {
        status.value = RequestStatus.error(it)
    }.getOrThrow()
}

suspend fun <T, R : IResponse<T>> extractStatus(
    status: Channel<RequestStatus<T>>,
    loadingMessage: String? = null,
    block: (suspend () -> R)
): R {
    return runCatching {
        loadingMessage?.let { status.send(RequestStatus.loading(loadingMessage)) }
        block()
    }.onSuccess {
        status.send(RequestStatus.result(it))
    }.onFailure {
        status.send(RequestStatus.error(it))
    }.getOrThrow()
}

suspend fun <T, R : IResponse<T>> extractStatus(
    status: FlowCollector<RequestStatus<T>>,
    loadingMessage: String? = null,
    block: (suspend () -> R)
): R {
    return runCatching {
        loadingMessage?.let { status.emit(RequestStatus.loading(loadingMessage)) }
        block()
    }.onSuccess {
        status.emit(RequestStatus.result(it))
    }.onFailure {
        status.emit(RequestStatus.error(it))
    }.getOrThrow()
}
