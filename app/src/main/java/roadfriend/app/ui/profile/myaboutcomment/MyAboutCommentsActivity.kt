package roadfriend.app.ui.profile.myaboutcomment

import org.koin.androidx.viewmodel.ext.android.viewModel
import roadfriend.app.R
import roadfriend.app.data.remote.model.comment.Comment
import roadfriend.app.databinding.MyAboutCommentsActivityBinding
import roadfriend.app.ui.base.BindingActivity
import roadfriend.app.utils.extensions.showEmpty
import roadfriend.app.utils.helper.genericadapter.GenericAdapter

class MyAboutCommentsActivity : BindingActivity<MyAboutCommentsActivityBinding>() {
    override val getLayoutBindId: Int
        get() = R.layout.my_about_comments_activity

    private val viewModel by viewModel<MyAboutCommentsViewModel>()

    private val adapter by lazy { GenericAdapter<Comment>(R.layout.item_my_about_comment) }

    override fun initNavigator() {
        viewModel.setPresenter(this)
    }

    override fun initUI() {
        toolBarTitle(getString(R.string.hakkimda_yorumlar))
        binding.vm = viewModel
        binding.lifecycleOwner = this
    }

    override fun initListener() {
        binding.clContainer.showEmpty(this)
//        binding.rv.adapter = adapter
//        adapter.addItems(DummyData.getComment())
    }


}
