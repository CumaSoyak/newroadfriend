package roadfriend.app.customviews

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import roadfriend.app.R
import roadfriend.app.data.remote.model.trips.GetTripRequest
import roadfriend.app.ui.biddetail.BidDetailActivity
import roadfriend.app.ui.profile.myaboutcomment.MyAboutCommentsActivity
import roadfriend.app.ui.profile.mytrip.MyTripsActivity
import roadfriend.app.ui.profile.savedtrip.SavedTripActivity
import roadfriend.app.utils.OptionData
import roadfriend.app.utils.extensions.gone
import roadfriend.app.utils.extensions.visible
import kotlinx.android.synthetic.main.cv_empty_view.view.*

class CVEmptyView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    init {
        LayoutInflater.from(context).inflate(R.layout.cv_empty_view, this)
    }

    fun initData(
        context: Context,
        fragmentName: String? = null,
        getTripRequest: GetTripRequest? = null
    ) {
        this.visible()
        if (!fragmentName.isNullOrEmpty()) {
            if (fragmentName.equals("home")) {
                ilanVer.visible()
                setData(homeSetring(getTripRequest), R.drawable.ic_search)
            } else if (fragmentName.equals("notification")) {
                setData("Gelen telkif yok", R.drawable.ic_cuzdan)
            } else if (fragmentName.equals("message")) {
                setData("Mesajınız yok.", R.drawable.ic_message)
            }
        } else if (context is Activity) {
            if (context is MyTripsActivity) {
                setData("Yolculuğunuz bulunmamaktadır.", R.drawable.ic_mytrips)
            } else if (context is SavedTripActivity) {
                setData("Kayıtlı yolculuğunuz \n bulunmamaktadır.", R.drawable.ic_save)
            } else if (context is MyAboutCommentsActivity) {
                setData("Hakkınızda yorum \n bulunmamaktadır.", R.drawable.ic_comment)
            } else if (context is BidDetailActivity) {
                setData("Bu ilan için teklif yok", R.drawable.ic_money)
            }

        }

    }

    private fun setData(text: String, icon: Int) {
        ivEmpty.setImageResource(icon)
        tvEmpty.text = text
    }

    fun homeSetring(tripRequest: GetTripRequest?): String {
        return "İlan Bulunamadı.\n \n  ${tripRequest?.startCity} -----> ${tripRequest?.endCity} \n \n ${OptionData.tripatus(
            tripRequest?.status!!
        )}  \n \n  İlanı Vermek ister misiniz ?"
    }

    fun initlistener(click: () -> Unit) {
        ilanVer.setOnClickListener {
            click.invoke()
        }
    }

    fun hideView() {
        this.gone()
    }
}

class CVEmptyEvent(val event: Boolean)