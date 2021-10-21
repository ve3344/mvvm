package me.lwb.mvvm.exception

import android.net.ParseException
import android.util.MalformedJsonException
import me.lwb.mvvm.exception.RequestErrorCode.*
import org.json.JSONException
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLException

class RequestException private constructor(
    val errorCode: RequestErrorCode,
    cause: Throwable? = null
) :
    BaseApiException(
        "${errorCode.code}",
        errorCode.message,
        cause
    ) {

    companion object {
        fun from(e: Throwable): RequestException {
            if (e is RequestException) {
                return e
            }

            val code = when (e) {
                is HttpException -> when (e.code()) {
                    in 400..499 -> HTTP_CLIENT_ERROR
                    in 500..599 -> HTTP_SERVER_ERROR
                    else -> HTTP_OTHER_ERROR
                }
                is JSONException, is ParseException, is MalformedJsonException -> PARSE_ERROR
                is ConnectException -> NETWORK_ERROR
                is SSLException -> SSL_ERROR
                is SocketTimeoutException -> TIMEOUT_ERROR
                is UnknownHostException -> TIMEOUT_ERROR
                else -> UNKNOWN
            }
            return RequestException(code, e)
        }

    }
}

