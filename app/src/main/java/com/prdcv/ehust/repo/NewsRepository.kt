package com.prdcv.ehust.repo

import com.prdcv.ehust.common.State
import com.prdcv.ehust.di.NetworkBoundRepository
import com.prdcv.ehust.model.News
import com.prdcv.ehust.network.EHustClient
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

class NewsRepository @Inject constructor(val eHustClient: EHustClient) {

    fun getNews(): Flow<State<List<News>>> {
        return object : NetworkBoundRepository<List<News>>() {
            override suspend fun fetchFromRemote(): Response<List<News>> {
                return eHustClient.getAllNews()
            }
        }.asFlow()
    }
}