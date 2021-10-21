package me.lwb.mvvm.exception

abstract class BaseApiException(
    val code: String,
    message: String? = null,
    cause: Throwable? = null
) :
    Exception(message, cause) {
}