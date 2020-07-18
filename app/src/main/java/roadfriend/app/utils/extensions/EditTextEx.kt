package roadfriend.app.utils.extensions

import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.widget.EditText
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.material.textfield.TextInputEditText

fun EditText.textString(): String {
    return this.text.toString().trim()
}

fun EditText.listener(iEditTextCallBack: (text: String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable) {
            iEditTextCallBack(s.toString())
        }

        override fun beforeTextChanged(
            s: CharSequence, start: Int,
            count: Int, after: Int
        ) {
            iEditTextCallBack(s.toString())
        }

        override fun onTextChanged(
            s: CharSequence, start: Int,
            before: Int, count: Int
        ) {
            iEditTextCallBack(s.toString())
        }
    })
}

fun TextInputEditText.listener(iEditTextCallBack: (text: String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable) {
            iEditTextCallBack(s.toString())
        }

        override fun beforeTextChanged(
            s: CharSequence, start: Int,
            count: Int, after: Int
        ) {
            iEditTextCallBack(s.toString())
        }

        override fun onTextChanged(
            s: CharSequence, start: Int,
            before: Int, count: Int
        ) {
            iEditTextCallBack(s.toString())
        }
    })
}

fun LoginButton.registerSimpleCallback(
    callbackManager: CallbackManager,
    onSuccess: (loginResult: LoginResult) -> Unit
) {
    this.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
        override fun onSuccess(result: LoginResult?) {
            onSuccess.invoke(result!!)
        }

        override fun onCancel() {}

        override fun onError(error: FacebookException?) {

        }
    })
}

fun EditText.numberFormat() {
    val myFilter = InputFilter { source, _, _, dest, _, _ ->
        if (dest.toString() == "0" && source.toString() == "0") {
            ""
        } else null
    }
    this.filters = arrayOf(myFilter)

}

interface IEditTextCallBack {
    fun afterTextChanged(text: String?)
    fun onTextChanged(text: String?)
}