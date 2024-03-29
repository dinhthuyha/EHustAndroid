package com.prdcv.ehust.ui.projects.topic

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.hadt.ehust.model.StatusTopic
import com.prdcv.ehust.common.State
import com.prdcv.ehust.model.Role
import com.prdcv.ehust.model.Topic
import com.prdcv.ehust.ui.compose.DefaultTheme
import com.prdcv.ehust.ui.profile.ToolBar
import com.prdcv.ehust.ui.task.FilterItem
import com.prdcv.ehust.viewmodel.ProjectsViewModel

//@Preview(showBackground = true)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun DefaultPreview(
    viewModel: ProjectsViewModel = viewModel(),
    id: Int,
    role: Role,
    navController: NavController
) {
    val state = viewModel.topicState.collectAsState()
    DefaultTheme {
        Scaffold(topBar = { ToolBar("Đề tài") }) {

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                LazyColumn(
                    Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                ) {
                    when (val topics = state.value) {
                        is State.Loading -> {}
                        is State.Error -> {}
                        is State.Success -> {
                            items(items = topics.data) { t ->
                                when (role) {
                                    Role.ROLE_TEACHER -> {
                                        TopicTeacherRow(t, navController, viewModel)
                                    }
                                    Role.ROLE_STUDENT -> {
                                        if (t.id != id && t.status == StatusTopic.ACCEPT) {

                                        } else {
                                            //update status, id sv
                                            TopicStudentRow(t, viewModel, id)
                                        }

                                    }
                                    else -> {}
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


@Composable
fun TopicStudentRow(
    topic: Topic = Topic(123, "lập trình web bán hàng online"),
    viewModel: ProjectsViewModel,
    idUser: Int
) {
    Card(
        elevation = 2.dp,
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { }
    ) {
        Column(
            modifier = Modifier
                .width(IntrinsicSize.Max)
                .padding(10.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
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
            Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                FilterItemStudent(
                    text = "${topic.status?.name}",
                    callback = {
                        if (topic.status == StatusTopic.REQUEST)
                            viewModel.updateTopicTable(topic.id!!, StatusTopic.REQUESTING, idUser)
                    })
            }
        }

    }
}

@Composable
fun TopicTeacherRow(
    topic: Topic = Topic(123, "lập trình web bán hàng online"),
    navController: NavController,
    viewModel: ProjectsViewModel
) {
    Card(
        elevation = 2.dp,
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable {
                navController.navigate(TopicsFragmentDirections.actionTopicsFragmentToNewTaskFragment())
            }
    ) {
        Column(
            modifier = Modifier
                .width(IntrinsicSize.Max)
                .padding(10.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
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
            ShowNameStudent(topic = topic)
            ShowStatusTopic(topic = topic, viewModel)
        }

    }
}

@Composable
fun ShowStatusTopic(topic: Topic, viewModel: ProjectsViewModel) {
    Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
        if (topic.status == StatusTopic.REQUESTING) {

        }
        when (topic.status) {
            StatusTopic.REQUEST, StatusTopic.ACCEPT -> {}
            StatusTopic.REQUESTING -> {
                FilterItemTeacher(text = "Chấp nhận", callback = {
                    //update lại status
                    viewModel.updateTopicTable(idTopic = topic.id!!,status = StatusTopic.ACCEPT)

                })
                Spacer(modifier = Modifier.width(8.dp))
                FilterItemTeacher(text = "Xoá", callback = {
                    //xoa yeu cau cua sinh vien
                    viewModel.updateTopicTable(idTopic = topic.id!!, status = StatusTopic.REQUEST, idStudent = 0)
                })

            }
        }

    }
}

@Composable
fun ShowNameStudent(topic: Topic) {
    Row(horizontalArrangement = Arrangement.Start, modifier = Modifier.fillMaxWidth()) {
        when (topic.status) {
            StatusTopic.REQUEST -> {}
            StatusTopic.REQUESTING -> {
                Spacer(modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 0.dp))
                Text(
                    text = "Sinh viên: ${topic.nameStudent ?: ""} yêu cầu được làm đề tài.",
                    color = Color.Gray
                )

            }
            StatusTopic.ACCEPT -> {
                Text(
                    text = "Sinh viên: ${topic.nameStudent ?: ""}",
                    color = Color.Gray
                )
            }
        }
    }

}

@Composable
fun FilterItemStudent(text: String, callback: () -> Unit) {
    val content = remember { mutableStateOf(text) }

    Button(onClick = {
        if (text == StatusTopic.REQUEST.name){
            callback.invoke()
            content.value = StatusTopic.REQUESTING.name
        }

    })
    {
        Text(
            text = content.value,
            fontSize = 11.sp
        )
    }
}

@Composable
fun FilterItemTeacher(text: String, callback: () -> Unit) {
    val content = remember { mutableStateOf(text) }
    Button(onClick = {
        if (text == "Chấp nhận"){
            callback.invoke()
        }
        if (text == "Xoá"){
            callback.invoke()
        }

    })
    {
        Text(
            text = content.value,
            fontSize = 11.sp
        )
    }
}