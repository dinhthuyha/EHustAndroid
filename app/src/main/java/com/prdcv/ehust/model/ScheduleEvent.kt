package com.prdcv.ehust.model


import java.time.LocalDate
import java.time.LocalTime

data class ScheduleEvent(
    val date: LocalDate,
    val subject: String,
    val startTime: LocalTime,
    var finishTime: LocalTime,
    var color: Int
)
