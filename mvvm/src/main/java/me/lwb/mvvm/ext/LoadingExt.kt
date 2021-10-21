package me.lwb.mvvm.ext

import android.app.Dialog
import android.content.Context
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.FlowCollector

/**
 * Created by luowenbin on 2021/10/9.
 */


suspend inline fun <R> withLoading(
    loading: FlowCollector<Boolean>,
    crossinline block: suspend () -> R
): R {
    loading.emit(true)
    return block().also {
        loading.emit(false)
    }
}

suspend inline fun <R> withLoading(
    loading: MutableLiveData<Boolean>,
    crossinline block: suspend () -> R
): R {
    loading.value = true
    return block().also {
        loading.value = false
    }
}

fun Context.simpleLoadingDialog(info: String): AlertDialog {
    val progressBar = ProgressBar(this)
    val dialog = AlertDialog.Builder(this)
        .setView(progressBar)
        .create()
    dialog.setMessage(info)

    return dialog
}

var Dialog.isDismiss
    get() = !isShowing
    set(value) {
        if (!isShowing == value) {
            return
        }
        if (value) {
            dismiss()
        } else {
            show()
        }
    }
var Dialog.isCancel
    get() = !isShowing
    set(value) {
        if (!isShowing == value) {
            return
        }
        if (value) {
            cancel()
        } else {
            show()
        }

    }

