package roadfriend.app.customviews

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
 import roadfriend.app.R
import roadfriend.app.data.remote.model.trips.GetTripRequest
import roadfriend.app.databinding.CvDetailBinding
import roadfriend.app.databinding.CvEmptyViewBinding
import roadfriend.app.ui.biddetail.BidDetailActivity
import roadfriend.app.ui.profile.myaboutcomment.MyAboutCommentsActivity
import roadfriend.app.ui.profile.mytrip.MyTripsActivity
import roadfriend.app.ui.profile.savedtrip.SavedTripActivity
import roadfriend.app.utils.OptionData
import roadfriend.app.utils.extensions.getString
import roadfriend.app.utils.extensions.gone
import roadfriend.app.utils.extensions.visible

class CVEmptyView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val binding = CvEmptyViewBinding.inflate(LayoutInflater.from(context), this, true)



    fun initData(
        context: Context,
        fragmentName: String? = null,
        getTripRequest: GetTripRequest? = null
    ) {
        this.visible()
        if (!fragmentName.isNullOrEmpty()) {
            if (fragmentName.equals("home")) {
             binding.   ilanVer.visible()
                setData(homeSetring(getTripRequest), R.drawable.ic_search)
            } else if (fragmentName.equals("homedefault")) {
                setData(context.getString(R.string.ilk_ilanı_sen_ver), R.drawable.ic_fireworks)
            } else if (fragmentName.equals("notification")) {
                setData(context.getString(R.string.gelen_teklif_yok), R.drawable.ic_cuzdan)
            } else if (fragmentName.equals("message")) {
                setData(context.getString(R.string.mesaj_yok), R.drawable.ic_message)
            }
        } else if (context is Activity) {
            if (context is MyTripsActivity) {
                setData(
                    context.getString(R.string.yolculugunuz_bulunmaktadir),
                    R.drawable.ic_mytrips
                )
            } else if (context is SavedTripActivity) {
                setData(
                    context.getString(R.string.kayitli_yolculugunuz_bulunmaktadir),
                    R.drawable.ic_save
                )
            } else if (context is MyAboutCommentsActivity) {
                setData(
                    context.getString(R.string.hakkinizda_yorum_bulunmaktadir),
                    R.drawable.ic_comment
                )
            } else if (context is BidDetailActivity) {
                setData(context.getString(R.string.bu_ilan_icin_teklif_yok), R.drawable.ic_money)
            }

        }

    }

    private fun setData(text: String, icon: Int) {
        binding.  ivEmpty.setImageResource(icon)
        binding.  tvEmpty.text = text
    }

    fun homeSetring(tripRequest: GetTripRequest?): String {
        return "${getString(R.string.ilan_bulunamadı)}\n \n  ${tripRequest?.startCity} -----> ${tripRequest?.endCity} \n \n  \n \n  ${getString(R.string.ilan_vermek_istermisiniz)}"
    }

    fun initlistener(click: () -> Unit) {
        binding.  ilanVer.setOnClickListener {
            click.invoke()
        }
    }

    fun hideView() {
        this.gone()
    }
}

class CVEmptyEvent(val event: Boolean)