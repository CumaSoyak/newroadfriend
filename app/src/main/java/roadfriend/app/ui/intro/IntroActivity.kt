package roadfriend.app.ui.intro

import org.koin.androidx.viewmodel.ext.android.viewModel
import roadfriend.app.R
import roadfriend.app.databinding.ActivityIntroBinding
import roadfriend.app.ui.auth.register.RegisterActivity
import roadfriend.app.ui.base.BindingActivity
import roadfriend.app.ui.main.MainActivity
import roadfriend.app.ui.search.SearchCityActivity
 import roadfriend.app.utils.extensions.launchActivity
import roadfriend.app.utils.extensions.onPageSelected

class IntroActivity : BindingActivity<ActivityIntroBinding>() {
    override val getLayoutBindId: Int
        get() = R.layout.activity_intro

    private val viewModel by viewModel<IntroViewModel>()


    override fun initNavigator() {
        viewModel.setPresenter(this)
    }

    override fun initUI() {
        binding.tutorialType = TutorialType.TUTORIAL_FIRST
        binding.pagerTutorial.adapter = TutorialAdapter()

    }

    override fun initListener() {
        binding.textSkip.setOnClickListener {
            val items = TutorialType.values()
            binding.pagerTutorial.currentItem = items.size - 1
        }

        binding.cardNext.setOnClickListener {
            binding.pagerTutorial.currentItem = binding.pagerTutorial.currentItem + 1
        }

        binding.cardStart.setOnClickListener {
            launchActivity<SearchCityActivity> { }
            finish()
        }

        binding.buttonLogin.setOnClickListener {
            launchActivity<RegisterActivity> { }
            finish()
        }

        binding.pagerTutorial.onPageSelected { position ->
            binding.position = position
            binding.tutorialType = TutorialType.values()[position]
        }
    }


}
