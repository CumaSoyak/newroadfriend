package roadfriend.app.data.remote.model.base

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Pagination (
    @SerializedName("total")
    @Expose
    val total:Int,
    @SerializedName("per_page")
    @Expose
    val per_page:Int,
    @SerializedName("current_page")
    @Expose
    val current_page:Int,
    @SerializedName("last_page")
    @Expose
    val last_page:Int,
    @SerializedName("first_item")
    @Expose
    val first_item:Int,
    @SerializedName("last_item")
    @Expose
    val last_item:Int

    )