package roadfriend.app.data.remote.model.auth.login

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import roadfriend.app.data.remote.model.user.User

class Login(
    @SerializedName("token")
    @Expose
    var token: String,
    @SerializedName("user")
    @Expose
    var user: User
)