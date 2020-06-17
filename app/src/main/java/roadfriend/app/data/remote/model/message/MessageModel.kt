package roadfriend.app.data.remote.model.message

import android.os.Parcelable
import roadfriend.app.utils.helper.genericadapter.ListItemViewModel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MessageModel(
    val messageText: String = "",
    val messageType: String = "",
    val fullName: String = ""
) :
    ListItemViewModel(), Parcelable