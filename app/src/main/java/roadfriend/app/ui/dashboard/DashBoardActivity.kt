package roadfriend.app.ui.dashboard

import roadfriend.app.CoreApp
import roadfriend.app.R
import roadfriend.app.databinding.ActivityDashBoardBinding
import roadfriend.app.ui.base.BindingActivity
import roadfriend.app.ui.main.MainActivity
import roadfriend.app.utils.extensions.launchActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class DashBoardActivity : BindingActivity<ActivityDashBoardBinding>() {

    private val viewModel by viewModel<DashBoardViewModel>()

    override val getLayoutBindId: Int
        get() = R.layout.activity_dash_board

    override fun initNavigator() {
        viewModel.setPresenter(this)
    }

    override fun initUI() {
        binding.vmDashBoard = viewModel
        binding.lifecycleOwner = this
    }

    override fun initListener() {
        viewModel.tripSize()
        viewModel.userSize()
        binding.devam.setOnClickListener {
            launchActivity<MainActivity> { }
        }
        binding.btnTest.setOnClickListener {
            CoreApp.testDatabase = "testDataBase-"
            launchActivity<MainActivity> { }
        }
        binding.btnAdd.setOnClickListener {
            CoreApp.addAdminTrip = true
            launchActivity<MainActivity> { }
        }
    }


}
