package me.lwb.mvvm.ext

import me.lwb.mvvm.network.IApiFactory
import retrofit2.Retrofit

/**
 * Created by luowenbin on 2021/10/19.
 */
inline fun <reified T> IApiFactory.lazy(): Lazy<T> =
    lazy(LazyThreadSafetyMode.NONE) { createApi(T::class.java) }


class RetrofitApiFactory(private val retrofit: Retrofit) : IApiFactory {
    override fun <T> createApi(clazz: Class<T>): T = retrofit.create(clazz)
}
