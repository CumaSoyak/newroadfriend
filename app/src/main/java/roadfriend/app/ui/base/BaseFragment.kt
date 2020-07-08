package roadfriend.app.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.toolbar_layout.*
import roadfriend.app.R
import roadfriend.app.utils.customscreen.LoadingDialog
import roadfriend.app.utils.extensions.logger
import roadfriend.app.utils.extensions.showError
import roadfriend.app.utils.extensions.showSucces


abstract class BaseFragment : Fragment(), IBasePresenter {


    @get:LayoutRes
    protected abstract val layoutId: Int?

    protected abstract fun initNavigator()

    protected abstract fun initUI(view: View)

    protected abstract fun initListener()

    private val dialog: AlertDialog? by lazy {
        LoadingDialog.Builder().setContext(context)
            .setCancelable(false)
            .setTheme(R.style.LoadingDialogDefault)
            //.setTheme(R.style.SpotsDialog)
            .build()
    }


    override fun showLoading() {
        try {
            if (!requireActivity().isFinishing && dialog != null) {
                dialog?.show()
            }
        } catch (e: WindowManager.BadTokenException) {
            logger("" + e.localizedMessage)
        }

    }


    override fun hideLoading() {
        dialog?.dismiss()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initNavigator()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // logLifecycleEvents("onCreateView")
        if (layoutId != null) {
            return inflater.inflate(layoutId!!, container, false)
        } else {
            return initBinding(inflater, container)
        }
    }

    private fun logLifecycleEvents(lifeCycleName: String) {
    }

    open fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): View? {
        return null
    }

    fun replaceFragment(fragment: Fragment) {
        var bundle = Bundle()
        bundle.putString("maps", "page")
        fragment.arguments = bundle
        fragmentManager?.beginTransaction()?.replace(R.id.frameLayout, fragment)?.commit()
    }

    fun toolBarTitle(title: String) {
        tvToolbarTitle.text = title
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI(view)
        initListener()
    }

    override fun onError(errorMessage: String, errorCode: Int) {
        showError(errorMessage)
    }

    override fun onSucces(message: String) {
        showSucces(message)
    }

    override fun onPause() {
        super.onPause()
        dialog?.dismiss()
    }

    override fun onStop() {
        super.onStop()
        dialog?.dismiss()
    }

    override fun onDestroy() {
        super.onDestroy()
        dialog?.dismiss()
    }
}