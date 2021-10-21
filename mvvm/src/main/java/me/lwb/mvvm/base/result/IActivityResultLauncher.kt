package me.lwb.mvvm.base.result

import android.app.Activity
import android.content.Intent
import androidx.fragment.app.Fragment
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

/**
 * Created by luowenbin on 2021/10/9.
 */
interface IActivityResultLauncher {
    suspend fun startActivityForResult(intent: Intent, requestCode: Int? = null): ActivityResultBean

    companion object {
        fun create(
            context: Activity,
            resultFlow: Flow<ActivityResultBean>
        ): IActivityResultLauncher = ActivityResultLauncherImpl(context, resultFlow)

        fun create(
            context: Fragment,
            resultFlow: Flow<ActivityResultBean>
        ): IActivityResultLauncher = ActivityResultLauncherImpl(context, resultFlow)

        fun <T> create(t: T) where T : IActivityResultPublisher, T : Activity =
            create(t, t.activityResults)

        fun <T> create(t: T) where T : IActivityResultPublisher, T : Fragment =
            create(t, t.activityResults)

    }

    private class ActivityResultLauncherImpl internal constructor(
        private val context: Any,
        private val resultFlow: Flow<ActivityResultBean>
    ) : IActivityResultLauncher {
        init {
            require(context is Activity || context is Fragment)
        }

        @Suppress("DEPRECATION")
        override suspend fun startActivityForResult(
            intent: Intent,
            requestCode: Int?
        ): ActivityResultBean {
            val reqCode = requestCode ?: globalRequestCode
            when (context) {
                is Activity -> context.startActivityForResult(intent, reqCode)
                is Fragment -> context.startActivityForResult(intent, reqCode)
                else -> throw IllegalStateException("Unable to start activity with context $context")
            }
            return resultFlow.first { it.requestCode == reqCode }
        }

        companion object {
            private var globalRequestCode = 100000000
                get() = field++
        }

    }
}

