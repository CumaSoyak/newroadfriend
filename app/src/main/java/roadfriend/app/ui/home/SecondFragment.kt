package roadfriend.app.ui.home

import android.content.Intent
import android.view.View
import kotlinx.android.synthetic.main.fragment_home_first.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import roadfriend.app.CoreApp
import roadfriend.app.CoreApp.Companion.notLogin
import roadfriend.app.R
import roadfriend.app.data.local.model.MapsModel
import roadfriend.app.data.local.model.Search
import roadfriend.app.data.local.model.VALUE
import roadfriend.app.data.remote.model.city.City
import roadfriend.app.data.remote.model.trips.GetTripRequest
import roadfriend.app.data.remote.model.trips.Trips
import roadfriend.app.databinding.FragmentHomeSecondBinding
import roadfriend.app.ui.add.detail.AddDetailActivity
import roadfriend.app.ui.auth.register.RegisterActivity
import roadfriend.app.ui.base.BindingFragment
import roadfriend.app.ui.maps.MapsDialogFragment
import roadfriend.app.ui.tripdetail.TripDetailActivity
import roadfriend.app.ui.userdetail.UserDetailActivity
import roadfriend.app.utils.OptionData
import roadfriend.app.utils.OtherUtils
import roadfriend.app.utils.PrefUtils
import roadfriend.app.utils.extensions.gone
import roadfriend.app.utils.extensions.launchActivity
import roadfriend.app.utils.extensions.logger
import roadfriend.app.utils.extensions.visible
import roadfriend.app.utils.firebasedatebase.FirebaseHelper
import roadfriend.app.utils.helper.LiveBus
import roadfriend.app.utils.helper.TripBundle
import roadfriend.app.utils.helper.genericadapter.GenericAdapter
import roadfriend.app.utils.helper.genericadapter.ListItemViewModel
import roadfriend.app.utils.manager.EventManager

class SecondFragment : BindingFragment<FragmentHomeSecondBinding>() {
    override val getLayoutBindId: Int
        get() = R.layout.fragment_home_second

    private val viewModel by viewModel<HomeViewModel>()

    val adapter = GenericAdapter<Trips>(R.layout.item_advert, itemType = "home")


    var mapsModel: MapsModel? = null

    var mTrips: ArrayList<Trips> = arrayListOf()

    var getTripRequest: GetTripRequest? = null

    var tripBundle: TripBundle? = TripBundle()

    companion object {
        val TAG: String = SecondFragment::class.java.name
        fun newInstance(): SecondFragment = SecondFragment().apply {}
    }

    override fun initNavigator() {
        viewModel.setPresenter(this)
    }

    override fun initUI(view: View) {
        binding.vmHome = viewModel
        binding.lifecycleOwner = this
    }

    override fun initListener() {

        listenerItem()
        listenerSearch()
        defaultRequest()
        tripFilter()
    }

    fun defaultRequest() {
        try {
            FirebaseHelper().getDefaultTrip("2") { data ->
                addData(data)
            }
        } catch (e: Exception) {
            logger(e.localizedMessage + "")
        }

    }


    fun tripFilter() {
        LiveBus.get(Search::class.java).observeForeverSticky {
            when (it.type) {
                VALUE.SECONDDATA -> {
                    addData(mTrips)

                }
                VALUE.SECONDFILTER -> {
                    viewModel.getPresenter()?.showLoading()
                    getTripRequest =
                        GetTripRequest(
                            OtherUtils.getCountryCode(),
                            CoreApp.statusSearch,
                            it.startCity?.name,
                            it.endCity?.name
                        )

                    val cityList: ArrayList<City> = arrayListOf(it.startCity!!, it.endCity!!)
                    tripBundle?.tripStatus = CoreApp.statusSearch
                    tripBundle?.tripsList?.clear()
                    tripBundle?.tripsList?.addAll(cityList)
                    getTrip(getTripRequest!!)
                }
            }
        }

    }

    fun listenerSearch() {
        /**ilkinde komple tarihe göre sıralı gelsin*/

        binding.ivMap.setOnClickListener {
            fragmentManager!!.beginTransaction().let { it1 ->
                MapsDialogFragment.newInstance(mapsModel!!)
                    .show(it1, "")
            }
        }
    }

    fun getTrip(getTripRequest: GetTripRequest) {
        try {
            FirebaseHelper().getFilterTrip(getTripRequest) { data ->
                viewModel.getPresenter()?.hideLoading()
                addData(data)
            }
        } catch (e: Exception) {
            logger(e.localizedMessage)
        }

    }


    fun addData(trips: ArrayList<Trips>) {
        if (trips.isEmpty()) {
            isAvailabledata(false)
            try {
                getTripRequest?.let {
                    cvEmptyView.initData(requireContext(), "home", getTripRequest)
                    cvEmptyView.initlistener(click = {
                        passAddDetail()
                    })

                }
            } catch (e: Exception) {

            }

        } else {
            try {
                isAvailabledata(true)
                recyclerview.adapter = adapter
                adapter.clearItems()
                adapter.addItems(calculateAdmobIndex(trips))

                if (mTrips.isNullOrEmpty()) {
                    mTrips.addAll(trips)
                }
                mapsModel = MapsModel()
                mapsModel!!.trips.addAll(trips)
                mapsModel!!.type = "home"

            } catch (e: Exception) {
                logger(e.localizedMessage)
            }

        }
    }

    fun listenerItem() {
        adapter.setOnListItemViewClickListener(object : GenericAdapter.OnListItemViewClickListener {
            override fun onClickDetail(view: View, position: Int, model: ListItemViewModel) {
                context!!.launchActivity<TripDetailActivity> {
                    putExtra("data", model as Trips)
                }
            }

            override fun onClickUserDetail(view: View, position: Int, model: ListItemViewModel) {
                context!!.launchActivity<UserDetailActivity> {
                    putExtra("model", model as Trips)
                }
            }
        })
    }

    fun calculateAdmobIndex(trips: ArrayList<Trips>): ArrayList<Trips> {
        val listWithAds: ArrayList<Trips> = arrayListOf()
        val listSize = trips.size + trips.size / 5
        var tripsIndex = 0
        for (index in 0 until listSize) {
//            if (index == 3) {
//                listWithAds.add(OptionData.admobTrip())
//            }
            if (index % 15 == 0 && index != 0) {
                listWithAds.add(OptionData.admobTrip())
            } else {
                if (trips.size > tripsIndex) {
                    listWithAds.add(trips.get(tripsIndex))
                }
                tripsIndex++
            }
        }
        return listWithAds
    }

    fun isAvailabledata(available: Boolean) {
        try {
            if (available) {
                binding.recyclerview.visible()
                binding.ivMap.visible()
                binding.cvEmptyView.gone()
            } else {
                binding.recyclerview.gone()
                binding.ivMap.gone()
                binding.cvEmptyView.visible()
            }
        } catch (e: Exception) {
            logger(e.localizedMessage)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        binding.cvEmptyView.hideView()
        addData(mTrips)
    }

    fun passAddDetail() {
        if (PrefUtils.isLogin()) {
            EventManager.clickNoSearch(tripBundle)
            val intent = Intent(context, AddDetailActivity::class.java)
            intent.putExtra("tripModel", tripBundle)
            startActivityForResult(intent, 1)
        } else {
            context?.launchActivity<RegisterActivity> {
                putExtra("ilan", "ilan")
                notLogin = true
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (notLogin) {
            notLogin = false
            passAddDetail()
        }
    }

}

