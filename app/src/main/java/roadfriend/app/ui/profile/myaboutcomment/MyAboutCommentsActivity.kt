package roadfriend.app.ui.profile.myaboutcomment

import com.google.firebase.firestore.SetOptions
import org.koin.androidx.viewmodel.ext.android.viewModel
import roadfriend.app.CoreApp
import roadfriend.app.R
import roadfriend.app.data.remote.model.comment.Comment
import roadfriend.app.data.remote.model.user.User
import roadfriend.app.databinding.MyAboutCommentsActivityBinding
import roadfriend.app.ui.auth.register.RegisterActivity
import roadfriend.app.ui.base.BindingActivity
import roadfriend.app.utils.PrefUtils
import roadfriend.app.utils.extensions.*
import roadfriend.app.utils.helper.genericadapter.GenericAdapter
import java.util.*

class MyAboutCommentsActivity : BindingActivity<MyAboutCommentsActivityBinding>() {
    override val getLayoutBindId: Int
        get() = R.layout.my_about_comments_activity

    private val viewModel by viewModel<MyAboutCommentsViewModel>()

    private val user: User by lazy { intent.getParcelableExtra("data") as User }

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
        binding.rv.adapter = adapter
        // adapter.addItems(DummyData.getComment())
        getComent()
        messageBoxListener()
        binding.btnSendMessage.setOnClickListener {
            if (PrefUtils.isLogin()) {
                setComment()
            }
            else{
                launchActivity<RegisterActivity> {
                    putExtra("comment", "comment")
                    CoreApp.notLogin = true
                }
            }
        }
    }

    fun messageBoxListener() {
        binding.etChatBox.listener { text ->
            if (text.isNullOrEmpty()) {
                binding.btnSendMessage.gone()
            } else {
                binding.btnSendMessage.visible()
            }
        }
    }

    fun getComent() {
        val comment: ArrayList<Comment> = arrayListOf()
        val docRef =
            CoreApp.db.collection(CoreApp.testDatabase + "comment")
                .document(user.id).collection("info")
        docRef.addSnapshotListener { snapshot, e ->
            snapshot?.forEachIndexed { index, queryDocumentSnapshot ->
                val data: Comment = queryDocumentSnapshot.toObject(Comment::class.java)
                comment.add(data)
            }
            addData(comment)
        }
    }

    private fun addData(data: ArrayList<Comment>) {
        if (data.isEmpty()) {
            binding.clContainer.showEmpty(this)
        } else {
            binding.rv.adapter = adapter
            adapter.clearItems()
            adapter.addItems(data)
        }
    }

    fun setComment() {
        val uuid = UUID.randomUUID().toString()
        val comment = Comment(uuid, binding.etChatBox.textString(), PrefUtils.getUser())
        CoreApp.db.collection(CoreApp.testDatabase + "comment")
            .document(user.id)
            .collection("info")
            .document(PrefUtils.getUserId())
            .set(comment, SetOptions.merge()).addOnSuccessListener {
                showSucces("Yolculuk kaydedilmiştir")
            }
    }
}
