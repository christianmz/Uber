package com.meazza.uber.vo

import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment

class PermissionsRequester(
    private val fragment: Fragment,
    private val permissions: Array<String>,
    private val onRationale: () -> Unit = {},
    private val onDenied: () -> Unit = {},
) {

    private var onGranted: () -> Unit = {}

    private fun permissionLauncher(): ActivityResultLauncher<String> =
        fragment.registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            when {
                isGranted -> onGranted()
                fragment.shouldShowRequestPermissionRationale(permissions[0]) -> onRationale()
                else -> onDenied()
            }
        }

    private fun permissionsLauncher(): ActivityResultLauncher<Array<String>> =
        fragment.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.entries.forEach {

                val permission = it.key
                val isGranted = it.value

                when {
                    isGranted -> onGranted()
                    fragment.shouldShowRequestPermissionRationale(permission) -> onRationale()
                    else -> onDenied()
                }
            }
        }

    fun runWithPermission(body: () -> Unit) {
        onGranted = body
        permissionLauncher().launch(permissions[0])
    }

    fun runWithMultiplePermissions(body: () -> Unit) {
        onGranted = body
        permissionsLauncher().launch(permissions)
    }
}