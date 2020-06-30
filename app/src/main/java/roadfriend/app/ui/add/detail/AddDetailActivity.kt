package roadfriend.app.ui.add.detail

import android.os.Bundle
import com.android.billingclient.api.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.analytics.FirebaseAnalytics
import kotlinx.android.synthetic.main.bottom_dialog_choose.view.*
import kotlinx.android.synthetic.main.toolbar_layout.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import roadfriend.app.CoreApp
import roadfriend.app.R
import roadfriend.app.data.remote.model.trips.Trips
import roadfriend.app.databinding.AddDetailActivityBinding
import roadfriend.app.ui.base.BindingActivity
import roadfriend.app.ui.sales.SalesActivity
import roadfriend.app.utils.DateUtils
import roadfriend.app.utils.OptionData
import roadfriend.app.utils.PrefUtils
import roadfriend.app.utils.extensions.*
import roadfriend.app.utils.helper.BottomSheetAdapter
import roadfriend.app.utils.helper.TripBundle

class AddDetailActivity : BindingActivity<AddDetailActivityBinding>(), DateUtils.DataListener,
    PurchasesUpdatedListener, AcknowledgePurchaseResponseListener {
    override val getLayoutBindId: Int
        get() = R.layout.add_detail_activity

    private val viewModel by viewModel<AddDetailViewModel>()

    /***Billing*/
    private lateinit var billingClient: BillingClient
    private val skuList = listOf("day", "week", "monday")
    private var isBillingSetupFinished: Boolean = false
    val TAG = this::class.java.name
    private var isPost: Boolean = true

    private val tripModel: TripBundle by lazy { intent.getParcelableExtra("tripModel") as TripBundle }

    override fun initNavigator() {
        viewModel.setPresenter(this)
    }

    override fun initUI() {
        binding.vm = viewModel
        binding.lifecycleOwner = this
        toolBarTitle(getString(R.string.title_add_detail))
        initTripStatus()
    }


    override fun initListener() {

        binding.btnSucces.clickWithDebounce {
            if (checkPhone()) {
                if (isPost) {
                    postTrip()
                }

            }
        }

        binding.cvTripOptionstatus.setOnClickListener {
            openCarTypeBottomSheet()

        }
        back.setOnClickListener {
            onBackPressedSetResult()
        }
    }

    fun checkPhone(): Boolean {
        if (binding.cvPhone.rawText.toString().checkPhoNumber()) {
            return true
        } else {
            showError("Telefon numarasını kontrol ediniz")
            return false
        }

    }

    private fun initTripStatus() {

        val tripType = OptionData.tripsDetailIcon(tripModel.tripStatus, this)
        binding.cvTripOptionstatus.setTopTextNotColor(tripType?.title!!)
        binding.cvTripOptionstatus.setIconNotColor(tripType.icon!!)

    }

    override fun date(date: String, serviceDate: String) {
//        binding.cvDate.setDescText(date)
//        binding.cvDate.setTagDesc(serviceDate)
    }

    override fun time(time: String) {
        // binding.cvTime.setDescText(time)
    }


    fun openCarTypeBottomSheet() {
        var chooseCarType: String? = null
        val dialog = BottomSheetDialog(this, R.style.SheetDialog)
        val view = layoutInflater.inflate(R.layout.bottom_dialog_choose, null)
        val adapterChoose by lazy {
            BottomSheetAdapter(this, arrayListOf(), object : BottomSheetAdapter.IChooseListener {
                override fun choose(position: Int, model: String) {
                    chooseCarType = model
                }
            })
        }
        view.rv.adapter = adapterChoose
        adapterChoose.add(OptionData.tripStatusChooseList(tripModel.tripStatus))

        dialog.setContentView(view)
        dialog.show()
    }

    override fun onBackPressed() {
        onBackPressedSetResult()
    }

    private fun setupBillingClient() {
        billingClient = BillingClient.newBuilder(this)
            .enablePendingPurchases()
            .setListener(this)
            .build()
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    // The BillingClient is ready. You can query purchases here.
                    logger("Setup Billing Done")
                    isBillingSetupFinished = true
                }
            }

            override fun onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
                logger("Failed")

            }
        })

    }

    private fun loadAllSKUs() = if (billingClient.isReady) {
        val params = SkuDetailsParams
            .newBuilder()
            .setSkusList(skuList)
            .setType(BillingClient.SkuType.INAPP)
            .build()
        billingClient.querySkuDetailsAsync(params) { billingResult, skuDetailsList ->
            // Process the result.
            viewModel.getPresenter()?.hideLoading()
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && skuDetailsList!!.isNotEmpty()) {
                for (skuDetails in skuDetailsList) {
                    if (skuDetails.sku == "post") {
                        val billingFlowParams = BillingFlowParams
                            .newBuilder()
                            .setSkuDetails(skuDetails)
                            .build()
                        billingClient.launchBillingFlow(this, billingFlowParams)
                    }
                }
            }

        }

    } else {
        println("Billing Client not ready")
    }

    override fun onPurchasesUpdated(
        billingResult: BillingResult,
        purchases: MutableList<Purchase>?
    ) {
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            for (purchase in purchases) {
                handlePurchase(purchase)
            }
        } else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
            // Handle an error caused by a user cancelling the purchase flow.
            logger(billingResult.debugMessage.toString())


        } else {
            logger(billingResult.debugMessage.toString())
            // Handle any other error codes.
        }
    }

    fun postTrip() {
        val userMe = PrefUtils.getUser()
        val trips = Trips(
            id = "",
            user = userMe,
            phone = binding.cvPhone.rawText.toString(),
            description = binding.editText.textString(),
            status = tripModel.tripStatus!!,
            price = binding.cvPrice.getPrice(),
            paymentType = "free",
            startCity = tripModel.tripsList.get(0),
            endCity = tripModel.tripsList.get(1),
            startCityName = tripModel.tripsList.get(0).name,
            endCityName = tripModel.tripsList.get(1).name,
            ads = false
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

    fun handlePurchase(purchase: Purchase) {
        val consumeParams =
            ConsumeParams.newBuilder()
                .setPurchaseToken(purchase.purchaseToken)
                .build()

        billingClient.consumeAsync(consumeParams) { billingResult, outToken ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                //Toast.makeText(this, "KAldırıldı", Toast.LENGTH_SHORT).show()
            }
        }
        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
            if (!purchase.isAcknowledged) {
                var acknowledgePurchaseParams =
                    AcknowledgePurchaseParams.newBuilder()
                        .setPurchaseToken(purchase.purchaseToken)
                        .build();
                billingClient.acknowledgePurchase(
                    acknowledgePurchaseParams,
                    this
                );

            }
        }
    }

    override fun onAcknowledgePurchaseResponse(p0: BillingResult) {
        var firebaseAnalytics: FirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        var bundle = Bundle()
        bundle.putString(CoreApp.testDatabase + "post", "post")
        firebaseAnalytics.logEvent(CoreApp.testDatabase + "satin_alma", bundle)
    }

}

