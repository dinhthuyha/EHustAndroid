package com.prdcv.ehust.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.hadt.ehust.model.TopicStatus
import kotlinx.parcelize.Parcelize

@Parcelize
data class Topic(
    val id: Int? = null,
    val name: String,
    @SerializedName(value = "id_student")
    val idStudent: Int? = null,
    @SerializedName("name_student")
    var nameStudent: String? = null,

    @SerializedName(value = "id_teacher")
    val idTeacher: Int? = null,

    @SerializedName(value = "status")
    val status: TopicStatus? = null,

    @SerializedName(value = "id_subject")
    val subject: Subject? = null,

    @SerializedName("name_teacher")
    val nameTeacher: String? = null
) : Parcelable
