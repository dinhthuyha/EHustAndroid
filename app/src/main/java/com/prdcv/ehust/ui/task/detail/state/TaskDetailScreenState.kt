package com.prdcv.ehust.ui.task.detail.state

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

    val commentState: List<Comment> = emptyList()

)
