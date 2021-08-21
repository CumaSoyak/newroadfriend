package roadfriend.app.newui

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import roadfriend.app.R
import roadfriend.app.databinding.ActivityMainBinding

import roadfriend.app.ui.base.BaseActivity


class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {
    override val viewModel: MainViewModel by viewModel()

    private val showBottomNavigationLiveData = MutableLiveData<Boolean>()
    override fun createBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        binding.customTab.customTabbar.setupWithNavController(navController)
        showBottomNavigationLiveData.observe(this) {
            binding.customTab.customTabbar.isVisible = it
        }
    }

    fun isShowBottomNavigation(b: Boolean) {
        showBottomNavigationLiveData.postValue(b)
    }

}
