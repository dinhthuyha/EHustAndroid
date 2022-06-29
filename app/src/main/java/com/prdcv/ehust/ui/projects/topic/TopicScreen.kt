package com.prdcv.ehust.ui.projects.topic

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.hadt.ehust.model.TopicStatus
import com.prdcv.ehust.model.Role
import com.prdcv.ehust.model.Topic
import com.prdcv.ehust.ui.compose.DefaultTheme
import com.prdcv.ehust.ui.compose.Shapes
import com.prdcv.ehust.ui.compose.dashedBorder
import com.prdcv.ehust.ui.profile.ToolBar
import com.prdcv.ehust.viewmodel.TopicsViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun TopicScreen(
    navController: NavController,
    viewModel: TopicsViewModel = viewModel()
) {
    val uiState = viewModel.uiState
    val updateTopics = {
        viewModel.fetchTopicList()
    }

    LaunchedEffect(key1 = Unit) {
        updateTopics()
    }

    val bottomSheetSate = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true
    )
    val coroutineScope = rememberCoroutineScope()

    DefaultTheme {

        ModalBottomSheetLayout(
            sheetShape = Shapes.small,
            sheetContent = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize()
                ) {
                    OutlinedTextField(
                        value = "",
                        onValueChange = {},
                        label = { Text("ten de tai") })
                    OutlinedTextField(value = "", onValueChange = {}, label = { Text("noi dung") })
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(onClick = { /*TODO*/ }) {
                            Text(text = "Submit")
                        }
                        Button(onClick = { /*TODO*/ }) {
                            Text(text = "Cancel")
                        }
                    }
                }
            },
            sheetState = bottomSheetSate
        ) {

            Scaffold(
                topBar = { ToolBar("Đề tài") },
            ) {
                SwipeRefresh(
                    state = uiState.refreshState,
                    onRefresh = updateTopics
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(10.dp)
                        ) {
                            when (viewModel.mRole) {
                                Role.ROLE_TEACHER -> {
                                    items(items = uiState.topics) {
                                        TopicTeacherRow(it, navController, viewModel)
                                    }
                                }
                                Role.ROLE_STUDENT -> {
                                    items(items = uiState.topics) {
                                        //update status, id sv
                                        TopicStudentRow(it, viewModel, navController)
                                    }
                                    item {
                                        TopicSuggestionRow {
                                            coroutineScope.launch { bottomSheetSate.show() }
                                        }
                                    }
                                }
                                else -> {}
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
fun TopicStudentRow(
    topic: Topic = fakeTopicPreview,
    viewModel: TopicsViewModel? = null,
    navController: NavController? = null
) {
    Card(
        elevation = 2.dp,
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable {
                if (topic.status == TopicStatus.ACCEPT) {
                    navController?.navigate(TopicsFragmentDirections.actionTopicsFragmentToNewTaskFragment())
                }
            }
    ) {
        Column(
            modifier = Modifier
                .width(IntrinsicSize.Max)
                .padding(10.dp)
        ) {
            TitleTopic(topic = topic)
            Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                FilterItemStudent(
                    status = topic.status
                ) {
                    if (topic.status == TopicStatus.REQUEST)
                        viewModel?.updateTopicStatus(topic.id!!, TopicStatus.REQUESTING)
                }
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun TopicTeacherRow(
    topic: Topic = fakeTopicPreview,
    navController: NavController? = null,
    viewModel: TopicsViewModel? = null
) {
    Card(
        elevation = 2.dp,
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable {
                navController?.navigate(TopicsFragmentDirections.actionTopicsFragmentToNewTaskFragment())
            }
    ) {
        Column(
            modifier = Modifier
                .width(IntrinsicSize.Max)
                .padding(10.dp)
        ) {
            TitleTopic(topic = topic)
            ShowNameStudent(topic = topic)
            ShowStatusTopic(topic = topic, viewModel) {}
        }

    }
}

@Preview(showBackground = true)
@Composable
fun TopicSuggestionRow(onClick: () -> Unit = {}) {
    Card(
        elevation = 0.dp,
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .dashedBorder(2.dp, Color.LightGray, MaterialTheme.shapes.medium, 8.dp, 5.dp)
            .clickable {}
    ) {
        Column(
            modifier = Modifier
                .width(IntrinsicSize.Max)
                .padding(10.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Đề xuất đề tài", color = Color.Gray, fontSize = 17.sp)
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.padding(5.dp)
                )
            }
        }
    }
}

@Composable
fun TitleTopic(topic: Topic) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier) {
        Text(
            text = "Đề tài: ",
            fontSize = 15.sp,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.size(5.dp))
        Text(
            text = "${topic.name} ",
            fontSize = 17.sp,
            modifier = Modifier
                .padding(2.dp)
        )
        Spacer(modifier = Modifier.size(3.dp))
    }
}

@Composable
fun ShowStatusTopic(
    topic: Topic,
    viewModel: TopicsViewModel?,
    refreshData: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.End,
        modifier = Modifier.fillMaxWidth()
    ) {

        when (topic.status) {
            TopicStatus.REQUEST, TopicStatus.ACCEPT -> {}
            TopicStatus.REQUESTING -> {
                FilterItemTeacher(text = "Chấp nhận") {
                    //update lại status
                    viewModel?.updateTopicStatus(idTopic = topic.id!!, status = TopicStatus.ACCEPT)
                }
                Spacer(modifier = Modifier.width(8.dp))
                FilterItemTeacher(text = "Xoá") {
                    //xoa yeu cau cua sinh vien
                    viewModel?.updateTopicStatus(idTopic = topic.id!!, status = TopicStatus.REQUEST)
                }

            }
            else -> {}
        }

    }
}

@Composable
fun ShowNameStudent(topic: Topic) {
    Row(horizontalArrangement = Arrangement.Start, modifier = Modifier.fillMaxWidth()) {
        when (topic.status) {
            TopicStatus.REQUEST -> {}
            TopicStatus.REQUESTING -> {
                Spacer(modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 0.dp))
                Text(
                    text = "Sinh viên: ${topic.nameStudent ?: ""} yêu cầu được làm đề tài.",
                    color = Color.Gray
                )

            }
            TopicStatus.ACCEPT -> {
                Text(
                    text = "Sinh viên: ${topic.nameStudent ?: ""}",
                    color = Color.Gray
                )
            }
            else -> {}
        }
    }

}

@Composable
fun FilterItemStudent(status: TopicStatus?, callback: () -> Unit) {
    Button(
        onClick = callback,
        enabled = status != TopicStatus.ACCEPT && status != TopicStatus.REQUESTING
    ) {
        Text(
            text = status?.name ?: "UNKNOWN",
            fontSize = 11.sp
        )
    }
}

@Composable
fun FilterItemTeacher(text: String, callback: () -> Unit) {
    val content = remember { mutableStateOf(text) }
    Button(onClick = {
        callback.invoke()
    })
    {
        Text(
            text = content.value,
            fontSize = 11.sp
        )
    }
}

private val fakeTopicPreview =
    Topic(
        id = 123,
        nameStudent = "Dinh Thuy Ha",
        name = "lập trình web bán hàng online",
        status = TopicStatus.REQUESTING
    )