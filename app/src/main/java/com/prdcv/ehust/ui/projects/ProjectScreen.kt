package com.prdcv.ehust.ui.projects

import android.annotation.SuppressLint
import android.os.Parcelable
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.prdcv.ehust.model.ClassStudent
import com.prdcv.ehust.model.ProjectTeacher
import com.prdcv.ehust.model.Role
import com.prdcv.ehust.model.User
import com.prdcv.ehust.ui.compose.DefaultTheme
import com.prdcv.ehust.ui.compose.Purple500
import com.prdcv.ehust.ui.profile.ToolBar
import com.prdcv.ehust.viewmodel.ProjectViewModel
import com.prdcv.ehust.viewmodel.ShareViewModel
import com.prdcv.ehust.viewmodel.TopicsViewModel
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ProjectScreen(
    viewModel: ProjectViewModel = viewModel(),
    topicsViewModel: TopicsViewModel = viewModel(),
    navController: NavController,
) {
    val uiState = viewModel.projectsScreenState
    uiState.semesterStatus.value = viewModel.semester!!

    LaunchedEffect(key1 = Unit) {
        viewModel.fetchDataProjectScreen()

    }

    DefaultTheme {
        Scaffold(topBar = { ToolBar("Projects ") }) {
            Log.d("TAG", "ProjectScreen: ${viewModel.semester}")
            SwipeRefresh(
                state = uiState.refreshState,
                onRefresh = { viewModel.fetchDataProjectScreen() }
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    LazyColumn(
                        Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    ) {

                            when (viewModel.user?.roleId) {
                                Role.ROLE_STUDENT -> {
                                    items(uiState.projects) { t ->
                                    if (t.semester == viewModel.semester)
                                            ProjectStudent(
                                                t,
                                                navController,
                                                viewModel.user?.id,
                                                topicsViewModel
                                            )
                                    }
                                }
                                Role.ROLE_TEACHER -> {
                                    item { Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(text = "Học kì: ")
                                        SpinnerSemester(
                                            options = uiState.listSemester,
                                            selectedOption = uiState.semesterStatus,
                                            onItemClick = viewModel::onSemesterSelected
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                    } }
                                    item { Spacer(modifier = Modifier.height(25.dp)) }
                                    item { Text(text = "Các đồ án hướng dẫn:") }
                                    item { Spacer(modifier = Modifier.height(12.dp)) }
                                    items(uiState.listProject) { t ->
                                        ProjectTeacher(t, navController, viewModel.user)
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SpinnerSemester(
    options: List<Int>,
    selectedOption: MutableState<Int?>,
    onItemClick: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            readOnly = true,
            value = selectedOption.value?.toString() ?: "",
            onValueChange = {},
            label = {  },
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(
                focusedBorderColor = Purple500,
                unfocusedBorderColor = Color.LightGray
            ),
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            }
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    onClick = {
                        onItemClick(selectionOption)
                        expanded = false
                    }
                ) {
                    Text(text = selectionOption.toString())
                }
            }
        }
    }
}

@Composable
fun ProjectTeacher(
    data: ProjectTeacher ,
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
                            idProject = data.codeProject
                        )
                    )
                )
            }
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = "${data.codeProject}: ${data.nameProject}",
                fontSize = 17.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .padding(2.dp)
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(text = "Hướng dẫn ${data.numberOfStudentsGuiding} sinh viên")

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