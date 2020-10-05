package roadfriend.app.ui.message.chat

import android.view.View
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import kotlinx.android.synthetic.main.chat_activity.*
import kotlinx.android.synthetic.main.toolbar_layout.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import roadfriend.app.CoreApp.Companion.chatID
import roadfriend.app.R
import roadfriend.app.data.local.model.chat.ChatAutomaticMessage
import roadfriend.app.data.remote.model.firebasemessage.Data
import roadfriend.app.data.remote.model.firebasemessage.NotificationRequest
import roadfriend.app.data.remote.model.message.MessageModel
import roadfriend.app.data.remote.model.user.User
import roadfriend.app.databinding.ChatActivityBinding
import roadfriend.app.ui.base.BindingActivity
import roadfriend.app.ui.main.MainActivity
import roadfriend.app.ui.profile.myaboutcomment.MyAboutCommentsActivity
import roadfriend.app.utils.OptionData
import roadfriend.app.utils.PrefUtils
import roadfriend.app.utils.extensions.*
import roadfriend.app.utils.firebasedatebase.FirebaseHelper
import roadfriend.app.utils.helper.genericadapter.GenericAdapter
import roadfriend.app.utils.helper.genericadapter.ListItemViewModel
import java.util.*

class ChatActivity : BindingActivity<ChatActivityBinding>() {
    override val getLayoutBindId: Int
        get() = R.layout.chat_activity

    private val viewModel by viewModel<ChatViewModel>()

    private val adapter by lazy { GenericAdapter<MessageModel>(R.layout.item_chat) }


    private val chatUser: User by lazy { intent.getParcelableExtra("model") as User }

    private lateinit var db: FirebaseFirestore
    var conversationsId: String? = null
    lateinit var senderId: String
    lateinit var receiverId: String
    val messageData: ArrayList<MessageModel> = arrayListOf()
    var beforeNoChating = false


    companion object {
        val TAG: String = ChatActivity::class.java.simpleName
    }

    override fun initNavigator() {
        viewModel.setPresenter(this)
    }

    override fun initUI() {
        binding.vm = viewModel
        binding.lifecycleOwner = this
        rvChat.adapter = adapter
        toolbarInit()
        db = FirebaseFirestore.getInstance()
        initAutomaticMessage()
        getConversitionId()
        try {
            getMessage()
        } catch (e: Exception) {
            logger("" + e.localizedMessage)
        }
        initUserData()
        chatID = chatUser.id
    }

    fun initUserData() {
        ivUserImage.load(chatUser.image)
        ivUserImage.visible()
        toolBarTitle(chatUser.fullName)
        ivUserImage.setOnClickListener {
            launchActivity<MyAboutCommentsActivity> {
                putExtra("data", chatUser)
            }
        }
    }

    override fun initListener() {
        btnSendMessage.setOnClickListener {
            sendMesssage()
        }
        messageBoxListener()


        if (intent.hasExtra("firebaseMessage")) {
            back.setOnClickListener {
                back()
            }
        }
        if (intent.hasExtra("tripDetail")) {
            beforeNoChating = true
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

    fun getConversitionId() {
        senderId = PrefUtils.getUserId().toString()
        receiverId = chatUser.id
        conversationsId = if (senderId < receiverId) {
            "${senderId}_$receiverId"
        } else {
            "${receiverId}_$senderId"
        }
    }

    fun sendMesssage() {
        val uuid = UUID.randomUUID().toString()
        val time = FieldValue.serverTimestamp()
        //Benim chat listeme ekle
        val data = hashMapOf(
            "messageText" to etChatBox.textString(),
            "fullName" to chatUser.fullName,
            "messageType" to "me",
            "time" to time
        )
        db.collection("message")
            .document(senderId)
            .collection(conversationsId!!)
            .document(uuid)
            .set(data, SetOptions.merge()).addOnCompleteListener {
                if (it.isSuccessful) {
                    sendYouMessageList(uuid, time)
                    sendNotification(etChatBox.textString())
                    if (beforeNoChating) {   //eğer daha önce mesajlaşılmadıysa user oluştur
                        FirebaseHelper().chatCreateUser(chatUser)
                        FirebaseHelper().setNotificationBadge(chatUser, true)
                    }
                    binding.etChatBox.text.clear()

                }
            }
    }

    fun sendYouMessageList(uuid: String, time: FieldValue) {
        //Onun chat listesine ekle
        val userMe = PrefUtils.getUser()
        val dataYou = hashMapOf(
            "messageText" to etChatBox.textString(),
            "fullName" to userMe.fullName,
            "messageType" to "you",
            "time" to time
        )
        db.collection("message")
            .document(receiverId)
            .collection(conversationsId!!)
            .document(uuid)
            .set(dataYou, SetOptions.merge())
    }

    fun getMessage() {
        val docRef = db.collection("message").document(senderId).collection(conversationsId!!)
            .orderBy("time", Query.Direction.DESCENDING).limit(100)
        docRef.addSnapshotListener { snapshot, e ->
            adapter.clearItems()
            messageData.clear()
            if (snapshot != null) {
                if (snapshot.size() > 0) {
                    binding.rvChat.smoothScrollToPosition(snapshot.size() - 1)
                }
            }
            if (snapshot?.size() == 0) {
                beforeNoChating = true //Daha önce mesajlaşılmı
            }
            snapshot?.forEachIndexed { index, queryDocumentSnapshot ->
                val data: MessageModel = queryDocumentSnapshot.toObject(MessageModel::class.java)
                messageData.add(data)
            }
            messageData.reverse()
            initMessageData(messageData)
        }
    }

    private fun initMessageData(data: ArrayList<MessageModel>?) {
        adapter.addItems(data!!)
    }


    private fun initAutomaticMessage() {
        val automaticMessageAdapter by lazy { GenericAdapter<ChatAutomaticMessage>(R.layout.item_automatic_message) }
        binding.rvAuthomaticMessage.adapter = automaticMessageAdapter
        automaticMessageAdapter.addItems(OptionData.gettAutomaticMessage())
        automaticMessageAdapter.setOnListItemViewClickListener(object :
            GenericAdapter.OnListItemViewClickListener {
            override fun onClick(view: View, position: Int, model: ListItemViewModel) {
                binding.etChatBox.setText((model as ChatAutomaticMessage).message)
            }

        })
    }

    private fun sendNotification(message: String) {
        val userMe = PrefUtils.getUser()
        val data = Data(
            userMe.id,
            userMe.image,
            userMe.fullName,
            message,
            "chat"
        )
        val request = NotificationRequest(data, chatUser.firebaseToken)
        viewModel.postNotification(request)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        back()
    }

    fun back() {
        if (intent.hasExtra("firebaseMessage")) {
            launchActivity<MainActivity> { }
        }
    }
}