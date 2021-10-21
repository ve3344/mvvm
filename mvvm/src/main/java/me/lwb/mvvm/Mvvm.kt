package me.lwb.mvvm

object Mvvm {
    var configs: MvvmConfigs = object : MvvmConfigs {
    }

    fun config(configs: MvvmConfigs) {
        Mvvm.configs = configs
    }
}