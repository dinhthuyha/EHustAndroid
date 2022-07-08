package com.prdcv.ehust.ui.task.detail

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TaskDetailArgs(
    val idTopic: Int = 0,
    val idTask: Int = 0,
    val isNewTask: Boolean = false
) : Parcelable