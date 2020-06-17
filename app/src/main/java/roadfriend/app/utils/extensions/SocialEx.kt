package roadfriend.app.utils.extensions

import android.os.Bundle
import com.facebook.AccessToken
import com.facebook.GraphRequest

fun getFacebookUserInfo(
    accessToken: AccessToken,
    nameCallBack: (name: String?, email: String?) -> Unit
) {
    val request = GraphRequest.newMeRequest(
        accessToken
    ) { `objectFacebook`, response ->
        // Application code
        if (objectFacebook != null) {
            val name_fb = objectFacebook.optString("name")
            val email_fb = objectFacebook.optString("email")
            nameCallBack(name_fb, email_fb)
        }
    }
    val parameters = Bundle()
    parameters.putString("fields", "id,name,email,link")
    request.setParameters(parameters)
    request.executeAsync()
}