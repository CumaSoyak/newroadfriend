package roadfriend.app.ui.profile.setting

import android.app.Activity
import android.content.Intent
import android.net.Uri
import roadfriend.app.R
import roadfriend.app.databinding.ProfilSettingsActivityBinding
import roadfriend.app.ui.base.BindingActivity
import roadfriend.app.utils.extensions.*
import roadfriend.app.utils.extensions.ImagePickerActivity.Companion.REQUEST_IMAGE
 import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.IOException

class ProfilSettingsActivity : BindingActivity<ProfilSettingsActivityBinding>(),
    ImagePickerActivity.PickerOptionListener {

    override fun createBinding()= ProfilSettingsActivityBinding.inflate(layoutInflater)

    private val viewModel by viewModel<ProfilSettingsViewModel>()

    var uri: Uri? = null

    override fun initNavigator() {
        viewModel.setPresenter(this)
    }

    override fun initUI() {
        toolBarTitle("Profil Ayarları")
        binding.vm = viewModel
        binding.lifecycleOwner = this
        viewModel.setUser()
    }

    override fun initListener() {
        binding.circleImageView.setOnClickListener {
            ImagePickerActivity().openCameraOrGalery(this, this)
        }
        binding.btnSave.setOnClickListener {
         }
        visibiltyPasswordChangePage()
    }

    override fun onChooseGallerySelected() {
        val intent = Intent(this, ImagePickerActivity::class.java)
        intent.putExtra(
            ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION,
            ImagePickerActivity.REQUEST_GALLERY_IMAGE
        )
        startActivityForResult(intent, REQUEST_IMAGE)
    }

    override fun onTakeCameraSelected() {
        val intent = Intent(this, ImagePickerActivity::class.java)
        intent.putExtra(
            ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION,
            ImagePickerActivity.REQUEST_IMAGE_CAPTURE
        )
        startActivityForResult(intent, REQUEST_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE && data != null) {
            if (resultCode == Activity.RESULT_OK) {
                uri = data.getParcelableExtra<Uri>("path")
                try {
                    binding.circleImageView.load(uri.toString())
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }

    }



    fun visibiltyPasswordChangePage() {
        binding.btnPasswordChangePage.setOnClickListener {
            launchActivity<ChangePasswordActivity> { }
            //  binding.passwordChangeContainer.visible()
        }
    }


}
