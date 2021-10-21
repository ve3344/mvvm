package me.lwb.mvvm.base

import android.app.Activity
import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import java.lang.ref.WeakReference
import java.util.*


object GlobalActivityManager {
    private var installed = false
    private val activityStack: Stack<WeakReference<Activity>> by lazy { Stack() }

    private fun addActivity(activity: Activity) = activityStack.add(WeakReference(activity))

    private fun removeActivity(activity: Activity) =
        activityStack.removeAll { it.get() == activity }

    val activityCount: Int
        get() = activityStack.size

    val hasActivity: Boolean
        get() = activityStack.isNotEmpty()

    val currentActivity: Activity?
        get() = activityStack.lastElement()?.get()

    fun finishCurrentActivity() = finishActivity(currentActivity)

    fun finishActivity(activity: Activity?) {
        activity ?: return
        if (!activity.isFinishing && !activity.isDestroyed) {
            activity.finish()
        }
    }

    /**
     * 结束指定类名的Activity
     */
    fun finishActivity(cls: Class<out Activity>) = finishActivity(getActivity(cls))

    fun activitySequence() = activityStack.asSequence().mapNotNull(WeakReference<Activity>::get)

    /**
     * 结束所有Activity
     */
    fun finishAllActivity() {
        activitySequence().forEach { finishActivity(it) }
        activityStack.clear()
    }

    /**
     * 获取指定的Activity
     */
    @Suppress("UNCHECKED_CAST")
    fun <A : Activity> getActivity(cls: Class<A>): A? =
        activityStack.firstOrNull { it.javaClass == cls }?.get() as? A


    fun install(application: Application) {
        if (installed) {
            return
        }
        installed = true
        application.registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                addActivity(activity)
            }

            override fun onActivityStarted(activity: Activity) {}
            override fun onActivityResumed(activity: Activity) {}
            override fun onActivityPaused(activity: Activity) {}
            override fun onActivityStopped(activity: Activity) {}
            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
            override fun onActivityDestroyed(activity: Activity) {
                removeActivity(activity)
            }
        })
    }
}