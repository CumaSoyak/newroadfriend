package roadfriend.app.ui.message


import android.view.View
import kotlinx.android.synthetic.main.fragment_message.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import roadfriend.app.R
import roadfriend.app.data.remote.model.trips.Trips
import roadfriend.app.data.remote.model.user.User
import roadfriend.app.databinding.FragmentMessageBinding
import roadfriend.app.ui.base.BindingFragment
import roadfriend.app.ui.message.chat.ChatActivity
import roadfriend.app.ui.profile.myaboutcomment.MyAboutCommentsActivity
import roadfriend.app.utils.extensions.gone
import roadfriend.app.utils.extensions.launchActivity
import roadfriend.app.utils.extensions.visible
import roadfriend.app.utils.firebasedatebase.FirebaseHelper
import roadfriend.app.utils.helper.genericadapter.GenericAdapter
import roadfriend.app.utils.helper.genericadapter.ListItemViewModel

class MessageFragment : BindingFragment<FragmentMessageBinding>() {
    override val getLayoutBindId: Int
        get() = R.layout.fragment_message


    companion object {
        val TAG: String = MessageFragment::class.java.simpleName
        fun newInstance(): MessageFragment = MessageFragment().apply {

        }
    }

    val baseAdapter by lazy { GenericAdapter<User>(R.layout.item_message) }

    val viewModel by viewModel<MessageViewModel>()

    override fun initNavigator() {
        viewModel.setPresenter(this)
    }

    override fun initUI(view: View) {
        binding.vm = viewModel
        binding.lifecycleOwner = this
    }

    override fun initListener() {
        obsreveBidData()
    }

    fun obsreveBidData() {
        FirebaseHelper().getMessageList { messageData ->
            initData(messageData)
        }
    }

    private fun initData(data: ArrayList<User>?) {
        try {
            if (data.isNullOrEmpty()) {
                binding.cvEmptyView.initData(requireContext(), "message")
                binding.cvEmptyView.visible()
            } else {
                binding.cvEmptyView.gone()
                rvBid.adapter = baseAdapter
                baseAdapter.clearItems()
                baseAdapter.addItems(data)
                adapterListener()
            }
        } catch (e: Exception) {

        }

    }

    fun adapterListener() {
        baseAdapter.setOnListItemViewClickListener(object :
            GenericAdapter.OnListItemViewClickListener {
            override fun onClick(view: View, position: Int, model: ListItemViewModel) {
                context?.launchActivity<ChatActivity> {
                    putExtra("model", model as User)
                }
            }

            override fun onClickCommentDetail(view: View, position: Int, model: ListItemViewModel) {
                requireContext().launchActivity<MyAboutCommentsActivity> {
                    putExtra(
                        "data",
                        (model as User)
                    )
                }
            }
        })
    }


}
