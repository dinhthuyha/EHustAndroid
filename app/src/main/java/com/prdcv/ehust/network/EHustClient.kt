package com.prdcv.ehust.network

import com.prdcv.ehust.model.News
import com.prdcv.ehust.model.User
import retrofit2.Response
import javax.inject.Inject

class EHustClient @Inject constructor(
    private val ehustService: EHustService) {
    suspend fun login(id: Int, password: String): Response<String> {
        return ehustService.login(id, password)
    }
    suspend fun getAllNews(): Response<List<News>>{
        return ehustService.getAllNews()
    }

    suspend fun getProfileById(id: Int): Response<User>{
        return ehustService.getProfileById(id)
    }
}