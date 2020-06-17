package roadfriend.app.ui.message.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import roadfriend.app.R
import roadfriend.app.data.remote.model.message.MessageModel
import roadfriend.app.databinding.ItemMessageBinding
import roadfriend.app.ui.base.BindingViewHolder

class BidAdapter(
    var items: ArrayList<MessageModel> = arrayListOf(), var listener: IBidListener
) :
    RecyclerView.Adapter<BidAdapter.BidViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BidViewHolder {
        return BidViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_message,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: BidViewHolder, position: Int) {
        holder.bind(items[position], listener)
    }

    override fun getItemCount() = items.size

    fun add(list: ArrayList<MessageModel>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    class BidViewHolder(view: View) : BindingViewHolder<ItemMessageBinding>(view) {
        fun bind(
            notification: MessageModel,
            listener: IBidListener
        ) {
            // binding.bid = notification
            binding.root.setOnClickListener {
                listener.onClickBid(notification)
            }
        }
    }
}

interface IBidListener {
    fun onClickBid(model: MessageModel)
}