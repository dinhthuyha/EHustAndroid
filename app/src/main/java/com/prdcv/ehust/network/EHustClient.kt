package com.prdcv.ehust.network

import com.prdcv.ehust.model.ClassStudent
import com.prdcv.ehust.model.News
import com.prdcv.ehust.model.Role
import com.prdcv.ehust.model.ScheduleEvent
import com.prdcv.ehust.model.Topic
import com.prdcv.ehust.model.User
import retrofit2.Response
import javax.inject.Inject

class EHustClient @Inject constructor(
    private val ehustService: EHustService
) {
    suspend fun login(id: Int, password: String): Response<Map<String,Any>> {
        return ehustService.login(id, password)
    }

    suspend fun getAllNews(): Response<List<News>> {
        return ehustService.getAllNews()
    }

    suspend fun getProfileById(id: Int): Response<User> {
        return ehustService.getProfileById(id)
    }

    suspend fun getListStudentInClass(grade: String): Response<List<User>> {
        return ehustService.getListStudentInClass(grade)
    }

    suspend fun findAllProjectsByStudentId(id: Int): Response<List<ClassStudent>> {
        return ehustService.findAllProjectsByStudentId(id)
    }

    suspend fun searchClassById(id: Int): Response<ClassStudent> {
        return ehustService.searchClassById(id)
    }

    suspend fun searchUserById(id: Int, roleId: Role): Response<User> {
        return ehustService.searchUserById(id, roleId)
    }

    suspend fun findAllSchedules(id: Int): Response<List<ScheduleEvent>>{
        return ehustService.findAllSchedule(id)
    }
    suspend fun findTopicByIdTeacherAndIdProject(
        idTeacher: Int,
        idProject: String
    ): Response<List<Topic>>{
        return ehustService.findTopicByIdTeacherAndIdProject(idTeacher, idProject)
    }

}