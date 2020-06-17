package roadfriend.app.utils.extensions

import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


fun String.toDate(): String {
    var date = SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(this)
    return SimpleDateFormat("dd MMMM yyyy", Locale("tr", "TR")).format(date).toString()
}

fun String.toDateMonthDay(): String {
    var date = SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(this)
    return SimpleDateFormat("dd MMMM", Locale("tr", "TR")).format(date).toString()
}

fun String.toDateYear(): String {
    var date = SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(this)
    return SimpleDateFormat("yyyy", Locale("tr", "TR")).format(date).toString()
}

fun String.toHours(): String {
    var date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(this)
    return SimpleDateFormat("HH:mm", Locale("tr", "TR")).format(date).toString()
}

fun toDateFormat(text: String?): String {
    var date = ""
    if (text!!.length >= 10)
        date = (text.substring(8, 10) + "." + text.substring(5, 7) + "." + text.substring(0, 4))
    return date
}

fun toDateMonthOfYear(text: String): String {
    if (text.length >= 10) {
        val simpledateformat = SimpleDateFormat("MMMM")
        val date = Date(
            text.substring(0, 4).toInt(),
            text.substring(5, 7).toInt() - 1,
            text.substring(8, 10).toInt()
        )
        val monthOfYear = simpledateformat.format(date)
        val day = text.substring(0, 4)
        return day + " " + monthOfYear
    } else
        return text

}

fun toDateMonthOfYearAndDay(text: String): String {
    if (text.length >= 10) {
        val simpledateformat = SimpleDateFormat("MMMM")
        val date = Date(
            text.substring(0, 4).toInt(),
            text.substring(5, 7).toInt() - 1,
            text.substring(8, 10).toInt()
        )
        val monthOfYear = simpledateformat.format(date)
        val day = text.substring(8, 10)


        val simpledateformatDay = SimpleDateFormat("EEEE")
        val dayString =
            Date(
                text.substring(0, 4).toInt(),
                text.substring(5, 7).toInt() - 1,
                text.substring(8, 10).toInt() - 1
            )
        val dayOfWeek = simpledateformatDay.format(dayString)

        return day + " " + monthOfYear + " " + dayOfWeek
    } else
        return text
}

fun toDateDayofMonthYear(text: String?): String {  //exapmle 11 Kasın 2019
    if (text!!.length >= 10) {
        val simpledateformat = SimpleDateFormat("MMMM")
        val date = Date(
            text.substring(0, 4).toInt(),
            text.substring(5, 7).toInt() - 1,
            text.substring(8, 10).toInt()
        )
        val monthOfYear = simpledateformat.format(date)
        val day = text.substring(8, 10)
        val year = text.substring(0, 4)

        return day + " " + monthOfYear + " " + year
    } else {
        return text
    }

}

fun toDateDayofMonth(text: String?): String {  //exapmle 11 Kasım
    if (text!!.length >= 10) {
        val simpledateformat = SimpleDateFormat("MMMM")
        val date = Date(
            text.substring(0, 4).toInt(),
            text.substring(5, 7).toInt() - 1,
            text.substring(8, 10).toInt()
        )
        val monthOfYear = simpledateformat.format(date)
        val day = text.substring(8, 10)
        return day + " " + monthOfYear
    } else {
        return text
    }

}

fun getCurrentDate(): Long {
    return TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())
}

