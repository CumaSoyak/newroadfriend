package roadfriend.app.utils

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import roadfriend.app.utils.extensions.toDateDayofMonthYear
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

    fun getDate(context: Context, dataListener: DataListener) {
        val takvim = Calendar.getInstance() // Takvim objesini oluşturuyoruz.
        val year = takvim.get(Calendar.YEAR) //Güncel Yılı alıyoruz.
        val month = takvim.get(Calendar.MONTH) //Güncel Ayı alıyoruz.
        val day = takvim.get(Calendar.DAY_OF_MONTH) //Güncel Günü alıyoruz.
        val datePicker = DatePickerDialog(
            context,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                var day: Int = dayOfMonth
                var month: Int = (monthOfYear + 1)
                var monthString: String = month.toString()
                var dayString: String = day.toString()

                if (month < 10) {
                    monthString = "0" + month.toString()
                }
                if (dayOfMonth < 10) {
                    dayString = "0" + day.toString()
                }

                val normalDate = "$year-$monthString-$dayString"
                val serviceDate = "$year/$month/$day"
                dataListener.date(toDateDayofMonthYear(normalDate), serviceDate)

            }, year, month, day
        )
        datePicker.setTitle("Tarih Seçiniz")
        datePicker.setButton(DatePickerDialog.BUTTON_POSITIVE, "SEÇ", datePicker)
        datePicker.setButton(DatePickerDialog.BUTTON_NEGATIVE, "İPTAL", datePicker)
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