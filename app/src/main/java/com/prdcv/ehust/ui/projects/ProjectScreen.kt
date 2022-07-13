package com.prdcv.ehust.ui.projects

import android.annotation.SuppressLint
import android.os.Parcelable
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.prdcv.ehust.model.ClassStudent
import com.prdcv.ehust.model.Role
import com.prdcv.ehust.model.User
import com.prdcv.ehust.ui.compose.DefaultTheme
import com.prdcv.ehust.ui.profile.ToolBar
import com.prdcv.ehust.viewmodel.ShareViewModel
import com.prdcv.ehust.viewmodel.TopicsViewModel
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ProjectScreen(
    viewModel: ShareViewModel = viewModel(),
    topicsViewModel: TopicsViewModel = viewModel(),
    navController: NavController,
) {
    val uiState = viewModel.projectsScreenState

    LaunchedEffect(key1 = Unit) {
        viewModel.findAllProjectsById()

    }

    DefaultTheme {
        Scaffold(topBar = { ToolBar("Projects ") }) {
            SwipeRefresh(
                state = uiState.refreshState,
                onRefresh = { viewModel.findAllProjectsById() }
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    LazyColumn(
                        Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    ) {

                        items(uiState.projects) { t ->
                            when (viewModel.user?.roleId) {
                                Role.ROLE_STUDENT -> {
                                    if (t.semester == uiState.maxSemester)
                                    ProjectStudent(t, navController, viewModel.user?.id, topicsViewModel)
                                }
                                Role.ROLE_TEACHER -> {
                                    ProjectTeacher(t, navController, viewModel.user)
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

@Composable
fun ProjectStudent(
    data: ClassStudent = classStudentPreview,
    navController: NavController? = null,
    userId: Int?,
    topicsViewModel: TopicsViewModel
) {
    val coroutineScope = rememberCoroutineScope()

    Card(
        elevation = 2.dp,
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth()
            .clickable {
                coroutineScope.launch {
                    val topic = topicsViewModel
                        .findAcceptedTopic(
                            nameTeacher = data.nameTeacher ?: "",
                            idProject = data.codeCourse,
                            currentUserId = userId
                        )

                    data.nameTeacher?.let {
                        navController?.navigate(
                            ProjectsFragmentDirections.actionProjectGraduateFragmentToNewTaskFragment(
                                topic
                            )
                        )
                    }
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
                    text = data.toString(),
                    fontSize = 17.sp,
                    modifier = Modifier
                        .padding(1.dp),
                    fontWeight = FontWeight.Medium
                )

            }
            Row(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = data.line2(),
                    fontSize = 16.sp,
                    modifier = Modifier
                        .padding(1.dp),
                    fontWeight = FontWeight.Light
                )

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProjectTeacher(
    data: ClassStudent = classStudentPreview,
    navController: NavController? = null,
    teacher: User? = User(id = 123, roleId = Role.ROLE_TEACHER)
) {
    Card(
        elevation = 2.dp,
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable {
                navController?.navigate(
                    ProjectsFragmentDirections.actionProjectGraduateFragmentToTopicsFragment(
                        ProjectArg(
                            idTeacher = teacher?.id,
                            idProject = data.codeCourse
                        )
                    )
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

private val classStudentPreview = ClassStudent(
    codeClass = 1234,
    semester = 20221,
    name = "Do an ky su",
    nameTeacher = "Ho Ten Giang Vien",
    codeCourse = "12345"
)

@Parcelize
data class ProjectArg(
    val nameTeacher: String? = null,
    val idTeacher: Int? = null,
    val idProject: String? = null
) : Parcelable