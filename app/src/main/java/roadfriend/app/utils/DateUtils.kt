package roadfriend.app.utils

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import roadfriend.app.R
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


object DateUtils {

    val c = Calendar.getInstance()

    interface DataListener {
        fun date(date: String, serviceDate: String)
        fun time(time: String)
    }

    var d = Date()

    @SuppressLint("SimpleDateFormat")
    fun currentDate(): String {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
        return simpleDateFormat.format(d)
    }

    @SuppressLint("SimpleDateFormat")
    fun currentTime(): String {
        val simpleDateFormat = SimpleDateFormat("HH:mm:ss")
        return simpleDateFormat.format(d)

    }

    fun Context.getDate(callBackDate: (date: String) -> Unit) {
        val calendar = Calendar.getInstance() // Takvim objesini oluşturuyoruz.
        val calendarYear = calendar.get(Calendar.YEAR) //Güncel Yılı alıyoruz.
        val calendarMonth = calendar.get(Calendar.MONTH) //Güncel Ayı alıyoruz.
        val calendarDay = calendar.get(Calendar.DAY_OF_MONTH) //Güncel Günü alıyoruz.
        val datePicker = DatePickerDialog(
            this,R.style.DialogTheme,
            DatePickerDialog.OnDateSetListener { view, year, month, day ->
                val cal = Calendar.getInstance()
                cal.timeInMillis = 0
                cal[year, month, day, 0, 0] = 0
                val chosenDate = cal.time
                val df_medium_country: DateFormat = DateFormat.getDateInstance(
                    DateFormat.MEDIUM,
                    Locale(OtherUtils.getCurrentCountryCode())
                )
                val resultDate: String = df_medium_country.format(chosenDate)
                callBackDate(resultDate)
            }, calendarYear, calendarMonth, calendarDay
        )
        datePicker.setTitle(this.getString(R.string.takvim_tarih_sec))
        datePicker.setButton(DatePickerDialog.BUTTON_POSITIVE, getString(R.string.takvim_sec), datePicker)

        datePicker.setButton(DatePickerDialog.BUTTON_NEGATIVE, getString(R.string.takvim_iptal), datePicker)
        datePicker.show()
    }

    fun getTime(context: Context, dataListener: DataListener) {
        val cal = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)
            dataListener.time(SimpleDateFormat("HH:mm").format(cal.time))
        }
        TimePickerDialog(
            context,
            timeSetListener,
            cal.get(Calendar.HOUR_OF_DAY),
            cal.get(Calendar.MINUTE),
            true
        ).show()
    }

}