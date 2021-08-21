package roadfriend.app.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
 import roadfriend.app.CoreApp
import roadfriend.app.R
import roadfriend.app.data.remote.model.city.City
import roadfriend.app.databinding.CvSearchBinding
import roadfriend.app.newui.search.SearchCityDialog
import roadfriend.app.utils.AppConstants
import roadfriend.app.utils.extensions.clickWithDebounce
import roadfriend.app.utils.extensions.gone
import roadfriend.app.utils.extensions.visible
import roadfriend.app.utils.manager.EventManager

class CVSearch @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), SearchCityDialog.ISearchCityListener {

    lateinit var callbackCity: (City?, City?, String?) -> Unit
    private val binding = CvSearchBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        LayoutInflater.from(context).inflate(R.layout.cv_search, this)
        val values = context?.obtainStyledAttributes(attrs, R.styleable.CVSearch, 0, 0)
        values.getString(R.styleable.CVSearch_text_hint)?.let {
            binding.  tvChooseCity.text = it
        }
    }

    fun openSearchCityDialog(
        fragment: Fragment
        , callBackFilter: () -> Unit,
        callback: (start: City?, end: City?, status: String?) -> Unit
    ) {
        this.callbackCity = callback
        binding.   containerSearch.clickWithDebounce {
            EventManager.searchClick()
            fragment.fragmentManager?.beginTransaction()?.let { it1 ->
                SearchCityDialog.newInstance(AppConstants.HOME_SEARCH, this)
                    .show(it1, "")
            }
        }
        binding. tvFilter.setOnClickListener {
            callBackFilter.invoke()
            binding.   groupNoChoose.visible()
            binding.   groupSelectedChosee.gone()
        }
    }

    override fun cityAndStatus(stationStart: City?, stationEnd: City?, status: String?) {
        visibleCotainer()
        binding.  tvStartCity.text = stationStart?.name
        binding.  tvEndCity.text = stationEnd?.name
        callbackCity(stationStart, stationEnd, CoreApp.statusSearch)
    }

    fun visibleCotainer() {
        binding.  groupNoChoose.gone()
        binding.  groupSelectedChosee.visible()
    }

    fun cleanedFilter() {
        binding. tvFilter.gone()
        binding. groupNoChoose.visible()
        binding.  groupSelectedChosee.gone()
    }

}