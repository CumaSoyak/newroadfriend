package roadfriend.app.customviews

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.cv_add_item.view.*
import org.jetbrains.anko.textColor
import roadfriend.app.R
import roadfriend.app.utils.OtherUtils
import roadfriend.app.utils.extensions.*

class CVAddItem : ConstraintLayout {
    var mTopTitle = ""
    var mDescTitle = ""
    lateinit var mContext: Context
    var date: String = ""

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
        LayoutInflater.from(context).inflate(R.layout.cv_add_item, this)

        try {
            setTextData(attrs, context)
        } catch (e: Exception) {
            print(e.localizedMessage + "")
        }
    }

    @SuppressLint("Recycle")
    private fun setTextData(attrs: AttributeSet?, context: Context?) {

        val values = context?.obtainStyledAttributes(attrs, R.styleable.CVAddItem, 0, 0)
        values?.getString(R.styleable.CVAddItem_topTitle)?.let {
            mTopTitle = it
        }
        values?.getString(R.styleable.CVAddItem_descTitle)?.let {
            mDescTitle = it
        }
        values?.getDrawable(R.styleable.CVAddItem_iconSet).let {
            ivOption.setImageDrawable(it)
            ivOption.filter(R.color.color_edittext)
        }
        values?.getBoolean(R.styleable.CVAddItem_price, false)?.let {
            if (it) {
                tvDescTitle.gone()
                etPrice.visible()
                etPrice.listener {
                    if (it.length > 0) {
                        setTopText(getString(R.string.add_detail_para))
                        ivOption.filter(R.color.border)
                        tvTL.visible()
                        tvTL.text = OtherUtils.getCurreny().toUpperCase()
                        groupNonSelected.visible()
                    } else {
                        tvTL.gone()
                        tvTopTitle.textColor = resources.getColor(R.color.color_edittext)
                        ivOption.filter(R.color.color_edittext)
                    }

                }
            }
        }
        if (!mTopTitle.isNullOrEmpty() && !mDescTitle.isNullOrEmpty()) {
            val item =
                AddItemModel(mTopTitle, mDescTitle)
            tvTopTitle.text = item.title
            tvDescTitle.text = item.desc
        }




        values?.recycle()

    }

    fun setTopText(topText: String) {
        tvTopTitle.text = topText
        tvTopTitle.textColor = resources.getColor(R.color.border)
    }

    fun setTopTextNotColor(topText: String) {
        tvTopTitle.text = topText
    }


    fun setDescText(descText: String?) {
        groupNonSelected.visible()
        tvDescTitle.text = descText
        tvDescTitle.textColor = resources.getColor(R.color.border)
        tvTopTitle.textColor = resources.getColor(R.color.border)
        ivOption.filter(R.color.border)
    }

    fun getDescText(): String {
        return tvDescTitle.text.toString()
    }

    fun getPrice(): String? {
        return etPrice.textString()
    }

    fun setTagDesc(tag: String) {
        date = tag
    }

    fun getTagDesc(): String {
        return date
    }

    fun setIcon(icon: Drawable) {
        ivOption.setImageDrawable(icon)
        ivOption.filter(R.color.border)
    }

    fun setIconNotColor(icon: Drawable) {
        ivOption.setImageDrawable(icon)
    }
}