package me.lwb.mvvm.network

interface IApiFactory {
    fun <T> createApi(clazz: Class<T>): T
}

