package roadfriend.app.newui.search

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import org.koin.androidx.viewmodel.ext.android.viewModel
import roadfriend.app.R
import roadfriend.app.data.remote.model.city.City
import roadfriend.app.data.remote.model.trips.GetTripRequest
import roadfriend.app.databinding.FragmentSearchBinding
import roadfriend.app.ui.base.BaseFragment
import roadfriend.app.ui.home.HomeViewModel
import roadfriend.app.utils.OtherUtils
import roadfriend.app.utils.PrefUtils
import roadfriend.app.utils.extensions.visible
import roadfriend.app.utils.helper.genericadapter.GenericAdapter

class SearchFragment : BaseFragment<FragmentSearchBinding, HomeViewModel>() {

    override fun createBinding() = FragmentSearchBinding.inflate(layoutInflater)

    override val viewModel: HomeViewModel by activityViewModels()

    private val adapter by lazy { GenericAdapter<City>(R.layout.item_station_list) }

    override fun onViewReady(bundle: Bundle?) {

    }

    override fun onViewListener() {

    }

    fun sendRequest() {
        binding.progressBar.visible()
        val data = PrefUtils.getTrip()
        var getTripRequest =
            GetTripRequest(
                OtherUtils.getCountryCode(),
                data.startCity?.name,
                data.endCity?.name
            )
        viewModel.tripRequest.value = getTripRequest
    }


}