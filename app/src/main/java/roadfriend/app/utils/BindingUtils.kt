package roadfriend.app.utils

import android.annotation.SuppressLint
import android.view.View
import android.widget.*
import androidx.databinding.BindingAdapter
import roadfriend.app.R
import roadfriend.app.customviews.CVAddItem
import roadfriend.app.utils.extensions.backgroundColor
import roadfriend.app.utils.extensions.gone
import roadfriend.app.utils.extensions.load
import org.jetbrains.anko.imageResource


@SuppressLint("StaticFieldLeak")
object BindingUtils {

    var mView: View? = null
    var mViewString: String? = null

    @JvmStatic
    @BindingAdapter(value = ["app:addItemDescText"])
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
    @BindingAdapter(value = ["app:progressScaled", "android:max"], requireAll = true)
    fun setProgress(progressBar: ProgressBar, likes: Int, max: Int) {
        progressBar.progress = (likes * max / 5).coerceAtMost(max)
    }
}
