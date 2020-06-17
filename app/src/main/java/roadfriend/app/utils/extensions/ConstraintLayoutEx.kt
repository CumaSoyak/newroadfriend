package roadfriend.app.utils.extensions

import android.content.Context
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import roadfriend.app.customviews.CVEmptyView
import roadfriend.app.data.remote.model.trips.GetTripRequest


fun ConstraintLayout.showEmpty(
    context: Context,
    fragmentName: String? = null,
    getTripRequest: GetTripRequest? = null
) {
    val layout = this
    val set = ConstraintSet()
    val view = CVEmptyView(context)
    view.initData(context, fragmentName, getTripRequest)
    view.id = View.generateViewId()

    if (fragmentName.equals("clear")) {
        set.clone(layout)
        set.clear(view.id, ConstraintSet.TOP)
        set.applyTo(layout)
    } else {
        layout.addView(view, 0)
        set.clone(layout)
        set.connect(view.getId(), ConstraintSet.LEFT, layout.id, ConstraintSet.LEFT, 0)
        set.connect(view.getId(), ConstraintSet.RIGHT, layout.id, ConstraintSet.RIGHT, 0)
        set.connect(view.getId(), ConstraintSet.TOP, layout.id, ConstraintSet.TOP, 0)
        set.connect(view.getId(), ConstraintSet.BOTTOM, layout.id, ConstraintSet.BOTTOM, 0)
        set.applyTo(layout)
    }

}

fun ConstraintLayout.hideEmpty() {

}