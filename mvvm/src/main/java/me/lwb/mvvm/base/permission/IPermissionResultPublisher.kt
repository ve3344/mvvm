package me.lwb.mvvm.base.permission

import kotlinx.coroutines.flow.Flow

/**
 * Created by luowenbin on 2021/9/10.
 */
interface IPermissionResultPublisher {
    val permissionResults: Flow<PermissionsResultBean>
}