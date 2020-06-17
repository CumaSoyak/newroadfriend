package roadfriend.app.customviews

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import roadfriend.app.R
import roadfriend.app.data.local.model.MapsModel
import roadfriend.app.data.remote.model.city.City
import roadfriend.app.data.remote.model.trips.Trips
import roadfriend.app.ui.base.BaseActivity
import roadfriend.app.ui.maps.MapFragment
import roadfriend.app.ui.sales.SalesActivity
import roadfriend.app.utils.OptionData
import roadfriend.app.utils.extensions.gone
import roadfriend.app.utils.extensions.phoneFormat
import roadfriend.app.utils.extensions.setString
import roadfriend.app.utils.helper.genericadapter.GenericAdapter
import kotlinx.android.synthetic.main.cv_detail.view.*

class CVDetail @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {


    val adapter = GenericAdapter<City>(R.layout.item_trips_station_list)

    init {
        LayoutInflater.from(context).inflate(R.layout.cv_detail, this)
    }

    fun initData(trips: Trips, activity: BaseActivity) {
        setStatus(trips)
        setMaps(trips, activity)
        setPhone(trips.phone)
        setPrice(trips.callPrice())
        setDescription(trips.description)
    }

    private fun setStatus(trips: Trips) {
        tripStatus.text = OptionData.tripatus(trips.status)
    }

    private fun setMaps(trips: Trips, activity: BaseActivity) {
        val tripsList: ArrayList<Trips> = arrayListOf(trips)
        val mapsModel = MapsModel()
        mapsModel.trips.addAll(tripsList)
        mapsModel.type = if (activity is SalesActivity) {
            "sales"
        } else {
            "tripDetail"
        }
        var bundle = Bundle()
        bundle.putParcelable("maps", mapsModel)
        bundle.putString("tripDetailActivity", "tripDetailActivity")
        val fragment = MapFragment()
        fragment.arguments = bundle
        activity.supportFragmentManager.beginTransaction().replace(R.id.fLTripDetail, fragment)
            .commit()
    }

    private fun setCity(trips: Trips) {
        cityShowListener()
    }

    fun cityShowListener() {
        tvAllCityShow.setOnClickListener {
            if (expandedLayout.isExpanded) {
                tvAllCityShow.setString(R.string.hide_all_city)
                expandedLayout.isExpanded = false
            } else {
                tvAllCityShow.setString(R.string.show_all_city)
                expandedLayout.isExpanded = true
            }
        }
    }

    fun setPhone(phone: String?) {
        if (phone.isNullOrEmpty()) {
            cvPhone.gone()
        } else {
            cvPhone.setDescText(phone.phoneFormat())
        }
    }

    fun setPrice(time: String?) {
        cvPrice.setDescText(time)
    }

    fun setTripOption(optionTrip: String?, optionType: String?) {
        /**Option Type = TRAVEL, DRIVER**/
        if (optionTrip.isNullOrEmpty())
            cvTripOptionstatus.gone()
        else {
            val tripOption = OptionData.tripsDetailIcon(optionType, context)
            cvTripOptionstatus.setDescText(optionTrip)
            cvTripOptionstatus.setIcon(tripOption?.icon!!)
            cvTripOptionstatus.setTopText(tripOption.title!!)
        }
    }

    fun setDescription(description: String?) {
        if (description.isNullOrEmpty()) {
            tvDescription.gone()
        } else {
            tvDescription.setText(description)
        }
    }


}