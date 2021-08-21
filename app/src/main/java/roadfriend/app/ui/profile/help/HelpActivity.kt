package roadfriend.app.ui.profile.help

import com.google.firebase.firestore.SetOptions
import org.koin.androidx.viewmodel.ext.android.viewModel
import roadfriend.app.CoreApp
import roadfriend.app.CoreApp.Companion.db
import roadfriend.app.R
import roadfriend.app.data.remote.model.Help
import roadfriend.app.databinding.HelpActivityBinding
import roadfriend.app.ui.base.BindingActivity
import roadfriend.app.utils.OptionData
import roadfriend.app.utils.PrefUtils
import roadfriend.app.utils.extensions.gone
import roadfriend.app.utils.extensions.listener
import roadfriend.app.utils.extensions.showSucces
import roadfriend.app.utils.extensions.visible
import roadfriend.app.utils.helper.genericadapter.GenericAdapter
import java.util.*

class HelpActivity : BindingActivity<HelpActivityBinding>() {

    override fun createBinding()= HelpActivityBinding.inflate(layoutInflater)

    private val viewModel by viewModel<HelpViewModel>()

    private val adapter = GenericAdapter<Help>(R.layout.item_help)

    override fun initNavigator() {
        viewModel.setPresenter(this)
    }

    override fun initUI() {
        binding.vm = viewModel
        binding.lifecycleOwner = this
        support()
        binding.btnMessage.setOnClickListener {
            createSupport()
        }
    }


    override fun initListener() {
        binding.rv.adapter = adapter
        initHelpType()
    }

    fun initHelpType() {
        if (intent.hasExtra("kosul")) {
            adapter.addItems(OptionData.getConditionList())
            toolBarTitle(getString(R.string.kosul_sartlar))
        } else if (intent.hasExtra("support")) {
            toolBarTitle(getString(R.string.iletisime_gec))
            binding.containerSupport.visible()
            binding.rv.gone()
        } else {
            adapter.addItems(OptionData.getHelpMessageList())
            toolBarTitle(getString(R.string.yardim_profil))
        }
    }

    fun support() {
        binding.btnMessage.gone()
        binding.etMessage.listener { text ->
            if (text.isNullOrEmpty()) {
                binding.btnMessage.gone()
            } else {
                binding.btnMessage.visible()
            }
        }
    }

    fun createSupport() {
        if (!binding.etMessage.text.toString().isNullOrEmpty()) {
            val uuid = UUID.randomUUID().toString()
            val data = hashMapOf(
                "text" to binding.etMessage.text.toString(),
                "user" to PrefUtils.getUser()
            )
            db.collection(CoreApp.testDatabase + "supportMessage")
                .document(uuid)
                .set(data, SetOptions.merge()).addOnSuccessListener {
                    showSucces("Mesajınız alındı \n Sizinle iletişime geçilecektir.")
                }
        } else {
            showSucces("Mesaj giriniz")
        }

    }

}
