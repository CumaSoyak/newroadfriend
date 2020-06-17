package roadfriend.app.ui.add.detail

import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.bottom_dialog_choose.view.*
import kotlinx.android.synthetic.main.toolbar_layout.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import roadfriend.app.R
import roadfriend.app.data.remote.model.trips.Trips
import roadfriend.app.databinding.AddDetailActivityBinding
import roadfriend.app.ui.base.BindingActivity
import roadfriend.app.ui.sales.SalesActivity
import roadfriend.app.utils.DateUtils
import roadfriend.app.utils.OptionData
import roadfriend.app.utils.PrefUtils
import roadfriend.app.utils.extensions.checkPhoNumber
import roadfriend.app.utils.extensions.launchActivity
import roadfriend.app.utils.extensions.showError
import roadfriend.app.utils.extensions.textString
import roadfriend.app.utils.helper.BottomSheetAdapter
import roadfriend.app.utils.helper.TripBundle

class AddDetailActivity : BindingActivity<AddDetailActivityBinding>(), DateUtils.DataListener {
    override val getLayoutBindId: Int
        get() = R.layout.add_detail_activity

    private val viewModel by viewModel<AddDetailViewModel>()

    val TAG = this::class.java.name

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

        binding.btnSucces.setOnClickListener {
            if (checkPhone()) {
//                val cityData = HashMap<String?, Int?>()
//
//                for (index in 0 until TripBundle.tripsList.size) {
//                    cityData.put("cities[${index}]", TripBundle.tripsList[index].id)
//                }
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
                        launchActivity<SalesActivity> {
                            trips?.id = tripUuid
                            putExtra("data", trips)
                            putExtra("intent", TAG)
                        }
                    }
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
}

