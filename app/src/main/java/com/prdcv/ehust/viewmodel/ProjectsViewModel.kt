package com.prdcv.ehust.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hadt.ehust.model.StatusTopic
import com.prdcv.ehust.common.State
import com.prdcv.ehust.model.Topic
import com.prdcv.ehust.repo.TopicRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import javax.inject.Inject

data class TopicScreenState(
    val topics: List<Topic> = emptyList(),
    val updateState: ResponseBody = "".toResponseBody()
)
@HiltViewModel
class ProjectsViewModel @Inject constructor(
    private val topicRepository: TopicRepository
) : ViewModel() {


    var uiState by mutableStateOf(TopicScreenState())
        private set

    fun findTopicByIdTeacherAndIdProject(
        nameTeacher: String = "a",
        idProject: String,
        idTeacher: Int = 0
    ) {
        viewModelScope.launch {
            topicRepository.findTopicByIdTeacherAndIdProject(
                nameTeacher = nameTeacher,
                idProject = idProject,
                idTeacher = idTeacher
            ).collect {
                when(val state = it){
                    is State.Success -> {
                        uiState = uiState.copy(topics = state.data)
                    }
                    else-> {}
                }
            }
        }
    }

    fun updateTopicTable(
        idTopic: Int,
        status: StatusTopic,
        idStudent: Int = 0
    ){
        viewModelScope.launch {
            topicRepository.updateTopicTable(idTopic,status, idStudent).collect{
                when(val state = it){
                    is State.Success -> {
                        uiState = uiState.copy(updateState = state.data)
                    }
                    else-> {}
                }
            }
        }
    }
}