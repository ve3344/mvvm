package me.lwb.mvvm.ext

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.lwb.mvvm.exception.BaseApiException
import me.lwb.mvvm.exception.RequestException
import me.lwb.mvvm.exception.UnexpectedResultException
import me.lwb.mvvm.network.IResponse
import me.lwb.mvvm.network.RequestStatus


/**
 *
 * @param block 请求
 * @param status 状态回调
 * @param loadingMessage 提示内容
 */
fun <T, R : IResponse<T>> ViewModel.request(
    block: suspend () -> R,
    status: MutableLiveData<RequestStatus<T>>,
    loadingMessage: String? = null
): Job {
    return viewModelScope.launch {
        extractStatus(status, loadingMessage, block)
    }
}

/**
 *
 * @param block 请求
 * @param success success回调
 * @param error error提回调
 */
fun <T> ViewModel.request(
    block: suspend () -> IResponse<T>,
    success: (T) -> Unit,
    error: (BaseApiException) -> Unit = {},
): Job = viewModelScope.launch {
    runCatching {
        val result = block()
        if (result.succeed) {
            success(result.data)
        } else {
            error(UnexpectedResultException.from(result))
        }
    }.onFailure {
        error(RequestException.from(it))
    }
}

/**
 *
 * @param block 请求
 * @param success success回调
 * @param error error提回调
 */
fun <T, R : IResponse<T>> ViewModel.requestRaw(
    block: suspend () -> R,
    success: (R) -> Unit,
    error: (BaseApiException) -> Unit = {},
): Job = viewModelScope.launch {
    runCatching {
        val result = block()
        if (result.succeed) {
            success(result)
        } else {
            error(UnexpectedResultException.from(result))
        }
    }.onFailure {
        error(RequestException.from(it))
    }
}

/**
 *
 * @param block 请求
 * @param success success回调
 * @param error error提回调
 */
fun <T> ViewModel.launchIo(
    block: () -> T,
    success: (T) -> Unit,
    error: (Throwable) -> Unit = {}
) = viewModelScope.launch {
    kotlin.runCatching {
        withContext(Dispatchers.IO) {
            block()
        }
    }.onSuccess(success)
        .onFailure(error)
}
