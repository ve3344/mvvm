@file:Suppress("unused")

package me.lwb.mvvm.ext

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import me.lwb.mvvm.exception.UnexpectedResultException
import me.lwb.mvvm.network.IResponse

fun <T> Flow<IResponse<T>>.extractData(): Flow<T> {
    return this
        .flowOn(Dispatchers.IO)
        .map {
            if (it.succeed) {
                return@map it.data
            } else {
                throw UnexpectedResultException.from(it)
            }
        }
}

