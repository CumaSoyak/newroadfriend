package roadfriend.app.utils

import roadfriend.app.data.remote.model.city.City
import roadfriend.app.data.remote.model.comment.Comment
import roadfriend.app.data.remote.model.message.MessageModel
import roadfriend.app.data.remote.model.notification.NotificationModel
import roadfriend.app.data.remote.model.trips.Trips
import roadfriend.app.data.remote.model.user.User

object DummyData {
    fun getUser(): User {
        val items = User(
            "0",
            " ",
            " ",
            " ",
            "", 1

        )
        return items
    }

    fun getCity(): City {
        return City( "", false)
    }


    fun getTrips(): ArrayList<Trips> {
        val items: ArrayList<Trips> = arrayListOf()
        val city: ArrayList<City> = arrayListOf()
        val description =
            "dşfmsdlfmlmdfldkm ldkfdfksdfmlskdfm lskdfmlzdfmldsf sldkfmsdlkfm lksdmflsdkmf lskdfml<kdfm"

        return items
    }



    fun getNotification(): ArrayList<NotificationModel> {
        val list: ArrayList<NotificationModel> = arrayListOf()
        list.add(
            NotificationModel(
                "",
                "https://cdn.yemeksepeti.com/App_Themes/Gamification/Avatarlar/200x200/1.png",
                "Cuma Soyak"
                , AppConstants.NOTIFICATION_TYPE_APP

            )
        )

        list.add(
            NotificationModel(
                "",
                "https://cdn.yemeksepeti.com/App_Themes/Gamification/Avatarlar/200x200/1.png",
                "Cuma Soyak",
                AppConstants.NOTIFICATION_TYPE_BID
            )
        )



        return list
    }



    fun getMessageData(): ArrayList<MessageModel> {

        val list: ArrayList<MessageModel> = arrayListOf()
        list.add(MessageModel("dd", "me"))
        list.add(MessageModel("dd", "you"))
        list.add(MessageModel("dd", "me"))
        list.add(MessageModel("dd", "you"))
        list.add(MessageModel("dd", "you"))
        list.add(MessageModel("dd", "you"))
        return list
    }


}