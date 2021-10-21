package me.lwb.mvvm.network.interceptor


class JsonHeaderInterceptor : ReplaceHeaderInterceptor(mapOf(KEY to VALUE)) {

    companion object {
        private const val KEY = "Content-Type"
        private const val VALUE = "application/json;charset=UTF-8"
    }
}