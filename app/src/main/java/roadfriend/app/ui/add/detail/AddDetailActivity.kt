package roadfriend.app.ui.add.detail

import com.android.billingclient.api.BillingClient
import kotlinx.android.synthetic.main.toolbar_layout.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import roadfriend.app.R
import roadfriend.app.data.remote.model.trips.Trips
import roadfriend.app.databinding.AddDetailActivityBinding
import roadfriend.app.ui.base.BindingActivity
import roadfriend.app.ui.sales.SalesActivity
import roadfriend.app.utils.DateUtils.getDate
import roadfriend.app.utils.PrefUtils
import roadfriend.app.utils.extensions.*
import roadfriend.app.utils.helper.TripBundle

class AddDetailActivity : BindingActivity<AddDetailActivityBinding>() {
    override val getLayoutBindId: Int
        get() = R.layout.add_detail_activity

    private val viewModel by viewModel<AddDetailViewModel>()

    /***Billing*/
    private lateinit var billingClient: BillingClient
    private val skuList = listOf("day", "week", "monday")
    private var isBillingSetupFinished: Boolean = false
    val TAG = this::class.java.name
    private var isPost: Boolean = true

    private val tripModel: TripBundle by lazy { intent.getParcelableExtra<TripBundle>("tripModel") as TripBundle }

    override fun initNavigator() {
        viewModel.setPresenter(this)
    }

    override fun initUI() {
        binding.vm = viewModel
        binding.lifecycleOwner = this
        toolBarTitle(getString(R.string.title_add_detail))
    }


    override fun initListener() {

        binding.btnSucces.clickWithDebounce {
            if (true) {
                if (isPost) {
                    postTrip()
                }

            }
        }
        back.setOnClickListener {
            onBackPressedSetResult()
        }
        binding.cvDate.setOnClickListener {
            getDate {
                binding.cvDate.setDescText(it)
                binding.cvDate.setTagDesc(it)
            }
        }
    }

    fun checkPhone(): Boolean {
        if (!binding.cvPhone.rawText.toString().checkPhoNumber()) {
            showError(getString(R.string.telefon_numarasi_giriniz))
            return false
        } else if (binding.cvDate.getTagDesc().isNullOrEmpty()) {
            showError(getString(R.string.takvim_tarih_sec))
            return false
        } else {
            return true
        }

    }


    override fun onBackPressed() {
        onBackPressedSetResult()
    }


    fun postTrip() {
        val userMe = PrefUtils.getUser()
        val trips = Trips(
            id = "",
            user = userMe,
            phone = "",
            description = binding.editText.textString(),
            status = tripModel.tripStatus!!,
            date = binding.cvDate.getTagDesc(),
            price = binding.cvPrice.getPrice(),
            paymentType = "free",
            startCity = tripModel.tripsList.get(0),
            endCity = tripModel.tripsList.get(1),
            startCityName = tripModel.tripsList.get(0).name,
            endCityName = tripModel.tripsList.get(1).name,
            ads = false, bidOption = false

        )


        viewModel.postTripAdverment(trips) { isSucces, trips, tripUuid ->
            if (isSucces) {
                isPost = false
                launchActivity<SalesActivity> {
                    trips?.id = tripUuid
                    putExtra("data", trips)
                    putExtra("intent", TAG)
                }
            }
        }
    }


}

