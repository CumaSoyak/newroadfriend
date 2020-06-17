package roadfriend.app.data.remote.model.comment

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import roadfriend.app.data.remote.model.user.User
import roadfriend.app.utils.helper.genericadapter.ListItemViewModel
import kotlinx.android.parcel.Parcelize

@Parcelize
class Comment(
    @SerializedName("id") var id: Int?,
    @SerializedName("rate") var rate: Double?,
    @SerializedName("comment") var comment: String?,
    @SerializedName("user") var user: User?
) : Parcelable, ListItemViewModel()