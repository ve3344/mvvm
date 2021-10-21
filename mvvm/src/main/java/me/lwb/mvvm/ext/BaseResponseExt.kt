package me.lwb.mvvm.ext

import me.lwb.mvvm.exception.UnexpectedResultException
import me.lwb.mvvm.network.IResponse

fun <T> IResponse<T>.getOrThrow(): T {
    if (succeed) {
        return data
    }
    throw UnexpectedResultException.from(this)
}