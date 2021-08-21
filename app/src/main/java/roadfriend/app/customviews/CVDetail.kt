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
import roadfriend.app.databinding.CvChooseStatusBinding
import roadfriend.app.databinding.CvDetailBinding
import roadfriend.app.ui.base.BaseActivity
import roadfriend.app.ui.maps.MapFragment
import roadfriend.app.ui.sales.SalesActivity
import roadfriend.app.utils.OptionData
import roadfriend.app.utils.extensions.gone
import roadfriend.app.utils.helper.genericadapter.GenericAdapter

class CVDetail @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding = CvDetailBinding.inflate(LayoutInflater.from(context), this, true)

    val adapter = GenericAdapter<City>(R.layout.item_trips_station_list)

    init {
     }

    fun initData(trips: Trips) {
         setDate(trips.date)

        setPrice(trips.callPrice())
        setDescription(trips.description)
        setCity(trips)
    }



    private fun setMaps(trips: Trips, activity: Context) {
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
       // activity.supportFragmentManager.beginTransaction().replace(R.id.fLTripDetail, fragment).commit()
    }

    private fun setCity(trips: Trips) {
        binding.   tvFirstCity.text = trips.startCityName
        binding. tvSecondCity.text = trips.endCityName
    }


    fun setDate(date: String?) {
        if (date.isNullOrEmpty()) {
            binding.   cvDate.gone()
        } else {
            binding.    cvDate.setDescText(date)
        }
    }

    fun setPrice(time: String?) {
        binding.   cvPrice.setDescText(time)
    }

    fun setTripOption(optionTrip: String?, optionType: String?) {
        /**Option Type = TRAVEL, DRIVER**/
        if (optionTrip.isNullOrEmpty())
            binding.   cvTripOptionstatus.gone()
        else {
            val tripOption = OptionData.tripsDetailIcon(optionType, context)
         binding.    cvTripOptionstatus.setDescText(optionTrip)
         binding.    cvTripOptionstatus.setIcon(tripOption?.icon!!)
         binding.    cvTripOptionstatus.setTopText(tripOption.title!!)
        }
    }

    fun setDescription(description: String?) {
        if (description.isNullOrEmpty()) {
            binding.     tvDescription.gone()
        } else {
            binding.   tvDescription.setText(description)
        }
    }


}