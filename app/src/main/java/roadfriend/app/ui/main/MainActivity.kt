package roadfriend.app.ui.main

import android.content.Intent
import androidx.fragment.app.Fragment
import com.google.android.material.badge.BadgeDrawable
import org.koin.androidx.viewmodel.ext.android.viewModel
import roadfriend.app.CoreApp
import roadfriend.app.R
import roadfriend.app.databinding.ActivityMainBinding
import roadfriend.app.ui.add.direction.AddDirectionActivity
import roadfriend.app.ui.auth.login.LoginActivity
import roadfriend.app.ui.base.BindingActivity
import roadfriend.app.newui.search.TravelerListFragment
import roadfriend.app.ui.message.MessageFragment
import roadfriend.app.ui.notification.SearchFragment
import roadfriend.app.ui.profile.ProfilFragment
import roadfriend.app.utils.PrefUtils
import roadfriend.app.utils.PrefUtils.isLogin
import roadfriend.app.utils.extensions.overridePendingTransitionEnter
import roadfriend.app.utils.firebasedatebase.FirebaseHelper


class MainActivity : BindingActivity<ActivityMainBinding>() {

    override fun createBinding() = ActivityMainBinding.inflate(layoutInflater)

    private val viewModel by viewModel<MainViewModel>()

    override fun initNavigator() {
        viewModel.setPresenter(this)
    }

    override fun initUI() {
        setupBottomNavigationBar()
    }

    override fun initListener() {

    }

    var activeFragment: Fragment? = null
    lateinit var badgeMessage: BadgeDrawable


    private fun setupBottomNavigationBar() {
        badgeMessage = binding.bottomNav.getOrCreateBadge(R.id.nav_message)
        badgeMessage.isVisible = false


        binding.bottomNav.setOnNavigationItemSelectedListener { menuItem ->
            var selectedFragment: Fragment? = null
            when (menuItem.itemId) {
                R.id.nav_find -> {

                    replaceFragmentMethod(SearchFragment.newInstance())
                }
                R.id.nav_mytraveler -> {
                    replaceFragmentMethod(TravelerListFragment.newInstance())
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
        binding.bottomNav.selectedItemId = R.id.nav_find
        checkBadgeNotification()

    }
    fun changeBottomStatus(nav: Int) {
        binding.bottomNav.selectedItemId = nav
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
        binding.bottomNav.selectedItemId = R.id.nav_find
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
