package roadfriend.app.data.remote.model.trips

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import roadfriend.app.R
import roadfriend.app.data.remote.model.city.City
import roadfriend.app.data.remote.model.user.User
import roadfriend.app.utils.DummyData
import roadfriend.app.utils.extensions.getString
import roadfriend.app.utils.helper.genericadapter.ListItemViewModel
import java.text.DecimalFormat

@Parcelize
data class Trips(
    @SerializedName("id") var id: String? = "",
    @SerializedName("user") var user: User = DummyData.getUser(),
    @SerializedName("phone") var phone: String? = "",
    @SerializedName("date") var date: String = "",
    @SerializedName("description") var description: String = "",
    @SerializedName("price") var price: String? = "",
    @SerializedName("paymentType") var paymentType: String = "free",
    @SerializedName("startCity") var startCity: City = DummyData.getCity(),
    @SerializedName("endCity") var endCity: City = DummyData.getCity(),
    @SerializedName("startCityName") var startCityName: String? = "",
    @SerializedName("endCityName") var endCityName: String? = "",
    @SerializedName("firebaseToken") var firebaseToken: String? = "",
    @SerializedName("firebaseTimeSecond") var firebaseTimeSecond: Long? = 0,
    @SerializedName("adminPost") var adminPost: Boolean = false,
    @SerializedName("documentKey") var documentKey: String? = "",
    @SerializedName("codeCountry") var codeCountry: String? = "",
    @SerializedName("ads") var ads: Boolean = false,
    @SerializedName("bidOption") var bidOption: Boolean = false,
    @SerializedName("purchaseToken") var purchaseToken: String = ""
) : Parcelable, ListItemViewModel() {
    fun dateIsNull(): Boolean {
        return !date.isNullOrEmpty()
    }

    fun callPrice(): String {
        if (price.isNullOrEmpty()) {
            return getString(R.string.fiyat_yok)
        } else {
            return DecimalFormat("#,###").format(price!!.toDouble()) + " TL"
        }
    }
}