package roadfriend.app.ui.splash

import android.content.Intent
import android.net.Uri
import org.koin.androidx.viewmodel.ext.android.viewModel
import roadfriend.app.BuildConfig
import roadfriend.app.R
import roadfriend.app.ui.base.BaseActivity
import roadfriend.app.ui.dashboard.DashBoardActivity
import roadfriend.app.ui.intro.IntroActivity
import roadfriend.app.ui.main.MainActivity
import roadfriend.app.ui.search.SearchStatusActivity
import roadfriend.app.utils.DialogUtils
import roadfriend.app.utils.NetworkUtils
import roadfriend.app.utils.PrefUtils
import roadfriend.app.utils.extensions.launchActivity
import roadfriend.app.utils.extensions.showError
import roadfriend.app.utils.firebasedatebase.FirebaseHelper

class SplashActivity : BaseActivity() {
    override val layoutId: Int?
        get() = null

    private val viewModel by viewModel<SplashViewModel>()

    override fun initNavigator() {
        viewModel.setPresenter(this)
    }

    override fun initUI() {
        if (NetworkUtils.isConnected(this)) {
             FirebaseHelper().isAppUpdate { update ->
                if (update) {
                    updateApp()
                } else {
                    launchActivity()
                }
            }
        } else {
            showError(getString(R.string.internet))
        }
    }

    override fun initListener() {
    }

    fun launchActivity() {
        if (BuildConfig.DEBUG) {
            launchActivity<DashBoardActivity>()
        } else if (PrefUtils.checkIsFirstTimeOpen()) {
            launchActivity<IntroActivity> { }
        } else if (!PrefUtils.checkIsFirstTimeOpen()) {
            launchActivity<SearchStatusActivity> {}
        }
    }

    fun updateApp() {
        val model = DialogUtils.DialogModel(
            resources.getString(R.string.update_title),
            getString(R.string.buton_guncelle),
            getString(R.string.buton_vazgec),
            R.drawable.ic_alert,
            false
        )
        DialogUtils.showPopupModel(this, model, object : DialogUtils.DialogListener {
            override fun onNegativeClick() {

            }

            override fun onPositiveClick() {
                openGooglePlay()
            }
        })
    }

    fun openGooglePlay() {
        val appPackageName = getPackageName()
        try {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=" + appPackageName)
                )
            )
        } catch (anfe: android.content.ActivityNotFoundException) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)
                )
            )
        }
    }


}
