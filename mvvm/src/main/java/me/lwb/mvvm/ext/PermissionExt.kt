@file:Suppress("unused", "deprecation")

package me.lwb.mvvm.ext

import android.app.Activity
import me.lwb.mvvm.base.permission.IPermissionResultLauncher
import me.lwb.mvvm.base.permission.IPermissionResultPublisher
import me.lwb.utils.android.ext.filterRationalePermissions


suspend fun <T> T.awaitPermissions(
    vararg permissions: String,
): List<String> where T : IPermissionResultPublisher, T : Activity {
    val notGrandPermissions = this.filterRationalePermissions(*permissions)
    if (notGrandPermissions.isEmpty()) {
        return emptyList()
    }
    IPermissionResultLauncher.create(this).requestPermissionsForResult(*permissions)
    return filterRationalePermissions(*permissions)
}
