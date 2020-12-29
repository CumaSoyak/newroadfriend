package roadfriend.app.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.badge.BadgeDrawable
import kotlinx.android.synthetic.main.activity_main.*
import roadfriend.app.CoreApp
import roadfriend.app.R
import roadfriend.app.ui.add.direction.AddDirectionActivity
import roadfriend.app.ui.auth.login.LoginActivity
import roadfriend.app.ui.home.FirstFragment
import roadfriend.app.ui.message.MessageFragment
import roadfriend.app.ui.notification.SearchFragment
import roadfriend.app.ui.profile.ProfilFragment
import roadfriend.app.utils.PrefUtils
import roadfriend.app.utils.PrefUtils.isLogin
import roadfriend.app.utils.extensions.overridePendingTransitionEnter
import roadfriend.app.utils.firebasedatebase.FirebaseHelper


class MainActivity : AppCompatActivity() {

    var activeFragment: Fragment? = null
    var homeFragment: Fragment? = null
    var searchFragment: Fragment? = null
    var bidFragment: Fragment? = null
    var profilFragment: Fragment? = null
    lateinit var badgeMessage: BadgeDrawable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupBottomNavigationBar()
    }

    private fun setupBottomNavigationBar() {
        badgeMessage = bottom_nav.getOrCreateBadge(R.id.nav_message)
        badgeMessage.isVisible = false


        bottom_nav.setOnNavigationItemSelectedListener { menuItem ->
            var selectedFragment: Fragment? = null
            when (menuItem.itemId) {
                R.id.nav_home -> {
                     replaceFragmentMethod(FirstFragment.newInstance())
                }
                R.id.nav_find -> {
                   // if (searchFragment == null) {
                 //       searchFragment = SearchFragment.newInstance()
                 //       activeFragment = searchFragment
                 //       addFragmentMethod(searchFragment!!)
                 //    }
                 //   selectedFragment = searchFragment
                    replaceFragmentMethod(SearchFragment.newInstance())
                }
                R.id.nav_add -> {
                    if (isLogin()) {
                        selectedFragment = null
                        val intent = Intent(this, AddDirectionActivity::class.java)
                        startActivityForResult(intent, 0)
                        overridePendingTransitionEnter()
                    } else {
                        openAuthActivity()
                    }
                }
                R.id.nav_message -> {
                    if (isLogin()) {
                        notidficationDisable()
                    //    if (bidFragment == null) {
                    //        bidFragment = MessageFragment.newInstance()
                    //        addFragmentMethod(bidFragment!!)
//
                    //    }
                    //    selectedFragment = bidFragment
                        replaceFragmentMethod(MessageFragment.newInstance())
                    } else {
                        openAuthActivity()
                    }

                }
                R.id.nav_profil -> {
                    if (isLogin()) {
                      //  if (profilFragment == null) {
                      //      profilFragment = ProfilFragment.newInstance()
                      //      addFragmentMethod(profilFragment!!)
                      //  }
                      //  selectedFragment = profilFragment
                        replaceFragmentMethod(ProfilFragment.newInstance())

                    } else {
                        openAuthActivity()
                    }

                }

            }

        //   if (true) {
        //       if (selectedFragment != null) {
        //           showFragment(selectedFragment)
        //       }
        //   }


            true

        }
        bottom_nav.selectedItemId = R.id.nav_home
        checkBadgeNotification()

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
    fun replaceFragmentMethod(fragment: Fragment) {

        supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
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

    fun checkBadgeNotification() {
        if (isLogin()) {
            FirebaseHelper().getNotification {
                if (it) {
                    badgeMessage.isVisible = true
                }
            }
            FirebaseHelper().getNotificationBid {
                if (it) {
                  //  badgeBid.isVisible = true
                }
            }
        }
    }

    fun notidficationDisable() {
        badgeMessage.isVisible = false
        FirebaseHelper().setNotificationBadge(PrefUtils.getUser(), false)
    }

    fun bidNotification() {
       // badgeBid.isVisible = false
        FirebaseHelper().setBidNotificationBadge(PrefUtils.getUser(), false)
    }
}
