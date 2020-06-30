package roadfriend.app.data.remote.model.bid

import android.os.Parcelable
import roadfriend.app.utils.helper.genericadapter.ListItemViewModel
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import roadfriend.app.R
import roadfriend.app.utils.extensions.getString
import java.text.DecimalFormat

@Parcelize
class Bid(
    @SerializedName("id") var id: String? = "",
    @SerializedName("fullName") var fullName: String? = "",
    @SerializedName("phone") var phone: String? = "",
    @SerializedName("price") var price: String? = ""
 ):Parcelable,ListItemViewModel(){
    fun callPrice(): String {
        if (price.isNullOrEmpty()) {
            return getString(R.string.fiyat_yok)
        } else {
            return DecimalFormat("#,###").format(price!!.toDouble()) + " TL"
        }
    }
}