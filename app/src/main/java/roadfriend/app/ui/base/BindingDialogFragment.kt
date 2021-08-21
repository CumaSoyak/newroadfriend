package roadfriend.app.ui.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.viewbinding.ViewBinding
import roadfriend.app.utils.helper.AutoClearedValue

abstract class BindingDialogFragment<VB : ViewBinding> : BaseDialogFragment<VB>() {

}