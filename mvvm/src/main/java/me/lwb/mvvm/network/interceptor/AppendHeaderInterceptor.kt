package me.lwb.mvvm.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response


open class AppendHeaderInterceptor(private val headers: Map<String, String>) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.request()
            .newBuilder()
            .run {
                for (headMap in headers) {
                    addHeader(headMap.key, headMap.value)
                }
                build()
            }.let { chain.proceed(it) }

    }
}