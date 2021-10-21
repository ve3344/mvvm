package me.lwb.mvvm.base

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.MutableSharedFlow
import me.lwb.mvvm.base.permission.IPermissionResultPublisher
import me.lwb.mvvm.base.permission.PermissionsResultBean
import me.lwb.mvvm.base.result.ActivityResultBean
import me.lwb.mvvm.base.result.IActivityResultPublisher

@Suppress("UNCHECKED_CAST")
open class BaseFragment : Fragment,
    IPermissionResultPublisher, IActivityResultPublisher {

    constructor(@LayoutRes layoutId: Int) : super(layoutId)
    constructor() : super()

    override val permissionResults: MutableSharedFlow<PermissionsResultBean> = MutableSharedFlow()
    override val activityResults: MutableSharedFlow<ActivityResultBean> = MutableSharedFlow()

    private var loaded: Boolean = false


    override fun onContextItemSelected(item: MenuItem): Boolean {
        return super.onContextItemSelected(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.isClickable = true
        checkLazyLoad()
    }

    override fun onResume() {
        super.onResume()
        checkLazyLoad()
    }

    @Suppress("DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        lifecycleScope.launchWhenCreated {
            activityResults.emit(ActivityResultBean(requestCode, resultCode, data))
        }

    }

    @Suppress("DEPRECATION")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        lifecycleScope.launchWhenCreated {
            permissionResults.emit(PermissionsResultBean(requestCode, permissions, grantResults))
        }
    }

    private fun checkLazyLoad() {
        if (lifecycle.currentState == Lifecycle.State.STARTED && !loaded) {
            onLazyLoad()
            loaded = true
        }
    }

    open fun onLazyLoad() {}

}