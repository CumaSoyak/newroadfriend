package roadfriend.app.data.remote.model.location

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
data class LocationModel(
    @SerializedName("latitude")
    @Expose
    var latitude: Double=0.0,
    @SerializedName("longitude")
    @Expose
    var longitude: Double=0.0
) : Parcelable