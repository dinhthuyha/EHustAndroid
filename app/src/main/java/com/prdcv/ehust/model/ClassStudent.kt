package com.prdcv.ehust.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.prdcv.ehust.ui.search.ItemSearch
import kotlinx.parcelize.Parcelize

@Parcelize
data class ClassStudent(
    @SerializedName("code_class")
    val codeClass: Int,
    val semester: Int,
    @SerializedName("name_teacher")
    val nameTeacher: String? = null,
    val name: String,
    @SerializedName("code_course")
    val codeCourse: String,

    @SerializedName("study_form")
    val studyForm: String? = null,

    @SerializedName("subject_class")
    val subject: Subject? = null
    ) : ItemSearch, Parcelable {
    override fun toString(): String {
        return if (semester == 0) {
            "$name - $codeCourse"
        } else
            "$name - $semester"
    }

    fun line2(): String {
        return if (nameTeacher.isNullOrEmpty() && codeClass != 0)
            "Lớp: $codeClass"
        else if (nameTeacher.isNullOrEmpty() && codeClass == 0) {
            ""
        } else {
            "Lớp: $codeClass - GV: $nameTeacher"
        }
    }

}
