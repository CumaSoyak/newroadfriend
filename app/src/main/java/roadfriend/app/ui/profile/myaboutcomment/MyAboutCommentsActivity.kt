package roadfriend.app.ui.profile.myaboutcomment

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query
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

    override fun createBinding()= MyAboutCommentsActivityBinding.inflate(layoutInflater)

    private val viewModel by viewModel<MyAboutCommentsViewModel>()

    private val user: User by lazy { intent.getParcelableExtra<User>("data") as User }

    private val adapter by lazy { GenericAdapter<Comment>(R.layout.item_my_about_comment) }

    override fun initNavigator() {
        viewModel.setPresenter(this)
    }

    override fun initUI() {
        toolBarTitle(getString(R.string.hakkimda_yorumlar))
        binding.vm = viewModel
        binding.lifecycleOwner = this

        if (intent.hasExtra("visibleComment")) {
            binding.chatContainer.visible()
        }
    }

    override fun initListener() {
        binding.rv.adapter = adapter
        getComent()
        messageBoxListener()

        if (intent.hasExtra("myComment")) {
            binding.chatContainer.gone()
        }
        binding.btnSendMessage.setOnClickListener {
            if (PrefUtils.isLogin()) {
                setComment()
            } else {
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
                .orderBy("firebaseTime", Query.Direction.DESCENDING)
        docRef.addSnapshotListener { snapshot, e ->
            snapshot?.forEachIndexed { index, queryDocumentSnapshot ->
                val data: Comment = queryDocumentSnapshot.toObject(Comment::class.java)
                if (queryDocumentSnapshot["firebaseTime"] != null) {
                    val seconds = (queryDocumentSnapshot["firebaseTime"] as Timestamp)
                    data.firebaseTimeSecond = seconds.seconds
                } else {
                    data.firebaseTimeSecond = 100
                }
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
        val time = FieldValue.serverTimestamp()
        val uuid = UUID.randomUUID().toString()

        val data = hashMapOf(
            "id" to uuid,
            "comment" to binding.etChatBox.textString(),
            "user" to PrefUtils.getUser(),
            "firebaseTime" to time
        )

        CoreApp.db.collection(CoreApp.testDatabase + "comment")
            .document(user.id)
            .collection("info")
            .document(PrefUtils.getUserId())
            .set(data, SetOptions.merge()).addOnSuccessListener {
                binding.etChatBox.setText("")
                getComent()
            }
    }
}
