package com.prdcv.ehust.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prdcv.ehust.common.State
import com.prdcv.ehust.model.TaskDetail
import com.prdcv.ehust.repo.TaskRepository
import com.prdcv.ehust.ui.task.TaskData
import com.prdcv.ehust.ui.task.detail.TaskDetailScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class TaskViewModel @Inject constructor(
    private val taskRepository: TaskRepository
): ViewModel() {

    private var _taskState: MutableStateFlow<State<List<TaskData>>> = MutableStateFlow(State.Loading)
    val taskState: StateFlow<State<List<TaskData>>> get() = _taskState

    fun findAllTaskByIdTopic(idTopic: Int){
        viewModelScope.launch {
            taskRepository.findAllTaskByIdTopic(idTopic).collect{
                _taskState.emit(it)
            }
        }
    }


}