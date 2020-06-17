package roadfriend.app.data.local.model

import android.os.Parcelable
import roadfriend.app.data.remote.model.trips.Trips
import kotlinx.android.parcel.Parcelize

@Parcelize
class MapsModel(
    val trips: ArrayList<Trips> = arrayListOf(),
    var type: String?=null
) : Parcelable