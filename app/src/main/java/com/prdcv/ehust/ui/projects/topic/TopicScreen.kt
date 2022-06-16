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
    role: Role
) {
    val state = viewModel.topicState.collectAsState()
    DefaultTheme {
        Scaffold ( topBar = { ToolBar("Đề tài") },){

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
                                when(role){
                                    Role.ROLE_TEACHER -> {
                                        TopicTeacherRow(t)
                                    }
                                    Role.ROLE_STUDENT -> {
                                        TopicStudentRow(t)
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


@Preview(showBackground = true)
@Composable
fun TopicStudentRow(topic: Topic = Topic(123, "lập trình web bán hàng online")) {
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
                FilterItem(text = "${topic.status?.name}")
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun TopicTeacherRow(topic: Topic = Topic(123, "lập trình web bán hàng online")) {
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
                if (topic.status == StatusTopic.REQUEST){
                    FilterItem(text = "${topic.status?.name}")
                    FilterItem(text = "Delete")
                }

            }
        }

    }
}
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FilterItem(text: String) {
    val state = remember { mutableStateOf(false) }

    FilterChip(
        selected = state.value,
        onClick = {
            state.value = !state.value
            if (state.value) {
                //send request update state
                Log.d("TAG", "FilterItem: ")
            }
        },
        border = ChipDefaults.outlinedBorder,
        colors = ChipDefaults.filterChipColors(
            selectedBackgroundColor = MaterialTheme.colors.secondaryVariant
        ),
        selectedIcon = {
            Icon(
                imageVector = Icons.Filled.Done,
                contentDescription = null,
                modifier = Modifier.requiredSize(ChipDefaults.SelectedIconSize)
            )
        },
        modifier = Modifier.padding(end = 10.dp)

    ) {
        Text(text = text)
    }
}
