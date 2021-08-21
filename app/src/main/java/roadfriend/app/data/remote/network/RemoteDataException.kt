package roadfriend.app.data.remote.network

import android.util.Log
import com.google.gson.Gson
import roadfriend.app.data.remote.model.base.BaseErrorMessage
import okhttp3.ResponseBody
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class RemoteDataException {
    var code: Int = 0
    var message: String = ""
    lateinit var errorStatus: ErrorStatus

    constructor(throwable: Throwable) {
        when (throwable) {

            // if throwable is an instance of HttpException
            // then attempt to parse error data from response body
            is HttpException -> {
                // handle UNAUTHORIZED situation (when token expired)
                if (throwable.code() == 401) {
                    errorStatus = ErrorStatus.UNAUTHORIZED
                    code = throwable.code()
                    message = "Lütfen Giriş Yapınız."
                    logOut(throwable.code())
                } else {
                    getHttpError(throwable.response()?.errorBody(), throwable.response()!!.code())
                }
            }

            // handle api call timeout error
            is SocketTimeoutException -> {
                errorStatus = ErrorStatus.TIMEOUT
                code = 0
                message = "Bağlantı Zaman Aşımına Uğradı."
            }

            // handle connection error
            is IOException -> {
                errorStatus = ErrorStatus.NO_CONNECTION
                code = 0
                message = "İnternet Bağlantınızı Kontrol Ediniz."
            }

            is UnknownHostException -> {
                errorStatus = ErrorStatus.NO_CONNECTION
                code = 0
                message = "İnternet Bağlantınızı Kontrol Ediniz."
            }

            is UninitializedPropertyAccessException -> {
                errorStatus = ErrorStatus.NO_CONNECTION
                code = 0
                message = "İnternet Bağlantınızı Kontrol Ediniz."
            }

            else -> null
        }
    }

    constructor(responseBody: ResponseBody?, code: Int) {
        getHttpError(responseBody, code)
    }


    /**
     * attempts to parse http response body and get error data from it
     *
     * @param body retrofit response body
     * @return returns an instance of [ErrorModel] with parsed data or NOT_DEFINED status
     */
    private fun getHttpError(body: ResponseBody?, codem: Int) {
        return try {
            // use response body to get error detail
            val result = body?.string()
            Log.d("getHttpError", "getErrorMessage() called with: errorBody = [$result]")
            val json = Gson().fromJson(result, BaseErrorMessage::class.java)
            errorStatus = ErrorStatus.BAD_RESPONSE
            message = json.message
            //Logger.w(message)
            logOut(codem)
        } catch (e: Throwable) {
            e.printStackTrace()
            errorStatus = ErrorStatus.NOT_DEFINED
            code = codem
            message = ""
        }

    }

    private fun logOut(code: Int) {
        if (code == 401) {
//            context.launchActivity<AuthLoginActivity> {
//                PrefUtils.logoutUser()
//
//            }
        }

    }
}

enum class ErrorStatus {
    /**
     * error in connecting to repository (Server or Database)
     */
    NO_CONNECTION,
    /**
     * error in getting value (Json Error, Server Error, etc)
     */
    BAD_RESPONSE,
    /**
     * Time out  error
     */
    TIMEOUT,
    /**
     * no data available in repository
     */
    EMPTY_RESPONSE,
    /**
     * an unexpected error
     */
    NOT_DEFINED,
    /**
     * bad credential
     */
    UNAUTHORIZED
}