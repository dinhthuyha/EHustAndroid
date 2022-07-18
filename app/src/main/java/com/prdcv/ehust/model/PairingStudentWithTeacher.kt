package com.prdcv.ehust.model

import com.google.gson.annotations.SerializedName

data class PairingStudentWithTeacher (
    val id: Int,
    @SerializedName("id_teacher")
    val idTeacher: Int,
    @SerializedName("id_student")
    val idStudent: Int,
    @SerializedName("name_project")
    val nameProject: String,
    val semester: Int,
    @SerializedName("name_student")
    val nameStudent: String,
    @SerializedName("name_teacher")
    val nameTeacher: String,
    @SerializedName("code_project")
    val codeProject: String,
    var numberOfStudentsGuiding: Int
        )