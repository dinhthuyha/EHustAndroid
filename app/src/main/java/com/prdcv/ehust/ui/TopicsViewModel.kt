package com.prdcv.ehust.ui

import android.util.Log
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.hadt.ehust.model.TopicStatus
import com.hadt.ehust.model.TypeSubject
import com.prdcv.ehust.common.State
import com.prdcv.ehust.data.model.MoreInformationTopic
import com.prdcv.ehust.data.model.ProgressStatus
import com.prdcv.ehust.data.model.Role
import com.prdcv.ehust.data.model.Subject
import com.prdcv.ehust.data.model.Topic
import com.prdcv.ehust.data.repo.TopicRepository
import com.prdcv.ehust.ui.projects.ProjectArg
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

data class TopicScreenState @OptIn(ExperimentalMaterialApi::class) constructor(
    var coroutineScope: CoroutineScope? = null,
    var _topics: List<Topic> = emptyList(),
    val topics: SnapshotStateList<Topic> = mutableStateListOf(),
    var moreInformationTopic: MutableState<MoreInformationTopic> = mutableStateOf(
        MoreInformationTopic()
    ),
    var readOnly: MutableState<Boolean> = mutableStateOf(true),
    val isEditing: MutableState<Boolean> = mutableStateOf(true),
    var listStatusProcess: SnapshotStateList<ProgressStatus> = mutableStateListOf(
        ProgressStatus.RESPONDING,
        ProgressStatus.DONE,
        ProgressStatus.UNFINISHED
    ),
    var statusProcess: MutableState<ProgressStatus> = mutableStateOf(ProgressStatus.RESPONDING),
    var stateProcessScore: MutableState<Float> = mutableStateOf(0f),
    var stateEndScore: MutableState<Float> = mutableStateOf(0f),
    val refreshState: SwipeRefreshState = SwipeRefreshState(false),
    val bottomSheetState: ModalBottomSheetState = ModalBottomSheetState(
        ModalBottomSheetValue.Hidden,
        isSkipHalfExpanded = true,
        confirmStateChange = { false }),
    val topicSuggestionAllowed: MutableState<Boolean> = mutableStateOf(false)
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

        topicSuggestionAllowed.value = !_topics
            .filter { it.idStudent == id }
            .any { it.status == TopicStatus.ACCEPT || it.status == TopicStatus.REQUESTING }

        when (role) {
            Role.ROLE_STUDENT -> {
                _topics.firstOrNull { it.idStudent == id && it.status == TopicStatus.ACCEPT }
                    ?.let {
                        topics.add(it)
                        return
                    }

                _topics.filter { it.status == TopicStatus.REQUEST || it.status == TopicStatus.REQUESTING }
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

    @OptIn(ExperimentalMaterialApi::class)
    fun hideBottomSheet() {
        coroutineScope?.launch {
            bottomSheetState.overflow
            bottomSheetState.hide()
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    fun showBottomSheet() {
        coroutineScope?.launch {
            bottomSheetState.show()
        }
    }

    fun findDetailInformationTopic(it: State<MoreInformationTopic>) {
        when (it) {
            is State.Error -> {
                refreshState.isRefreshing = true
            }
            is State.Success -> {

                refreshState.isRefreshing = false
                moreInformationTopic.value = it.data
                statusProcess.value = it.data.stateProcess?:ProgressStatus.UNFINISHED
                stateProcessScore.value = it.data.processScore?:0f
                stateEndScore.value = it.data.endScore?:0f
            }
            is State.Loading -> {
                refreshState.isRefreshing = true
            }
        }
    }

    fun updateStatusProcess(topic: MoreInformationTopic) {
        moreInformationTopic.value = topic
    }

}

@HiltViewModel
class TopicsViewModel @Inject constructor(
    private val topicRepository: TopicRepository
) : ViewModel() {
    val uiState = TopicScreenState()
    var currentSemester: Int? = 0
    private  val TAG = "TopicsViewModel"
    var mUserId: Int = 0
    var mProject: ProjectArg? = null
    var mRole: Role = Role.ROLE_UNKNOWN

    fun saveInformationTopic() {
        uiState.isEditing.value = !uiState.isEditing.value
        var topic: MoreInformationTopic = uiState.moreInformationTopic.value
        if (uiState.moreInformationTopic.value.type == TypeSubject.PROJECT) {
            topic = uiState.moreInformationTopic.value.copy(
                processScore = uiState.stateProcessScore.value,
                endScore = uiState.stateEndScore.value
            )
        }
        viewModelScope.launch {
            topicRepository.updateStateProcessInformationTopic(topic).collect {
                Log.d(TAG, "saveInformationTopic: ")
            }
        }
    }

    fun findDetailInformationTopic(id: Int) {
        viewModelScope.launch {
            topicRepository.findByDetailTopic(id).collect {
                uiState.readOnly.value = true
                if (it is State.Success){
                    uiState.readOnly.value = it.data.semester!= currentSemester
                    delay(600)
                }

                uiState.findDetailInformationTopic(it)
            }
        }
    }


    fun onStatusProcessSelected(topic: MoreInformationTopic) {
        uiState.updateStatusProcess(topic)
        uiState.statusProcess.value = topic.stateProcess!!

    }

    fun findTopicByIdTeacherAndIdProject(
        nameTeacher: String = "a",
        idProject: String,
        idTeacher: Int = 0,
        semester: Int = 20212
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            topicRepository.findTopicByIdTeacherAndIdProject(
                nameTeacher = nameTeacher,
                idProject = idProject,
                idTeacher = idTeacher,
                semester = semester
            ).collect {
                uiState.addTopicsFromState(it)
                uiState.filterUnassignedTopic(mUserId, mRole)
            }
        }
    }

    suspend fun findAcceptedTopic(
        nameTeacher: String = "a",
        idProject: String,
        idTeacher: Int = 0,
        currentUserId: Int?,
        semester: Int = 0
    ): Topic? {
        return withContext(Dispatchers.IO) {
            topicRepository.findTopicByIdTeacherAndIdProject(
                nameTeacher = nameTeacher,
                idProject = idProject,
                idTeacher = idTeacher,
                semester = semester
            )
                .filterIsInstance<State.Success<List<Topic>>>()
                .last().data.firstOrNull { it.status == TopicStatus.ACCEPT && it.idStudent == currentUserId }
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
                        idProject = it.idProject!!,
                        semester = it.semester ?: 20212
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
                        fetchTopicList()
                    }
                    else -> {}
                }
            }
        }
    }

    fun submitTopicSuggestion(name: String, description: String) {
        val sTopic = Topic(
            name = name,
            idStudent = mUserId,
            nameTeacher = mProject?.nameTeacher,
            subject = Subject(mProject?.idProject, "", TypeSubject.PROJECT)
        )
        viewModelScope.launch(Dispatchers.IO) {
            topicRepository.submitTopicSuggestion(sTopic).collect {
                when (val _state = it) {
                    is State.Error -> {}
                    State.Loading -> {}
                    is State.Success -> {
                        uiState.hideBottomSheet()
                        fetchTopicList()
                    }
                }
            }
        }
    }
}