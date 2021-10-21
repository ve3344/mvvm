package me.lwb.mvvm.exception

import me.lwb.mvvm.network.IResponse

open class UnexpectedResultException private constructor(
    code: String,
    message: String? = null,
    cause: Throwable? = null
) :
    BaseApiException(code, message, cause) {
    companion object {
        fun from(response: IResponse<*>) = UnexpectedResultException(response.status, response.msg)
    }
}

