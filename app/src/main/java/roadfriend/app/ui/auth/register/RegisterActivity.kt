package roadfriend.app.ui.auth.register


import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResultCallback
import com.google.android.gms.common.api.Status
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.register_activity.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import roadfriend.app.CoreApp
import roadfriend.app.R
import roadfriend.app.data.remote.model.user.User
import roadfriend.app.databinding.RegisterActivityBinding
import roadfriend.app.ui.auth.login.LoginActivity
import roadfriend.app.ui.base.BindingActivity
import roadfriend.app.ui.main.MainActivity
import roadfriend.app.ui.profile.help.HelpActivity
import roadfriend.app.utils.OtherUtils
import roadfriend.app.utils.PrefUtils
import roadfriend.app.utils.extensions.*
import roadfriend.app.utils.extensions.ImagePickerActivity.Companion.REQUEST_IMAGE
import roadfriend.app.utils.firebasedatebase.AuthFirebase
import roadfriend.app.utils.firebasedatebase.FirebaseHelper
import java.io.IOException
import java.util.*
import java.util.regex.Pattern

/**
 * A simple [Fragment] subclass.
 */
class RegisterActivity : BindingActivity<RegisterActivityBinding>(),
    ImagePickerActivity.PickerOptionListener,
    GoogleApiClient.OnConnectionFailedListener {
    override val getLayoutBindId: Int
        get() = R.layout.register_activity

    private val viewModel by viewModel<RegisterViewMoel>()
    var uri: Uri? = null
    private var imageReference: StorageReference? = null
    private var callbackManager: CallbackManager? = null

    lateinit var googleSignInClient: GoogleSignInClient

    private var mGoogleApiClient: GoogleApiClient? = null


    override fun initNavigator() {
        viewModel.setPresenter(this)
    }

    override fun initUI() {
        binding.vm = viewModel
        setContract()
        facebook()
        setUpGoogle()
        initData()
    }

    fun initData() {
        if (intent.hasExtra("when")) {
            showError("Mesaj yazabilmek için kaydolmalısınız")
        }
        if (intent.hasExtra("ilan")) {
            showError("İlan paylaşabilmek için kaydolmalısınız")
        }
        if (intent.hasExtra("default")) {
            showError("Lütfen üye olunuz.")
        }
    }

    override fun initListener() {
        binding.btnRegister.setOnClickListener {
            if (inputValidate()) {
                requestRegisterUser()
            }
        }
        binding.ivGoogle.setOnClickListener {
            viewModel.getPresenter()?.showLoading()
            googleSocialLogin()
        }
        binding.btnLogin.setOnClickListener {
            launchActivity<LoginActivity> { }
            finish()
        }
        binding.llSelectPhoto.setOnClickListener {
            ImagePickerActivity().openCameraOrGalery(this, this)
        }
        binding.tvSkip.setOnClickListener {
            launchActivity<MainActivity> { }
        }
        binding.ivFacebook.setOnClickListener {
            viewModel.getPresenter()?.showLoading()
            binding.buttonFacebookNative.performClick()
        }
    }


    override fun onChooseGallerySelected() {
        val intent = Intent(this, ImagePickerActivity::class.java)
        intent.putExtra(
            ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION,
            ImagePickerActivity.REQUEST_GALLERY_IMAGE
        )
        startActivityForResult(intent, REQUEST_IMAGE)
    }

    override fun onTakeCameraSelected() {
        val intent = Intent(this, ImagePickerActivity::class.java)
        intent.putExtra(
            ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION,
            ImagePickerActivity.REQUEST_IMAGE_CAPTURE
        )
        startActivityForResult(intent, REQUEST_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE && data != null) {
            if (resultCode == Activity.RESULT_OK) {
                uri = data.getParcelableExtra<Uri>("path")
                try {
                    if (uri != null) {
                        ivUserImage.visible()
                        llSelectPhoto.gone()
                        ivUserImage.load(uri.toString())
                    }

                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }
        if (requestCode == 101) {
            //GoogleLogin
            try {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                val account = task.getResult(ApiException::class.java)
                account?.let { googleLoggedIn(it) }

            } catch (e: ApiException) {
                viewModel.getPresenter()?.hideLoading()
                showError(e.message.toString())
                logger(e.message.toString())
            }
        } else {
            callbackManager?.onActivityResult(requestCode, resultCode, data)
        }
        super.onActivityResult(requestCode, resultCode, data)


    }

    private fun requestRegisterUser() {
        if (uri != null) {
            viewModel.getPresenter()?.showLoading()
            FirebaseHelper().getUserRegister(etEmail.textString()) { isAvailable ->
                if (isAvailable) {
                    viewModel.getPresenter()?.hideLoading()
                    showSucces("Bu kişi vardır")
                } else {
                    photoUpload(uri!!)
                }
            }

        } else {
            showError("Fotoğraf seçiniz lütfen :)")
        }
    }

    fun photoUpload(path: Uri) {
        imageReference = FirebaseStorage.getInstance().reference.child("images")
        val fileRef = imageReference!!.child("image" + UUID.randomUUID().toString())
        fileRef.putFile(path)
            .addOnSuccessListener { taskSnapshot ->

                viewModel.getPresenter()?.hideLoading()

                fileRef.downloadUrl.addOnSuccessListener { it ->

                    AuthFirebase().newUserCreate(
                        User(
                            "",
                            etFullName.textString(),
                            etEmail.textString(),
                            etPassword.textString(),
                            it.toString(),
                            0,
                            PrefUtils.getFirebaseToken(), country = OtherUtils.getCountryCode()
                        )
                        , succes = {
                            nextMainActivity()
                        }
                    )
                }
            }
            .addOnFailureListener { exception ->
                viewModel.getPresenter()?.hideLoading()
                showError("Lütfen tekrar deneyiniz.")
            }
            .addOnProgressListener { taskSnapshot ->
                viewModel.getPresenter()?.showLoading()
            }
            .addOnPausedListener { viewModel.getPresenter()?.hideLoading() }
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

    fun inputValidate(): Boolean {
        if (etFullName.textString().isNullOrEmpty() || !Pattern.matches(
                "[A-Za-zçıüğöşİĞÜÖŞÇ]{2,}+\\s[A-Za-zçıüğöşİĞÜÖŞÇ]{2,}",
                etFullName.text
            )
        ) {
            etFullName.error = "Ad soyad arasında boşluk olmalıdır"
            return false
        } else if (etEmail.textString().isNullOrEmpty() || !etEmail.textString().contains("@")) {
            etEmail.error = "E-mail adresi yanlış"
            return false
        } else if (etPassword.textString().isNullOrEmpty()) {
            etPassword.error = "Parola giriniz"
            return false
        } else {
            return true
        }
    }


    private fun setContract() {

        val ss = SpannableString(resources.getString(R.string.contract))
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {

                launchActivity<HelpActivity> {
                    this.putExtra("kosul", "kosul")
                }
                overridePendingTransitionEnter()
            }

            @RequiresApi(Build.VERSION_CODES.M)
            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = true
                ds.color = resources.getColor(R.color.colorTextColor)
                ds.isFakeBoldText = true
            }
        }

        ss.setSpan(clickableSpan, 13, 33, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        tvContract.text = ss
        tvContract.movementMethod = LinkMovementMethod.getInstance()
        tvContract.setTextColor(resources.getColor(R.color.border))
    }

    override fun onConnectionFailed(p0: ConnectionResult) {}

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

    fun googleSocialLogin() {
        val signInIntent: Intent? = googleSignInClient?.signInIntent
        startActivityForResult(signInIntent, 101)
    }

    fun googleLogOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient)
            .setResultCallback(object : ResultCallback<Status> {
                override fun onResult(p0: Status) {
                    Log.i("GoogleLogin", "" + p0.statusMessage)
                }

            })
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
            PrefUtils.getFirebaseToken(), country = OtherUtils.getCountryCode()
        )
        AuthFirebase().getUserSocialLogin(user) {
            nextMainActivity()
        }
    }

    fun facebook() {
        callbackManager = CallbackManager.Factory.create()
        binding.buttonFacebookNative.setReadPermissions(listOf("email"))
        binding.buttonFacebookNative.registerCallback(
            callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
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
                            PrefUtils.getFirebaseToken(), country = OtherUtils.getCountryCode()
                        )
                        AuthFirebase().getUserSocialLogin(user) {
                            nextMainActivity()
                        }
                    }

                }

                override fun onCancel() {
                    viewModel.getPresenter()?.hideLoading()
                }

                override fun onError(exception: FacebookException) {
                    viewModel.getPresenter()?.hideLoading()
                }
            })

    }


}
