package com.prdcv.ehust.utils.extension

import android.Manifest
import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.ContextWrapper
import android.net.Uri
import android.provider.OpenableColumns
import android.view.View
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.CompositeMultiplePermissionsListener
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.multi.SnackbarOnAnyDeniedMultiplePermissionsListener
import java.io.File


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

fun Context.getFileName(uri: Uri): String? = when(uri.scheme) {
    ContentResolver.SCHEME_CONTENT -> getContentFileName(uri)
    else -> uri.path?.let(::File)?.name
}

private fun Context.getContentFileName(uri: Uri): String? = runCatching {
    contentResolver.query(uri, null, null, null, null)?.use { cursor ->
        cursor.moveToFirst()
        return@use cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME).let(cursor::getString)
    }
}.getOrNull()

fun Context.openInputStream(uri: Uri) = contentResolver.openInputStream(uri)

fun Context.getType(uri: Uri) = contentResolver.getType(uri)

fun Context.findActivity(): Activity {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    throw IllegalStateException("should be called in the context of an Activity")
}