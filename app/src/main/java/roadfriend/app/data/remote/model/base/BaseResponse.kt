package roadfriend.app.data.remote.model.base

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

open class BaseResponse<out T>(
    @SerializedName("code")
    @Expose
    val code: String = "",
    @SerializedName("message")
    @Expose
    val message: String = "",
    @SerializedName("data")
    @Expose
    val data: T? = null
)