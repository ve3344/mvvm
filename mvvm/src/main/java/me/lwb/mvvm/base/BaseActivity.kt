package me.lwb.mvvm.base

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.MutableSharedFlow
import me.lwb.mvvm.base.permission.IPermissionResultPublisher
import me.lwb.mvvm.base.permission.PermissionsResultBean
import me.lwb.mvvm.base.result.ActivityResultBean
import me.lwb.mvvm.base.result.IActivityResultPublisher

@Suppress("unchecked_cast")
open class BaseActivity : AppCompatActivity(),
    IPermissionResultPublisher, IActivityResultPublisher {

    override val permissionResults: MutableSharedFlow<PermissionsResultBean> = MutableSharedFlow()

    override val activityResults: MutableSharedFlow<ActivityResultBean> = MutableSharedFlow()

    var backPressedOverride: () -> Unit = {
        super.onBackPressed()
    }

    override fun onBackPressed() {
        backPressedOverride()
    }


    @Suppress("DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        lifecycleScope.launchWhenCreated {
            activityResults.emit(ActivityResultBean(requestCode, resultCode, data))
        }
    }

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
}