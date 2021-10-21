package me.lwb.mvvm.ext

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

/**
 * Created by luowenbin on 2021/9/7.
 */
inline fun <reified T> LifecycleOwner.observe(
    channel: Channel<T>,
    noinline action: (t: T) -> Unit
) =
    lifecycleScope.launch {
        channel.receiveAsFlow()
            .collect { action(it) }
    }
