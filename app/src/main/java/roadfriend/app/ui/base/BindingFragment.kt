package roadfriend.app.ui.base
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import roadfriend.app.utils.helper.AutoClearedValue

abstract class BindingFragment<T : ViewDataBinding> : BaseFragment() {
    protected var binding: T by AutoClearedValue<T>()

    @get:LayoutRes
    abstract val getLayoutBindId: Int

    override val layoutId: Int?
        get() = null

    override fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): View? {
        return DataBindingUtil.inflate<T>(
            inflater,
            getLayoutBindId,
            container,
            false
        ).apply { binding = this }.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        //binding = null
    }

}