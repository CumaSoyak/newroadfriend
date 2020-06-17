package roadfriend.app.data.remote.model.auth.login

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LoginRequest(
    @SerializedName("email")
    @Expose
    val email: String,
    @SerializedName("password")
    @Expose
    val password: String
)