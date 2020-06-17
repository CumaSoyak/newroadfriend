package roadfriend.app.data.remote.model.profil

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UserUpdate(
    @SerializedName("id") @Expose val id: Int,
    @SerializedName("email") @Expose val email: String,
    @SerializedName("full_name") @Expose val full_name: String,
    @SerializedName("image") @Expose val image: String
)