package roadfriend.app.data.remote.model.notification

import roadfriend.app.utils.helper.genericadapter.ListItemViewModel

class NotificationModel(
    val id: String,
    val image: String,
    val fullName: String,
    val notificationType: String
) : ListItemViewModel()