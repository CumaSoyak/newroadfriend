package roadfriend.app.utils.helper

import android.os.Parcelable
import roadfriend.app.data.remote.model.city.City
import kotlinx.android.parcel.Parcelize

@Parcelize
class TripBundle(
     var tripsList: ArrayList<City> = arrayListOf()
):Parcelable