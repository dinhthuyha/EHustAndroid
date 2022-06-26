package com.prdcv.ehust.ui.task

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.prdcv.ehust.R
import com.prdcv.ehust.model.TaskData
import com.prdcv.ehust.ui.compose.*
import com.prdcv.ehust.ui.profile.ToolBar
import com.prdcv.ehust.viewmodel.TaskStatus
import com.prdcv.ehust.viewmodel.TaskViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.Period


@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Preview(showBackground = true)
@Composable
fun TaskScreen(viewModel: TaskViewModel = viewModel()) {
    val uiState = viewModel.uiState

    LaunchedEffect(key1 = Unit) {
        viewModel.findAllTaskByIdTopic(19)
    }

    val coroutineScope = rememberCoroutineScope()
    val bottomSheetSate = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true
    )

    DefaultTheme {
        ModalBottomSheetLayout(
            sheetShape = Shapes.small,
            sheetContent = {
                LazyColumn {
                    items(10) {
                        ListItem(
                            text = { Text("Item $it") },
                            icon = { Icon(Icons.Default.Favorite, null) }
                        )
                    }
                }
            },
            sheetState = bottomSheetSate
        ) {
            Scaffold(
                floatingActionButton = { FloatButton { coroutineScope.launch { bottomSheetSate.show() } } },
                topBar = { ToolBar("My tasks") },
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    ChipGroup(uiState.selectedTaskStatus, onClick = uiState::filterTaskByStatus)
                    SwipeRefresh(
                        state = uiState.refreshState,
                        onRefresh = { viewModel.findAllTaskByIdTopic(19) }
                    ) {
                        LazyColumn(
                            Modifier
                                .fillMaxSize()
                                .padding(10.dp)
                        ) {
                            items(items = uiState.filteredTaskList, key = { it.id }) { item ->
                                TaskRow(data = item, modifier = Modifier.animateItemPlacement())
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ChipGroup(selectedTaskStatus: MutableState<TaskStatus>, onClick: (TaskStatus?) -> Unit) {
    LazyRow(
        Modifier.padding(start = 10.dp, end = 10.dp, top = 10.dp)
    ) {
        item {
            FilterItem(TaskStatus.NEW, selectedTaskStatus, onClick)
            FilterItem(TaskStatus.IN_PROGRESS, selectedTaskStatus, onClick)
            FilterItem(TaskStatus.FINISHED, selectedTaskStatus, onClick)
            FilterItem(TaskStatus.CANCELED, selectedTaskStatus, onClick)
        }
    }
}

//@Preview(showBackground = true)
@Composable
fun TaskRow(data: TaskData, modifier: Modifier) {
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
            .clickable { }
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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Tag(data.status.text, selectTagColor(data.status))
                    Spacer(modifier = Modifier.size(3.dp))
                    Text(text = "#${data.id}", fontWeight = FontWeight.Light, fontSize = 12.sp)

                }
                Text(
                    text = data.title,
                    fontSize = 15.sp,
                    modifier = Modifier.padding(bottom = 20.dp, top = 2.dp)
                )
                Text(
                    text = "${data.dueDate} ${showTimeRemain()}",
                    fontWeight = FontWeight.Light,
                    fontSize = 12.sp
                )
            }
            CircularProgressWithPercent(progress = data.progress)
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
fun CircularProgressWithPercent(progress: Float) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(all = 10.dp)
            .size(40.dp)
    ) {
        CircularProgressIndicator(
            progress = 1f,
            strokeWidth = 2.dp,
            color = Color.LightGray
        )
        CircularProgressIndicator(progress = progress, strokeWidth = 2.dp)
        Text(
            text = progress.percent,
            fontSize = 12.sp
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