package roadfriend.app.ui.biddetail

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.view.View
import com.google.firebase.firestore.Query
 import org.koin.androidx.viewmodel.ext.android.viewModel
import roadfriend.app.CoreApp
import roadfriend.app.CoreApp.Companion.db
import roadfriend.app.R
import roadfriend.app.data.remote.model.bid.Bid
import roadfriend.app.databinding.BidDetailActivityBinding
import roadfriend.app.ui.base.BindingActivity
import roadfriend.app.ui.main.MainActivity
import roadfriend.app.utils.PrefUtils
import roadfriend.app.utils.extensions.launchActivity
import roadfriend.app.utils.extensions.showEmpty
import roadfriend.app.utils.firebasedatebase.FirebaseHelper
import roadfriend.app.utils.helper.genericadapter.GenericAdapter
import roadfriend.app.utils.helper.genericadapter.ListItemViewModel

class BidDetailActivity : BindingActivity<BidDetailActivityBinding>() {
    override fun createBinding()= BidDetailActivityBinding.inflate(layoutInflater)

    private val tripsId: String by lazy { intent.getStringExtra("model") as String }

    private val viewModel by viewModel<BidDetailViewModel>()

    private val adapterBidList by lazy { GenericAdapter<Bid>(R.layout.item_bid_detail) }

    override fun initNavigator() {
        viewModel.setPresenter(this)
    }

    override fun initUI() {
        binding.vm = viewModel
        binding.lifecycleOwner = this
        toolBarTitle("Teklif Detayı")
        FirebaseHelper().setBidNotificationBadge(PrefUtils.getUser(), false)
    }

    override fun initListener() {
        binding.rv.adapter = adapterBidList
        getBidList()
        adapterListener()
        binding.include5.back.setOnClickListener {
            back()
        }
    }

    fun adapterListener() {
        adapterBidList.setOnListItemViewClickListener(object :
            GenericAdapter.OnListItemViewClickListener {
            override fun onClickDetail(view: View, position: Int, model: ListItemViewModel) {
                val model = model as Bid
                callPhone(model.phone)
            }
        })
    }

    fun getBidList() {
        val userMe = PrefUtils.getUser()
        val trips: ArrayList<Bid> = arrayListOf()
        val docRef =
            db.collection(CoreApp.testDatabase + "bid")
                .document(userMe!!.id)
                .collection(tripsId)
                .orderBy("firebaseTime", Query.Direction.DESCENDING)
        docRef.addSnapshotListener { snapshot, e ->
            snapshot?.forEachIndexed { index, queryDocumentSnapshot ->
                val data: Bid = queryDocumentSnapshot.toObject(Bid::class.java)
                trips.add(data)
            }
            initData(trips)
        }
    }

    fun initData(bid: ArrayList<Bid>) {
        if (bid.isEmpty()) {
            binding.clContainer.showEmpty(this)
        } else {
            adapterBidList.clearItems()
            adapterBidList.addItems(bid)
        }

    }

    @SuppressLint("MissingPermission")
    fun callPhone(phone: String?) {
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone))
        startActivity(intent)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        back()
    }

    fun back() {
        if (intent.hasExtra("firebaseMessage")) {
            launchActivity<MainActivity> { }
            finish()
        } else {
            finish()
        }
    }
}
