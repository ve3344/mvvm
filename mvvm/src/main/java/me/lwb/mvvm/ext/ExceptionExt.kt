package me.lwb.mvvm.ext

import kotlinx.coroutines.flow.FlowCollector
import me.lwb.mvvm.exception.ExceptionReportManager

fun <T> Result<T>.reportError() {
    onFailure {
        ExceptionReportManager.reporter(it)
    }
}

suspend fun <T> Result<T>.extractError(collector: FlowCollector<Throwable>) {
    exceptionOrNull()?.let { collector.emit(it) }
}