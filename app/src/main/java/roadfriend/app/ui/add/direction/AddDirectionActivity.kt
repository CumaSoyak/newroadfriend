package roadfriend.app.ui.add.direction

import kotlinx.android.synthetic.main.add_direction_activity.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import roadfriend.app.R
import roadfriend.app.data.remote.model.city.City
import roadfriend.app.databinding.AddDirectionActivityBinding
import roadfriend.app.ui.add.adapter.AddStationAdapter
import roadfriend.app.ui.add.adapter.IRemoveStationListener
import roadfriend.app.ui.add.detail.AddDetailActivity
import roadfriend.app.ui.base.BindingActivity
import roadfriend.app.ui.base.IBasePresenter
import roadfriend.app.ui.searchcity.SearchCityDialog
import roadfriend.app.utils.AppConstants.HOME_SEARCH
import roadfriend.app.utils.extensions.gone
import roadfriend.app.utils.extensions.launchActivity
import roadfriend.app.utils.extensions.showError
import roadfriend.app.utils.extensions.visible
import roadfriend.app.utils.helper.TripBundle

class AddDirectionActivity : BindingActivity<AddDirectionActivityBinding>(),
    IBasePresenter, SearchCityDialog.ISearchCityListener, IRemoveStationListener {


    override val getLayoutBindId: Int
        get() = R.layout.add_direction_activity

    private val viewModel by viewModel<AddDirectionViewModel>()

    private val adapter by lazy { AddStationAdapter(arrayListOf(), this) }

    private val cityList: ArrayList<City> = arrayListOf()
    private var mStationStart: City? = null
    private var mStationEnd: City? = null


    private val status: String by lazy { intent.getStringExtra("status") }

    enum class StationType {
        FROMANDTO
    }

    var GLOBAL_STATION: StationType? = null

    override fun initNavigator() {
        viewModel.setPresenter(this)
    }

    override fun initUI() {
        binding.vm = viewModel
        binding.lifecycleOwner = this
        toolBarTitle(getString(R.string.title_add_direction))
    }


    override fun initListener() {

        cityList.clear()

        recyclerview.adapter = adapter


        binding.svStartCity.setOnClickListener {
            GLOBAL_STATION = StationType.FROMANDTO
            goSearchCityPage()
        }
        binding.tvClear.setOnClickListener {
            binding.svStartCity.visible()
            binding.recyclerview.gone()
            containerEnd.gone()
            containerStart.gone()
            binding.tvClear.gone()
        }


        binding.btnNext.setOnClickListener {
            if (mStationStart == null && mStationEnd == null) {
                showError("Şehir seçilirken bir hata oluştu tekrar seçiniz !")
            } else {
                cityList.add(mStationStart!!)
                cityList.add(mStationEnd!!)
                launchActivity<AddDetailActivity> {
                    putExtra("tripModel", TripBundle(status, cityList))
                }
            }

        }

    }

    override fun cityAndStatus(stationStart: City?, stationEnd: City?, status: String?) {
        binding.svStartCity.gone()
        binding.recyclerview.visible()
        containerEnd.visible()
        containerStart.visible()

        tvStartCity.text = stationStart?.name
        tvEndCity.text = stationEnd?.name

        mStationStart = stationStart
        mStationEnd = stationEnd

        checkStartStationAndEndStation()
    }

    override fun removeStation(position: Int) {
        adapter.remove(position)
    }

    fun goSearchCityPage() {
        supportFragmentManager.beginTransaction().let { it1 ->
            SearchCityDialog.newInstance(HOME_SEARCH, this)
                .show(it1, "")
        }

    }

    fun checkStartStationAndEndStation() {
        binding.btnNext.visible()
        binding.tvClear.visible()

    }

}