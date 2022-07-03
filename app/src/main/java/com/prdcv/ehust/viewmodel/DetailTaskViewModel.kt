package com.prdcv.ehust.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.prdcv.ehust.calendar.model.CalendarState
import com.prdcv.ehust.common.State
import com.prdcv.ehust.model.Comment
import com.prdcv.ehust.model.TaskDetail
import com.prdcv.ehust.repo.CommentRepository
import com.prdcv.ehust.repo.TaskRepository
import com.prdcv.ehust.ui.task.detail.convertToDate
import com.prdcv.ehust.ui.task.detail.state.TaskDetailScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class DetailTaskViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val commentRepository: CommentRepository
) : BaseViewModel()  {
    var idTask: Int = 0

    var uiTaskState by mutableStateOf(TaskDetailScreenState())
        private set

    val calendarState = CalendarState()
    fun onDaySelected(daySelected: LocalDate) {
        viewModelScope.launch {
            calendarState.setSelectedDay(daySelected)
        }
    }


    fun postComment(content: String){
        viewModelScope.launch {
            val comment = Comment(content = content)
            commentRepository.postComment(idTask, comment).collect{
                when (val state = it) {
                    is State.Success -> {
                        uiTaskState = uiTaskState.copy(commentState = state.data)

                    }
                    else -> {}
                }
                uiTaskState.commentState
            }
        }
    }

    fun getDetailTask() {
        viewModelScope.launch {
            taskRepository.getDetailTask(idTask).collect {
                when (val state = it) {
                    is State.Success -> {
                        uiTaskState = uiTaskState.copy(taskDetailState = state.data)

                    }
                    else -> {}
                }
            }
            commentRepository.findAllCommentByIdTask(idTask).collect{
                when(val state = it){
                    is State.Success -> {
                        uiTaskState = uiTaskState.copy(commentState = state.data)
                    }
                    else -> {}
                }
            }
        }
    }

    fun updateTask(){
        var task = TaskDetail()
        uiTaskState.apply {
            val id = taskDetailState.id
            val des = if (uiTaskState.onDescriptionTextChange == "") null else onDescriptionTextChange
            val spendTime = if (uiTaskState.onSpendTimeTextChange== "")null else onSpendTimeTextChange.toInt()
            val estimateTime = if (uiTaskState.onEstimateTimeTextChange== "")null else onEstimateTimeTextChange.toInt()
            val done = if (uiTaskState.onPercentDoneTextChange== "")null else (onPercentDoneTextChange.toFloat()/100)
            val assign = if (uiTaskState.onAssigneeTextChange == "") null else onAssigneeTextChange
            var startDate: LocalDate? = null
            var dueDate: LocalDate? = null
            val arr = if (calendarState.calendarUiState.value.selectedDatesFormatted == "") null else{
                calendarState.calendarUiState.value.selectedDatesFormatted.split(" - ")
            }
            arr?.let {
                startDate = if (it[0]=="") null else {it[0].convertToDate()}
                dueDate = if (it[1]=="") null else {it[1].convertToDate()}
            }
            task = TaskDetail(id = id,
                description = des,
                spendTime = spendTime,
                estimateTime = estimateTime,
                progress = done,
                assignee = assign,
                startDate = startDate,
                dueDate = dueDate)
        }

        viewModelScope.launch {
            taskRepository.updateTask(task).collect{
                when(val state = it){
                    is State.Success -> {
                        getDetailTask()
                    }
                    else -> {}
                }
            }
        }

    }
    fun addFile(name: String){
        uiTaskState = uiTaskState.copy(filesState = uiTaskState.addFile(name))
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

    companion object {
        private val DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    }
}