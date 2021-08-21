package roadfriend.app.newui.message

import android.content.Intent
import android.os.Bundle
import android.view.View
import org.koin.androidx.viewmodel.ext.android.viewModel
import roadfriend.app.CoreApp.Companion.notLogin
import roadfriend.app.R
import roadfriend.app.data.local.model.MapsModel
import roadfriend.app.data.remote.model.city.City
import roadfriend.app.data.remote.model.trips.GetTripRequest
import roadfriend.app.data.remote.model.trips.Trips
import roadfriend.app.databinding.FragmentHomeFirstBinding
import roadfriend.app.ui.add.detail.AddDetailActivity
import roadfriend.app.ui.auth.register.RegisterActivity
import roadfriend.app.ui.base.BaseFragment
import roadfriend.app.ui.base.BindingFragment
import roadfriend.app.ui.home.HomeViewModel
import roadfriend.app.ui.main.MainActivity
import roadfriend.app.ui.maps.MapsDialogFragment
import roadfriend.app.ui.profile.myaboutcomment.MyAboutCommentsActivity
import roadfriend.app.ui.tripdetail.TripDetailActivity
import roadfriend.app.ui.userdetail.UserDetailActivity
import roadfriend.app.utils.OptionData
import roadfriend.app.utils.OtherUtils
import roadfriend.app.utils.PrefUtils
import roadfriend.app.utils.extensions.*
import roadfriend.app.utils.firebasedatebase.FirebaseHelper
import roadfriend.app.utils.helper.TripBundle
import roadfriend.app.utils.helper.genericadapter.GenericAdapter
import roadfriend.app.utils.helper.genericadapter.ListItemViewModel
import roadfriend.app.utils.manager.EventManager

class MessageFragment : BaseFragment<FragmentHomeFirstBinding, HomeViewModel>() {

    override fun createBinding() = FragmentHomeFirstBinding.inflate(layoutInflater)

    override val viewModel: HomeViewModel by viewModel()

    override fun onViewReady(bundle: Bundle?) {

    }


}