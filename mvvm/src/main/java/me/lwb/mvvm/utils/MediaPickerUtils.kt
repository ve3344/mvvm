package me.lwb.mvvm.utils

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.annotation.RequiresPermission
import androidx.core.net.toUri
import me.lwb.mvvm.base.result.IActivityResultLauncher
import me.lwb.utils.android.ext.setDataAndType
import me.lwb.utils.android.ext.targetPath
import me.lwb.utils.android.utils.CacheFileUtils
import me.lwb.utils.android.utils.androidUri
import java.io.File

object MediaPickerUtils {

    @RequiresPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    suspend fun choseImageGallery(launcher: IActivityResultLauncher): String? {
        val intentFromGallery = Intent(ACTION_PICK, null)
        intentFromGallery.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
        val ret = launcher.startActivityForResult(intentFromGallery)

        if (ret.resultCode == Activity.RESULT_OK) {
            return ret.data?.data?.targetPath()
        }
        return null

    }

    @RequiresPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    suspend fun choseVideoGallery(launcher: IActivityResultLauncher): String? {
        val intentFromGallery = Intent(ACTION_PICK, null)
        intentFromGallery.setDataAndType(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, "video/*")
        val ret = launcher.startActivityForResult(intentFromGallery)
        val uri = ret.data?.data ?: return null
        if (ret.resultCode == Activity.RESULT_OK) {
            return uri.targetPath()
        }
        return null

    }

    @RequiresPermission(Manifest.permission.CAMERA)
    suspend fun choseImageCamera(launcher: IActivityResultLauncher): String? {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        val storeFile = CacheFileUtils.createCacheFile("picker", ".jpg")
        val pictureUri: Uri = storeFile.androidUri()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, pictureUri)
        val ret = launcher.startActivityForResult(intent)
        if (ret.resultCode == Activity.RESULT_OK) {
            return storeFile.absolutePath
        }
        return null
    }


    suspend fun cropPhoto(
        launcher: IActivityResultLauncher,
        file: String,
        cropOption: CropOption = CropOption()
    ): String? {
        val storeFile = CacheFileUtils.createCacheFile("picker", ".jpg")
        val intent = Intent(ACTION_CROP)

        intent.setDataAndType(File(file), "image/*", true)
        intent.putExtra("crop", "true")
        intent.putExtra("return-data", false) //太占内存，不返回
        intent.putExtra(MediaStore.EXTRA_OUTPUT, storeFile.toUri())

        cropOption.apply {
            if (aspectX > 0 && aspectY > 0) {
                intent.putExtra("aspectX", aspectX)
                intent.putExtra("aspectY", aspectY)
            }
            if (outputX > 0 && outputY > 0) {
                intent.putExtra("outputX", outputX)
                intent.putExtra("outputY", outputY)
            }
            intent.putExtra("scale", scale) //保持比例，去除黑边
            intent.putExtra("scaleUpIfNeeded", scaleUpIfNeeded) //去除黑边
            intent.putExtra("outputFormat", outputFormat)
        }
        val ret = launcher.startActivityForResult(intent)
        if (ret.resultCode == Activity.RESULT_OK) {
            return storeFile.absolutePath
        }
        return null
    }

    @RequiresPermission(Manifest.permission.CAMERA)
    suspend fun choseImageCameraCrop(
        launcher: IActivityResultLauncher,
        cropOption: CropOption = CropOption()
    ): String? {
        return choseImageCamera(launcher)?.let { cropPhoto(launcher, it, cropOption) }
    }

    @RequiresPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    suspend fun choseImageGalleryCrop(
        launcher: IActivityResultLauncher,
        cropOption: CropOption = CropOption()
    ): String? {
        return choseImageGallery(launcher)?.let { cropPhoto(launcher, it, cropOption) }
    }

    private const val ACTION_PICK: String = Intent.ACTION_GET_CONTENT
    private const val ACTION_CROP: String = "com.android.camera.action.CROP"

    data class CropOption(
        val aspectX: Int = 0,
        val aspectY: Int = 0,
        val outputX: Int = 0,
        val outputY: Int = 0,
        val scale: Boolean = true,
        val scaleUpIfNeeded: Boolean = true,
        val outputFormat: String = Bitmap.CompressFormat.JPEG.toString(),
    )


}