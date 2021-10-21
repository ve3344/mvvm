package me.lwb.mvvm.network

interface IResponse<T> {
    val status: String
    val msg: String?
    val data: T
    val succeed: Boolean
}

