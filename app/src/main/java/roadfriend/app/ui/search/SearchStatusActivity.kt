package roadfriend.app.ui.search

import android.view.View
import androidx.fragment.app.Fragment
import kotlinx.coroutines.selects.select
import org.jetbrains.anko.textColor
import org.koin.androidx.viewmodel.ext.android.viewModel
import roadfriend.app.CoreApp
import roadfriend.app.R
import roadfriend.app.databinding.ActivitySearchBinding
import roadfriend.app.ui.base.BindingActivity
import roadfriend.app.ui.main.MainActivity
import roadfriend.app.utils.extensions.launchActivity
import roadfriend.app.utils.extensions.visible

class SearchStatusActivity : BindingActivity<ActivitySearchBinding>() {

    override fun createBinding()= ActivitySearchBinding.inflate(layoutInflater)

    private val viewModel by viewModel<SearchViewModel>()

    override fun initNavigator() {
        viewModel.setPresenter(this)
    }

    override fun initUI() {
        CoreApp.statusSearch = ""
    }

    override fun initListener() {

        binding.car.setOnClickListener {
            select(it)
            unSelect(binding.human)
            CoreApp.statusSearch = "1"
            binding.tvCar.textColor=resources.getColor(R.color.border)
            binding.tvPerson.textColor=resources.getColor(R.color.color_edittext)

        }
        binding.human.setOnClickListener {
            select(it)
            unSelect(binding.car)
            CoreApp.statusSearch = "2"
            binding.tvCar.textColor=resources.getColor(R.color.color_edittext)
            binding.tvPerson.textColor=resources.getColor(R.color.border)


        }
        binding.next.setOnClickListener {
            launchActivity<SearchCityActivity> {
            }
        }
    }

    fun select(view: View) {
        binding.next.visible()
        view.background = resources.getDrawable(R.drawable.custom_border_blue)
    }

    fun unSelect(view: View) {
        view.background = resources.getDrawable(R.drawable.card_background)
    }
    override fun onBackPressed() {
        onBackPressedSetResult()
    }


}
