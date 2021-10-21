package me.lwb.mvvm.base.permission

import android.app.Activity
import androidx.core.app.ActivityCompat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import me.lwb.utils.android.ext.filterRationalePermissions

/**
 * Created by luowenbin on 2021/10/18.
 */
interface IPermissionResultLauncher {
    suspend fun requestPermissionsForResult(
        vararg permissions: String,
        requestCode: Int? = null
    ): PermissionsResultBean

    companion object {
        fun create(
            context: Activity,
            resultFlow: Flow<PermissionsResultBean>
        ): IPermissionResultLauncher =
            PermissionResultLauncherImpl(context, resultFlow)


        fun <T> create(t: T) where T : IPermissionResultPublisher, T : Activity =
            create(t, t.permissionResults)

    }

    private class PermissionResultLauncherImpl(
        private val context: Activity,
        private val resultFlow: Flow<PermissionsResultBean>
    ) : IPermissionResultLauncher {
        companion object {
            private var globalRequestCode = 200000000
                get() = field++
        }

        override suspend fun requestPermissionsForResult(
            vararg permissions: String, requestCode: Int?
        ): PermissionsResultBean {
            val reqCode = requestCode ?: globalRequestCode

            context.filterRationalePermissions(*permissions)
            ActivityCompat.requestPermissions(context, permissions, reqCode)
            return resultFlow.first { it.requestCode == reqCode }
        }

    }


}