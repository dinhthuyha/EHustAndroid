package com.prdcv.ehust.network

import com.hadt.ehust.model.StatusTopic
import com.prdcv.ehust.model.ClassStudent
import com.prdcv.ehust.model.Comment
import com.prdcv.ehust.model.News
import com.prdcv.ehust.model.Role
import com.prdcv.ehust.model.ScheduleEvent
import com.prdcv.ehust.model.Subject
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
        nameTeacher: String,
        idProject: String,
        idTeacher: Int
    ): Response<List<Topic>>{
        return ehustService.findTopicByIdTeacherAndIdProject(nameTeacher, idProject, idTeacher)
    }

    suspend fun getAllUserInClass(nameCourse: String, role: Role): Response< List<User>>{
        return ehustService.getAllUserInClass(nameCourse, role)
    }

    suspend fun getAllProjectCurrentSemester():Response<List<Subject>>{
        return ehustService.getAllProjectCurrent()
    }

    suspend fun assignProjectInstructions(
        idStudent: Int,
        idTeacher: Int,
        nameProject: String
    ) =
        ehustService.assignProjectInstructions(idStudent, idTeacher, nameProject)

    suspend fun updateTopicTable(
        idTopic: Int,
        status: StatusTopic,
        idStudent: Int
    ) = ehustService.updateStatusAndIdStudentTopic(idTopic, status, idStudent)

    suspend fun findAllTaskByIdTopic(
        idTopic: Int
    ) = ehustService.findAllTaskByIdTopic(idTopic)

    suspend fun getDetailTask(id: Int) = ehustService.getDetailTask(id)

    suspend fun findAllCommentByIdTask(idTask: Int) = ehustService.findAllCommentByIdTask(idTask)

    suspend fun postComment(idTask: Int, comment: Comment) = ehustService.postComment(idTask, comment)

    suspend fun deleteComment(id: Int) = ehustService.deleteComment(id)
}