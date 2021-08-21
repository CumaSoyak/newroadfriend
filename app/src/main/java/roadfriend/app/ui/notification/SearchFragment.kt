package roadfriend.app.ui.notification


import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.koin.androidx.viewmodel.ext.android.viewModel
import roadfriend.app.R
import roadfriend.app.data.local.model.Search
import roadfriend.app.data.remote.model.city.City
import roadfriend.app.databinding.FragmentSearchCityBinding
import roadfriend.app.ui.base.BindingFragment
import roadfriend.app.ui.main.MainActivity
import roadfriend.app.ui.searchcity.SearchCityDialog
import roadfriend.app.utils.AppConstants
import roadfriend.app.utils.PrefUtils
import roadfriend.app.utils.extensions.gone
import roadfriend.app.utils.extensions.visible

/**
 * A simple [Fragment] subclass.
 */
class SearchFragment : BindingFragment<FragmentSearchCityBinding>(),
    SearchCityDialog.ISearchCityListener {

    override fun createBinding() = FragmentSearchCityBinding.inflate(layoutInflater)

    companion object {
        val TAG: String = SearchFragment::class.java.simpleName
        fun newInstance(): SearchFragment = SearchFragment().apply {

        }
    }

    private val cityList: ArrayList<City> = arrayListOf()


    private val viewModel by viewModel<NotificationViewModel>()


    override fun initNavigator() {
        viewModel.setPresenter(this)
    }

    override fun initUI(view: View) {
        binding.lifecycleOwner = this
        binding.include.back.gone()

    }

    override fun initListener() {
        binding.selectCity.setOnClickListener {
            goSearchCityPage()
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
            PrefUtils.createTrip(Search(stationStart, stationEnd))

            (requireActivity() as MainActivity).changeBottomStatus(R.id.nav_home)
         }
    }

    fun goSearchCityPage() {
        requireActivity().supportFragmentManager.beginTransaction().let { it1 ->
            SearchCityDialog.newInstance(AppConstants.HOME_SEARCH, this)
                .show(it1, "")
        }

    }

}
