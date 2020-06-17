package roadfriend.app.ui.notification.adapter

//
//class NotificationAdapter(
//    var items: ArrayList<NotificationModel> = arrayListOf(), var listener: INotificationListener
//) :
//    RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
//        return NotificationViewHolder(
//            LayoutInflater.from(parent.context).inflate(
//                R.layout.item_notification_app,
//                parent,
//                false
//            )
//        )
//    }
//
//    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
//        holder.bind(items[position], listener)
//    }
//
//    override fun getItemCount() = items.size
//
//    fun add(list: ArrayList<NotificationModel>) {
//        items.clear()
//        items.addAll(list)
//        notifyDataSetChanged()
//    }
//
//    class NotificationViewHolder(view: View) : BindingViewHolder<ItemNotificationBinding>(view) {
//        fun bind(
//            notification: NotificationModel,
//            listener: INotificationListener
//        ) {
//            binding.root.setOnClickListener {
//                listener.onClickNotification(notification)
//            }
//        }
//    }
//}
//
//interface INotificationListener {
//    fun onClickNotification(model: NotificationModel)
//}