package me.lwb.mvvm.exception

object ExceptionReportManager {
    var reporter: (e: Throwable) -> Unit = {}
}