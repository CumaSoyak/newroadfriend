package roadfriend.app.data.remote.model.user

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import roadfriend.app.utils.OtherUtils
import roadfriend.app.utils.helper.genericadapter.ListItemViewModel

@Parcelize
class User(
    @SerializedName("id") var id: String = "",
    @SerializedName("fullName") var fullName: String = "",
    @SerializedName("email") var email: String = "",
    @SerializedName("password") var password: String = "",
    @SerializedName("image") var image: String = "",
    @SerializedName("rate") var rate: Int = 2,
    @SerializedName("firebaseToken") var firebaseToken: String = "",
    @SerializedName("documentKey") var documentKey: String = "",
    @SerializedName("phone") var phone: String = "",
    @SerializedName("country") var country: String = OtherUtils.getCountryCode()
) : Parcelable, ListItemViewModel() {
    fun getRateCustom(): Float {
        return rate.toFloat()
    }
}