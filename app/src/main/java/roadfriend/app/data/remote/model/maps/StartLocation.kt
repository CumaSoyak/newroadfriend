package roadfriend.app.data.remote.model.maps

import com.google.gson.annotations.SerializedName


data class StartLocation(
    @SerializedName("lat")
    var lat: Double?,
    @SerializedName("lng")
    var lng: Double?
)