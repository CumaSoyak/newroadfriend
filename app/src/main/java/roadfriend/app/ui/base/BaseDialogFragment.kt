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

abstract class BaseDialogFragment<VB : ViewBinding> : DialogFragment(), IBasePresenter {

    private var _binding: VB? = null

    val binding
        get() = _binding
            ?: throw IllegalStateException(
                "Cannot access view in after view destroyed " +
                        "and before view creation"
            )

    protected var viewId: Int = -1

    protected abstract fun initNavigator()

    protected abstract fun initUI()

    protected abstract fun initListener()

    abstract fun createBinding(): VB

    private val dialog: AlertDialog by lazy {
        SpotsDialog.Builder().setContext(context)
            .setCancelable(false)
            .setTheme(R.style.SpotsDialog)
            .build()
    }

    override fun showLoading() {
        dialog.show()
    }

    override fun hideLoading() {
        dialog.dismiss()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initNavigator()
    }

    open fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): View? {
        return null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = createBinding()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        initListener()
        goTopBar()

    }

    fun goTopBar() {
        if (requireView().findViewById<ImageView>(R.id.back) != null) {
            requireView().findViewById<ImageView>(R.id.back).setOnClickListener {
                dismiss()
            }
        }
    }

    fun toolBarTitle(title: String?) {
        if (requireView().findViewById<TextView>(R.id.tvToolbarTitle) != null) {
            requireView().findViewById<TextView>(R.id.tvToolbarTitle).text = title
        }
    }

    override fun onError(message: String, code: Int) {

    }

    override fun onSucces(message: String) {

    }

}