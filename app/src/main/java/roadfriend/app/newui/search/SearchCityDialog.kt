package roadfriend.app.newui.search

import android.os.Bundle
import android.view.View
import android.widget.SearchView
import org.koin.androidx.viewmodel.ext.android.viewModel
import roadfriend.app.CoreApp.Companion.statusSearch
import roadfriend.app.R
import roadfriend.app.data.remote.model.city.City
import roadfriend.app.databinding.DialogSearchCityBinding
import roadfriend.app.ui.base.BaseDialogFragment
import roadfriend.app.ui.home.HomeViewModel
import roadfriend.app.utils.AppConstants
import roadfriend.app.utils.OtherUtils
import roadfriend.app.utils.PrefUtils
import roadfriend.app.utils.extensions.hideKeyboard
import roadfriend.app.utils.extensions.showToast
import roadfriend.app.utils.extensions.visible
import roadfriend.app.utils.helper.genericadapter.GenericAdapter
import roadfriend.app.utils.helper.genericadapter.ListItemViewModel

class SearchCityDialog : BaseDialogFragment<DialogSearchCityBinding, HomeViewModel>() {

    override fun createBinding() = DialogSearchCityBinding.inflate(layoutInflater)

    override val viewModel by viewModel<HomeViewModel>()

    private val adapter by lazy { GenericAdapter<City>(R.layout.item_station_list) }

    private val mCityList: ArrayList<City> = arrayListOf()

    private var searchCityListener: ISearchCityListener? = null

    private var stationStart: City? = null
    private var stationEnd: City? = null
    private var searchType: String? = null
    private var isOpen = true

    val keepCity: ArrayList<City?> = arrayListOf()

    interface ISearchCityListener {
        fun city(stationStart: City? = null, stationEnd: City? = null) {}
        fun cityAndStatus(stationStart: City?, stationEnd: City?, status: String?) {}
    }

    enum class DirectionType {
        START, END
    }

    var DIRECTION: DirectionType? = null

    companion object {

        fun newInstance(searchType: String, listener: ISearchCityListener): SearchCityDialog {
            return SearchCityDialog().apply {
                searchCityListener = listener
                setStyle(STYLE_NORMAL, R.style.DialogFragmentTheme)
                arguments = Bundle().apply { putString("type", searchType) }
            }
        }
    }

    override fun onViewReady(bundle: Bundle?) {
        binding.include.tvToolbarTitle.text = getString(R.string.title_add_direction)
        binding.recyclerview.adapter = adapter
        getCity()
        searchType()
        adapterListener()

        if (OtherUtils.isEurope()) {
            showToast("You can select all European cities")
        }

    }

    fun getCity() {
        if (OtherUtils.getCountryCode().equals("europe")) {
            OtherUtils.europeList.forEachIndexed { index, s ->
                PrefUtils.getCity(s + ".json")
                    ?.let { mCityList.addAll(it) }
            }
        } else {
            PrefUtils.getCity(OtherUtils.getCountryCode() + ".json")?.let { mCityList.addAll(it) }
        }
        adapter.addItems(mCityList)

    }

    fun searchType() {
        searchType = arguments?.getString("type", "")
        when (searchType) {
            AppConstants.HOME_SEARCH -> {
                keepCity.clear()
            }
        }
    }

    override fun onViewListener() {
        super.onViewListener()
        cityInputListener()
        submitOk()
        DIRECTION = DirectionType.START
    }

    fun submitOk() {
        binding.btnDecline.setOnClickListener {
            searchCityListener?.city(stationStart, stationEnd)
            dismiss()
        }
    }

    fun adapterListener() {
        adapter.setOnListItemViewClickListener(object : GenericAdapter.OnListItemViewClickListener {
            override fun onClick(view: View, position: Int, model: ListItemViewModel) {
                /**Eğer önceden seçilen şehir varsa + lı ve yeşil getir*/
                val station = model as City

                keepCity.add(station)

                when (DIRECTION) {
                    DirectionType.START -> {
                        binding.svStartCity.hideKeyboard()
                        binding.svStartCity.setQuery(station.name, false)
                        stationStart = station
                        DIRECTION = DirectionType.END
                        adapter.addItems(mCityList)

                    }
                }
                if (stationStart != null && stationEnd != null) {
                    if (searchType.equals(AppConstants.HOME_SEARCH) && isOpen) {
                        searchCityListener?.cityAndStatus(stationStart, stationEnd, statusSearch)
                        dismiss()
                    } else {
                        buttonAcceptVisible()
                    }
                }
            }
        })

    }

    fun cityInputListener() {
        binding.svStartCity.onActionViewExpanded()
        binding.svStartCity.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                filter(p0.toString())
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                DIRECTION = DirectionType.START
                if (binding.svStartCity.query.isEmpty()) {
                    isOpen = true
                    stationStart = null
                    buttonAcceptVisible()
                    return true
                }
                filter(p0.toString())
                return false
            }
        })

    }

    fun filter(text: String) {
        val filteredCourseAry: ArrayList<City> = arrayListOf()
        val listCity: ArrayList<City> = arrayListOf()
        listCity.addAll(mCityList)
        for (city in listCity) {
            when (DIRECTION) {
                DirectionType.START -> {
                    if (binding.svStartCity.query.toString().equals(city.name?.toLowerCase())) {
                        stationStart = city
                    }
                }


            }
            if (stationStart != null && stationEnd != null) {
                if (searchType.equals(AppConstants.HOME_SEARCH) && isOpen) {
                    //openSearchTypeBottomSheet()
                    searchCityListener?.cityAndStatus(stationStart, stationEnd, statusSearch)
                    dismiss()
                    /**Eğer filtreleme işlemi gerekiyorsa*/
                } else {
                    buttonAcceptVisible()
                    /**Sadece durak gerekiyorsa*/
                }
            }
            if (city.name!!.length > text.length) {
                if (city.name!!.substring(0, text.length).toLowerCase().equals(
                        text.substring(
                            0,
                            text.length
                        ).toLowerCase()
                    )
                ) {
                    keepCity.forEachIndexed { index, addStation ->
                        if (city.name.equals(addStation?.name)) {
                            city.isStationDisable = true
                        }
                    }

                    filteredCourseAry.add(city)
                }
            }
        }
        adapter.addItems(filteredCourseAry)

    }

    fun buttonAcceptVisible() {
        if (!searchType.equals(AppConstants.HOME_SEARCH)) {
            binding.btnDecline.visible()
        }
    }


}