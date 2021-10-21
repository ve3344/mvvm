package me.lwb.mvvm.base.result

import kotlinx.coroutines.flow.Flow

/**
 * Created by luowenbin on 2021/9/10.
 */
interface IActivityResultPublisher {
    val activityResults: Flow<ActivityResultBean>
}