package roadfriend.app.utils

import android.app.Activity
import android.content.Context
import roadfriend.app.CoreApp
import roadfriend.app.R
import roadfriend.app.data.local.model.TripStatus
import roadfriend.app.data.local.model.chat.ChatAutomaticMessage
import roadfriend.app.data.remote.model.Help
import roadfriend.app.data.remote.model.trips.Trips
import roadfriend.app.ui.profile.mytrip.MyTripsActivity
import roadfriend.app.ui.profile.savedtrip.SavedTripActivity
import roadfriend.app.ui.tripdetail.TripDetailActivity
import roadfriend.app.utils.extensions.getString
import com.google.firebase.firestore.FieldValue

object OptionData {
    val context = CoreApp.context
    fun searchType(): ArrayList<String> {
        val searchList: ArrayList<String> = arrayListOf()
        searchList.add(getString(R.string.add_arac_ariyorum))
        searchList.add(getString(R.string.add_yuk_ariyorum))
        return searchList
    }

    fun tripatus(status: String): String {
        when (status) {
            "1" -> return getString(R.string.add_arac_ariyorum)
            "2" -> return getString(R.string.add_yuk_ariyorum)
            else -> return ""
        }
    }


    fun tripatusSales(status: String): String {
        when (status) {
            "1" -> return getString(R.string.arac)
            "2" -> return getString(R.string.yuk)
            else -> return ""
        }
    }

    fun firebaseTime(): FieldValue {
        return FieldValue.serverTimestamp()
    }

    fun admobTrip(): Trips {
        return Trips(ads = true)
    }

    fun advertOption(isMyTrip: Boolean, activity: Activity): ArrayList<String> {
        val searchList: ArrayList<String> = arrayListOf()
        if (activity is MyTripsActivity) {
            if (isMyTrip) {
                searchList.add("Sil")
             }
        } else if (activity is SavedTripActivity) {
            searchList.add("Sil")
        } else if (activity is TripDetailActivity) {
            if (isMyTrip) {
                searchList.add("Sil")
             }
            if (!isMyTrip) {
                searchList.add("Kaydet")
                searchList.add("Şikayet et")
            }
        }
        return searchList
    }

    fun tripStatusChooseList(type: String?): ArrayList<String> {
        var tripChooseList: ArrayList<String> = arrayListOf()
        when (type) {
            "TRAVEL" -> {
                tripChooseList.add("1 kişiyim")
                tripChooseList.add("2 kişiyiz")
                tripChooseList.add("3 kişiyiz")
                tripChooseList.add("4 kişiyiz")
            }
            "DRIVER" -> {
                tripChooseList.add("1 kişi")
                tripChooseList.add("2 kişi")
                tripChooseList.add("3 kişi")
                tripChooseList.add("4 kişi")

            }
            "TRUCK_DRIVER" -> {
                tripChooseList.add("Hafif")
                tripChooseList.add("Tır")
                tripChooseList.add("Kamyon")
                tripChooseList.add("Dorse")
            }
            "TRUCK_PACKET" -> {
                tripChooseList.add("Ev Eşyası")
                tripChooseList.add("Yiyecek")
                tripChooseList.add("Ağır Kargo")
                tripChooseList.add("Kuru nakliyat")
            }

        }
        return tripChooseList
    }

    fun tripsDetailIcon(type: String?, context: Context): TripStatus? {
        val tripStatus: TripStatus? = TripStatus()
        when (type) {
            "1" -> {
                /**3 kişiyiz*/
                tripStatus?.icon = context.resources.getDrawable(R.drawable.ic_group)
                tripStatus?.title = "Kişi sayısı"
                tripStatus?.description = "3 kişisiniz?"
            }
            "2" -> {
                /**3 kişi götürebilirim*/
                tripStatus?.icon = context.resources.getDrawable(R.drawable.ic_group)
                tripStatus?.title = "Kişi sayısı"
                tripStatus?.description = "3 kişi götürebilirsin ?"
            }
            "3" -> {
                /***/
                tripStatus?.icon = context.resources.getDrawable(R.drawable.ic_car)
                tripStatus?.title = "Araç türü"
                tripStatus?.description = "Seçiniz"
            }
            "4" -> {
                /***/
                tripStatus?.icon = context.resources.getDrawable(R.drawable.ic_boxes)
                tripStatus?.title = "Eşya türü"
                tripStatus?.description = "Seçiniz"

            }
        }
        return tripStatus
    }

    fun getPaymanetInfoText(position: Int, tripSatus: String): String {
        val list: ArrayList<String> =
            arrayListOf(
                getString(R.string.sales_info_text_first) + " " + tripatusSales(tripSatus) +" "+ getString(R.string.sales_info_text_second_24),
                getString(R.string.sales_info_text_first) + " " + tripatusSales(tripSatus) +" "+ getString(R.string.sales_info_text_second_24),
                getString(R.string.sales_info_text_first) + " " + tripatusSales(tripSatus) +" "+ getString(R.string.sales_info_text_second_24)
            )
        return list.get(position)
    }

    fun gettAutomaticMessage(): ArrayList<ChatAutomaticMessage> {
        val listMessage: ArrayList<ChatAutomaticMessage> = arrayListOf()
        listMessage.add((ChatAutomaticMessage("Merhaba")))
        listMessage.add((ChatAutomaticMessage("Fiyatta pazarlık payı var mı?")))
        listMessage.add((ChatAutomaticMessage("Fiyatta pazarlık payı var mı?")))
        listMessage.add((ChatAutomaticMessage("Fiyatta pazarlık payı var mı?")))
        return listMessage
    }

    fun getHelpMessageList(): ArrayList<Help> {
        val listHelp: ArrayList<Help> = arrayListOf()
        listHelp.add(Help("1.İlan nasıl yüklenir", ""))
        listHelp.add(Help("", "Ortadaki 'ilan ver' butonuna tıklayınız."))
        listHelp.add(Help("", "Araç veya Yük aradığınızı seçiniz."))
        listHelp.add(Help("", "Çıkış ve Varış şehirlerini seçiniz."))
        listHelp.add(Help("", "Tarih, Para tutarı veya açıklama giriniz."))
        listHelp.add(Help("", "'İlan ver' butonuna basınız."))
        listHelp.add(Help("", "İlanınızın önce çıkması için ilanınızı öne çıkarınız."))
        return listHelp
    }

    fun getConditionList(): ArrayList<Help> {
        val listHelp: ArrayList<Help> = arrayListOf()
        listHelp.add(Help("", CoreApp.context.resources.getString(R.string.contition)))
        return listHelp
    }
}