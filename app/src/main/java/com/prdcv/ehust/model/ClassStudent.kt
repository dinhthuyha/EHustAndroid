package com.prdcv.ehust.model

import com.google.gson.annotations.SerializedName
import com.prdcv.ehust.ui.search.ItemSearch


data class ClassStudent(
    @SerializedName("code_class")
    val codeClass: Int,
    @SerializedName("code_course")
    val codeCourse: String,
    @SerializedName("name_course")
    val nameCourse:String,
    val semester: Int,
    @SerializedName("name_teacher")
    val nameTeacher: String?=null,
    @SerializedName("study_form")
    val studyForm:String?=null
): ItemSearch {
    override fun toString(): String {
        return "$nameCourse - $semester"
    }
    fun line2():String{
        return if (nameTeacher.isNullOrEmpty())
            "Lớp: $codeClass"
        else "Lớp: $codeClass - GV:$nameTeacher"

    }

}
