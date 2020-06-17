package roadfriend.app.data.remote.model.city

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import roadfriend.app.utils.helper.genericadapter.ListItemViewModel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class City(
    @SerializedName("id") var id: Int=0,
    @SerializedName("name") var name: String? = "",
    @SerializedName("latitude") var latitude: Double = 0.0,
    @SerializedName("longitude") var longitude: Double = 0.0,
    @SerializedName("isStationDisable") var isStationDisable: Boolean = false


) : Parcelable, ListItemViewModel()