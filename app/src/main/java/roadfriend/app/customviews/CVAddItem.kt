package roadfriend.app.customviews

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import org.jetbrains.anko.textColor
import roadfriend.app.R
import roadfriend.app.databinding.CvAddItemBinding
import roadfriend.app.utils.OtherUtils
import roadfriend.app.utils.extensions.*

class CVAddItem : ConstraintLayout {
    var mTopTitle = ""
    var mDescTitle = ""
    lateinit var mContext: Context
    var date: String = ""
    private val binding = CvAddItemBinding.inflate(LayoutInflater.from(context), this)

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
            binding.ivOption.setImageDrawable(it)
            binding.ivOption.filter(R.color.color_edittext)
        }
        values?.getBoolean(R.styleable.CVAddItem_price, false)?.let {
            if (it) {
                binding.tvDescTitle.gone()
                binding.etPrice.visible()
                binding.etPrice.listener {
                    if (it.length > 0) {
                        setTopText(getString(R.string.add_detail_para))
                        binding.ivOption.filter(R.color.border)
                        binding.tvTL.visible()
                        binding.tvTL.text = OtherUtils.getCurreny().toUpperCase()
                        binding.groupNonSelected.visible()
                    } else {
                        binding.tvTL.gone()
                        binding.tvTopTitle.textColor = resources.getColor(R.color.color_edittext)
                        binding.ivOption.filter(R.color.color_edittext)
                    }

                }
            }
        }
        if (!mTopTitle.isNullOrEmpty() && !mDescTitle.isNullOrEmpty()) {
            val item =
                AddItemModel(mTopTitle, mDescTitle)
            binding.tvTopTitle.text = item.title
            binding.tvDescTitle.text = item.desc
        }




        values?.recycle()

    }

    fun setTopText(topText: String) {
        binding.tvTopTitle.text = topText
        binding.tvTopTitle.textColor = resources.getColor(R.color.border)
    }

    fun setTopTextNotColor(topText: String) {
        binding.tvTopTitle.text = topText
    }


    fun setDescText(descText: String?) {
        binding.groupNonSelected.visible()
        binding.tvDescTitle.text = descText
        binding.tvDescTitle.textColor = resources.getColor(R.color.border)
        binding.tvTopTitle.textColor = resources.getColor(R.color.border)
        binding.ivOption.filter(R.color.border)
    }

    fun getDescText(): String {
        return binding.tvDescTitle.text.toString()
    }

    fun getPrice(): String? {
        return binding.etPrice.textString()
    }

    fun setTagDesc(tag: String) {
        date = tag
    }

    fun getTagDesc(): String {
        return date
    }

    fun setIcon(icon: Drawable) {
        binding.ivOption.setImageDrawable(icon)
        binding.ivOption.filter(R.color.border)
    }

    fun setIconNotColor(icon: Drawable) {
        binding.ivOption.setImageDrawable(icon)
    }
}