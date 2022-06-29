package com.prdcv.ehust.ui.task.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prdcv.ehust.calendar.model.CalendarState
import com.prdcv.ehust.common.State
import com.prdcv.ehust.model.TaskDetail
import com.prdcv.ehust.repo.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class TaskDetailScreenState(
    val taskDetailState: TaskDetail = TaskDetail()
)

@HiltViewModel
class DetailTaskViewModel @Inject constructor(
    private val taskRepository: TaskRepository
): ViewModel() {
    var uiTaskState by mutableStateOf(TaskDetailScreenState())
        private set

    val calendarState = CalendarState()
    fun onDaySelected(daySelected: LocalDate) {
        viewModelScope.launch {
            calendarState.setSelectedDay(daySelected)
        }
    }

    fun getDetailTask(idTask: Int){
        viewModelScope.launch {
            taskRepository.getDetailTask(idTask).collect{
                when(val state = it){
                    is State.Success -> {
                        uiTaskState = uiTaskState.copy(taskDetailState = state.data)
                    }
                    else-> {}
                }
            }
        }
    }
}