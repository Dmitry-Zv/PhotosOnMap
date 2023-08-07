package by.zharikov.photosonmap.utils

import android.view.View
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
import java.text.SimpleDateFormat
import java.util.*


fun Fragment.showAlert(
    title: Int,
    message:Int,
    positiveButtonResId: Int = R.string.positive_button,
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



fun <T> Fragment.collectLatestLifecycleFlow(flow: Flow<T>, collect: suspend (T) -> Unit) {
    viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collectLatest(collect)
        }
    }
}

fun Long.formatToData(timestamp: Long): String =
    SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(timestamp)


fun Fragment.showSnackBar(msg: String, view: View) {
    Snackbar.make(view, msg, Snackbar.LENGTH_SHORT)
        .show()
}