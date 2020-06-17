package roadfriend.app.ui.base

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.DialogFragment
import roadfriend.app.R
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.toolbar_layout.*

abstract class BaseDialogFragment : DialogFragment(), IBasePresenter {
    @get:LayoutRes
    abstract val layoutId: Int?

    protected abstract fun initNavigator()

    protected abstract fun initUI()

    protected abstract fun initListener()

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
        if (layoutId != null) {
            return inflater.inflate(layoutId!!, container, false)
        } else {
            return initBinding(inflater, container)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        initListener()
        goTopBar()

    }

    fun goTopBar() {
        if (back != null) {
            back.setOnClickListener {
                dismiss()
            }
        }
    }

    fun toolBarTitle(title: String?) {
        if (tvToolbarTitle != null) {
            tvToolbarTitle.text = title
        }
    }

    override fun onError(message: String, code: Int) {

    }

    override fun onSucces(message: String) {

    }

}