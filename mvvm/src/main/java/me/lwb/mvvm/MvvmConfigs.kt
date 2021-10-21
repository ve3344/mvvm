package me.lwb.mvvm

import androidx.lifecycle.ViewModelProvider

interface MvvmConfigs {

    fun viewModelFactory(): ViewModelProvider.Factory? = ViewModelProvider.NewInstanceFactory()

}