package roadfriend.app.ui.base

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import roadfriend.app.R
import roadfriend.app.utils.NetworkUtils
import roadfriend.app.utils.customscreen.LoadingDialog
import roadfriend.app.utils.extensions.observeLiveData
import roadfriend.app.utils.extensions.overridePendingTransitionExit
import roadfriend.app.utils.extensions.showError
import roadfriend.app.utils.extensions.showSucces
import java.lang.ref.WeakReference


abstract class BaseActivity<VB : ViewBinding, VM : BaseViewModel> : BindingActivity<VB>() {

    private val loadingDialog: AlertDialog by lazy {
        LoadingDialog.Builder().setContext(this)
            .setCancelable(false)
            .setTheme(R.style.LoadingDialogDefault)
            //.setTheme(R.style.SpotsDialog)
            .build()
    }
    abstract val viewModel: VM
    var customBackAction:(()->Unit)? = null

    override fun onBackPressed() {
        customBackAction?.let {
            it.invoke()
            return
        }
        super.onBackPressed()
    }

    override fun showLoading() {
        super.showLoading()
        loadingDialog?.show()
    }

    override fun hideLoading() {
        super.hideLoading()
        loadingDialog?.dismiss()
    }

    override fun showErrorMessage(message: String) {
        super.showErrorMessage(message)
     }

    override fun onObserveState() {
        super.onObserveState()
        observeLoadingState()
        observeErrorMessage()
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

    private fun observeErrorMessage() {
        observeLiveData(viewModel.errorMessage) { message ->
            message?.let { showErrorMessage(it) }
        }
    }

}
