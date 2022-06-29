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
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.prdcv.ehust.model.ClassStudent
import com.prdcv.ehust.model.Role
import com.prdcv.ehust.ui.compose.DefaultTheme
import com.prdcv.ehust.ui.profile.ToolBar
import com.prdcv.ehust.viewmodel.ShareViewModel
import kotlinx.coroutines.*
import kotlinx.parcelize.Parcelize

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun DefaultPreview(
    viewModel: ShareViewModel = viewModel(),
    navController: NavController,
) {
    val uiState = viewModel.uiProjectsState

    val refreshingState = rememberSwipeRefreshState(isRefreshing = false)
    val coroutineScope = rememberCoroutineScope()
    fun refreshTaskList() {

        refreshingState.isRefreshing = true
        coroutineScope.launch {
            withContext(Dispatchers.IO + Job()) {
                viewModel.callbackGetData()
            }
        }
        coroutineScope.launch {
            delay(3000)
            refreshingState.isRefreshing = false
        }
    }

    DefaultTheme {
        Scaffold(topBar = { ToolBar("Projects ") }) {
            SwipeRefresh(
                state = refreshingState,
                onRefresh = ::refreshTaskList
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
                                    ProjectStudent(t, navController)
                                }
                                Role.ROLE_TEACHER -> {
                                    ProjectTeacher(t, navController, viewModel)
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
fun ProjectStudent(data: ClassStudent = classStudentPreview, navController: NavController? = null) {
    Card(
        elevation = 2.dp,
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth()
            .clickable {
                data.nameTeacher?.let {
                    navController?.navigate(
                        ProjectsFragmentDirections.actionProjectGraduateFragmentToTopicsFragment(
                            ProjectArg(
                                nameTeacher = it, idProject = data.codeCourse
                            )
                        )
                    )
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
fun ProjectTeacher(
    data: ClassStudent,
    navController: NavController,
    shareViewModel: ShareViewModel
) {
    Card(
        elevation = 2.dp,
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable {
                navController.navigate(
                    ProjectsFragmentDirections.actionProjectGraduateFragmentToTopicsFragment(
                        ProjectArg(
                            idTeacher = shareViewModel.user?.id,
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