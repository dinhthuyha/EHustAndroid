package com.prdcv.ehust.repo

import com.prdcv.ehust.model.StatusNotification
import com.hadt.ehust.model.TypeNotification
import com.prdcv.ehust.common.State
import com.prdcv.ehust.di.NetworkBoundRepository
import com.prdcv.ehust.model.News
import com.prdcv.ehust.network.EHustClient
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

class NewsRepository @Inject constructor(val eHustClient: EHustClient) {

    fun getNews(type:TypeNotification): Flow<State<List<News>>> {
        return object : NetworkBoundRepository<List<News>>() {
            override suspend fun fetchFromRemote(): Response<List<News>> {
                return eHustClient.getAllNews(type)
            }
        }.asFlow()
    }
     fun updateStatusNew(
        id: Int,
        type: TypeNotification,
        status: StatusNotification
    ): Flow<State<List<News>>> {
        return object : NetworkBoundRepository<List<News>>() {
            override suspend fun fetchFromRemote(): Response<List<News>> {
                return eHustClient.updateStatusNew(id, type, status)
            }
        }.asFlow()
    }

}