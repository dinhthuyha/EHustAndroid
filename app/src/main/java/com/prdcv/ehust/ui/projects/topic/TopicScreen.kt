package com.prdcv.ehust.ui.projects.topic

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
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
import java.net.IDN

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun TopicScreen(
    navController: NavController,
    viewModel: TopicsViewModel = viewModel()
) {
    val uiState = viewModel.uiState
    uiState.coroutineScope = rememberCoroutineScope()
    val updateTopics = {
        viewModel.fetchTopicList()
    }

    LaunchedEffect(key1 = Unit) {
        updateTopics()
    }

    DefaultTheme {

        ModalBottomSheetLayout(
            sheetShape = Shapes.small,
            sheetContent = {
                AddTopicModal(
                    onCancel = uiState::hideBottomSheet,
                    onSubmit = viewModel::submitTopicSuggestion
                )
            },
            sheetState = uiState.bottomSheetState
        ) {

            Scaffold(
                topBar = { ToolBar("Đề tài") },
            ) {
                SwipeRefresh(
                    state = uiState.refreshState,
                    onRefresh = updateTopics
                ) {
                    fun checkTopic(topics: List<Topic>): List<Topic> {
                        try {
                            //chi sho cac de tai chua co sinh vien nao request
                            topics.firstOrNull { it.idStudent == viewModel.mUserId && it.status == TopicStatus.ACCEPT }?.let {
                                return listOf(it)
                            }
                            topics.filter { it.status == TopicStatus.REQUEST ||( it.idStudent == viewModel.mUserId && it.status == TopicStatus.REQUESTING) }.let {
                                return it
                            }
                        } catch (e: Exception) {
                            return listOf<Topic>()
                        }
                    }

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
                                    val items = checkTopic(uiState.topics)
                                    items(items = items) {
                                        //update status, id sv
                                        TopicStudentRow(it, viewModel, navController)
                                    }
                                    if (uiState.topicSuggestionAllowed.value) {
                                        item {
                                            TopicSuggestionRow(onClick = uiState::showBottomSheet)
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
fun AddTopicModal(onSubmit: ((String, String) -> Unit)? = null, onCancel: (() -> Unit)? = null) {
    var topicName by remember { mutableStateOf("") }
    var topicDescription by remember { mutableStateOf("") }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        OutlinedTextField(
            value = topicName,
            onValueChange = { topicName = it },
            label = { Text("Tên đề tài") },
            leadingIcon = { Icon(Icons.Filled.Edit, null) },
            trailingIcon = {
                IconButton(onClick = { topicName = "" }) {
                    Icon(Icons.Filled.Clear, null)
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = topicDescription,
            onValueChange = { topicDescription = it },
            label = { Text("Mô tả đề tài") },
            leadingIcon = { Icon(Icons.Filled.Info, null) },
            trailingIcon = {
                IconButton(onClick = { topicDescription = "" }) {
                    Icon(Icons.Filled.Clear, null)
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                enabled = topicName.isNotBlank(),
                onClick = { onSubmit?.invoke(topicName, topicDescription) }) {
                Text(text = "Gửi đề xuất")
            }
            Button(onClick = { onCancel?.invoke() }) {
                Text(text = "Hủy")
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
                    //navController?.navigate(TopicsFragmentDirections.actionTopicsFragmentToNewTaskFragment(topic.id!!))
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
                //navController?.navigate(TopicsFragmentDirections.actionTopicsFragmentToNewTaskFragment(topic.id!!))
            }
    ) {
        Column(
            modifier = Modifier
                .width(IntrinsicSize.Max)
                .padding(10.dp)
        ) {
            TitleTopic(topic = topic)
            ShowNameStudent(topic = topic)
            ShowStatusTopic(topic = topic, viewModel)
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
            .clickable { onClick() }
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
    viewModel: TopicsViewModel?
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