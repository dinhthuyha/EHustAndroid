package com.prdcv.ehust.viewmodel

import android.content.Context
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.viewModelScope
import com.hadt.ehust.model.TypeNotification
import com.prdcv.ehust.calendar.model.CalendarState
import com.prdcv.ehust.common.State
import com.prdcv.ehust.model.Attachment
import com.prdcv.ehust.model.AttachmentInfo
import com.prdcv.ehust.model.Comment
import com.prdcv.ehust.model.News
import com.prdcv.ehust.model.StatusNotification
import com.prdcv.ehust.model.TaskDetail
import com.prdcv.ehust.repo.CommentRepository
import com.prdcv.ehust.repo.TaskRepository
import com.prdcv.ehust.ui.task.detail.state.TaskDetailScreenState
import com.prdcv.ehust.utils.ProgressStream
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.time.LocalDate
import java.time.format.DateTimeFormatter
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
            }
            getComments()
        }
    }

    fun getDetailTask() {
        if (isNewTask) return
        uiState.readOnly.value = true
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
                        updateNotificationNewTask(task)
                        getDetailTask()
                    }
                    else -> {}
                }
            }
        }
    }

    private fun updateNotificationNewTask(taskDetail: TaskDetail){
        val dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val datePost = dtf.format(LocalDate.now())
        val notification = News(title = "${taskDetail.assignee} vừa tạo task ${taskDetail.title},",
        content = taskDetail.description?:"",
        datePost = datePost,
        type = TypeNotification.TYPE_PROJECT,
        status = StatusNotification.STATUS_UNREAD,
        nameUserPost = user?.fullName?:"",
        idUserPost = user?.id?:0,
        idTask = taskDetail.id)
        viewModelScope.launch {
            taskRepository.updateNotificationNewTask(notification).collect{
                Log.d("TAG", "updateNotificationNewTask: ")
            }
        }

    }
    private fun updateTaskDetails() {
        val task = uiState.newTaskDetail

        viewModelScope.launch {
            async { taskRepository.updateTask(task).collect {
                when (it) {
                    is State.Success -> {
                        uiState.readOnly.value = true
                        getDetailTask()

                    }
                    else -> {}
                }
            } }
            async { notificationUpdateTask(task) }

        }
    }

    private fun notificationUpdateTask(taskDetail: TaskDetail){
        viewModelScope.launch {
            taskRepository.notificationUpdateTask(taskDetail).collect{
                Log.d("TAG", "updateNotificationNewTask: ")
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

    fun downloadFile(url: String, context: Context, fileName: String){
        viewModelScope.launch {
            taskRepository.downloadFile(url).collect{
                Log.d("TAG", "downloadFile: ")
                if (it is State.Success){
                    try {
                        var pdfFileName: File? = null
                        var dirPath: String? = null
                        dirPath =
                            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                                .toString()
                        val dirFile = File(dirPath)
                        if (!dirFile.exists()) {
                            dirFile.mkdirs()
                        }
                        val file = "${dirPath}/${fileName}"
                        pdfFileName = File(file)
                        if (pdfFileName?.exists() == true) {
                            pdfFileName?.delete()
                        }

                        val body = it.data
                        // todo change the file location/name according to your needs

                        Log.e("retrofitBetaFile", pdfFileName.path)
                        var inputStream: InputStream? = null
                        var outputStream: OutputStream? = null

                        try {
                            val fileReader = ByteArray(4096)

                            val fileSize = body?.contentLength()
                            var fileSizeDownloaded: Long = 0

                            inputStream = body?.byteStream()
                            outputStream = FileOutputStream(pdfFileName)

                            while (true) {
                                val read = inputStream!!.read(fileReader)
                                //Nó được sử dụng để trả về một ký tự trong mẫu ASCII. Nó trả về -1 vào cuối tập tin.
                                if (read == -1) {
                                    break
                                }
                                outputStream!!.write(fileReader, 0, read)
                                fileSizeDownloaded += read.toLong()
                                Log.d("writeResponseBodyToDisk", "file download: $fileSizeDownloaded of $fileSize")
                            }

                            outputStream!!.flush()
                            Toast.makeText(context,"Lưu file thành công", Toast.LENGTH_SHORT).show()
                            Log.d("TAG", "downloadFile: done")

                        } catch (e: Exception) {
                            Log.d("TAG", "error:${e.message} ")
                        } finally {
                            if (inputStream != null) {
                                inputStream!!.close()
                            }

                            if (outputStream != null) {
                                outputStream!!.close()
                            }
                        }
                    } catch (e: Exception) {
                        Log.d("TAG", "error:${e.message} ")
                    }

                }

            }
        }
    }
}