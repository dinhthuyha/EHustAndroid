package com.prdcv.ehust.ui.projects.topic

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
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
import com.prdcv.ehust.viewmodel.ProjectsViewModel
import kotlin.Exception

//@Preview(showBackground = true)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun DefaultPreview(
    viewModel: ProjectsViewModel = viewModel(),
    id: Int,
    role: Role,
    navController: NavController
) {
    fun checkTopic(topics: List<Topic>): List<Topic>{
       try {
           //chi sho cac de tai chua co sinh vien nao request
           topics.firstOrNull { it.idStudent ==id && it.status == StatusTopic.ACCEPT }?.let {
               return listOf(it)
           }
           topics.filter { it.status == StatusTopic.REQUEST }.let {
               return it
           }
        }catch (e: Exception){
           return listOf<Topic>()
        }
    }

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
                            when (role) {
                                Role.ROLE_TEACHER -> {
                                    items(items = topics.data) { t ->
                                        TopicTeacherRow(t, navController, viewModel)
                                    }

                                }
                                Role.ROLE_STUDENT -> {

                                    var items = checkTopic(topics.data)

                                    items(items = items) { t ->
                                            //update status, id sv
                                            TopicStudentRow(t, viewModel, id, navController)

                                    }


                                }
                                else -> {}
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
    idUser: Int,
    navController: NavController
) {
    Card(
        elevation = 2.dp,
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable {
                if (topic.status == StatusTopic.ACCEPT) {
                    navController.navigate(TopicsFragmentDirections.actionTopicsFragmentToNewTaskFragment())
                }
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
    val buttonVisible = remember { mutableStateOf(true) }
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
            TitleTopic(topic = topic)
            ShowNameStudent(topic = topic)
            ShowStatusTopic(topic = topic, viewModel, buttonVisible)
        }

    }
}

@Composable
fun TitleTopic(topic: Topic){
    Row(verticalAlignment = Alignment.CenterVertically, modifier =  Modifier) {
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
fun ShowStatusTopic(topic: Topic, viewModel: ProjectsViewModel, buttonVisible: MutableState<Boolean>) {

    fun isVisibleButton()= if (buttonVisible.value) 1f else 0f

    Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()
        .alpha(isVisibleButton())) {
        if (topic.status == StatusTopic.REQUESTING) {

        }
        when (topic.status) {
            StatusTopic.REQUEST, StatusTopic.ACCEPT -> {}
            StatusTopic.REQUESTING -> {
                FilterItemTeacher(text = "Chấp nhận", callback = {
                    //update lại status
                    buttonVisible.value = false
                    viewModel.updateTopicTable(idTopic = topic.id!!,status = StatusTopic.ACCEPT)

                })
                Spacer(modifier = Modifier.width(8.dp))
                FilterItemTeacher(text = "Xoá", callback = {
                    //xoa yeu cau cua sinh vien
                    buttonVisible.value = false
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
            callback.invoke()
    })
    {
        Text(
            text = content.value,
            fontSize = 11.sp
        )
    }
}