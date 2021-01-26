package doa.ai.extentions

import java.text.SimpleDateFormat
import java.util.*

const val REGISTER_DATE_FORMAT = "E,dd/MM/yyyy"

internal fun getCurrentDay(): Int = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

internal fun getCurrentMonth(): Int = Calendar.getInstance().get(Calendar.MONTH)

internal fun getCurrentYear(): Int = Calendar.getInstance().get(Calendar.YEAR)

internal fun getCurrentHour(): Int = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)

internal fun getCurrentMinute(): Int = Calendar.getInstance().get(Calendar.MINUTE)

internal fun getCurrentDate(): String {
    val dateTime = SimpleDateFormat(REGISTER_DATE_FORMAT, Locale.ENGLISH)
    return dateTime.format(Date().time)
}

internal fun getCustomDateRange(calendar: Calendar): String {
    val dateTime = SimpleDateFormat(REGISTER_DATE_FORMAT, Locale.ENGLISH)
    return dateTime.format(calendar.time)
}
