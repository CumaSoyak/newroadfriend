package roadfriend.app.customviews

import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable

class CVDrawable: Drawable() {
    override fun draw(canvas: Canvas) {

    }
    override fun setAlpha(alpha: Int) {
     }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
     }

    override fun setColorFilter(colorFilter: ColorFilter?) {
     }
}