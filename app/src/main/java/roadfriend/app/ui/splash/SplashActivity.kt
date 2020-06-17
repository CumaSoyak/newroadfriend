package roadfriend.app.ui.splash

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Base64
import android.util.Log
import roadfriend.app.BuildConfig
import roadfriend.app.R
import roadfriend.app.ui.base.BaseActivity
import roadfriend.app.ui.dashboard.DashBoardActivity
import roadfriend.app.ui.main.MainActivity
import roadfriend.app.utils.DialogUtils
import roadfriend.app.utils.NetworkUtils
import roadfriend.app.utils.extensions.launchActivity
import roadfriend.app.utils.extensions.showError
import roadfriend.app.utils.firebasedatebase.FirebaseHelper
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

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
            showError("İnternet bağlantınızı kontrol ediniz !")
        }


        try
        {
            val info = getPackageManager().getPackageInfo("roadfriend.app", PackageManager.GET_SIGNATURES)
            for (signature in info.signatures)
            {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.e("MYKEYHASH:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            }
        }
        catch (e:PackageManager.NameNotFoundException) {
        }
        catch (e: NoSuchAlgorithmException) {
        }

    }

    override fun initListener() {
    }

    fun launchActivity() {
        if (BuildConfig.DEBUG) {
            launchActivity<DashBoardActivity>()
        } else {
            launchActivity<MainActivity>()
        }
//        if (PrefUtils.checkIsFirstTimeOpen()) {
//            launchActivity<IntroActivity> { }
//        } else if (!PrefUtils.getToken().isNullOrEmpty()) {
//            launchActivity<MainActivity> {}
//        } else {
//            launchActivity<RegisterActivity> { }
//        }
    }

    fun updateApp() {
        val model = DialogUtils.DialogModel(
            "Daha fazla yük bulabilmen için yeni özelliler eklendi \n Uygulamayı güncelleyiniz",
            "Güncelle",
            "Vazgeç",
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
