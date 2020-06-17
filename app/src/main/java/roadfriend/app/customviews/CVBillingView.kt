package roadfriend.app.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Button
import android.widget.FrameLayout
import roadfriend.app.R
import roadfriend.app.utils.extensions.visible
import kotlinx.android.synthetic.main.cv_billing_view.view.*

class CVBillingView : FrameLayout {

    lateinit var mContext: Context

    constructor(context: Context?) : super(context!!) {
        if (context != null) {
            this.mContext = context
        }
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs) {
        init(attrs, context)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context!!,
        attrs,
        defStyleAttr
    ) {
        init(attrs, context)
    }

    fun init(attrs: AttributeSet?, context: Context?) {
        LayoutInflater.from(context).inflate(R.layout.cv_billing_view, this)
    }

    fun setDay(day: String) {
        tvDay.text = day
    }

    fun selectItem() {
        container.background =
            context.resources.getDrawable(R.drawable.card_background_border_green)
    }

    fun unSelectItem() {
        container.background = context.resources.getDrawable(R.drawable.card_background)
    }

    fun select(
        cVBillingView: CVBillingView,
        btnSucces: Button,
        list: ArrayList<CVBillingView>
    ) {
        btnSucces.visible()
        list.forEachIndexed { index, mCVBillingView ->
            if (list.get(index) == cVBillingView) {
                mCVBillingView.selectItem()
            } else {
                mCVBillingView.unSelectItem()
            }
        }
    }
}