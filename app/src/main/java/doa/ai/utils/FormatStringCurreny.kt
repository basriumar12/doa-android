package doa.ai.utils

import java.text.NumberFormat
import java.util.*

fun Number?.formatRupiah() = if (this == null) "-"
else NumberFormat.getCurrencyInstance(Locale("in", "ID")).format(this)

fun Number?.formatSeparator() = if (this == null) ""
else NumberFormat.getNumberInstance(Locale("in", "ID")).format(this)

fun String?.formatStringCurrency() =
        if (this.isNullOrBlank()) {
            ""
        } else {
            NumberFormat.getNumberInstance(Locale("in", "ID"))
                    .format(this?.toDouble())
        }

fun String?.removePeriodString() = this?.replace(".", "") ?: ""


fun Number?.toCoupleDigit(): String {
    if (this.toString().length > 1) {
        return this.toString()
    } else {
        return "0${this}"
    }
}

fun String?.toCoupleDigit(): String {
    if (this.isNullOrEmpty()) {
        return "-"
    } else {
        return "0${this}"
    }
}