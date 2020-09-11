package roadfriend.app.ui.dashboard

import com.google.firebase.Timestamp
import org.koin.androidx.viewmodel.ext.android.viewModel
import roadfriend.app.CoreApp
import roadfriend.app.R
import roadfriend.app.data.remote.model.trips.Trips
import roadfriend.app.databinding.ActivityDashBoardBinding
import roadfriend.app.ui.base.BindingActivity
import roadfriend.app.ui.main.MainActivity
import roadfriend.app.utils.extensions.launchActivity
import roadfriend.app.utils.extensions.showError
import roadfriend.app.utils.extensions.showToast

class DashBoardActivity : BindingActivity<ActivityDashBoardBinding>() {

    private val viewModel by viewModel<DashBoardViewModel>()

    override val getLayoutBindId: Int
        get() = R.layout.activity_dash_board

    override fun initNavigator() {
        viewModel.setPresenter(this)
    }

    override fun initUI() {
        binding.vmDashBoard = viewModel
        binding.lifecycleOwner = this
        //getDefaultTrip()
    }

    override fun initListener() {
//        viewModel.tripSize()
//        viewModel.userSize()
        binding.devam.setOnClickListener {
            launchActivity<MainActivity> { }
        }
        binding.btnTest.setOnClickListener {
            CoreApp.testDatabase = "testDataBase-"
            launchActivity<MainActivity> { }
        }
        binding.btnAdd.setOnClickListener {
            CoreApp.addAdminTrip = true
            launchActivity<MainActivity> { }
        }
      //  getDefaultTrip()
    }

    fun getDefaultTrip() {
        //1 gün 86400
        //1 hafta 604800
        //1 ay 2629743
        val docRef = CoreApp.db.collection("trip")
        docRef.addSnapshotListener { snapshot, e ->
            snapshot?.forEachIndexed { index, queryDocumentSnapshot ->
                val data: Trips = queryDocumentSnapshot.toObject(Trips::class.java)
                if (queryDocumentSnapshot["firebaseTime"] != null) {
                    val seconds = (queryDocumentSnapshot["firebaseTime"] as Timestamp)
                    data.firebaseTimeSecond = seconds.seconds
                } else {
                    data.firebaseTimeSecond = 1000
                }
                if (!data.purchaseToken.isNullOrEmpty()) {
                    print("")
                }
//                if (!data.paymentType.equals("free")) {
//                    val current = getCurrentDate()
//                    if (data.paymentType.equals("day") && current - data.firebaseTimeSecond!! > 432000000)
//                        updateTrip(data)
//                    if (data.paymentType.equals("week") && current - data.firebaseTimeSecond!! > 604800000)
//                        updateTrip(data)
//                    if (data.paymentType.equals("monday") && current - data.firebaseTimeSecond!! > 864000000)
//                        updateTrip(data)
//                }
//                if (data.documentKey != null) {
//                    if (arrayListOf(
//                            "at",
//                            "be",
//                            "bg",
//                            "dk",
//                            "fi",
//                            "fr",
//                            "de",
//                            "gr",
//                            "hu",
//                            "ıt",
//                            "ıe",
//                            "lt",
//                            "mt",
//                            "pl",
//                            "pt",
//                            "ro",
//                            "sk",
//                            "si",
//                            "es",
//                            "gb"
//                        ).contains(data.codeCountry)
//                    ) {
//                        CoreApp.db.collection("trip").document(data.documentKey!!)
//                            .update("codeCountry", "europe").addOnSuccessListener {
//                                print("CumaSoyak")
//                            }
//                    }
//
//                }

            }

        }
    }

    fun updateTrip(trips: Trips) {
        CoreApp.db.collection("trip").document(trips.documentKey!!)
            .update("paymentType", "free")
            .addOnSuccessListener {
                showToast("Güncellendi")
            }
            .addOnFailureListener {
                showError("Bir şeyler ters gitti")
            }
    }

}
