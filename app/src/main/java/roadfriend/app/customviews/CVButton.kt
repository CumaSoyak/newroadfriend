package roadfriend.app.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatButton
import roadfriend.app.R
import org.jetbrains.anko.textColor

class CVButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatButton(context, attrs, defStyleAttr) {
    init {
        val values = context.obtainStyledAttributes(attrs, R.styleable.CVButton, 0, 0)
        val type = values.getInt(R.styleable.CVButton_type, -1)
        buttonTypeSet(type)
    }

    private fun buttonTypeSet(type: Int) {
        gravity = Gravity.CENTER

        when (type) {
            1 -> {
                setActive()
            }
            0 -> {
                background = context.resources.getDrawable(R.drawable.custom_border_blue_button)
                //       typeface = Typeface.createFromAsset(context.assets, "semibold.ttf")
                textColor = context.resources.getColor(R.color.border)

            }
        }
    }

    fun setActive() {
        background = context.resources.getDrawable(R.drawable.custom_green_button)
        // typeface = Typeface.createFromAsset(context.assets, "semibold.ttf")
        textColor = context.resources.getColor(R.color.white)
    }
}