package roadfriend.app.data.remote.model.city

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import roadfriend.app.utils.helper.genericadapter.ListItemViewModel

@Parcelize
data class City(
    @SerializedName("city") var name: String? = "",
    @SerializedName("isStationDisable") var isStationDisable: Boolean = false
) : Parcelable, ListItemViewModel()