package com.prdcv.ehust.network

import com.hadt.ehust.model.TopicStatus
import com.prdcv.ehust.model.*
import io.minio.MinioClient
import io.minio.ObjectWriteResponse
import io.minio.PutObjectArgs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class EHustClient @Inject constructor(
    private val ehustService: EHustService,
    private val minioClient: MinioClient
) {
    suspend fun login(id: Int, password: String): Response<Map<String, Any>> {
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

    suspend fun searchUserByFullName(fullName: String, roleId: Role): Response<User> {
        return ehustService.searchUserByFullName(fullName, roleId)
    }

    suspend fun findAllSchedules(id: Int): Response<List<ScheduleEvent>> {
        return ehustService.findAllSchedule(id)
    }

    suspend fun findTopicByIdTeacherAndIdProject(
        nameTeacher: String,
        idProject: String,
        idTeacher: Int
    ): Response<List<Topic>> {
        return ehustService.findTopicByIdTeacherAndIdProject(nameTeacher, idProject, idTeacher)
    }

    suspend fun getAllUserInClass(nameCourse: String, role: Role): Response<List<User>> {
        return ehustService.getAllUserInClass(nameCourse, role)
    }

    suspend fun getAllProjectCurrentSemester(): Response<List<Subject>> {
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
        status: TopicStatus,
        idStudent: Int
    ) = ehustService.updateStatusAndIdStudentTopic(idTopic, status, idStudent)

    suspend fun findAllTaskByIdTopic(
        idTopic: Int
    ) = ehustService.findAllTaskByIdTopic(idTopic)

    suspend fun submitTopicSuggestion(topic: Topic) = ehustService.submitTopicSuggestion(topic)

    suspend fun getDetailTask(id: Int) = ehustService.getDetailTask(id)

    suspend fun newTask(idTopic: Int, task: TaskDetail) = ehustService.newTask(idTopic, task)

    suspend fun updateTask(taskDetail: TaskDetail) = ehustService.updateTask(taskDetail)

    suspend fun findAllCommentByIdTask(idTask: Int) = ehustService.findAllCommentByIdTask(idTask)

    suspend fun postComment(idTask: Int, comment: Comment) = ehustService.postComment(idTask, comment)

    suspend fun deleteComment(id: Int) = ehustService.deleteComment(id)

    suspend fun addAttachment(idComment: Int, attachment: Attachment) =
        ehustService.addAttachmentComment(idComment, attachment)

    suspend fun uploadAttachment(attachmentInfo: AttachmentInfo): ObjectWriteResponse =
        withContext(Dispatchers.IO) {
            minioClient.putObject(
                PutObjectArgs.builder()
                    .bucket("attachment")
                    .`object`(attachmentInfo.filename)
                    .apply { attachmentInfo.contentType?.let(this::contentType) }
                    .stream(
                        attachmentInfo.inputStream,
                        attachmentInfo.inputStream.available().toLong(),
                        -1
                    )
                    .build()
            )
        }

    suspend fun getAttachments(idTask: Int): Response<List<Attachment>> =
        ehustService.getAttachments(idTask)

    suspend fun findAllTaskWillExpire() = ehustService.findAllTaskWillExpire()

    suspend fun postMeeting(meeting: Meeting) = ehustService.postMeeting(meeting)

    suspend fun findAllMeeting(idUserteacher: Int, idUserStudent: Int) =
        ehustService.findAllMeeting(idUserteacher, idUserStudent)

    suspend fun findByDetailTopic(id: Int) = ehustService.findByDetailTopic(id)

    suspend fun getAllSemester() = ehustService.getAllSemester()

    suspend fun getAllProjectByIdTeacherAndSemester(idTeacher: Int,  semester: Int) = ehustService.getAllProjectByIdTeacherAndSemester(idTeacher, semester)
}