package roadfriend.app.customviews

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import roadfriend.app.R
import roadfriend.app.databinding.CvEmptyViewBinding
import roadfriend.app.databinding.CvItemImageBinding
import roadfriend.app.utils.extensions.load

class CVItemImage : FrameLayout {
    var mImage = ""
    lateinit var mContext: Context
    private val binding = CvItemImageBinding.inflate(LayoutInflater.from(context), this)

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
        LayoutInflater.from(context).inflate(R.layout.cv_item_image, this)
        setTextData(attrs, context)
    }

    @SuppressLint("Recycle")
    private fun setTextData(attrs: AttributeSet?, context: Context?) {

        val values = context?.obtainStyledAttributes(attrs, R.styleable.CVItemImage, 0, 0)
        mImage = values?.getString(R.styleable.CVItemImage_image)!!
        if (!mImage.isNullOrEmpty())
            binding.ivItem.load(mImage)
        values.recycle()

    }
}