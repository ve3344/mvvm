package me.lwb.mvvm.base

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import me.lwb.context.AppContext


object GlobalVm : ViewModelStoreOwner {
    private val globalViewModelStore: ViewModelStore by lazy { ViewModelStore() }

    private val factory: ViewModelProvider.Factory by lazy {
        ViewModelProvider.AndroidViewModelFactory.getInstance(AppContext.context as Application)
    }

    override fun getViewModelStore() = globalViewModelStore

    val viewModelProvider by lazy { ViewModelProvider(this, factory) }

}