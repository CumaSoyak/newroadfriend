package roadfriend.app.data.remote.model.maps

import com.google.gson.annotations.SerializedName


data class PolylineModel(
    @SerializedName("points")
    var points: String?
)