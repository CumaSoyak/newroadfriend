package roadfriend.app.ui.base

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.fragment.app.DialogFragment
import androidx.viewbinding.ViewBinding
import roadfriend.app.R
import dmax.dialog.SpotsDialog
import roadfriend.app.utils.extensions.observeLiveData


abstract class BaseDialogFragment<VB : ViewBinding, VM : BaseViewModel> : BindingDialogFragment<VB>() {
    abstract val viewModel: VM

    override fun onObserveState() {
        super.onObserveState()
        observeLoadingState()
        observeErrorMessage()
    }

    private fun observeLoadingState() {
        observeLiveData(viewModel.isLoading) { state ->
            state?.let {
                if (it) {
                    showLoading()
                } else {
                    hideLoading()
                }
            }
        }
    }

    private fun observeErrorMessage() {
        observeLiveData(viewModel.errorMessage) { message ->
            message?.let { showErrorMessage(it) }
        }
    }

}