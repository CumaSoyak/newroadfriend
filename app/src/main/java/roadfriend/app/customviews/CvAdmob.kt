package roadfriend.app.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import roadfriend.app.BuildConfig
import roadfriend.app.R
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView

class CvAdmob @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    init {
        if (BuildConfig.DEBUG) {
            LayoutInflater.from(context).inflate(R.layout.item_admob_debug, this)
        } else {
            LayoutInflater.from(context).inflate(R.layout.item_admob, this)
            val adRequest = AdRequest.Builder().build()
            val addMob = findViewById<AdView>(R.id.adView)
            addMob.loadAd(adRequest)
        }

    }
}