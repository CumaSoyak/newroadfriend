package roadfriend.app.ui.profile.setting

import roadfriend.app.R
import roadfriend.app.databinding.ChangePasswordActivityBinding
import roadfriend.app.ui.base.BindingActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChangePasswordActivity : BindingActivity<ChangePasswordActivityBinding>() {

    override fun createBinding()= ChangePasswordActivityBinding.inflate(layoutInflater)

    private val viewModel by viewModel<ChangePasswordViewModel>()

    override fun initNavigator() {
        viewModel.setPresenter(this)
    }

    override fun initUI() {
        toolBarTitle("Parola Değiştir")
        binding.vm = viewModel
        binding.lifecycleOwner = this
    }

    override fun initListener() {

    }

}
