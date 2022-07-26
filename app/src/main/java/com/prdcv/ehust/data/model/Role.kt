package com.prdcv.ehust.data.model

enum class Role {
    ROLE_UNKNOWN, ROLE_ADMIN, ROLE_TEACHER, ROLE_STUDENT;

    companion object {
        const val ADMIN = "ADMIN"
        const val TEACHER = "TEACHER"
        const val STUDENT = "STUDENT"
        const val UNKNOWN = "UNKNOWN"
    }
}