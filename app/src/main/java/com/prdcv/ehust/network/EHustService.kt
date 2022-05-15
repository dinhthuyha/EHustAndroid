package com.prdcv.ehust.network

import com.prdcv.ehust.model.ClassStudent
import com.prdcv.ehust.model.News
import com.prdcv.ehust.model.User
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface EHustService {
    @FormUrlEncoded
    @POST("signin")
    suspend fun login(@Field("id") id: Int,
                      @Field("password") password: String): Response<String>

    @GET("news")
    suspend fun getAllNews(): Response<List<News>>

    @GET("user/profile")
    suspend fun getProfileById(@Query("id") id: Int): Response<User>

    @GET("search/user/name")
    suspend fun searchUserByFullName(@Query("full_name") id: Int, @Query("role_id") roleId: Int): Response<User>

    @GET("search/user/id")
    suspend fun searchUserById(@Query("full_name") id: Int, @Query("role_id") roleId: Int): Response<User>

    @GET("search/class")
    suspend fun searchClassById(@Query("id") id: Int, @Query("role_id") roleId: Int): Response<ClassStudent>

}