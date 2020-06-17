package roadfriend.app.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import roadfriend.app.CoreApp
import roadfriend.app.R
import roadfriend.app.ui.add.AddActivity
import roadfriend.app.ui.auth.login.LoginActivity
import roadfriend.app.ui.homemain.HomeMainFragment
import roadfriend.app.ui.message.MessageFragment
import roadfriend.app.ui.notification.NotificationFragment
import roadfriend.app.ui.profile.ProfilFragment
import roadfriend.app.utils.PrefUtils.isLogin
import roadfriend.app.utils.extensions.overridePendingTransitionEnter
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    var activeFragment: Fragment? = null
    var homeFragment: Fragment? = null
    var notificationFragment: Fragment? = null
    var bidFragment: Fragment? = null
    var profilFragment: Fragment? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupBottomNavigationBar()
    }

    private fun setupBottomNavigationBar() {
//        var badge = bottom_nav.getOrCreateBadge(R.id.nav_notification)


        bottom_nav.setOnNavigationItemSelectedListener { menuItem ->
            var selectedFragment: Fragment? = null
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    if (homeFragment == null) {
                        homeFragment = HomeMainFragment.newInstance()
                        addFragmentMethod(homeFragment!!)
                        activeFragment = homeFragment
                    }
                    selectedFragment = homeFragment
                }
                R.id.nav_notification -> {
                    if (isLogin()) {
                        //      bottom_nav.removeBadge(R.id.nav_notification)
                        if (notificationFragment == null) {
                            notificationFragment = NotificationFragment.newInstance()
                            addFragmentMethod(notificationFragment!!)

                        }
                        selectedFragment = notificationFragment
                    } else {
                        openAuthActivity()
                    }

                }
                R.id.nav_add -> {
                    if (isLogin()) {
                        selectedFragment = null
                        val intent = Intent(this, AddActivity::class.java)
                        startActivityForResult(intent, 0)
                        overridePendingTransitionEnter()
                    } else {
                        openAuthActivity()
                    }
                }
                R.id.nav_message -> {
                    if (isLogin()) {
                        if (bidFragment == null) {
                            bidFragment = MessageFragment.newInstance()
                            addFragmentMethod(bidFragment!!)

                        }
                        selectedFragment = bidFragment
                    } else {
                        openAuthActivity()
                    }

                }
                R.id.nav_profil -> {
                    if (isLogin()) {
                        if (profilFragment == null) {
                            profilFragment = ProfilFragment.newInstance()
                            addFragmentMethod(profilFragment!!)
                        }
                        selectedFragment = profilFragment
                    } else {
                        openAuthActivity()
                    }

                }

            }

            if (isLogin()) {
                if (selectedFragment != null) {
                    showFragment(selectedFragment)
                }
            }


            true

        }
        bottom_nav.selectedItemId = R.id.nav_home


    }

    private fun openAuthActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.putExtra("default", "default")
        startActivityForResult(intent, 0)
        CoreApp.notLogin = true
    }

    fun addFragmentMethod(fragment: Fragment) {

        supportFragmentManager.beginTransaction().add(R.id.container, fragment).commit()
    }

    fun showFragment(fragment: Fragment) {
        if (fragment != activeFragment) {
            supportFragmentManager.beginTransaction().hide(activeFragment!!).show(fragment)
                .commitAllowingStateLoss()
            activeFragment = fragment
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        bottom_nav.selectedItemId = R.id.nav_home
    }

    override fun onResume() {
        super.onResume()
        CoreApp.notLogin = false

    }
}
