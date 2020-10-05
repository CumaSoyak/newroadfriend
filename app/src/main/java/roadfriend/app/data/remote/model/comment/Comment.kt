package roadfriend.app.data.remote.model.comment

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import roadfriend.app.data.remote.model.user.User
import roadfriend.app.utils.helper.genericadapter.ListItemViewModel
import kotlinx.android.parcel.Parcelize
import roadfriend.app.utils.DummyData

@Parcelize
class Comment(
    @SerializedName("id") var id: String="",
    @SerializedName("comment") var comment: String="",
    @SerializedName("user") var user: User=DummyData.getUser(),
    @SerializedName("firebaseTimeSecond") var firebaseTimeSecond: Long? = 0
    ) : Parcelable, ListItemViewModel()