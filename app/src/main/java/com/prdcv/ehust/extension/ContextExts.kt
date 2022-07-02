package com.prdcv.ehust.extension

import android.Manifest
import android.content.Context
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.CompositeMultiplePermissionsListener
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.multi.SnackbarOnAnyDeniedMultiplePermissionsListener


fun Context.checkPermission( view: View, callback: () -> Unit ){

   val compositePermissionsListener = CompositeMultiplePermissionsListener(object: MultiplePermissionsListener{
       override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
           callback.invoke()
       }

       override fun onPermissionRationaleShouldBeShown(
           p0: MutableList<PermissionRequest>?,
           p1: PermissionToken?
       ) {

       }

   }, SnackbarOnAnyDeniedMultiplePermissionsListener.Builder
       .with(view, "Camera and audio access is needed to take pictures of your dog")
       .withOpenSettingsButton("Settings")
       .build()
   )
    Dexter.withContext(this)
        .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)
        .withListener(compositePermissionsListener)
        .check()
}