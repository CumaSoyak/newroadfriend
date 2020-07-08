package roadfriend.app.data.remote.model.city

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import roadfriend.app.utils.helper.genericadapter.ListItemViewModel

@Parcelize
data class City(
    @SerializedName("city") var name: String? = "",
    @SerializedName("lat") var latitude: Double = 0.0,
    @SerializedName("lng") var longitude: Double = 0.0,
    @SerializedName("isStationDisable") var isStationDisable: Boolean = false


) : Parcelable, ListItemViewModel()