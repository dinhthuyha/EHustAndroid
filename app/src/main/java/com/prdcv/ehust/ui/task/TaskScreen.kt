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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.prdcv.ehust.R
import com.prdcv.ehust.ui.compose.*
import com.prdcv.ehust.ui.profile.ToolBar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Preview(showBackground = true)
@Composable
fun TaskScreenPreview() {
    val state = remember {
        mutableStateOf(tasks)
    }

    fun filterByStatus(status: String?) {
        state.value = status?.let { s ->
            tasks.filter { it.status == s }
        } ?: tasks
    }

    val refreshingState = rememberSwipeRefreshState(isRefreshing = false)
    val coroutineScope = rememberCoroutineScope()
    val bottomSheetSate = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true
    )

    fun refreshTaskList() {
        refreshingState.isRefreshing = true
        coroutineScope.launch {
            delay(3000)
            refreshingState.isRefreshing = false
        }
    }


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
                bottomBar = { BottomBar() },
                isFloatingActionButtonDocked = true,
                floatingActionButtonPosition = FabPosition.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    ChipGroup(action = ::filterByStatus)
                    SwipeRefresh(
                        state = refreshingState,
                        onRefresh = ::refreshTaskList
                    ) {
                        LazyColumn(
                            Modifier
                                .fillMaxWidth()
                                .fillMaxHeight()
                                .padding(10.dp)
                        ) {
                            items(state.value, key = { it.id }) { t ->
                                Task(data = t, modifier = Modifier.animateItemPlacement())
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ChipGroup(action: (String?) -> Unit) {
    LazyRow(
        Modifier.padding(start = 10.dp, end = 10.dp, top = 10.dp)
    ) {
        item {
            FilterItem("New") { action(it) }
            FilterItem("In progress") { action(it) }
            FilterItem("Finished") { action(it) }
            FilterItem("Done") { action(it) }
            FilterItem("Canceled") { action(it) }
        }
    }
}

//@Preview(showBackground = true)
@Composable
fun Task(data: TaskData, modifier: Modifier) {
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
                    Tag(data.status)
                    Spacer(modifier = Modifier.size(3.dp))
                    Text(text = "#${data.id}", fontWeight = FontWeight.Light, fontSize = 12.sp)

                }
                Text(
                    text = data.title,
                    fontSize = 15.sp,
                    modifier = Modifier.padding(bottom = 20.dp, top = 2.dp)
                )
                Text(
                    text = "${data.dueDate} (in 7 days)",
                    fontWeight = FontWeight.Light,
                    fontSize = 12.sp
                )
            }
            CircularProgressWithPercent(progress = data.progress)
        }
    }
}

@Composable
fun Tag(status: String) {
    Text(
        text = status,
        fontSize = 10.sp,
        modifier = Modifier
            .background(
                color = when (status) {
                    "New" -> TagNew
                    "In progress" -> TagInProgress
                    "Done" -> TagDone
                    "Finished" -> TagFinished
                    else -> Color.Gray
                },
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

@Composable
fun BottomBar() {
    BottomAppBar(
        cutoutShape = RoundedCornerShape(50),
        backgroundColor = colorResource(id = R.color.text_color)
    ) {

    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FilterItem(text: String, onClick: (String?) -> Unit) {
    val state = remember { mutableStateOf(false) }

    FilterChip(
        selected = state.value,
        onClick = {
            state.value = !state.value
            if (state.value) onClick(text) else onClick(null)
        },
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
        Text(text = text)
    }
}

data class TaskData(
    val id: Int = 23851,
    val title: String = "Tạo màn hình quản lý task",
    val status: String = "In progress",
    val progress: Float = 0.8f,
    val dueDate: String = "22-02-2022"
)

private val tasks = listOf(
    TaskData(),
    TaskData(
        id = 96321,
        title = "Hiển thị file pdf trong giao diện task",
        status = "New",
        progress = 0f,
        dueDate = "29-06-2022"
    ),
    TaskData(
        id = 73732,
        title = "Lọc task theo filter",
        status = "Done",
        progress = 1f,
        dueDate = "12-06-2022"
    ),
    TaskData(id = 415155, status = "Finished"),
    TaskData(
        id = 93521,
        title = "Hiển thị file pdf trong giao diện task",
        status = "New",
        progress = 0f,
        dueDate = "29-06-2022"
    ),
    TaskData(12345),
    TaskData(95362),
    TaskData(94949),
    TaskData(35326)
)

private val Float.percent: String
    get() = "${(this * 100).toInt()}%"