package com.prdcv.ehust.network

import com.prdcv.ehust.model.StatusNotification
import com.hadt.ehust.model.TopicStatus
import com.hadt.ehust.model.TypeNotification
import com.prdcv.ehust.model.*
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface EHustService {
    @FormUrlEncoded
    @POST("signin")
    suspend fun login(@Field("id") id: Int,
                      @Field("password") password: String): Response<Map<String,Any>>

    @GET("news/{type}")
    suspend fun getAllNews(@Path("type") type: TypeNotification): Response<List<News>>

    @FormUrlEncoded
    @PUT("new/update/status/{id}/{type}")
    suspend fun updateStatusNew(@Path("id") id: Int, @Path("type") type: TypeNotification, @Field(value = "status") status: StatusNotification):Response<List<News>>

    @GET("user/profile/{id}")
    suspend fun getProfileById(@Path("id") id: Int): Response<User>

    @GET("search/user/name/{fullName}/{roleId}")
    suspend fun searchUserByFullName(@Path("fullName") fullName: String, @Path("roleId") roleId: Role): Response<User>

    @GET("search/user/id/{id}/{roleId}")
    suspend fun searchUserById(@Path("id") id: Int, @Path("roleId") roleId: Role): Response<User>

    @GET("search/class/{id}")
    suspend fun searchClassById(@Path("id") id: Int): Response<ClassStudent>

    @GET("classstudent/{grade}")
    suspend fun getListStudentInClass(@Path("grade") grade: String): Response<List<User>>

    @GET("user/projects/{id}")
    suspend fun findAllProjectsByStudentId(@Path("id") id: Int): Response<List<ClassStudent>>

    @GET("user/{id}/schedule")
    suspend fun findAllSchedule(@Path("id") id: Int): Response<List<ScheduleEvent>>

    @GET("topic/teacher/{id_teacher}/{name_teacher}/{id_project}/{semester}")
    suspend fun findTopicByIdTeacherAndIdProject(
        @Path("name_teacher") nameTeacher: String,
        @Path("id_project") idProject: String,
        @Path("id_teacher") idTeacher: Int,
        @Path("semester") semester: Int
    ): Response<List<Topic>>


    /**
     * Tim tat cac user trong lop hoc ki B va hoc mon A
     */
    @GET("project/users/{nameCourse}/{role}")
    suspend fun getAllUserInClass(@Path("nameCourse") nameCourse: String, @Path("role") role: Role): Response< List<User>>

    /**
     * ds project trong ki hien tai
     */
    @GET("project/current/semester")
    suspend fun getAllProjectCurrent(): Response<List<Subject>>

    @FormUrlEncoded
    @POST("assignProject")
    suspend fun assignProjectInstructions(@Field("id_student") idStudent: Int,
                                          @Field("id_teacher") idTeacher: Int,
                                          @Field("name_project") nameProject: String): Response<ResponseBody>

    /**
     * update status cho topic
     */
    @FormUrlEncoded
    @PUT("updatetopic/{id_topic}")
    suspend fun updateStatusAndIdStudentTopic(
        @Path("id_topic") id: Int,
        @Field(value = "status") status: TopicStatus,
        @Field(value = "id_student") idStudent: Int
    ): Response<ResponseBody>

    /**
     * get all task theo id topic
     */
    @GET("allTask/idTopic/{id_topic}")
    suspend fun findAllTaskByIdTopic(
        @Path("id_topic") id: Int
    ): Response<List<TaskData>>

    @POST("topic/{id_topic}/task")
    suspend fun newTask(@Path("id_topic") idTopic: Int, @Body taskData: TaskDetail): Response<Int>

    @POST("topic/suggestion")
    suspend fun submitTopicSuggestion(@Body topic: Topic): Response<ResponseBody>

    /**
     * get detail task
     */
    @GET("task/{id}")
    suspend fun getDetailTask(@Path("id") id: Int): Response<TaskDetail>

    @PUT("updateTask")
    suspend fun updateTask(@Body taskDetail: TaskDetail): Response<ResponseBody>
    /**
     * get list comments
     */
    @GET("comments/{id_task}")
    suspend fun findAllCommentByIdTask(@Path("id_task") idTask: Int): Response<List<Comment>>

    /**
     * post comment
     */
    @POST("comments/{id_task}")
    suspend fun postComment(@Path("id_task") idTask: Int, @Body comment: Comment): Response<Int>

    /**
     * delete comment
     */
    @DELETE("deleteComment")
    suspend fun deleteComment(@Path("id") id: Int): Response<ResponseBody>

    @POST("task/{id_task}/attachment")
    suspend fun addAttachment(@Path("id_task") idTask: Int, @Body attachment: Attachment): Response<List<Attachment>>

    @POST("comments/{id_cmt}/attachment")
    suspend fun addAttachmentComment(@Path("id_cmt") idTask: Int, @Body attachment: Attachment): Response<Any>

    @GET("task/{id_task}/attachment")
    suspend fun getAttachments(@Path("id_task") idTask: Int): Response<List<Attachment>>

    @GET("task/willexpire")
    suspend fun findAllTaskWillExpire(): Response<List<TaskData>>

    @POST("metting")
    suspend fun postMeeting(@Body meeting: Meeting): Response<List<Meeting>>

    @GET("all/meeting/{id_user_teacher}/{id_user_student}")
    suspend fun findAllMeeting(@Path("id_user_teacher") idUserteacher: Int, @Path("id_user_student") idUserStudent: Int): Response<List<Meeting>>

    @GET("detail/topic/{id}")
    suspend fun findByDetailTopic(@Path("id") id: Int): Response<MoreInformationTopic>

    @GET("all/semester")
    suspend fun getAllSemester(): Response< List<Int>>

    @GET("project/teacher/{id_teacher}/{semester}")
   suspend fun getAllProjectByIdTeacherAndSemester(@Path("id_teacher") idTeacher: Int, @Path("semester") semester: Int): Response<List<PairingStudentWithTeacher>>

    @GET("max/semester")
    suspend fun findMaxSemester(): Response<Int>

    @POST("notification/new/task")
    suspend fun updateNotificationNewTask(@Body notification: News): Response<ResponseBody>

    @PUT("notification/update/task")
    suspend fun notificationUpdateTask(@Body taskDetail: TaskDetail): Response<ResponseBody>

    @POST("clear/notification/read")
    suspend fun clearNotificationRead(@Body newsReads: List<News>): Response<ResponseBody>

    @GET("information/dashboard")
    suspend fun getInformationDashBoard(): Response<DashBoard>

    @GET("checkpoint")
    suspend fun checkToken(): Response<Any>

    @GET("all/pairing/{semester}")
    suspend fun getAllDataBySemester(@Path("semester") semester: Int): Response<List<PairingStudentWithTeacher>>
}