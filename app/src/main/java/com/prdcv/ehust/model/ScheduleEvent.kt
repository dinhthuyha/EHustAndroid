package com.prdcv.ehust.model


import com.google.gson.annotations.SerializedName
import java.time.LocalDate
import java.time.LocalTime


data class ScheduleEvent(
    var date: LocalDate,
    @SerializedName("name_course")
    val courseName: String,
    @SerializedName("start_time")
    val startTime: LocalTime,
    @SerializedName("finish_time")
    var finishTime: LocalTime,
    var color: Int,
    @SerializedName("date_finish_course")
    var dueDateStudy: LocalDate?=null,
    @SerializedName("date_start_course")
    var startDateStudy: LocalDate
)
