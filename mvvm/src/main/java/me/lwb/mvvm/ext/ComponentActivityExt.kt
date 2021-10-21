package me.lwb.mvvm.ext

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import kotlinx.coroutines.suspendCancellableCoroutine
import me.lwb.mvvm.base.result.ActivityResultBean
import me.lwb.mvvm.base.result.IActivityResultLauncher
import kotlin.coroutines.resume

/**
 * Created by luowenbin on 2021/10/19.
 */

fun IActivityResultLauncher.Companion.create(activity: ComponentActivity): IActivityResultLauncher =
    ActivityResultLauncherImpl(activity)

internal class ActivityResultLauncherImpl internal constructor(
    private val activity: ComponentActivity
) : IActivityResultLauncher {


    @Suppress("DEPRECATION")
    override suspend fun startActivityForResult(
        intent: Intent,
        requestCode: Int?
    ): ActivityResultBean = suspendCancellableCoroutine { c ->
        activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            c.resume(ActivityResultBean(requestCode ?: 0, it.resultCode, it.data))
        }.launch(intent)
    }

}

