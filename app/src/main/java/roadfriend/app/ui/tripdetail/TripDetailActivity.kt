package roadfriend.app.ui.tripdetail

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import roadfriend.app.CoreApp
import roadfriend.app.R
import roadfriend.app.customviews.CvOption
import roadfriend.app.data.remote.model.firebasemessage.Data
import roadfriend.app.data.remote.model.firebasemessage.NotificationRequest
import roadfriend.app.data.remote.model.trips.Trips
import roadfriend.app.data.remote.model.user.User
import roadfriend.app.databinding.TripDetailActivityBinding
import roadfriend.app.ui.auth.register.RegisterActivity
import roadfriend.app.ui.base.BindingActivity
import roadfriend.app.ui.message.chat.ChatActivity
import roadfriend.app.utils.DialogUtils
import roadfriend.app.utils.PrefUtils
import roadfriend.app.utils.extensions.*
import roadfriend.app.utils.firebasedatebase.TripFirebase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.android.synthetic.main.toolbar_layout.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class TripDetailActivity : BindingActivity<TripDetailActivityBinding>() {
    override val getLayoutBindId: Int
        get() = R.layout.trip_detail_activity

    private val viewModel by viewModel<TripDetailViewModel>()

    private val trips: Trips by lazy { intent.getParcelableExtra("data") as Trips }


    var db: FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun initNavigator() {
        viewModel.setPresenter(this)
    }

    override fun initUI() {
        binding.vm = viewModel
        binding.lifecycleOwner = this
        viewModel.setTrip(trips)

        toolBarTitle(getString(R.string.title_trip_detail))
        initData()
    }

    fun initData() {
        if (PrefUtils.isLogin()) {
            if (trips.user.id.equals(PrefUtils.getUser().id)) {
                binding.clBottomContainer.gone()
            }
        }
        if (trips.adminPost) {
            binding.clBottomContainer.gone()
            binding.btnCall.visible()
        }
    }

    override fun initListener() {
        addStation()
        //directionMap()

        ivOptionToolbar.visible()
        ivOptionToolbar.setOnClickListener {
            initOption()
        }
        listenerBidEditText()
    }

    fun addStation() {
        initTripDetil()
    }

    fun initTripDetil() {
        binding.cvDetail.initData(trips, this)
    }

    fun listenerBidEditText() {
        if (trips.status.equals("1"))
        /**Araç arıyorum*/
        {
            binding.etBid.visible()
        }
        if (trips.status.equals("2"))
        /**Yük arıyorum*/
        {
            binding.btnMessage.visible()
        }
        binding.etBid.listener { text ->
            if (!text.isNullOrEmpty()) {
                binding.btnSendBid.visible()
            } else {
                binding.btnSendBid.gone()
            }
        }
        binding.btnSendBid.setOnClickListener {
            sendBid()
        }
        binding.btnCall.setOnClickListener {
            callPhone()
        }
        binding.btnMessage.setOnClickListener {
            if (PrefUtils.isLogin()) {
                launchActivity<ChatActivity> {
                    val user = User(
                        trips.user.id,
                        trips.user.fullName,
                        trips.user.email,
                        "",
                        trips.user.image,
                        trips.user.rate,
                        trips.firebaseToken!!
                    )
                    putExtra("model", user)
                    putExtra("tripDetail", "tripDetail")
                }
            } else {
                launchActivity<RegisterActivity> {
                    putExtra("when", "chat")
                    CoreApp.notLogin = true
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun callPhone() {
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + trips.phone))
        startActivity(intent)
    }

    fun initOption() {
        if (PrefUtils.isLogin()) {
            val isMyTrip = trips.user.id == PrefUtils.getUserId()
            CvOption().initOption(isMyTrip, this, trips, deleteListener = {
                TripFirebase().deleteTrip(this, trips.id.toString())
            })
        } else {
            launchActivity<RegisterActivity> {
                putExtra("default", "default")
                CoreApp.notLogin = true
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (CoreApp.notLogin) {
            CoreApp.notLogin = false
        }
    }

    fun sendBid() {
        if (PrefUtils.isLogin()) {
            val userMe = PrefUtils.getUser()
            if (userMe?.phone.isNullOrEmpty()) {
                val time = FieldValue.serverTimestamp()
                DialogUtils.showPopupPhone(this) { phoneNumber ->
                    val bid = hashMapOf(
                        "id" to userMe?.id,
                        "fullName" to userMe?.fullName,
                        "phone" to phoneNumber,
                        "price" to binding.etBid.textString(),
                        "firebaseTime" to time
                    )
                    sendData(bid)
                    viewModel.userPhoheUpdate(userMe!!.id, phoneNumber)
                }
            } else {
                val time = FieldValue.serverTimestamp()
                val bid = hashMapOf(
                    "id" to userMe?.id,
                    "fullName" to userMe?.fullName,
                    "phone" to userMe?.phone,
                    "price" to binding.etBid.textString(),
                    "firebaseTime" to time
                )
                sendData(bid)
            }
        } else {
            launchActivity<RegisterActivity> {
                putExtra("bid", "bid")
                CoreApp.notLogin = true
            }
        }
    }

    fun sendData(bid: HashMap<String, Any>) {
        val userMe = PrefUtils.getUser()
        val message = trips.startCityName + " -> " + trips.endCityName + " ilanına " + bid["price"] + " tl teklif verdi \uD83C\uDF89"

        db.collection(CoreApp.testDatabase + "bid")
            .document(trips.user.id)
            .collection(trips.id!!)
            .document(userMe.id)
            .set(bid, SetOptions.merge()).addOnSuccessListener {
                showSucces("Teklifiniz iletilmiştir")
                val data = Data(
                    trips.id!!,
                    userMe.image,
                    userMe.fullName,
                    message,
                    "bid"
                )
                val request = NotificationRequest(data, trips.user.firebaseToken)
                viewModel.postNotification(request)
            }

        binding.etBid.setText("")
    }

}
