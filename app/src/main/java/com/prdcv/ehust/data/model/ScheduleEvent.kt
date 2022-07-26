package com.prdcv.ehust.data.model


import com.google.gson.annotations.SerializedName
import java.time.LocalDate
import java.time.LocalTime


data class ScheduleEvent(
    var date: LocalDate,
    @SerializedName("start_time")
    val startTime: LocalTime? = null,
    @SerializedName("finish_time")
    var finishTime: LocalTime? = null,
    var color: Int,
    @SerializedName("date_finish_course")
    var dueDateStudy: LocalDate?=null,
    @SerializedName("date_start_course")
    var startDateStudy: LocalDate? = null,
    @SerializedName("subject_class")
    val subjectClass: Subject
): SChedule
