package roadfriend.app.ui.base
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import roadfriend.app.utils.helper.AutoClearedValue

abstract class BindingFragment<VB : ViewDataBinding> : BaseFragment<VB>() {

}