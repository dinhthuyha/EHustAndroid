package com.prdcv.ehust.ui.projects

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.prdcv.ehust.common.State
import com.prdcv.ehust.model.ClassStudent
import com.prdcv.ehust.model.Role
import com.prdcv.ehust.ui.compose.DefaultTheme
import com.prdcv.ehust.ui.profile.ToolBar
import com.prdcv.ehust.viewmodel.ProjectsViewModel
import com.prdcv.ehust.viewmodel.ShareViewModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun DefaultPreview(
    viewModel: ShareViewModel = viewModel(),
    navController: NavController,
    topicViewModel: ProjectsViewModel
) {
    val state = viewModel.projectsState.collectAsState()
    DefaultTheme {
        Scaffold ( topBar = { ToolBar("Projects ") },){

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
                                when(viewModel.user?.roleId){
                                    Role.ROLE_STUDENT -> { ProjectStudent(t, navController, topicViewModel, viewModel )}
                                    Role.ROLE_TEACHER -> { ProjectTeacher(t, navController, topicViewModel, viewModel) }
                                    else -> {}
                                }

                            }
                        }

                    }
                }
            }
        }
    }
}


@Composable
fun ProjectStudent(data: ClassStudent, navController: NavController, topicViewModel: ProjectsViewModel, shareViewModel: ShareViewModel){
    Card(
        elevation = 2.dp,
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth()
            .clickable {
                data.nameTeacher?.let {
                    navController.navigate(ProjectsFragmentDirections.actionProjectGraduateFragmentToTopicsFragment())
                    topicViewModel.findTopicByIdTeacherAndIdProject(nameTeacher = it,idProject = data.codeCourse)
                }


            }
    ) {
        Column(
            modifier = Modifier
                .width(IntrinsicSize.Max)
                .padding(8.dp)
        ) {
            Row(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = "${data.toString()} ",
                    fontSize = 17.sp,
                    modifier = Modifier
                        .padding(1.dp),
                    fontWeight = FontWeight.Medium
                )

            }
            Row(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = "${data.line2()}",
                    fontSize = 16.sp,
                    modifier = Modifier
                        .padding(1.dp),
                    fontWeight = FontWeight.Light
                )

            }
        }
    }
}
@Composable
fun ProjectTeacher(data: ClassStudent, navController: NavController, topicViewModel: ProjectsViewModel, shareViewModel: ShareViewModel){
    Card(
        elevation = 2.dp,
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable {
                navController.navigate(ProjectsFragmentDirections.actionProjectGraduateFragmentToTopicsFragment())
                topicViewModel.findTopicByIdTeacherAndIdProject(
                    idTeacher = shareViewModel.user?.id!!,
                    idProject = data.codeCourse
                )
            }
    ) {
        Row(modifier = Modifier.padding(8.dp)) {
            Text(
                text = "${data.name} - ${data.codeCourse} ",
                fontSize = 17.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .padding(2.dp)
            )

        }
    }
}