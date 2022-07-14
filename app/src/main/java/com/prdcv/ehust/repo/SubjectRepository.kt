package com.prdcv.ehust.repo

import com.prdcv.ehust.common.State
import com.prdcv.ehust.di.NetworkBoundRepository
import com.prdcv.ehust.model.ProjectTeacher
import com.prdcv.ehust.model.Subject
import com.prdcv.ehust.network.EHustClient

import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

class SubjectRepository @Inject constructor(
    val eHustClient: EHustClient
    ) {

    fun getAllProjectCurrentSemester(): Flow<State<List<Subject>>>{
        return object: NetworkBoundRepository<List<Subject>>(){
            override suspend fun fetchFromRemote(): Response<List<Subject>> {
                return eHustClient.getAllProjectCurrentSemester()
            }
        }.asFlow()
   }

    fun getAllSemester(): Flow<State<List<Int>>>{
        return object : NetworkBoundRepository<List<Int>>(){
            override suspend fun fetchFromRemote(): Response<List<Int>> {
                return eHustClient.getAllSemester()
            }
        }.asFlow()
    }

    fun getAllProjectByIdTeacherAndSemester(idTeacher: Int, semester: Int): Flow<State< List<ProjectTeacher>>>{
        return object : NetworkBoundRepository< List<ProjectTeacher>>(){
            override suspend fun fetchFromRemote(): Response<List<ProjectTeacher>> {
                return eHustClient.getAllProjectByIdTeacherAndSemester(idTeacher, semester)
            }
        }.asFlow()
    }
}