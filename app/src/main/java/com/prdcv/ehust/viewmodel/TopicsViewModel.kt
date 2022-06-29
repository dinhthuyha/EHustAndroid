package com.prdcv.ehust.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.hadt.ehust.model.TopicStatus
import com.prdcv.ehust.common.State
import com.prdcv.ehust.model.Role
import com.prdcv.ehust.model.Topic
import com.prdcv.ehust.repo.TopicRepository
import com.prdcv.ehust.ui.projects.ProjectArg
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import javax.inject.Inject

data class TopicScreenState(
    var _topics: List<Topic> = emptyList(),
    val topics: SnapshotStateList<Topic> = mutableStateListOf(),
    val refreshState: SwipeRefreshState = SwipeRefreshState(false),
    var updateState: ResponseBody = "".toResponseBody(),
) {
    fun addTopicsFromState(state: State<List<Topic>>) {
        when (val _state = state) {
            is State.Error -> refreshState.isRefreshing = false
            State.Loading -> refreshState.isRefreshing = true
            is State.Success -> {
                _topics = _state.data
                refreshState.isRefreshing = false
            }
        }
    }

    fun filterUnassignedTopic(id: Int, role: Role) {
        topics.clear()

        when (role) {
            Role.ROLE_STUDENT -> {
                _topics.firstOrNull { it.idStudent == id && it.status == TopicStatus.ACCEPT }
                    ?.let {
                        topics.add(it)
                        return
                    }

                _topics.filter { it.status == TopicStatus.REQUEST }
                    .let {
                        topics.addAll(it)
                        return
                    }
            }
            else -> {
                topics.addAll(_topics)
            }
        }
    }

}

@HiltViewModel
class TopicsViewModel @Inject constructor(
    private val topicRepository: TopicRepository
) : ViewModel() {
    val uiState = TopicScreenState()

    var mUserId: Int = 0
    var mProject: ProjectArg? = null
    var mRole: Role = Role.ROLE_UNKNOWN

    private fun findTopicByIdTeacherAndIdProject(
        nameTeacher: String = "a",
        idProject: String,
        idTeacher: Int = 0
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            topicRepository.findTopicByIdTeacherAndIdProject(
                nameTeacher = nameTeacher,
                idProject = idProject,
                idTeacher = idTeacher
            ).collect {
                uiState.addTopicsFromState(it)
                uiState.filterUnassignedTopic(mUserId, mRole)
            }
        }
    }

    fun fetchTopicList() {
        mProject?.let {
            when (mRole) {
                Role.ROLE_STUDENT -> {
                    findTopicByIdTeacherAndIdProject(
                        nameTeacher = it.nameTeacher!!,
                        idProject = it.idProject!!
                    )
                }
                Role.ROLE_TEACHER -> {
                    findTopicByIdTeacherAndIdProject(
                        idTeacher = it.idTeacher!!,
                        idProject = it.idProject!!
                    )
                }
                else -> {}
            }
        }
    }

    fun updateTopicStatus(
        idTopic: Int,
        status: TopicStatus
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val idStudent = if (mRole == Role.ROLE_STUDENT) mUserId else 0
            topicRepository.updateTopicTable(idTopic, status, idStudent).collect {
                when (val state = it) {
                    is State.Success -> {
                        uiState.updateState = state.data
                        fetchTopicList()
                    }
                    else -> {}
                }
            }
        }
    }
}