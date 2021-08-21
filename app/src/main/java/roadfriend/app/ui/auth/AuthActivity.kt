package roadfriend.app.ui.auth

import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import roadfriend.app.R
import roadfriend.app.databinding.AuthActivityBinding
import roadfriend.app.ui.base.BaseActivity
import roadfriend.app.ui.base.BindingActivity
import roadfriend.app.ui.base.BindingFragment
import roadfriend.app.ui.sales.SalesViewModel

class AuthActivity : BindingActivity<AuthActivityBinding>() {

    override fun createBinding()= AuthActivityBinding.inflate(layoutInflater)
    private val viewModel by viewModel<AuthViewModel>()

    var activeFragment: Fragment? = null
    var registerFragment: Fragment? = null
    var loginFragment: Fragment? = null

    override fun initNavigator() {
        viewModel.setPresenter(this)
    }

    override fun initUI() {

    }

    override fun initListener() {


    }

    fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.frameLayout, fragment)
            .commit()
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        outState.clear()
        super.onSaveInstanceState(outState, outPersistentState)

    }

    override fun onBackPressed() {
        onBackPressedSetResult()
    }

}

