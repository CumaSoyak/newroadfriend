package roadfriend.app.data.local.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import roadfriend.app.data.remote.model.city.City
@Parcelize
class Search(
     var startCity: City?,
    var endCity: City?

):Parcelable

