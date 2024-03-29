package com.prdcv.ehust.network

import com.hadt.ehust.model.StatusTopic
import com.prdcv.ehust.model.ClassStudent
import com.prdcv.ehust.model.News
import com.prdcv.ehust.model.Role
import com.prdcv.ehust.model.ScheduleEvent
import com.prdcv.ehust.model.Subject
import com.prdcv.ehust.model.Topic
import com.prdcv.ehust.model.User
import okhttp3.ResponseBody
import retrofit2.Response
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

    @GET("news")
    suspend fun getAllNews(): Response<List<News>>

    @GET("user/profile/{id}")
    suspend fun getProfileById(@Path("id") id: Int): Response<User>

    @GET("search/user/name/{fullName}/{roleId}")
    suspend fun searchUserByFullName(@Path("fullName") fullName: String, @Path("roleId") roleId: Int): Response<User>

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

    @GET("topic/teacher/{id_teacher}/{name_teacher}/{id_project}")
    suspend fun findTopicByIdTeacherAndIdProject(
        @Path("name_teacher") nameTeacher: String,
        @Path("id_project") idProject: String,
        @Path("id_teacher") idTeacher: Int
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
        @Field(value = "status") status: StatusTopic,
        @Field(value = "id_student") idStudent: Int
    ): Response<ResponseBody>
}