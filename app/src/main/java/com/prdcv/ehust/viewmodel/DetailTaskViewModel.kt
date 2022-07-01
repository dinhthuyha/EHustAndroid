package com.prdcv.ehust.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prdcv.ehust.calendar.model.CalendarState
import com.prdcv.ehust.common.State
import com.prdcv.ehust.repo.TaskRepository
import com.prdcv.ehust.ui.task.detail.state.TaskDetailScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class DetailTaskViewModel @Inject constructor(
    private val taskRepository: TaskRepository
) : ViewModel() {
    var uiTaskState by mutableStateOf(TaskDetailScreenState())
        private set

    val calendarState = CalendarState()
    fun onDaySelected(daySelected: LocalDate) {
        viewModelScope.launch {
            calendarState.setSelectedDay(daySelected)
        }
    }

    fun getDetailTask(idTask: Int) {
        viewModelScope.launch {
            taskRepository.getDetailTask(idTask).collect {
                when (val state = it) {
                    is State.Success -> {
                        uiTaskState = uiTaskState.copy(taskDetailState = state.data)

                    }
                    else -> {}
                }
            }
        }
    }

    fun onChangeDescription(mDes: String) {
        uiTaskState = uiTaskState.copy(onDescriptionTextChange = mDes)
    }

    fun onDateDistanceTextChange(value: String) {
        uiTaskState = uiTaskState.copy(onDateDistanceTextChange = value)
    }

    fun onEstimateTimeTextChange(value: String) {
        uiTaskState = uiTaskState.copy(onEstimateTimeTextChange = value)
    }

    fun onSpendTimeTextChange(value: String) {
        uiTaskState = uiTaskState.copy(onSpendTimeTextChange = value)
    }

    fun onPercentDoneTextChange(value: String) {
        uiTaskState = uiTaskState.copy(onPercentDoneTextChange = value)
    }


    fun onAssigneeTextChange(value: String) {
        uiTaskState = uiTaskState.copy(onAssigneeTextChange = value)

    }
}