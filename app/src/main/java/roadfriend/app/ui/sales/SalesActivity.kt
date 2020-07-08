package roadfriend.app.ui.sales

import android.os.Bundle
import android.widget.Button
import com.android.billingclient.api.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.analytics.FirebaseAnalytics
import kotlinx.android.synthetic.main.bottom_dialog_sales.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import roadfriend.app.CoreApp
import roadfriend.app.CoreApp.Companion.db
import roadfriend.app.R
import roadfriend.app.customviews.CVBillingView
import roadfriend.app.data.remote.model.trips.Trips
import roadfriend.app.databinding.SalesActivityBinding
import roadfriend.app.ui.add.detail.AddDetailActivity
import roadfriend.app.ui.base.BindingActivity
import roadfriend.app.ui.home.FirstFragment
import roadfriend.app.ui.home.SecondFragment
import roadfriend.app.ui.main.MainActivity
import roadfriend.app.utils.DialogUtils
import roadfriend.app.utils.OptionData
import roadfriend.app.utils.PrefUtils
import roadfriend.app.utils.extensions.launchActivity
import roadfriend.app.utils.extensions.logger
import roadfriend.app.utils.extensions.showError
import roadfriend.app.utils.extensions.showSucces
import roadfriend.app.utils.manager.EventManager

class SalesActivity : BindingActivity<SalesActivityBinding>(), PurchasesUpdatedListener,
    AcknowledgePurchaseResponseListener {
    override val getLayoutBindId: Int
        get() = R.layout.sales_activity


    private val viewModel by viewModel<SalesViewModel>()

    /***Billing*/
    private lateinit var billingClient: BillingClient
    private val skuList = listOf("day", "week", "monday")
    private var moneyType: String? = null
    private var isBillingSetupFinished: Boolean = false
    lateinit var salesBottomDialog: BottomSheetDialog


    /**get data*/
    private val tripData: Trips by lazy { intent.getParcelableExtra("data") as Trips }

    override fun initNavigator() {
        viewModel.setPresenter(this)
    }

    override fun initUI() {
        binding.vm = viewModel
        binding.lifecycleOwner = this
        setupBillingClient()
        initData()
        whereComing()
    }

    fun initData() {
        binding.vm!!.setTrips(tripData)
        binding.cvDetail.initData(tripData, this)
    }

    override fun initListener() {
        binding.btnPayment.setOnClickListener {
            EventManager.clickSatisOnizleme()
            openPaymentBottomSheet()
        }
        binding.btnNoThanks.setOnClickListener {
            onBackPressed()
        }
    }

    fun whereComing() {
        if (intent.getStringExtra("intent") == AddDetailActivity::class.java.name) {
            showSucces(getString(R.string.ilan_paylasildi_mesaj))
        }
        if (intent.hasExtra("premium")){
            openPaymentBottomSheet()
        }
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

    private fun loadAllSKUs(btnPayment: Button) = if (billingClient.isReady) {
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
                    if (skuDetails.sku == moneyType) {
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
        setupBillingClient()
        EventManager.setupBillingClient()
        println("Billing Client not ready")
    }

    override fun onPurchasesUpdated(
        billingResult: BillingResult,
        purchases: MutableList<Purchase>?
    ) {
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            for (purchase in purchases) {
                salesBottomDialog.dismiss()
                tripUpdatePremium()
                handlePurchase(purchase)
                // acknowledgePurchase(purchase.purchaseToken)

            }
        } else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
            // Handle an error caused by a user cancelling the purchase flow.
            logger("User Cancelled")
            EventManager.userCancelled()
            logger(billingResult.debugMessage.toString())



        } else {
            logger(billingResult.debugMessage.toString())
            // Handle any other error codes.
            EventManager.salesError()
        }
    }


    override fun onBackPressed() {
        if (PrefUtils.isRated()) {
            if (intent.getStringExtra("intent") == AddDetailActivity::class.java.name) {
                launchActivity<MainActivity> { }
            }
            if (intent.getStringExtra("intent") == FirstFragment::class.java.name) {
                finish()
            }
            if (intent.getStringExtra("intent") == SecondFragment::class.java.name) {
                finish()
            } else {
                finish()
            }
        } else {
            DialogUtils.showPopupRate(this, this) {
                launchActivity<MainActivity> { }
            }
        }

    }

    fun openPaymentBottomSheet() {
        salesBottomDialog = BottomSheetDialog(this, R.style.SheetDialog)
        val view = layoutInflater.inflate(R.layout.bottom_dialog_sales, null)

        val list: ArrayList<CVBillingView> = arrayListOf(
            view.billingOne, view.billingTwo, view.billingThree
        )

        view.billingOne.setDay("5 " + resources.getString(R.string.day))
        view.billingTwo.setDay("7 " + resources.getString(R.string.day))
        view.billingThree.setDay("10 " + resources.getString(R.string.day))

        /**Default select*/
        view.billingOne.select(view.billingOne, view.btnPayment, list)
        moneyType = skuList.get(0)
        view.tvContent.text = OptionData.getPaymanetInfoText(0, tripData.status)

        view.billingOne.setOnClickListener {
            moneyType = skuList.get(0)
            view.billingOne.select(view.billingOne, view.btnPayment, list)
            view.tvContent.text = OptionData.getPaymanetInfoText(0, tripData.status)
        }
        view.billingTwo.setOnClickListener {
            moneyType = skuList.get(1)
            view.billingTwo.select(view.billingTwo, view.btnPayment, list)
            view.tvContent.text = OptionData.getPaymanetInfoText(1, tripData.status)
        }
        view.billingThree.setOnClickListener {
            moneyType = skuList.get(2)
            view.billingThree.select(view.billingThree, view.btnPayment, list)
            view.tvContent.text = OptionData.getPaymanetInfoText(2, tripData.status)
        }

        view.btnPayment.setOnClickListener {
            if (isBillingSetupFinished) {
                loadAllSKUs(view.btnPayment)
            }
        }
        view.ivClose.setOnClickListener {
            salesBottomDialog.dismiss()
        }
        salesBottomDialog.setContentView(view)
        salesBottomDialog.show()
    }

    fun tripUpdatePremium() {
        db.collection(CoreApp.testDatabase + "trip").document(tripData.documentKey!!)
            .update("paymentType", moneyType)
            .addOnSuccessListener {
                val model = DialogUtils.DialogModel(
                    "Tebrikler ilanınız daha fazla kişiye ulaşacaktır.",
                    "Tamam",
                    "",
                    R.drawable.ic_success,
                    false
                )
                DialogUtils.showPopupInfo(this, model, btnOk = {
                    onBackPressed()
                })
            }
            .addOnFailureListener {
                showError("Bir şeyler ters gitti")
            }
    }

    fun handlePurchase(purchase: Purchase) {
        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
            if (!purchase.isAcknowledged) {
                var acknowledgePurchaseParams =
                    AcknowledgePurchaseParams.newBuilder()
                        .setPurchaseToken(purchase.purchaseToken)
                        .build();
                billingClient.acknowledgePurchase(
                    acknowledgePurchaseParams,
                    this
                )

            }
        }
        val consumeParams =
            ConsumeParams.newBuilder()
                .setPurchaseToken(purchase.purchaseToken)
                .build()

        billingClient.consumeAsync(consumeParams) { billingResult, outToken ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                var firebaseAnalytics: FirebaseAnalytics = FirebaseAnalytics.getInstance(this)
                var bundle = Bundle()
                bundle.putString(CoreApp.testDatabase + "tuketildi", "tuketildi")
                firebaseAnalytics.logEvent(CoreApp.testDatabase + "tuketildi", bundle)
            }
        }
    }

    override fun onAcknowledgePurchaseResponse(p0: BillingResult) {
        EventManager.analyticsSendOrderComplete(tripData, moneyType)
    }


}
