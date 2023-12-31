package by.zharikov.photosonmap.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageProxy
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import by.zharikov.photosonmap.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*


fun Fragment.showAlert(
    title: Int,
    message: Int,
    positiveButtonResId: Int = R.string.button_yes,
    negativeButtonResId: Int = R.string.negative_button,
    positiveButtonFun: () -> Unit,
    negativeButtonFun: () -> Unit
) {
    MaterialAlertDialogBuilder(requireContext())
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(
            positiveButtonResId
        ) { dialog, _ ->
            positiveButtonFun()
            dialog?.dismiss()
        }
        .setNegativeButton(
            negativeButtonResId
        ) { dialog, _ ->
            negativeButtonFun()
            dialog?.dismiss()
        }
        .show()
}


fun AppCompatActivity.showAlert(
    title: Int,
    message: Int,
    positiveButtonResId: Int = R.string.button_yes,
    negativeButtonResId: Int = R.string.negative_button,
    positiveButtonFun: () -> Unit,
    negativeButtonFun: () -> Unit
) {
    MaterialAlertDialogBuilder(this)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(
            positiveButtonResId
        ) { dialog, _ ->
            positiveButtonFun()
            dialog?.dismiss()
        }
        .setNegativeButton(
            negativeButtonResId
        ) { dialog, _ ->
            negativeButtonFun()
            dialog?.dismiss()
        }
        .show()
}


fun <T> Fragment.collectLatestLifecycleFlow(flow: Flow<T>, collect: suspend (T) -> Unit) {
    viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collectLatest(collect)
        }
    }
}

fun <T> AppCompatActivity.collectLatestLifecycleFlow(flow: Flow<T>, collect: suspend (T) -> Unit) {
    lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collectLatest(collect)
        }
    }
}

fun Long.formatToData(timestamp: Long): String =
    SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault()).format(timestamp * 1000)


fun Fragment.showSnackBar(msg: String, view: View) {
    Snackbar.make(view, msg, Snackbar.LENGTH_SHORT)
        .show()
}

fun ImageCapture.OnImageCapturedCallback.imageProxyToBitmap(image: ImageProxy): Bitmap {
    val buffer = image.planes[0].buffer
    val bytes = ByteArray(buffer.remaining())
    buffer.get(bytes)
    return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
}

fun ImageCapture.OnImageCapturedCallback.bitmapToBase64(bitmap: Bitmap): String {
    val outputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
    val b = outputStream.toByteArray()
    return Base64.encodeToString(b, Base64.DEFAULT)
}

fun Context.hasLocationPermission(): Boolean =
    ContextCompat.checkSelfPermission(
        this, Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED



