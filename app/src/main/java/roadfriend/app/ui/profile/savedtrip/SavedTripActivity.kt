package roadfriend.app.ui.profile.savedtrip

import android.view.View
import roadfriend.app.CoreApp
import roadfriend.app.CoreApp.Companion.db
import roadfriend.app.R
import roadfriend.app.customviews.CvOption
import roadfriend.app.data.local.model.MapsModel
import roadfriend.app.data.remote.model.trips.Trips
import roadfriend.app.databinding.SavedTripActivityBinding
import roadfriend.app.ui.base.BindingActivity
import roadfriend.app.ui.tripdetail.TripDetailActivity
import roadfriend.app.utils.PrefUtils
import roadfriend.app.utils.extensions.launchActivity
import roadfriend.app.utils.extensions.showError
import roadfriend.app.utils.extensions.showSucces
import roadfriend.app.utils.firebasedatebase.FirebaseHelper
import roadfriend.app.utils.helper.genericadapter.GenericAdapter
import roadfriend.app.utils.helper.genericadapter.ListItemViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import roadfriend.app.utils.extensions.showEmpty
import java.util.*

class SavedTripActivity : BindingActivity<SavedTripActivityBinding>() {
    override val getLayoutBindId: Int
        get() = R.layout.saved_trip_activity

    private val viewModel by viewModel<SavedTripViewModel>()

    private val adapter by lazy { GenericAdapter<Trips>(R.layout.item_my_trip) }

    var mapsModel: MapsModel? = null

    override fun initNavigator() {
        viewModel.setPresenter(this)
    }

    override fun initUI() {
        toolBarTitle("Kaydedilen Yolculuklar")
        binding.vm = viewModel
        binding.lifecycleOwner = this
    }

    override fun initListener() {
        listener()
        requestTrip()
    }

    fun requestTrip() {
        FirebaseHelper().getSavedTrip { data ->
            addData(data)
        }
    }

    private fun addData(data: ArrayList<Trips>) {
        if (data.isEmpty()) {
            binding.clContainer.showEmpty(this)
        } else {
            binding.rv.adapter = adapter
            adapter.clearItems()
            adapter.addItems(data)
        }
    }

    fun listener() {
        mapsModel = MapsModel()
        mapsModel!!.type = "home"
        adapter.setOnListItemViewClickListener(object : GenericAdapter.OnListItemViewClickListener {
            override fun onClickDetail(view: View, position: Int, model: ListItemViewModel) {
                launchActivity<TripDetailActivity> {
                    putExtra("maps", mapsModel)
                    putExtra("data", model as Trips)
                }
            }

            override fun onClickOptionSettings(
                view: View,
                position: Int,
                model: ListItemViewModel
            ) {
                CvOption().initOption(true, this@SavedTripActivity, deleteListener = {
                    deleteTrip((model as Trips).documentKey!!)
                })
            }
        })
    }

    fun deleteTrip(document: String) {
        db.collection(CoreApp.testDatabase + "savedTrip")
            .document(PrefUtils.getUserId())
            .collection("info")
            .document(document)
            .delete()
            .addOnSuccessListener {
                showSucces("Yolculuk Silinmiştir")
            }
            .addOnFailureListener {
                showError("Bir şeyler ters gitti tekrar deneyiniz")
            }
    }
}
