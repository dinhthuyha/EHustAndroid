package com.prdcv.ehust.viewmodel

import androidx.lifecycle.viewModelScope
import com.prdcv.ehust.calendar.model.CalendarState
import com.prdcv.ehust.common.State
import com.prdcv.ehust.model.Attachment
import com.prdcv.ehust.model.AttachmentInfo
import com.prdcv.ehust.model.Comment
import com.prdcv.ehust.repo.CommentRepository
import com.prdcv.ehust.repo.TaskRepository
import com.prdcv.ehust.ui.task.detail.state.TaskDetailScreenState
import com.prdcv.ehust.utils.ProgressStream
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
    fun onStatusTaskSelected(status: TaskStatus) {
        uiState.taskStatus.value = status
    }

    suspend fun postComment(content: String) {
        withContext(Dispatchers.IO) {
            val comment = Comment(content = content)
            val commentId = commentRepository.postComment(idTask, comment)
                .filterIsInstance<State.Success<Int>>().last().data

            uiState.fileToUpload.value?.let {
                uploadAttachment(commentId, it)
                uiState.fileToUpload.value = null
                uiState.progressBarVisible.value = false
                getComments()
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

    suspend fun getComments() {
        if (isNewTask) return
        commentRepository.findAllCommentByIdTask(idTask).collect {
            when (val state = it) {
                is State.Success -> {
                    uiState.taskComments.value = state.data
                }
                else -> {}
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

    fun onAttachmentSelected(inputStream: InputStream?, filename: String?, contentType: String?) {
        if (inputStream == null) return
        val filePath = UUID.randomUUID().toString()
        val attachmentInfo = AttachmentInfo(
            ProgressStream(inputStream, uiState::updateUploadProgress),
            filename ?: filePath,
            contentType
        )
        uiState.fileToUpload.value = attachmentInfo
    }

    private suspend fun uploadAttachment(commentId: Int, attachmentInfo: AttachmentInfo) {
        uiState.progressBarVisible.value = true
        val filePath = UUID.randomUUID().toString()
        // TODO: xử lý các trường hợp lỗi
        taskRepository.uploadAttachment(attachmentInfo.copy(filename = filePath)).filterIsInstance<State.Success<Any>>().last()
        taskRepository.addAttachment(commentId, Attachment(attachmentInfo.filename, filePath)).filterIsInstance<State.Success<Any>>().last()
    }
}