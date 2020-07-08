package roadfriend.app.ui.profile.mytrip

import android.view.View
import org.koin.androidx.viewmodel.ext.android.viewModel
import roadfriend.app.R
import roadfriend.app.customviews.CvOption
import roadfriend.app.data.remote.model.trips.Trips
import roadfriend.app.databinding.MyTripsActivityBinding
import roadfriend.app.ui.base.BindingActivity
import roadfriend.app.ui.sales.SalesActivity
import roadfriend.app.utils.extensions.launchActivity
import roadfriend.app.utils.extensions.showSucces
import roadfriend.app.utils.firebasedatebase.FirebaseHelper
import roadfriend.app.utils.firebasedatebase.TripFirebase
import roadfriend.app.utils.helper.genericadapter.GenericAdapter
import roadfriend.app.utils.helper.genericadapter.ListItemViewModel

class MyTripsActivity : BindingActivity<MyTripsActivityBinding>() {
    override val getLayoutBindId: Int
        get() = R.layout.my_trips_activity

    private val viewModel by viewModel<MyTripsViewModel>()

    private val adapter by lazy { GenericAdapter<Trips>(R.layout.item_my_trip) }

    val TAG = this::class.java.name

    override fun initNavigator() {
        viewModel.setPresenter(this)
    }

    override fun initUI() {
        setActivity(this)
        toolBarTitle(getString(R.string.yolculuklarım))
        binding.vm = viewModel
        binding.lifecycleOwner = this
    }

    override fun initListener() {
        requestTrip()
        listener()
        if (intent.hasExtra("premium")) {
            showSucces("Öne çıkarmak istediğiniz ilanı seçin lütfen")
        }

    }

    fun requestTrip() {
        FirebaseHelper().getMyTrip { data ->
            addData(data)
        }
    }

    private fun addData(data: ArrayList<Trips>) {
        if (data.isEmpty()) {
            binding.cvEmptyView.initData(this)
        } else {
            binding.cvEmptyView.hideView()
            binding.rv.adapter = adapter
            adapter.clearItems()
            adapter.addItems(data)
        }

    }

    fun goSalesScreen(trips: Trips) {
        launchActivity<SalesActivity> {
            putExtra("data", trips)
            putExtra("intent", TAG)
            putExtra("premium", "premium")
        }
    }

    fun listener() {
        adapter.setOnListItemViewClickListener(object : GenericAdapter.OnListItemViewClickListener {
            override fun onClickDetail(view: View, position: Int, model: ListItemViewModel) {
                goSalesScreen(model as Trips)
            }

            override fun onClickOptionSettings(
                view: View,
                position: Int,
                model: ListItemViewModel
            ) {
                CvOption().initOption(true, this@MyTripsActivity, deleteListener = {
                    TripFirebase().deleteTrip(
                        this@MyTripsActivity,
                        (model as Trips).documentKey!!
                    )
                    requestTrip()
                })
            }
        })
    }


}
