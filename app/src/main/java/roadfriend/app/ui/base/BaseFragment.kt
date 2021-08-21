package roadfriend.app.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import roadfriend.app.R
import roadfriend.app.utils.customscreen.LoadingDialog
import roadfriend.app.utils.extensions.logger
import roadfriend.app.utils.extensions.showError
import roadfriend.app.utils.extensions.showSucces


abstract class BaseFragment<VB : ViewBinding> : Fragment(), IBasePresenter {


    private var _binding: VB? = null

    val binding
        get() = _binding
            ?: throw IllegalStateException(
                "Cannot access view in after view destroyed " +
                        "and before view creation"
            )

    protected var viewId: Int = -1

    protected abstract fun initNavigator()

    protected abstract fun initUI(view: View)

    protected abstract fun initListener()

    abstract fun createBinding(): VB

    private val dialog: AlertDialog? by lazy {
        LoadingDialog.Builder().setContext(context)
            .setCancelable(false)
            .setTheme(R.style.LoadingDialogDefault)
            //.setTheme(R.style.SpotsDialog)
            .build()
    }


    override fun showLoading() {
        try {
            if (dialog != null) {
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
        _binding = createBinding()
        return binding.root
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


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewId = binding.root.id
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
    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}