package com.prdcv.ehust.model

import com.google.gson.annotations.SerializedName
import java.time.LocalDate

data class MoreInformationTopic(
    val id: Int? = null,
    val title: String?= null,
    val description: String? = null,
    @SerializedName("start_time_progress")
    val startTimeProgress: LocalDate? = null,
    @SerializedName("due_time")
    val dueTime: LocalDate? = null,
    @SerializedName("name_teacher")
    var nameTeacher: String ? = null,
    @SerializedName("id_teacher")
    val idTeacher: Int? = null,
    @SerializedName("email_teacher")
    val emailTeacher: String? = null,
    @SerializedName("name_student")
    var nameStudent: String? = null,
    @SerializedName("id_student")
    val idStudent: Int? = null,
    @SerializedName("email_student")
    val emailStudent: String? = null
)