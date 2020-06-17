package roadfriend.app.data.remote.model.user

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class BaseImage(
    @SerializedName("url")
    val url: String = ""
) : Parcelable