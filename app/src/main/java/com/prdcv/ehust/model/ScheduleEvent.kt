package com.prdcv.ehust.model


import java.time.LocalDate
import java.time.LocalTime


data class ScheduleEvent(
    var date: LocalDate,
    val courseName: String,
    val startTime: LocalTime,
    var finishTime: LocalTime,
    var color: Int,
    var dueDateStudy: LocalDate?=null,
    var dateStudy: String,
)
