package roadfriend.app.utils

import android.annotation.SuppressLint
import android.view.View
import android.widget.*
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.BindingAdapter
import org.jetbrains.anko.imageResource
import roadfriend.app.CoreApp.Companion.context
import roadfriend.app.R
import roadfriend.app.customviews.CVAddItem
import roadfriend.app.data.remote.model.trips.Trips
import roadfriend.app.ui.intro.TutorialType
import roadfriend.app.utils.AppConstants.ORDER_DAY
import roadfriend.app.utils.AppConstants.ORDER_MONDAY
import roadfriend.app.utils.AppConstants.ORDER_WEEK
import roadfriend.app.utils.extensions.*


@SuppressLint("StaticFieldLeak")
object BindingUtils {

    var mView: View? = null
    var mViewString: String? = null

    @JvmStatic
    @BindingAdapter("app:addItemDescText")
    fun cvText(textView: CVAddItem, text: String) {
        textView.setDescText(text)
    }

    @JvmStatic
    @BindingAdapter("app:statusTrip")
    fun statusTrip(textView: TextView, status: String) {
        textView.text = OptionData.tripatus(status)
    }

    @JvmStatic
    @BindingAdapter(value = ["viewType"])
    fun viewType(view: View, text: String) {
        this.mView = view
        this.mViewString = text
    }


    @JvmStatic
    @BindingAdapter("toImageUrl")
    fun showImage(view: ImageView, url: String?) {
        view.load(url)
    }

    /**Önceden Seçilen şehirleri disable yap**/

    @JvmStatic
    @BindingAdapter("app:isStationDisable")
    fun stationDisable(view: View, isStationDisable: Boolean) {
        val imageView: ImageView = view as ImageView
        if (isStationDisable) {
            imageView.imageResource = R.drawable.ic_success
        } else {
            imageView.imageResource = R.drawable.ic_add
        }
    }

    fun viewType(type: String) {
        when (type) {
            "search_item_list" -> {
//                val view = mView as ConstraintLayout
//                view.backgroundColor = R.color.textColor
            }
        }
    }


    @JvmStatic
    @BindingAdapter("messageType")
    fun messageType(view: View, messageType: String) {
        if (view is RelativeLayout) {
            val params = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            )
            if (messageType.equals("me")) {
                params.setMargins(65, 3, 4, 3)
            } else {
                params.setMargins(4, 3, 65, 3)
            }
            view.layoutParams = params
        } else if (view is TextView) {
            if (messageType.equals("me")) {
                //  view.textColor = R.color.white
            } else {
                //   view.textColor = R.color.textColor
            }
        } else if (view is LinearLayout) {
            val params = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            )
            if (messageType.equals("me")) {
                params.addRule(RelativeLayout.ALIGN_PARENT_END)
                params.addRule(RelativeLayout.LEFT_OF, R.id.container)
                view.backgroundColor(R.drawable.custom_message_green)
            } else {
                params.addRule(RelativeLayout.ALIGN_PARENT_START)
                params.addRule(RelativeLayout.LEFT_OF, R.id.container)
                view.backgroundColor(R.drawable.custom_message_green)
            }
            view.layoutParams = params
        }
    }


    @JvmStatic
    @BindingAdapter(value = ["app:position", "app:listSize"], requireAll = true)
    fun tripStatus(imageView: ImageView, position: Int, size: Int) {
        when (position) {
            0 -> {
                imageView.load(R.drawable.ic_location)
            }
            size - 1 -> {
                imageView.load(R.drawable.ic_endlocation)
            }
            else -> {
                imageView.load(R.drawable.ic_dot_road)
            }
        }

    }

    /**Admob*/
    @JvmStatic
    @BindingAdapter("viewVisibilty")
    fun admobLoad(view: View, text: String?) {
        if (text.isNullOrEmpty()) {
            view.gone()
        }
    }

    @JvmStatic
    @BindingAdapter("premium")
    fun premium(view: View, text: String?) {
        if (!text.equals("free")) {
            view.visible()
        } else {
            view.gone()
        }
    }

    @JvmStatic
    @BindingAdapter("tripsRefund")
    fun premium(view: TextView, trips: Trips) {
        if (!trips.paymentType.equals("free") && !trips.paymentType.isNullOrEmpty() && !trips.purchaseToken.isNullOrEmpty()) {
            val current = getCurrentDate()
            when (trips.paymentType) {
                "day" -> {   //8-7=1    //9-7=2
                    if (current - trips.firebaseTimeSecond!! < ORDER_DAY.calculateDaySecond()) {
                        //daha bitmemiş
                        view.visible()
                    }
                }
                "week" -> {
                    if (current - trips.firebaseTimeSecond!! < ORDER_WEEK.calculateDaySecond()) {
                        view.visible()
                    }
                }
                "monday" -> {
                    if (current - trips.firebaseTimeSecond!! < ORDER_MONDAY.calculateDaySecond()) {
                        view.visible()
                    }
                }
            }
        } else {
            view.gone()
        }
    }

    @JvmStatic
    @BindingAdapter("tutorialType")
    fun setTutorialImage(view: ImageView, tutorialType: TutorialType) {
        val imageId = when (tutorialType) {
            TutorialType.TUTORIAL_FIRST -> OtherUtils.introImage(1)
            TutorialType.TUTORIAL_SECOND -> OtherUtils.introImage(2)
            TutorialType.TUTORIAL_THIRD -> OtherUtils.introImage(3)
            TutorialType.TUTORIAL_FOURTH -> OtherUtils.introImage(4)
            TutorialType.TUTORIAL_FIFTH -> OtherUtils.introImage(5)
        }

        view.setImageResource(imageId)
    }

    @JvmStatic
    @BindingAdapter("tutorialTitle")
    fun setTutorialTitle(
        view: AppCompatTextView, tutorialType: TutorialType
    ) {
        view.text = when (tutorialType) {
            TutorialType.TUTORIAL_FIRST -> context.getString(R.string.tutorial_first)
            TutorialType.TUTORIAL_SECOND -> context.getString(R.string.tutorial_second)
            TutorialType.TUTORIAL_THIRD -> context.getString(R.string.tutorial_thirty)
            TutorialType.TUTORIAL_FOURTH -> context.getString(R.string.tutorial_fourthy)
            TutorialType.TUTORIAL_FIFTH -> context.getString(R.string.tutorial_five)
        }
    }

    @JvmStatic
    @BindingAdapter("visibleGone")
    fun showHide(view: View, show: Boolean?) {
        view.visibility = if (show != null && show) View.VISIBLE else View.GONE
    }

    @JvmStatic
    @BindingAdapter(value = ["app:progressScaled", "android:max"], requireAll = true)
    fun setProgress(progressBar: ProgressBar, likes: Int, max: Int) {
        progressBar.progress = (likes * max / 5).coerceAtMost(max)
    }

}
