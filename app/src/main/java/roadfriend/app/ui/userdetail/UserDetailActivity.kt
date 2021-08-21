package roadfriend.app.ui.userdetail

import androidx.lifecycle.Observer
import roadfriend.app.R
import roadfriend.app.data.remote.model.comment.Comment
import roadfriend.app.databinding.UserDetailActivityBinding
import roadfriend.app.data.remote.model.trips.Trips
import roadfriend.app.ui.base.BindingActivity
import roadfriend.app.utils.helper.genericadapter.GenericAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class UserDetailActivity : BindingActivity<UserDetailActivityBinding>() {

    override fun createBinding()= UserDetailActivityBinding.inflate(layoutInflater)

    private val viewModel by viewModel<UserDetailViewModel>()
    private val adapter by lazy { GenericAdapter<Comment>(R.layout.item_my_about_comment) }
    private val userDetailData by lazy {
        intent.getParcelableExtra<Trips>("model") as Trips
    }

    companion object {
        fun newInstance() = UserDetailActivity()
    }

    override fun initNavigator() {
        viewModel.setPresenter(this)
    }

    override fun initUI() {
        binding.vm = viewModel
        binding.lifecycleOwner = this

        viewModel.getCommentOfUser()
        viewModel.setUser(userDetailData.user)

    }

    override fun initListener() {
        observerViewModel()
    }

    fun observerViewModel() {
        viewModel.commentOfUser.observe(this, Observer { initCommentData(it) })
    }

    private fun initCommentData(data: ArrayList<Comment>?) {
        data?.let {
            binding.rv.adapter = adapter
            adapter.addItems(it)
        }

    }


}
