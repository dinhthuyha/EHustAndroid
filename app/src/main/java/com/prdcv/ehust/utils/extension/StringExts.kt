package com.prdcv.ehust.utils.extension

import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

fun String.toLocalDate(): LocalDate {
    val dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    return LocalDate.parse(this, dtf)
}

fun String.toLocalTime():LocalTime{

    val dtf = DateTimeFormatter.ofPattern("HH:mm");
    return LocalTime.parse(this, dtf)
}

fun LocalTime.toString(): String{
    val dtf = DateTimeFormatter.ofPattern("HH:mm")
    return dtf.format(this)
}

fun LocalDate.toString(): String{
    val dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    return dtf.format(this)
}