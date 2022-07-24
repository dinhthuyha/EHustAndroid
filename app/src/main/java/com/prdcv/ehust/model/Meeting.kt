package com.prdcv.ehust.model

import com.google.gson.annotations.SerializedName
import java.time.LocalDate
import java.time.LocalTime

data class Meeting (
    val id: Int? = null,
    @SerializedName( "id_teacher")
    val idUserTeacher: Int,
    @SerializedName( "id_student")
    val idUserStudent: Int? = null,
    @SerializedName( "name_student")
    val nameStudent: String,
    val title: String,
    val date: LocalDate,
    @SerializedName( "start_time")
    val startTime: LocalTime,
    @SerializedName( "end_time")
    val endTime: LocalTime,
    var color: Int? = null,
): SChedule