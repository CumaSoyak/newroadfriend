package roadfriend.app.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.item_premium.view.*
import roadfriend.app.R
import roadfriend.app.data.remote.model.trips.Trips
import roadfriend.app.ui.profile.mytrip.MyTripsActivity
import roadfriend.app.ui.sales.SalesActivity
import roadfriend.app.utils.DialogUtils
import roadfriend.app.utils.extensions.launchActivity
import roadfriend.app.utils.manager.EventManager

class CvPremium @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    init {
        LayoutInflater.from(context).inflate(R.layout.item_premium, this)

    }

    fun visibleContainer(trips: ArrayList<Trips>) {
        tvTitle.isSelected = true
        container.setOnClickListener {
            EventManager.premiumShow()
            if (trips.size > 1) {
                context.launchActivity<MyTripsActivity> {
                    putExtra("premium", "premium")
                }

            } else {
                context.launchActivity<SalesActivity> {
                    putExtra("data", trips.get(0))
                    putExtra("intent", "TAG")
                    putExtra("premium", "premium")
                }
            }
        }


    }

    fun showPremium() {
        val model = DialogUtils.DialogModel(
            "İlanını öne çıkararak daha fazla kişiye ulaşmak istermisin.",
            context.getString(R.string.tamam),
            context.getString(R.string.vazgec),
            R.drawable.ic_success,
            true,
            false
        )
        DialogUtils.showPopupInfo(context, model, btnOk = {
            context.launchActivity<MyTripsActivity> {
                putExtra("premium", "premium")
            }
        }, btnDesc = {

        })
    }
}