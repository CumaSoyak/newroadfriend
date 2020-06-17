package roadfriend.app.ui.maps

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import roadfriend.app.R
import roadfriend.app.data.local.model.MapsModel
import kotlinx.android.synthetic.main.toolbar_layout.*

class MapsDialogFragment : DialogFragment() {

    companion object {
        fun newInstance(searchType: MapsModel): MapsDialogFragment {
            return MapsDialogFragment().apply {
                setStyle(STYLE_NORMAL, R.style.DialogFragmentTheme)
                arguments = Bundle().apply { putParcelable("maps", searchType) }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.maps_dialog_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val model = arguments?.getParcelable<MapsModel>("maps") as MapsModel
        var bundle = Bundle()
        bundle.putParcelable("maps", model)
        val fragment = MapFragment()
        fragment.arguments = bundle
        childFragmentManager.beginTransaction().replace(R.id.frameLayoutMaps, fragment).commit()
        setToolbar()
    }

    fun setToolbar() {
        tvToolbarTitle.text = "Haritada Gör"
    //    containerToolbar.backgroundColor(R.color.white)
        back.setOnClickListener {
            dismiss()
        }
    }

}
