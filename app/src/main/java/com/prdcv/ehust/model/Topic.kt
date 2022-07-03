package com.prdcv.ehust.model

import com.google.gson.annotations.SerializedName
import com.hadt.ehust.model.TopicStatus

data class Topic(
    val id: Int? = null,
    val name: String,
    @SerializedName(value = "id_student")
    val idStudent: Int? = null,
    @SerializedName("name_student")
    val nameStudent: String? = null,

    @SerializedName(value = "id_teacher")
    val idTeacher: Int? = null,

    @SerializedName(value = "status")
    val status: TopicStatus? = null,

    @SerializedName(value = "id_subject")
    val subject: Subject? = null,

    @SerializedName("name_teacher")
    val nameTeacher: String? = null
)
