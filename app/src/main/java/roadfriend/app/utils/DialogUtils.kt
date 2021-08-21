package roadfriend.app.utils

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.FragmentActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import org.jetbrains.anko.layoutInflater
import roadfriend.app.R
import roadfriend.app.customviews.MaskEditText
import roadfriend.app.databinding.BottomDialogChooseBinding
import roadfriend.app.databinding.BottomDialogSalesBinding
import roadfriend.app.ui.sales.SalesActivity
import roadfriend.app.utils.extensions.checkPhoNumber
import roadfriend.app.utils.extensions.gone
import roadfriend.app.utils.extensions.visible
import roadfriend.app.utils.helper.BottomSheetAdapter

object DialogUtils {
    private var myDialog: Dialog? = null

    @SuppressLint("SupportAnnotationUsage")
    data class DialogModel(

        var title: String? = null,

        var positive: String? = null,

        var negative: String? = null,

        var icon: Int? = null,
        @DrawableRes

        var isNegativeButton: Boolean = false,
        var imageVisibilty: Boolean = true

    )

    interface DialogListener {

        fun onPositiveClick()

        fun onNegativeClick()

    }

    interface IBottomSheetListener {
        fun selectOption(position: Int, model: String)
    }


    /**Bilgilendirme mesajı*/
    fun showPopupInfo(
        context: Context,
        model: DialogModel,
        btnOk: (() -> Unit?)? = null,
        btnDesc: (() -> Unit?)? = null
    ) {
        myDialog = Dialog(context)
        myDialog?.setContentView(R.layout.custom_dialog)
        val btnDecline: AppCompatButton = myDialog!!.findViewById(R.id.btnDecline)
        val btnAccept: AppCompatButton = myDialog!!.findViewById(R.id.btnAccept)
        val txtPopup: TextView = myDialog!!.findViewById(R.id.tvTitle)
        val close: ImageView = myDialog!!.findViewById(R.id.ivClose)
        val image: AppCompatImageView = myDialog!!.findViewById(R.id.lAnimationView)

        if (model.imageVisibilty) {
            image.visible()
        }
        if (model.isNegativeButton) {
            btnDecline.visible()
        }
        if (model.icon == null) {
            image.gone()
        } else {
            image.visible()
        }
        txtPopup.text = model.title
        btnDecline.text = model.negative
        btnAccept.text = model.positive

        btnAccept.setOnClickListener {
            btnOk?.invoke()
            myDialog?.dismiss()
        }
        btnDecline.setOnClickListener {
            btnDesc?.invoke()
        }

        btnDecline.setOnClickListener {
            myDialog?.dismiss()
        }
        myDialog?.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        myDialog!!.show()
    }

    /**Listener mesajları*/
    fun showPopupModel(context: Context, model: DialogModel, listener: DialogListener) {
        myDialog = Dialog(context)
        myDialog?.setContentView(R.layout.custom_dialog)
        val btnDecline: AppCompatButton = myDialog!!.findViewById(R.id.btnDecline)
        val btnAccept: AppCompatButton = myDialog!!.findViewById(R.id.btnAccept)
        val txtPopup: TextView = myDialog!!.findViewById(R.id.tvTitle)
        val close: ImageView = myDialog!!.findViewById(R.id.ivClose)
        val image: AppCompatImageView = myDialog!!.findViewById(R.id.lAnimationView)


        if (model.isNegativeButton) {
            btnDecline.visible()
        }
        model.icon?.let {
            image.visible()
            image.setImageDrawable(context.resources.getDrawable(it))
        }
        txtPopup.text = model.title
        btnDecline.text = model.negative
        btnAccept.text = model.positive

        btnDecline.setOnClickListener {
            myDialog?.dismiss()
            listener.onNegativeClick()

        }
        btnAccept.setOnClickListener {
            myDialog?.dismiss()
            listener.onPositiveClick()

        }
        myDialog!!.setCancelable(false)
        myDialog?.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        myDialog!!.show()
    }

    data class BottomSheetModel(
        val title: String?,
        val isClose: Boolean?
    )

    fun bottomSheet(
        context: Context,
        list: ArrayList<String>,
        iBottomSheetListener: IBottomSheetListener,
        bottomSheetModel: BottomSheetModel? = null
    ) {

        val dialog = BottomSheetDialog(context, R.style.SheetDialog)
        val view = BottomDialogChooseBinding.inflate(LayoutInflater.from(context))

        val adapterChoose by lazy {
            BottomSheetAdapter(
                context,
                arrayListOf(),
                object : BottomSheetAdapter.IChooseListener {
                    override fun choose(position: Int, model: String) {
                        dialog.dismiss()
                        iBottomSheetListener.selectOption(position, model)
                    }
                })
        }

        if (bottomSheetModel != null) {
            view.tvTitle.visible()
            view.tvTitle.text = bottomSheetModel.title
            view.icClose.visibility = if (bottomSheetModel.isClose!!) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
        view.rv.adapter = adapterChoose
        adapterChoose.add(list)
        view.icClose.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    fun showPopupPhone(context: Context, phoneNumber: (phone: String) -> Unit) {
        myDialog = Dialog(context)
        myDialog?.setContentView(R.layout.dialog_phone)
        val phone: MaskEditText = myDialog!!.findViewById(R.id.etPhone)
        val btnAccept: AppCompatButton = myDialog!!.findViewById(R.id.btnSend)
        val info: TextView = myDialog!!.findViewById(R.id.errorMessage)

        btnAccept.setOnClickListener {
            if (phone.rawText.toString().isNullOrEmpty()) {
                info.visible()
            } else {
                if (phone.rawText?.checkPhoNumber()!!) {
                    myDialog?.dismiss()
                    phoneNumber(phone.rawText.toString())
                } else {
                    info.visible()
                }
            }
        }

        myDialog?.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        myDialog!!.show()
    }

    fun showPopupRate(context: Context, activity: SalesActivity, noRate: () -> Unit) {
        myDialog = Dialog(context)
        myDialog?.setContentView(R.layout.dialog_rate)
        val btnNo: AppCompatButton = myDialog!!.findViewById(R.id.btnNo)
        val btnYes: AppCompatButton = myDialog!!.findViewById(R.id.btnOk)
        btnNo.setOnClickListener {
            noRate.invoke()
        }
        btnYes.setOnClickListener {
            PrefUtils.setRated()
            myDialog?.dismiss()
            activity.inAppReview()
        }
        myDialog?.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        myDialog!!.show()
    }

    fun FragmentActivity.inAppReview() {
        var reviewInfo: ReviewInfo? = null
        var manager: ReviewManager = ReviewManagerFactory.create(this)
        val requestFlow = manager.requestReviewFlow()
        requestFlow.addOnCompleteListener { request ->
            if (request.isSuccessful) {
                reviewInfo = request.result
                reviewInfo?.let {
                    val flow = manager.launchReviewFlow(this, it)
                }

            } else {
                reviewInfo = null
            }
        }
    }

}