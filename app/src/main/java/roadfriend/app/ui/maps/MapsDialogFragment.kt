package roadfriend.app.ui.maps

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import roadfriend.app.R
import roadfriend.app.data.local.model.MapsModel
import roadfriend.app.databinding.MapsDialogFragmentBinding
import roadfriend.app.ui.base.BaseDialogFragment
import roadfriend.app.ui.base.BindingDialogFragment
import roadfriend.app.ui.main.MainViewModel

class MapsDialogFragment : BindingDialogFragment<MapsDialogFragmentBinding>() {

    override fun createBinding() = MapsDialogFragmentBinding.inflate(layoutInflater)

    private val viewModel by viewModel<MapViewModel>()

    companion object {
        fun newInstance(searchType: MapsModel): MapsDialogFragment {
            return MapsDialogFragment().apply {
                setStyle(STYLE_NORMAL, R.style.DialogFragmentTheme)
                arguments = Bundle().apply { putParcelable("maps", searchType) }
            }
        }
    }

    override fun initListener() {

    }

    override fun initNavigator() {
        viewModel.setPresenter(this)
    }

    override fun initUI() {
        val model = arguments?.getParcelable<MapsModel>("maps") as MapsModel
        var bundle = Bundle()
        bundle.putParcelable("maps", model)
        val fragment = MapFragment()
        fragment.arguments = bundle
        childFragmentManager.beginTransaction().replace(R.id.frameLayoutMaps, fragment).commit()
        setToolbar()
    }


    fun setToolbar() {
        binding.include4.tvToolbarTitle.text = getString(R.string.haritada_gor)
        //    containerToolbar.backgroundColor(R.color.white)
        binding.include4.back.setOnClickListener {
            dismiss()
        }
    }

}
