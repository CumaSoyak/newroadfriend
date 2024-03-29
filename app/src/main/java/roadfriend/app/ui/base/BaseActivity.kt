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
import roadfriend.app.utils.extensions.overridePendingTransitionExit
import roadfriend.app.utils.extensions.showError
import roadfriend.app.utils.extensions.showSucces
import java.lang.ref.WeakReference


abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity(), IBasePresenter {


    lateinit var binding: VB

    abstract fun createBinding(): VB

    protected abstract fun initNavigator()

    protected abstract fun initUI()

    protected abstract fun initListener()

    private val dialog: AlertDialog by lazy {
        LoadingDialog.Builder().setContext(this)
            .setCancelable(false)
            .setTheme(R.style.LoadingDialogDefault)
            //.setTheme(R.style.SpotsDialog)
            .build()
    }

    companion object {
        lateinit var activityWeakReference: WeakReference<Activity>

        fun setActivity(activity: Activity) {
            activityWeakReference = WeakReference<Activity>(activity)
        }

        fun getActivity(): Activity {
            return activityWeakReference as Activity
        }
    }


    override fun showLoading() {
        if (NetworkUtils.isConnected(this))
            dialog.show()
        else {
            showError("İnternet bağlantısını kontrol ediniz")
        }
    }

    override fun hideLoading() {
        dialog.dismiss()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (::binding.isInitialized.not()) {
            binding = createBinding()
            setContentView(binding.root)
        }
        initNavigator()
        initUI()
        initListener()
        toolbarInit()

        Log.d("LIFECYCLE", "onCreate")
    }

    fun toolbarInit() {
        if (findViewById<ImageView>(R.id.back) != null) {
            findViewById<ImageView>(R.id.back).setOnClickListener {
                overridePendingTransitionExit()
                finish()
            }
        }
    }

    fun toolBarTitle(title: String?) {
        if (findViewById<TextView>(R.id.tvToolbarTitle) != null) {
            findViewById<TextView>(R.id.tvToolbarTitle).text = title
        }
    }

    override fun onError(errorMessage: String, errorCode: Int) {
        showError(errorMessage)
    }

    override fun onSucces(message: String) {
        showSucces(message)
    }


    override fun onResume() {
        super.onResume()
        Log.d("LIFECYCLE", "onResume")
    }

    override fun onPause() {
        super.onPause()
        dialog.dismiss()
    }

    override fun onStop() {
        super.onStop()
        dialog.dismiss()
    }

    override fun onDestroy() {
        super.onDestroy()
        dialog.dismiss()

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        overridePendingTransitionExit()
    }

    fun onBackPressedSetResult() {
        val intent = Intent()
        setResult(0, intent)
        finish()
        overridePendingTransitionExit()
    }


}

