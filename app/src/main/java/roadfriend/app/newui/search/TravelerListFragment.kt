package roadfriend.app.newui.search

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import org.koin.androidx.viewmodel.ext.android.viewModel
import roadfriend.app.CoreApp.Companion.notLogin
import roadfriend.app.R
import roadfriend.app.data.local.model.MapsModel
import roadfriend.app.data.remote.model.city.City
import roadfriend.app.data.remote.model.trips.GetTripRequest
import roadfriend.app.data.remote.model.trips.Trips
import roadfriend.app.databinding.FragmentTravelerListBinding
import roadfriend.app.ui.add.detail.AddDetailActivity
import roadfriend.app.ui.auth.register.RegisterActivity
import roadfriend.app.ui.base.BaseFragment
import roadfriend.app.ui.home.HomeViewModel
import roadfriend.app.ui.profile.myaboutcomment.MyAboutCommentsActivity
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
import roadfriend.app.utils.helper.TripBundle
import roadfriend.app.utils.helper.genericadapter.GenericAdapter
import roadfriend.app.utils.helper.genericadapter.ListItemViewModel
import roadfriend.app.utils.manager.EventManager

class TravelerListFragment : BaseFragment<FragmentTravelerListBinding, HomeViewModel>() {

    override fun createBinding() = FragmentTravelerListBinding.inflate(layoutInflater)

    override val viewModel:HomeViewModel by activityViewModels()

    val adapter = GenericAdapter<Trips>(R.layout.item_advert, itemType = "home")


    var mapsModel: MapsModel? = null

    var mTrips: ArrayList<Trips> = arrayListOf()

    var getTripRequest: GetTripRequest? = null

    var tripBundle: TripBundle? = TripBundle()

    companion object {
        val TAG: String = TravelerListFragment::class.java.name
        fun newInstance(): TravelerListFragment =
            TravelerListFragment().apply {

            }

    }

    override fun onViewReady(bundle: Bundle?) {
        binding.vmHome = viewModel
        binding.lifecycleOwner = this

        binding.include.tvToolbarTitle.text = resources.getString(R.string.search_trip)
    }


    override fun onViewListener() {
        listenerItem()
        tripFilter()

        binding.include.back.gone()
    }


    fun tripFilter() {
        binding.progressBar.visible()
        val data = PrefUtils.getTrip()
        getTripRequest =
            GetTripRequest(
                OtherUtils.getCountryCode(),
                data.startCity?.name,
                data.endCity?.name
            )
        val cityList: ArrayList<City> = arrayListOf(data.startCity!!, data.endCity!!)
        tripBundle?.tripsList?.clear()
        tripBundle?.tripsList?.addAll(cityList)
        getTrip(getTripRequest!!)

    }

    fun getTrip(getTripRequest: GetTripRequest) {
        try {
            FirebaseHelper().getFilterTrip(getTripRequest) { data ->
                addData(data, "home")
                binding.progressBar.gone()
            }
        } catch (e: Exception) {
            logger(e.localizedMessage)
        }

    }


    fun addData(trips: ArrayList<Trips>, emptyKey: String) {
        if (trips.isEmpty()) {
            isAvailabledata(false)
            try {
                if (getTripRequest != null) {
                    binding.cvEmptyView.initData(requireContext(), emptyKey, getTripRequest)
                    binding.cvEmptyView.initlistener(click = {
                        passAddDetail()
                    })
                } else {
                    binding.cvEmptyView.initData(requireContext(), emptyKey, getTripRequest)
                }

            } catch (e: Exception) {

            }
        } else {
            try {
                isAvailabledata(true)
                binding.recyclerview.adapter = adapter
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

            override fun onClickCommentDetail(view: View, position: Int, model: ListItemViewModel) {
                requireContext().launchActivity<MyAboutCommentsActivity> {
                    putExtra(
                        "data",
                        (model as Trips).user
                    )
                }
            }
        })
    }

    fun calculateAdmobIndex(trips: ArrayList<Trips>): ArrayList<Trips> {
        val listWithAds: ArrayList<Trips> = arrayListOf()
        val listSize = trips.size + trips.size / 5
        var tripsIndex = 0
        for (index in 0 until listSize) {
            if (index == 3) {
                listWithAds.add(OptionData.admobTrip())
            }
            if (index % 5 == 0 && index != 0) {
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
                binding.include.tvToolbarTitle.text = resources.getString(R.string.search_trip)
                binding.recyclerview.visible()
                binding.cvEmptyView.gone()
            } else {
                binding.include.tvToolbarTitle.text = resources.getString(R.string.search_trip)
                binding.recyclerview.gone()
                binding.cvEmptyView.visible()
            }
        } catch (e: Exception) {
            logger(e.localizedMessage)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        binding.cvEmptyView.hideView()
        //  binding.cvSearch.cleanedFilter()
        addData(mTrips, "home")
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

