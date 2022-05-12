package com.prdcv.ehust.network

import com.prdcv.ehust.model.News
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface EHustService {
    @FormUrlEncoded
    @POST("signin")
    suspend fun login(@Field("id") id: Int,
                      @Field("password") password: String): Response<String>

    @GET("news")
    suspend fun getAllNews(): Response<List<News>>
}