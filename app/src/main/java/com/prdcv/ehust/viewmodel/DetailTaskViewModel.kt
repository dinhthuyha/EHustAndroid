package com.prdcv.ehust.viewmodel

import androidx.lifecycle.viewModelScope
import com.prdcv.ehust.calendar.model.CalendarState
import com.prdcv.ehust.common.State
import com.prdcv.ehust.model.Attachment
import com.prdcv.ehust.model.AttachmentInfo
import com.prdcv.ehust.model.Comment
import com.prdcv.ehust.model.User
import com.prdcv.ehust.repo.CommentRepository
import com.prdcv.ehust.repo.TaskRepository
import com.prdcv.ehust.ui.task.detail.state.TaskDetailScreenState
import com.prdcv.ehust.utils.ProgressStream
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.InputStream
import java.time.LocalDate
import java.util.*
import javax.inject.Inject

@HiltViewModel
class DetailTaskViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val commentRepository: CommentRepository
) : BaseViewModel() {
    var idTopic: Int = 0
    var idTask: Int = 0
    var isNewTask = false
        set(value) {
            uiState.readOnly.value = !value
            field = value
        }
    var uiState = TaskDetailScreenState()

    val calendarState = CalendarState()
    fun onDaySelected(daySelected: LocalDate) {
        viewModelScope.launch {
            calendarState.setSelectedDay(daySelected)
        }
    }
    fun onStatusTaskSelected(status: String) {
        uiState.selectedStatusTask.value = status
    }

    fun postComment(content: String) {
        viewModelScope.launch {
            val comment = Comment(content = content)
            commentRepository.postComment(idTask, comment).collect {
                when (val state = it) {
                    is State.Success -> {
                        uiState.taskComments.value = state.data
                    }
                    else -> {}
                }
            }
        }
    }

    fun getDetailTask() {
        if (isNewTask) return
        viewModelScope.launch(Dispatchers.IO) {
            taskRepository.getDetailTask(idTask).collect {
                when (val state = it) {
                    is State.Loading -> uiState.isLoading.value = true
                    is State.Success -> {
                        uiState.updateStates(state.data)
                        uiState.isLoading.value = false
                    }
                    else -> uiState.isLoading.value = false
                }
            }
        }
    }

    fun getComments() {
        if (isNewTask) return
        viewModelScope.launch(Dispatchers.IO) {
            commentRepository.findAllCommentByIdTask(idTask).collect {
                when (val state = it) {
                    is State.Success -> {
                        uiState.taskComments.value = state.data
                    }
                    else -> {}
                }
            }
        }
    }

    fun getAttachments() {
        if (isNewTask) return
        viewModelScope.launch(Dispatchers.IO) {
            taskRepository.getAttachments(idTask).collect {
                when (val state = it) {
                    is State.Error -> {}
                    State.Loading -> {}
                    is State.Success -> uiState.taskAttachments.value = state.data
                }
            }
        }
    }

    fun saveTask() {
        if (isNewTask) {
            postNewTask()
        } else {
            updateTaskDetails()
        }
    }

    private fun postNewTask() {
        val task = uiState.newTaskDetail.copy(id = 0)
        viewModelScope.launch(Dispatchers.IO) {
            taskRepository.newTask(idTopic, task).collect {
                when (it) {
                    is State.Success -> {
                        idTask = it.data
                        uiState.readOnly.value = true
                        getDetailTask()
                    }
                    else -> {}
                }
            }
        }
    }

    private fun updateTaskDetails() {
        val task = uiState.newTaskDetail

        viewModelScope.launch {
            taskRepository.updateTask(task).collect {
                when (it) {
                    is State.Success -> {
                        uiState.readOnly.value = true
                        getDetailTask()
                    }
                    else -> {}
                }
            }
        }
    }

    private fun addAttachment(attachment: Attachment) {
        viewModelScope.launch(Dispatchers.IO) {
            taskRepository.addAttachment(idTask, attachment).collect {
                when (val state = it) {
                    is State.Error -> {}
                    State.Loading -> {}
                    is State.Success -> uiState.taskAttachments.value = state.data
                }
            }
        }
    }

    fun onAttachmentSelected(inputStream: InputStream?, filename: String?, contentType: String?) {
        if (inputStream == null) return
//        filename?.let(uiState.taskAttachments::add)
        val filePath = UUID.randomUUID().toString()
        val attachmentInfo = AttachmentInfo(
            ProgressStream(inputStream, uiState::updateUploadProgress),
            filePath,
            contentType
        )
        uiState.progressBarVisible.value = true

        viewModelScope.launch(Dispatchers.IO) {
            taskRepository.uploadAttachment(attachmentInfo).collect {
                // TOOD: update status
                when (it) {
                    is State.Error -> {}
                    State.Loading -> {}
                    is State.Success -> {
                        uiState.progressBarVisible.value = false
                        addAttachment(Attachment(filename, filePath))
                    }
                }
            }
        }
    }
}