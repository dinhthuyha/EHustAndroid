package com.prdcv.ehust.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prdcv.ehust.common.SingleLiveEvent
import com.prdcv.ehust.common.State
import com.prdcv.ehust.model.Topic
import com.prdcv.ehust.repo.TopicRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProjectsViewModel @Inject constructor(
    private val topicRepository: TopicRepository
): ViewModel() {

    private var _topicState = SingleLiveEvent<State<List<Topic>>>()
    val topicState get() = _topicState
    fun findTopicByIdTeacherAndIdProject(idTeacher: Int, idProject: String){
        viewModelScope.launch {
            topicRepository.findTopicByIdTeacherAndIdProject(idTeacher, idProject).collect {
                _topicState.postValue(it)
            }
        }
    }
}