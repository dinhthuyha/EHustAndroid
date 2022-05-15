package com.prdcv.ehust.model

data class ClassStudent(
    val codeClass: Int,
    val codeCourse: String,
    val nameCourse:String,
    val semester: Int,
    val nameTeacher: String?=null
){
    override fun toString(): String {
        return "$nameCourse - $semester"
    }
    fun line2():String{
        return if (nameTeacher.isNullOrEmpty())
            "Lớp: $codeClass"
        else "Lớp: $codeClass - GV:$nameTeacher"

    }

}
