package com.prdcv.ehust.ui.task.detail.state

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.prdcv.ehust.model.Comment
import com.prdcv.ehust.model.TaskDetail

data class TaskDetailScreenState(
    val taskDetailState: TaskDetail = TaskDetail(),
    val onDescriptionTextChange: String = "",

    val onDateDistanceTextChange: String = "",
    val onEstimateTimeTextChange: String = "",
    val onSpendTimeTextChange: String = "",
    val onPercentDoneTextChange: String = "",
    val onAssigneeTextChange: String = "",

    val commentState: List<Comment> = emptyList(),
    val filesState: SnapshotStateList<String> = mutableStateListOf(
        "kindpng_3651626.png",
        "kindpng_3651626.png",
        "kindpng_3651626.png",
        "kindpng_3651626.png",
        "kindpng_3651626.png"
    ),

    ){
    fun addFile(name: String): SnapshotStateList<String>{
        filesState.add(name)
        return filesState
    }
}


