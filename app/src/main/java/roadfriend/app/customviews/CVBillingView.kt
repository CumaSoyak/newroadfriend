package roadfriend.app.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Button
import android.widget.FrameLayout
import roadfriend.app.R
import roadfriend.app.databinding.CvBillingViewBinding
import roadfriend.app.utils.extensions.visible

class CVBillingView : FrameLayout {

    lateinit var mContext: Context
    private val binding = CvBillingViewBinding.inflate(LayoutInflater.from(context), this)

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
    }

    fun setDay(day: String) {
        binding.tvDay.text = day
    }

    fun selectItem() {
        binding.container.background =
            context.resources.getDrawable(R.drawable.card_background_border_green)
    }

    fun unSelectItem() {
        binding. container.background = context.resources.getDrawable(R.drawable.card_background)
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