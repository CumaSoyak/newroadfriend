package roadfriend.app.customviews

import android.app.Activity
import roadfriend.app.data.remote.model.trips.Trips
import roadfriend.app.ui.add.AddActivity
import roadfriend.app.utils.DialogUtils
import roadfriend.app.utils.OptionData
import roadfriend.app.utils.extensions.launchActivity
import roadfriend.app.utils.extensions.showSucces
import roadfriend.app.utils.firebasedatebase.FirebaseHelper

class CvOption {
    fun initOption(
        isMyTrip: Boolean,
        context: Activity,
        trips: Trips? = null,
        deleteListener: () -> Unit?
    ) {
        var mModel: String = ""
        DialogUtils.bottomSheet(
            context,
            OptionData.advertOption(isMyTrip, context),
            object : DialogUtils.IBottomSheetListener {
                override fun selectOption(position: Int, model: String) {
                    mModel = model
                    if (mModel.equals("Düzenle")) {
                        context.launchActivity<AddActivity> { }
                    }
                    if (mModel.equals("Sil")) {
                        deleteListener.invoke()
                    }
                    if (mModel.equals("Şikayet et")) {
                        context.showSucces("Şikayetiniz alındı")
                    }
                    if (mModel.equals("Kaydet")) {
                        FirebaseHelper().savedTrip(context, trips!!)
                    }
                }
            })
    }
    fun postPremiumOption(){

    }
}