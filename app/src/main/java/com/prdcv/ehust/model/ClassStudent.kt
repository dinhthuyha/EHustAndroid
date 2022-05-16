package com.prdcv.ehust.model

import com.prdcv.ehust.ui.search.ItemSearch

data class ClassStudent(
    val codeClass: Int,
    val codeCourse: String,
    val nameCourse:String,
    val semester: Int,
    val nameTeacher: String?=null,
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
