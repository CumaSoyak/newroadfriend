package roadfriend.app.ui.auth.login


import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.fragment.app.Fragment
import roadfriend.app.CoreApp
import roadfriend.app.R
import roadfriend.app.data.remote.model.user.User
import roadfriend.app.databinding.LoginActivityBinding
import roadfriend.app.ui.auth.register.RegisterActivity
import roadfriend.app.ui.base.BindingActivity
import roadfriend.app.ui.main.MainActivity
import roadfriend.app.utils.PrefUtils
import roadfriend.app.utils.extensions.*
import roadfriend.app.utils.firebasedatebase.AuthFirebase
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.login.LoginBehavior
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApiClient
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass.
 */
class LoginActivity : BindingActivity<LoginActivityBinding>(),
    GoogleApiClient.OnConnectionFailedListener {

    override fun createBinding() = LoginActivityBinding.inflate(layoutInflater)

    private lateinit var callbackManager: CallbackManager

    private var googleSignInClient: GoogleSignInClient? = null

    private var mGoogleApiClient: GoogleApiClient? = null

    private val viewModel by viewModel<LoginViewModel>()

    override fun initNavigator() {
        viewModel.setPresenter(this)
    }

    override fun initUI() {
        binding.vm = viewModel
    }

    override fun initListener() {
        binding.btnLogin.setOnClickListener {
            viewModel.getPresenter()?.showLoading()
            request()
        }
        binding.btnRegister.setOnClickListener {
            launchActivity<RegisterActivity> { }
            finish()
        }
        binding.tvSkip.setOnClickListener {
            nextMainActivity()
        }
        binding.ivFacebook.setOnClickListener {
            viewModel.getPresenter()?.showLoading()
            binding.buttonFacebookNative.performClick()
        }
        binding.ivGoogle.setOnClickListener {
            viewModel.getPresenter()?.showLoading()
            googleSocialLogin()
        }
        setUpFacebook()

        setUpGoogle()

    }

    fun request() {
        val mail =binding. etEmail.textString()
        val password =binding. etPassword.textString()

        AuthFirebase().getUserLogin(mail, password) { user ->
            if (user == null) {
                viewModel.getPresenter()?.hideLoading()
                showError("E-posta veya parola yanlış")
            } else {
                nextMainActivity()
            }
        }
    }


    fun setUpFacebook() {
        callbackManager = CallbackManager.Factory.create()
        binding.buttonFacebookNative.setReadPermissions(listOf("email"))
        binding.buttonFacebookNative.loginBehavior = LoginBehavior.NATIVE_WITH_FALLBACK
        binding.buttonFacebookNative.registerSimpleCallback(callbackManager) {
            val accessToken = AccessToken.getCurrentAccessToken()
            getFacebookUserInfo(accessToken) { name, email ->
                val imageURL =
                    "https://graph.facebook.com/" + accessToken.userId + "/picture?type=normal"
                val user = User(
                    "",
                    name!!,
                    email!!,
                    "facebook",
                    imageURL,
                    0,
                    PrefUtils.getFirebaseToken()
                )
                AuthFirebase().getUserSocialLogin(user) {
                    nextMainActivity()
                }

            }

        }
    }

    fun nextMainActivity() {
        viewModel.getPresenter()?.hideLoading()
        if (CoreApp.notLogin) {
            finish()
        } else {
            launchActivity<MainActivity> {
                this.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                this.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            //GoogleLogin
            when (requestCode) {
                101 ->
                    try {
                        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                        val account = task.getResult(ApiException::class.java)
                        account?.let { googleLoggedIn(it) }
                    } catch (e: ApiException) {
                        viewModel.getPresenter()?.hideLoading()
                        Log.d("GoogleLoginResult", e.statusCode.toString())
                    }
            }
        }
    }

    override fun onConnectionFailed(p0: ConnectionResult) {}

    fun googleSocialLogin() {
        val signInIntent: Intent? = googleSignInClient?.signInIntent
        startActivityForResult(signInIntent, 101)
    }

    fun setUpGoogle() {
        val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(resources.getString(R.string.web_client_id))
            .requestEmail()
            .build()

        mGoogleApiClient = GoogleApiClient.Builder(this)
            .addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions)
            .addOnConnectionFailedListener(this)
            .build()
        mGoogleApiClient?.connect()

        googleSignInClient = GoogleSignIn.getClient(this, signInOptions)
    }

    fun googleLoggedIn(account: GoogleSignInAccount) {
        val imageURL = account.photoUrl
        val user = User(
            "",
            account.displayName!!,
            account.email!!,
            "google",
            imageURL.toString(),
            0,
            PrefUtils.getFirebaseToken()
        )
        AuthFirebase().getUserSocialLogin(user) {
            nextMainActivity()
        }
    }
}
