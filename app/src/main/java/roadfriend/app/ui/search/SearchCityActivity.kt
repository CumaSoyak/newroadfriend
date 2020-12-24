package roadfriend.app.ui.search

import kotlinx.android.synthetic.main.toolbar_layout.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import roadfriend.app.R
import roadfriend.app.data.local.model.Search
import roadfriend.app.data.remote.model.city.City
import roadfriend.app.databinding.FragmentSearchCityBinding
import roadfriend.app.ui.base.BindingActivity
import roadfriend.app.ui.main.MainActivity
import roadfriend.app.ui.searchcity.SearchCityDialog
import roadfriend.app.utils.AppConstants
import roadfriend.app.utils.extensions.gone
import roadfriend.app.utils.extensions.launchActivity
import roadfriend.app.utils.extensions.visible
import roadfriend.app.utils.helper.LiveBus


class SearchCityActivity : BindingActivity<FragmentSearchCityBinding>(),
    SearchCityDialog.ISearchCityListener {
    override val getLayoutBindId: Int
        get() = R.layout.fragment_search_city
    private val cityList: ArrayList<City> = arrayListOf()

    companion object {
        val TAG: String = SearchCityActivity::class.java.simpleName
        fun newInstance(): SearchCityActivity = SearchCityActivity().apply {

        }
    }

    private val viewModel by viewModel<SearchViewModel>()


    override fun initNavigator() {
        viewModel.setPresenter(this)
    }

    override fun initUI() {
        // binding.vm = viewModel
        binding.lifecycleOwner = this
    }


    override fun initListener() {
        binding.selectCity.setOnClickListener {
            goSearchCityPage()
        }
        back.setOnClickListener {
            finish()
        }
        binding.tvClear.setOnClickListener {
            binding.selectCity.visible()
            binding.gropSelected.gone()
        }
    }

    override fun cityAndStatus(stationStart: City?, stationEnd: City?, status: String?) {
        binding.selectCity.gone()
        binding.gropSelected.visible()

        binding.tvStart.text = stationStart?.name
        binding.tvEnd.text = stationEnd?.name
        cityList.add(stationStart!!)
        cityList.add(stationEnd!!)

        binding.next.setOnClickListener {
            launchActivity<MainActivity> {
                putExtra("data", Search(stationStart, stationEnd))
            }
            LiveBus.setAndSendSticky(Search(stationStart, stationEnd))

        }
    }

    fun goSearchCityPage() {
        supportFragmentManager.beginTransaction().let { it1 ->
            SearchCityDialog.newInstance(AppConstants.HOME_SEARCH, this)
                .show(it1, "")
        }

    }

}