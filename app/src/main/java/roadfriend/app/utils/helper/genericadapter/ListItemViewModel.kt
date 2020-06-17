package roadfriend.app.utils.helper.genericadapter


abstract class ListItemViewModel {
    var adapterPosition: Int = -1
    var onListItemViewClickListener: GenericAdapter.OnListItemViewClickListener? = null
    var itemSize: Int = -1
    val list = mutableListOf<Any>()
}