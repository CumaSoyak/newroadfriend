package roadfriend.app.utils.helper.genericadapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdRequest
import kotlinx.android.synthetic.main.item_admob.view.*
import roadfriend.app.R
import roadfriend.app.data.remote.model.notification.NotificationModel
import roadfriend.app.data.remote.model.trips.Trips
import roadfriend.app.utils.AppConstants

class GenericAdapter<T : ListItemViewModel>(
    @LayoutRes val layoutId: Int, val listener: OnListItemViewClickListener? = null,
    val itemType: String? = null
) :
    RecyclerView.Adapter<GenericAdapter.GenericViewHolder<T>>() {

    private val items = mutableListOf<T>()
    private var inflater: LayoutInflater? = null
    private var onListItemViewClickListener = listener

    companion object {
        private var isAdMobRequestFirst = true
        lateinit var adRequest: AdRequest

    }

    fun addItems(items: ArrayList<T>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    fun clearItems() {
        this.items.clear()
        notifyDataSetChanged()
    }

    fun setOnListItemViewClickListener(onListItemViewClickListener: OnListItemViewClickListener?) {
        this.onListItemViewClickListener = onListItemViewClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenericViewHolder<T> {
        val layoutInflater = inflater ?: LayoutInflater.from(parent.context)
        val binding =
            DataBindingUtil.inflate<ViewDataBinding>(layoutInflater, viewType, parent, false)
        return GenericViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GenericViewHolder<T>, position: Int) {
        val itemViewModel = items[position]
        itemViewModel.adapterPosition = position
        itemViewModel.itemSize = items.size
        //itemViewModel.list.addAll(items)
        onListItemViewClickListener?.let { itemViewModel.onListItemViewClickListener = it }
        holder.bind(getItemViewType(position) == R.layout.item_admob, itemViewModel)
    }


    class GenericViewHolder<T : ListItemViewModel>(private val binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(isAdmob: Boolean, itemViewModel: T) {
            binding.setVariable(BR.vm, itemViewModel)
            binding.executePendingBindings()
            if (isAdmob) {
                if (isAdMobRequestFirst) {
                    adRequest = AdRequest.Builder().build()
                    isAdMobRequestFirst = false
                }
                binding.root.adView.loadAd(adRequest)
            }
        }

    }

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int {
        if (items.get(position) is NotificationModel) {
            return viewTypeNotification(items.get(position) as NotificationModel)
        }
        if (!itemType.isNullOrEmpty()) {
            when (itemType) {
                "home" -> {
                    return homeViewType(position)
                }
                else -> {
                    return layoutId
                }
            }

        } else {
            return layoutId
        }
    }


    interface OnListItemViewClickListener {
        fun onClick(view: View, position: Int, model: ListItemViewModel) {}
        fun onClickDetail(view: View, position: Int, model: ListItemViewModel) {}
        fun onClickUserDetail(view: View, position: Int, model: ListItemViewModel) {}
        fun onClickOptionSettings(view: View, position: Int, model: ListItemViewModel) {}
    }

    fun viewTypeNotification(model: NotificationModel): Int {
        when (model.notificationType) {
            AppConstants.NOTIFICATION_TYPE_APP -> {
                return R.layout.item_notification_app
            }
            AppConstants.NOTIFICATION_TYPE_FOND_ROUTE -> {
                return R.layout.item_notification_route
            }
            AppConstants.NOTIFICATION_TYPE_BID -> {
                return R.layout.item_notification_bid
            }
            else -> {
                return layoutId
            }
        }
    }

    fun homeViewType(position: Int): Int {
        val model = items.get(position) as Trips
        if (position == items.size - 1 && items.size > 5) {
            return R.layout.item_empty
        }
        if (model.ads) {
            return R.layout.item_admob
        } else {
            return R.layout.item_advert
        }
    }
}