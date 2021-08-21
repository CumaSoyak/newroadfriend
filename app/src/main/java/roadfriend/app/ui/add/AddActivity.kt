package roadfriend.app.ui.add


import org.koin.androidx.viewmodel.ext.android.viewModel
import roadfriend.app.R
import roadfriend.app.customviews.CVChooseStatus
import roadfriend.app.databinding.ActivityAddBinding
import roadfriend.app.ui.add.direction.AddDirectionActivity
import roadfriend.app.ui.base.BindingActivity
import roadfriend.app.utils.extensions.launchActivity


class AddActivity : BindingActivity<ActivityAddBinding>() {

    override fun createBinding()= ActivityAddBinding.inflate(layoutInflater)

    private val viewModel by viewModel<AddViewModel>()

    override fun initNavigator() {
        viewModel.setPresenter(this)
    }

    companion object {
        var mTripType: String? = null
    }

    override fun initUI() {
        binding.vm = viewModel
        binding.lifecycleOwner = this

        binding.btnSucces.setOnClickListener {
            passPage()
        }
        toolBarTitle(getString(R.string.ne_ariyorsun))
    }

    private fun passPage() {
        launchActivity<AddDirectionActivity> {
            putExtra("status", mTripType)
        }
    }

    override fun initListener() {
        selectStatus()

    }


    fun selectStatus() {
        val list: ArrayList<CVChooseStatus> = arrayListOf(binding.truckDriver, binding.truckPacket)
        binding.truckDriver.setOnClickListener {
            binding.truckDriver.select(binding.truckDriver, binding.btnSucces, list)
            mTripType = "1"
        }

        binding.truckPacket.setOnClickListener {
            binding.truckPacket.select(binding.truckPacket, binding.btnSucces, list)
            mTripType = "2"
        }
    }

    override fun onBackPressed() {
        onBackPressedSetResult()
    }


}
