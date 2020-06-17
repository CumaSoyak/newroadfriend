package roadfriend.app.data.remote.model.auth

import com.google.gson.annotations.SerializedName
import roadfriend.app.data.remote.model.user.User

data class Register(
    @SerializedName("token")
    val token: String,

    @SerializedName("user")
    val user: User

)