package com.prdcv.ehust.network

import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.POST

interface EHustService {
    @POST("/signin")
    suspend fun login(@Field("id") id: Int,
                      @Field("password") password: String): Response<String>
}