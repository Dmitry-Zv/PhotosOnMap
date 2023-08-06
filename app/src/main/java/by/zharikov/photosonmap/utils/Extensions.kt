package by.zharikov.photosonmap.utils

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


//fun Fragment.showAlert(
//    title: Int,
//    view: View,
//    positiveButtonResId: Int = R.string.positive_button,
//    negativeButtonResId: Int = R.string.negative_button,
//    positiveButtonFun: () -> Unit,
//    negativeButtonFun: () -> Unit
//) {
//    MaterialAlertDialogBuilder(this)
//        .setTitle(title)
//        .setView(view)
//        .setPositiveButton(
//            positiveButtonResId
//        ) { dialog, _ ->
//            positiveButtonFun()
//            dialog?.dismiss()
//        }
//        .setNegativeButton(
//            negativeButtonResId
//        ) { dialog, _ ->
//            negativeButtonFun()
//            dialog?.dismiss()
//        }
//        .show()
//}
//


fun <T> Fragment.collectLatestLifecycleFlow(flow: Flow<T>, collect: suspend (T) -> Unit) {
    viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collectLatest(collect)
        }
    }
}



fun Fragment.showSnackBar(msg: String, view: View) {
    Snackbar.make(view, msg, Snackbar.LENGTH_SHORT)
        .show()
}