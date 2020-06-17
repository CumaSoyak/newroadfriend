package roadfriend.app.ui.auth

import android.os.Bundle
import android.os.PersistableBundle
import androidx.fragment.app.Fragment
import roadfriend.app.R
import roadfriend.app.ui.base.BaseActivity

class AuthActivity : BaseActivity() {
    override val layoutId: Int?
        get() = R.layout.auth_activity

    var activeFragment: Fragment? = null
    var registerFragment: Fragment? = null
    var loginFragment: Fragment? = null

    override fun initNavigator() {
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

