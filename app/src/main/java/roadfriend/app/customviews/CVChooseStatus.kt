package roadfriend.app.customviews

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.cv_choose_status.view.*
import roadfriend.app.R
import roadfriend.app.utils.extensions.filter
import roadfriend.app.utils.extensions.visible

class CVChooseStatus : ConstraintLayout {
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
        LayoutInflater.from(context).inflate(R.layout.cv_choose_status, this)
        try {
            setTextData(attrs, context)
        } catch (e: Exception) {

        }


    }

    @SuppressLint("Recycle")
    private fun setTextData(attrs: AttributeSet?, context: Context?) {
        val values = context?.obtainStyledAttributes(attrs, R.styleable.CVChooseStatus, 0, 0)
        val status = values?.getInt(R.styleable.CVChooseStatus_status, -1)

        values?.getString(R.styleable.CVChooseStatus_text)?.let {
            tvTitle.text = it
        }


        when (status) {
            1 -> ivChooseStatus.setImageDrawable(resources.getDrawable(R.drawable.ic_car))
            2 -> ivChooseStatus.setImageDrawable(resources.getDrawable(R.drawable.ic_travel))
//            1 -> ivChooseStatus.setImageDrawable(resources.getDrawable(R.drawable.ic_suitcase))
//            2 -> ivChooseStatus.setImageDrawable(resources.getDrawable(R.drawable.ic_car))

        }

    }

    fun selectItem() {
        val drawable = context.resources.getDrawable(R.drawable.custom_border_blue)
        container.background = drawable
        ivChooseStatus.filter(R.color.border)
        rBChoose.isChecked = true
        tvTitle.setTextColor(context.resources.getColor(R.color.border))

    }

    fun unSelectItem() {
        val drawable = context.resources.getDrawable(R.drawable.card_background)
        container.background = drawable
        ivChooseStatus.filter(R.color.non_selected_color)
        rBChoose.isChecked = false
        tvTitle.setTextColor(context.resources.getColor(R.color.non_selected_color))
    }


    fun select(
        cvChooseStatus: CVChooseStatus,
        btnSucces: AppCompatButton,
        list: ArrayList<CVChooseStatus>
    ) {
        btnSucces.visible()
        list.forEachIndexed { index, mCvChooseStatus ->
            if (list.get(index) == cvChooseStatus) {
                mCvChooseStatus.selectItem()
            } else {
                mCvChooseStatus.unSelectItem()
            }
        }
    }


}