package com.prdcv.ehust.repo

import com.prdcv.ehust.common.State
import com.prdcv.ehust.di.NetworkBoundRepository
import com.prdcv.ehust.model.ClassStudent
import com.prdcv.ehust.model.Role
import com.prdcv.ehust.model.ScheduleEvent
import com.prdcv.ehust.model.User
import com.prdcv.ehust.network.EHustClient
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

class UserRepository @Inject constructor(val eHustClient: EHustClient) {
    fun login(id:Int, password: String):Flow<State<Map<String,Any>>>{
        return object : NetworkBoundRepository<Map<String,Any>>() {
            override suspend fun fetchFromRemote(): Response<Map<String,Any>> {
                return eHustClient.login(id, password)
            }
        }.asFlow()
    }

    fun getProfileById(id: Int): Flow<State<User>>{
        return object : NetworkBoundRepository<User>(){
            override suspend fun fetchFromRemote(): Response<User> {
                return eHustClient.getProfileById(id)
            }
        }.asFlow()
    }

    fun getListStudentInClass( grade: String): Flow<State<List<User>>>{
        return object: NetworkBoundRepository<List<User>>(){
            override suspend fun fetchFromRemote(): Response<List<User>> {
                return eHustClient.getListStudentInClass(grade)
            }
        }.asFlow()
    }

    fun findAllProjectsByStudentId( id: Int ): Flow<State<List<ClassStudent>>>{
        return object: NetworkBoundRepository<List<ClassStudent>>(){
            override suspend fun fetchFromRemote(): Response<List<ClassStudent>> {
                return eHustClient.findAllProjectsByStudentId(id)
            }
        }.asFlow()
    }

    fun searchClassById(id: Int): Flow<State<ClassStudent>>{
        return object : NetworkBoundRepository<ClassStudent>(){
            override suspend fun fetchFromRemote(): Response<ClassStudent> {
                return eHustClient.searchClassById(id)
            }
        }.asFlow()
    }
     fun searchUserById(id: Int, role: Role): Flow<State<User>>{
        return object : NetworkBoundRepository<User>(){
            override suspend fun fetchFromRemote(): Response<User> {
                return eHustClient.searchUserById(id, role)
            }
        }.asFlow()
    }

    fun findAllSchedules( id: Int ): Flow<State<List<ScheduleEvent>>>{
        return object: NetworkBoundRepository<List<ScheduleEvent>>(){
            override suspend fun fetchFromRemote(): Response<List<ScheduleEvent>> {
                return eHustClient.findAllSchedules(id)
            }
        }.asFlow()
    }

}