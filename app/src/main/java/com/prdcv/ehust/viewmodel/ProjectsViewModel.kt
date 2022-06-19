package com.prdcv.ehust.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prdcv.ehust.common.SingleLiveEvent
import com.prdcv.ehust.common.State
import com.prdcv.ehust.model.Role
import com.prdcv.ehust.model.Topic
import com.prdcv.ehust.repo.TopicRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProjectsViewModel @Inject constructor(
    private val topicRepository: TopicRepository
): ViewModel() {

    private var _topicState: MutableStateFlow<State<List<Topic>>> = MutableStateFlow(State.Loading)
    val topicState: StateFlow<State<List<Topic>>> get() = _topicState
    fun findTopicByIdTeacherAndIdProject(nameTeacher: String = "a", idProject: String, idTeacher: Int = 0){
        viewModelScope.launch {
            topicRepository.findTopicByIdTeacherAndIdProject(
                nameTeacher = nameTeacher,
                idProject =idProject,
                idTeacher = idTeacher).collect {
                _topicState.emit(it)

            }
        }
    }
}