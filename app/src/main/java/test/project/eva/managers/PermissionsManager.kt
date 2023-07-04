package test.project.eva.managers

import android.Manifest
import android.app.Activity
import androidx.core.app.ActivityCompat

class PermissionsManager(private val activity: Activity) {

    companion object {
        private const val PERMISSION_REQUEST_CODE = 1
    }

    fun requestPermissions() {
        val permissionList = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        ActivityCompat.requestPermissions(activity, permissionList, PERMISSION_REQUEST_CODE)
    }
}