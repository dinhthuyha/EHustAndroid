package com.prdcv.ehust.viewmodel

import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.google.gson.annotations.SerializedName
import com.prdcv.ehust.common.State
import com.prdcv.ehust.repo.TaskRepository
import com.prdcv.ehust.model.TaskData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TaskScreenState(
    private var taskList: List<TaskData> = emptyList(),
    val filteredTaskList: SnapshotStateList<TaskData> = mutableStateListOf(),
    val refreshState: SwipeRefreshState = SwipeRefreshState(false),
    val selectedTaskStatus: MutableState<TaskStatus> = mutableStateOf(TaskStatus.ALL)
) {
    fun addTasksFromState(state: State<List<TaskData>>) {
        when (val _state = state) {
            is State.Error -> {}
            State.Loading -> {
                refreshState.isRefreshing = true
            }
            is State.Success -> {
                refreshState.isRefreshing = false
                taskList = _state.data
                filterTaskByStatus()
            }
        }
    }


    fun filterTaskByStatus(status: TaskStatus? = null) {
        selectedTaskStatus.value = status?.let {
            if (selectedTaskStatus.value == it) TaskStatus.ALL else it
        } ?: TaskStatus.ALL

        filteredTaskList.clear()

        if (selectedTaskStatus.value == TaskStatus.ALL) {
            filteredTaskList.addAll(taskList)
            return
        }

        taskList
            .filter { selectedTaskStatus.value == it.status }
            .forEach { filteredTaskList.add(it) }
    }
}

enum class TaskStatus(val text: String) {
    @SerializedName("New")
    NEW("Mới"),
    @SerializedName("In progress")
    IN_PROGRESS("Đang thực hiện"),
    @SerializedName("Finished")
    FINISHED("Hoàn thành"),
    @SerializedName("Cancelled")
    CANCELED("Huỷ"),
    ALL("all")
}

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val taskRepository: TaskRepository
) : ViewModel() {

    val uiState = TaskScreenState()

    fun findAllTaskByIdTopic(idTopic: Int) {
        uiState.refreshState.isRefreshing = true
        viewModelScope.launch(Dispatchers.IO) {
            delay(2000)
            taskRepository.findAllTaskByIdTopic(idTopic).collect { uiState.addTasksFromState(it) }
        }
    }
}