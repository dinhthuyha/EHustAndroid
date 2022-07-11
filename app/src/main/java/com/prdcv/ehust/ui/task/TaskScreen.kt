package com.prdcv.ehust.ui.task

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.hadt.ehust.model.TopicStatus
import com.prdcv.ehust.R
import com.prdcv.ehust.model.TaskData
import com.prdcv.ehust.model.Topic
import com.prdcv.ehust.ui.compose.*
import com.prdcv.ehust.ui.profile.ToolBar
import com.prdcv.ehust.ui.projects.topic.FilterItemStudent
import com.prdcv.ehust.ui.projects.topic.TitleTopic
import com.prdcv.ehust.ui.projects.topic.TopicsFragmentDirections
import com.prdcv.ehust.ui.task.detail.TaskDetailArgs
import com.prdcv.ehust.viewmodel.TaskStatus
import com.prdcv.ehust.viewmodel.TaskViewModel
import com.prdcv.ehust.viewmodel.TopicsViewModel
import java.time.LocalDate
import java.time.Period


@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
fun TaskScreenPreview(navController: NavController, idTopic: Int) {
    val viewModel: TaskViewModel = hiltViewModel()
    val uiState = viewModel.uiState

    LaunchedEffect(key1 = Unit) {
        viewModel.findAllTaskByIdTopic(idTopic)
    }

    val coroutineScope = rememberCoroutineScope()

    DefaultTheme {
        Scaffold(
            floatingActionButton = { FloatButton {
                if (uiState.refreshState.isRefreshing) return@FloatButton
                navController.navigate(NewTaskFragmentDirections.actionNewTaskFragmentToDetailTaskFragment(
                    TaskDetailArgs(idTopic = idTopic, isNewTask = true)
                ))
            } },
            topBar = { ToolBar("Các công việc") },
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                ChipGroup(uiState.selectedTaskStatus, onClick = uiState::filterTaskByStatus)
                SwipeRefresh(
                    state = uiState.refreshState,
                    onRefresh = { viewModel.findAllTaskByIdTopic(idTopic) }
                ) {
                    LazyColumn(
                        Modifier
                            .fillMaxSize()
                            .padding(start = 5.dp, end = 5.dp)
                    ) {
                        if (uiState.refreshState.isRefreshing) {
                            items(7) {
                                TaskRow(isLoading = true)
                            }
                        } else {
                            item { TopicRow(navController =  navController) }
                            items(items = uiState.filteredTaskList, key = { it.id }) { item ->

                                TaskRow(
                                    data = item,
                                    modifier = Modifier
                                        .animateItemPlacement()
                                        .padding(it)
                                        .clickable {
                                            navController.navigate(
                                                NewTaskFragmentDirections.actionNewTaskFragmentToDetailTaskFragment(
                                                    TaskDetailArgs(idTask = item.id)
                                                )
                                            )
                                        }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TopicRow(idTopic: Int=0,
    topic: Topic = fakeTopicPreview,
    viewModel: TopicsViewModel? = null,
    navController: NavController? = null
) {
    Card(
        elevation = 4.dp,
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable {

            }
    ) {
        Column(
            modifier = Modifier
                .width(IntrinsicSize.Max)
                .padding(10.dp)
        ) {
            TitleTopic(topic = topic)
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = "Sinh viên: Đinh Thuý Hà ${idTopic}")
            Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                Button(onClick = { }) {
                    Text(text = "Chi tiết",
                        color = Color.White,
                        modifier = Modifier.clickable {
                            navController?.navigate(NewTaskFragmentDirections.actionNewTaskFragmentToInformationTopicFragment())
                        })
                }
            }
        }

    }
}

private val fakeTopicPreview =
    Topic(
        id = 123,
        nameStudent = "Dinh Thuy Ha",
        name = "lập trình web bán hàng online",
        status = TopicStatus.REQUESTING
    )
@Preview(showBackground = true)
@Composable
fun ChipGroup(
    selectedTaskStatus: MutableState<TaskStatus> = mutableStateOf(TaskStatus.FINISHED),
    onClick: (TaskStatus?) -> Unit = {}
) {
    LazyRow(
        Modifier.padding(all = 5.dp)
    ) {
        item {
            FilterItem(TaskStatus.NEW, selectedTaskStatus, onClick)
            FilterItem(TaskStatus.IN_PROGRESS, selectedTaskStatus, onClick)
            FilterItem(TaskStatus.FINISHED, selectedTaskStatus, onClick)
            FilterItem(TaskStatus.CANCELED, selectedTaskStatus, onClick)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TaskRowLoading() {
    TaskRow(isLoading = true)
}

@Preview(showBackground = true)
@Composable
fun TaskRow(
    data: TaskData = TaskData(),
    isLoading: Boolean = false,
    modifier: Modifier = Modifier
) {
    fun showTimeRemain(): String {
        if (data.status == TaskStatus.IN_PROGRESS) {
            val today = LocalDate.now()
            val dueDate = data.dueDate
            val dateRemain = Period.between(today, dueDate).days
            return "(in $dateRemain days)"
        }

        return ""
    }
    Card(
        elevation = 4.dp,
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth()
            .then(modifier)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(
                modifier = Modifier
                    .width(IntrinsicSize.Max)
                    .padding(10.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.placeholder(
                        visible = isLoading,
                        highlight = PlaceholderHighlight.shimmer()
                    )
                ) {
                    Tag(data.status.text, selectTagColor(data.status))
                    Spacer(modifier = Modifier.size(3.dp))
                    Text(text = "#${data.id}", fontWeight = FontWeight.Light, fontSize = 13.sp)
                }
                Text(
                    text = data.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .padding(bottom = 20.dp, top = 2.dp)
                        .placeholder(
                            visible = isLoading,
                            highlight = PlaceholderHighlight.shimmer()
                        )
                )
                Text(
                    text = "${data.dueDate} ${showTimeRemain()}",
                    fontWeight = FontWeight.Light,
                    fontSize = 13.sp,
                    modifier = Modifier.placeholder(
                        visible = isLoading,
                        highlight = PlaceholderHighlight.shimmer()
                    )
                )
            }
            CircularProgressWithPercent(progress = data.progress, isLoading = isLoading)
        }
    }
}

@Composable
fun Tag(status: String, backgroundColor: Color = Color.LightGray) {
    Text(
        text = status,
        fontSize = 10.sp,
        modifier = Modifier
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(3.dp)
            )
            .padding(2.dp)
    )
}

@Composable
fun CircularProgressWithPercent(
    progress: Float,
    color: Color = MaterialTheme.colors.secondaryVariant,
    isLoading: Boolean = false
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(all = 10.dp)
            .size(40.dp)
            .placeholder(visible = isLoading, highlight = PlaceholderHighlight.shimmer())
    ) {
        CircularProgressIndicator(
            progress = 1f,
            strokeWidth = 2.dp,
            color = Color.LightGray
        )
        CircularProgressIndicator(
            progress = progress,
            strokeWidth = 2.dp,
            color = color
        )
        Text(
            text = progress.percent,
            fontSize = 13.sp
        )
    }
}

@Composable
fun FloatButton(action: () -> Unit) {
    FloatingActionButton(
        onClick = action,
        backgroundColor = colorResource(id = R.color.text_color),
    ) {
        Icon(
            Icons.Filled.Add,
            contentDescription = null,
            tint = Color.White
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FilterItem(
    taskStatus: TaskStatus,
    selectedTaskStatus: MutableState<TaskStatus>,
    onClick: (TaskStatus?) -> Unit
) {
    FilterChip(
        selected = selectedTaskStatus.value == taskStatus,
        onClick = { onClick(taskStatus) },
        border = ChipDefaults.outlinedBorder,
        colors = ChipDefaults.filterChipColors(
            selectedBackgroundColor = MaterialTheme.colors.secondaryVariant,
            selectedContentColor = Color.White
        ),
        selectedIcon = {
            Icon(
                imageVector = Icons.Filled.Done,
                contentDescription = null,
                modifier = Modifier.requiredSize(ChipDefaults.SelectedIconSize)
            )
        }, modifier = Modifier.padding(end = 10.dp)
    ) {
        Text(text = taskStatus.text)
    }
}

private fun selectTagColor(taskStatus: TaskStatus): Color {
    return when (taskStatus) {
        TaskStatus.NEW -> TagNew
        TaskStatus.IN_PROGRESS -> TagInProgress
        TaskStatus.FINISHED -> TagFinished
        else -> Color.LightGray
    }
}

//private val tasks = listOf(
//    TaskData(),
//    TaskData(
//        id = 96321,
//        title = "Hiển thị file pdf trong giao diện task",
//        status = "New",
//        progress = 0f,
//        dueDate = "29-06-2022"
//    ),
//    TaskData(
//        id = 73732,
//        title = "Lọc task theo filter",
//        status = "Done",
//        progress = 1f,
//        dueDate = "12-06-2022"
//    ),
//    TaskData(id = 415155, status = "Finished"),
//    TaskData(
//        id = 93521,
//        title = "Hiển thị file pdf trong giao diện task",
//        status = "New",
//        progress = 0f,
//        dueDate = "29-06-2022"
//    ),
//    TaskData(12345),
//    TaskData(95362),
//    TaskData(94949),
//    TaskData(35326)
//)

private val Float.percent: String
    get() = "${(this * 100).toInt()}%"