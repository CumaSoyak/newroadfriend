package roadfriend.app.ui.base

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import roadfriend.app.R
import roadfriend.app.utils.DialogUtils
import roadfriend.app.utils.customscreen.LoadingDialog
import roadfriend.app.utils.extensions.logger
import roadfriend.app.utils.extensions.observeLiveData
import roadfriend.app.utils.extensions.showError
import roadfriend.app.utils.extensions.showSucces


abstract class BaseFragment<VB : ViewBinding, VM : BaseViewModel> : BindingFragment<VB>() {
    abstract val viewModel: VM
    override fun onObserveState() {
        super.onObserveState()
        observeLoadingState()
        observeDialogMessage()
    }

    private fun observeLoadingState() {
        observeLiveData(viewModel.isLoading) { result ->
            if (result == true) {
                showLoading()
            } else {
                hideLoading()
            }
        }
    }

    private fun observeDialogMessage() {
        observeLiveData(viewModel.errorMessage) { message ->
            message?.let {
               // DialogUtils.DialogModel(context = requireContext(), title = it).showErrorDialog()
            }
        }
        observeLiveData(viewModel.succesMessage) { message ->
            message?.let {
               // DialogUtils.DialogModel(context = requireContext(), title = it).showSuccesDialog{}
            }
        }
        observeLiveData(viewModel.errorMessageAlerter) { message ->
           // requireActivity().errorAlerter(message)
        }
        observeLiveData(viewModel.succesMessageAlerter) { message ->
           // requireActivity().succesAlerter(message)
        }
    }
}