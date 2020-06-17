package roadfriend.app.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import roadfriend.app.CoreApp
import roadfriend.app.R
import roadfriend.app.data.remote.model.city.City
import roadfriend.app.ui.searchcity.SearchCityDialog
import roadfriend.app.utils.AppConstants
import roadfriend.app.utils.extensions.gone
import roadfriend.app.utils.extensions.visible
import kotlinx.android.synthetic.main.cv_search.view.*

class CVSearch @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), SearchCityDialog.ISearchCityListener {

    lateinit var callbackCity: (City?, City?, String?) -> Unit

    init {
        LayoutInflater.from(context).inflate(R.layout.cv_search, this)
        val values = context?.obtainStyledAttributes(attrs, R.styleable.CVSearch, 0, 0)
        values.getString(R.styleable.CVSearch_text_hint)?.let {
            tvChooseCity.text = it
        }
    }

    fun openSearchCityDialog(
        fragment: Fragment
        , callBackFilter: () -> Unit,
        callback: (start: City?, end: City?, status: String?) -> Unit
    ) {
        this.callbackCity = callback
        containerSearch.setOnClickListener {
            fragment.fragmentManager?.beginTransaction()?.let { it1 ->
                SearchCityDialog.newInstance(AppConstants.HOME_SEARCH, this)
                    .show(it1, "")
            }
        }
        tvFilter.setOnClickListener {
            callBackFilter.invoke()
            groupNoChoose.visible()
            groupSelectedChosee.gone()
        }
    }

    override fun cityAndStatus(stationStart: City?, stationEnd: City?, status: String?) {
        visibleCotainer()
        tvStartCity.text = stationStart?.name
        tvEndCity.text = stationEnd?.name
        callbackCity(stationStart, stationEnd, CoreApp.statusSearch)
     }

    fun visibleCotainer() {
        groupNoChoose.gone()
        groupSelectedChosee.visible()
    }

    fun cleanedFilter() {
        tvFilter.gone()
        groupNoChoose.visible()
        groupSelectedChosee.gone()
    }

}