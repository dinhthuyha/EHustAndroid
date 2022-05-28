package com.prdcv.ehust.extension

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import java.time.DayOfWeek
import java.time.temporal.WeekFields
import java.util.*

fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}
fun Context.getColorCompat( color: Int) = ContextCompat.getColor(this, color)

fun daysOfWeekFromLocale(): Array<DayOfWeek> {
    val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
    var daysOfWeek = DayOfWeek.values()

    if (firstDayOfWeek != DayOfWeek.MONDAY) {
        val rhs = daysOfWeek.sliceArray(firstDayOfWeek.ordinal..daysOfWeek.indices.last)
        val lhs = daysOfWeek.sliceArray(0 until firstDayOfWeek.ordinal)
        Log.d("hadinh", "daysOfWeekFromLocale: ${rhs}, ${lhs}")
        daysOfWeek = rhs + lhs
    }
    return daysOfWeek
}

fun TextView.setTextColorRes(@ColorRes color: Int) = setTextColor(context.getColorCompat(color))

internal val Context.layoutInflater: LayoutInflater
    get() = LayoutInflater.from(this)

internal fun randomColor(): Int {
    val rnd = Random()
    return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
}

fun String.convertDateEnglishToVN(): String{
    return when(this){
        "SUN" -> "CN"
        "MON" -> "T2"
        "TUE" -> "T3"
        "WEB" -> "T4"
        "THU" -> "T5"
        "FRI" -> "T6"
        else -> "T7"

    }
}

fun String.convertMonthToVN():String {
    return when(this.lowercase()) {
        "january" -> "thg 1"
        "february" -> "thg 2"
        "march" -> "thg 3"
        "april" -> "thg 4"
        "may" -> "thg 5"
        "june" -> "thg 6"
        "july" -> "thg 7"
        "august" -> "thg 8"
        "september" -> "thg 9"
        "october" -> "thg 10"
        "november" -> "thg 11"
        else -> "thg 12"
    }
}
