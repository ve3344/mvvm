package me.lwb.mvvm.ext

import android.app.Dialog
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import kotlin.reflect.KMutableProperty0

/**
 * Created by luowenbin on 2021/9/6.
 */


fun <V : ViewBinding> Dialog.setContentView(
    creator: (inflater: LayoutInflater) -> V,
    params: ViewGroup.LayoutParams? = null,
    builder: V.() -> Unit
) {
    if (params == null) {
        setContentView(creator(layoutInflater).apply(builder).root)
    } else {
        setContentView(creator(layoutInflater).apply(builder).root, params)
    }
}

inline fun <T : Dialog> KMutableProperty0<T?>.show(
    createDialog: () -> T,
    updateDialog: T.() -> Unit
) {
    val dialog: T = this.get() ?: createDialog().also { this.set(it) }
    dialog.updateDialog()
    if (!dialog.isShowing) {
        dialog.show()
    }
}

