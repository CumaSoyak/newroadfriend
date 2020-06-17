package roadfriend.app.ui.notification


import android.view.View
import androidx.fragment.app.Fragment
import roadfriend.app.CoreApp
import roadfriend.app.CoreApp.Companion.db
import roadfriend.app.R
import roadfriend.app.data.remote.model.trips.Trips
import roadfriend.app.databinding.FragmentNotificationBinding
import roadfriend.app.ui.base.BindingFragment
import roadfriend.app.ui.biddetail.BidDetailActivity
import roadfriend.app.utils.PrefUtils
import roadfriend.app.utils.extensions.launchActivity
import roadfriend.app.utils.extensions.showEmpty
import roadfriend.app.utils.helper.genericadapter.GenericAdapter
import roadfriend.app.utils.helper.genericadapter.ListItemViewModel
import kotlinx.android.synthetic.main.fragment_notification.*
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass.
 */
class NotificationFragment : BindingFragment<FragmentNotificationBinding>() {
    override val getLayoutBindId: Int
        get() = R.layout.fragment_notification

    companion object {
        val TAG: String = NotificationFragment::class.java.simpleName
        fun newInstance(): NotificationFragment = NotificationFragment().apply {

        }
    }

    private val viewModel by viewModel<NotificationViewModel>()

    private val adapterNotification by lazy { GenericAdapter<Trips>(R.layout.item_notification_bid) }


    override fun initNavigator() {
        viewModel.setPresenter(this)
    }

    override fun initUI(view: View) {
        binding.vm = viewModel
        binding.lifecycleOwner = this
        getBidLidt()
    }


    override fun initListener() {
        listener()
    }

    private fun listener() {
        adapterNotification.setOnListItemViewClickListener(object :
            GenericAdapter.OnListItemViewClickListener {
            override fun onClickDetail(view: View, position: Int, model: ListItemViewModel) {
                val model = model as Trips
                context?.launchActivity<BidDetailActivity> {
                    putExtra("model", model.id)
                }
            }
        })
    }

    fun getBidLidt() {
        val trips: ArrayList<Trips> = arrayListOf()
        val userMe = PrefUtils.getUser()
        val docRef = db.collection(CoreApp.testDatabase + "bidList")
            .document(userMe!!.id).collection("info")
        docRef.addSnapshotListener { snapshot, e ->
            snapshot?.forEachIndexed { index, queryDocumentSnapshot ->
                val data: Trips = queryDocumentSnapshot.toObject(Trips::class.java)
                trips.add(data)
            }
            initData(trips)
        }
    }

    private fun initData(data: ArrayList<Trips>) {
        if (data.isEmpty()) {
            clContainer.showEmpty(requireContext(), "notification")
        } else {
            binding.rv.adapter = adapterNotification
            adapterNotification.clearItems()
            adapterNotification.addItems(data)
        }
    }
}
