package com.prdcv.ehust.network

import retrofit2.Response
import javax.inject.Inject

class EHustClient @Inject constructor(
    private val ehustService: EHustService) {
    suspend fun login(id: Int, password: String): Response<String> {
        return ehustService.login(id, password)
    }
}