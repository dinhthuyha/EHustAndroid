package com.prdcv.ehust.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.prdcv.ehust.R
import com.prdcv.ehust.model.Role
import com.prdcv.ehust.ui.compose.Button
import com.prdcv.ehust.ui.compose.DefaultTheme
import com.prdcv.ehust.ui.main.MainFragmentDirections
import com.prdcv.ehust.ui.task.TaskRow
import com.prdcv.ehust.ui.task.detail.TaskDetailArgs
import com.prdcv.ehust.viewmodel.TaskViewModel
import org.bouncycastle.asn1.x500.style.RFC4519Style.title


@Composable
fun HomeScreen(
    role: Role = Role.ROLE_STUDENT,
    navController: NavController,
    taskViewModel: TaskViewModel,
    callback: () -> Unit
) {
    val uiState = taskViewModel.uiState
    DefaultTheme() {
        Scaffold(topBar = { ToolBar(title = "Trang chủ", nav = navController) }) {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                RowScheduleToday(role = role, callback = callback)
                Spacer(modifier = Modifier.height(12.dp))
                uiState.filteredTaskList.forEach { item ->
                    TaskRow(
                        data = item,
                        modifier = Modifier
                            .padding(start = 10.dp, end = 10.dp, bottom = 5.dp)
                            .clickable {
                                navController.navigate(
                                    MainFragmentDirections.actionMainFragmentToDetailTaskFragment(
                                        TaskDetailArgs(idTask = item.id)
                                    )
                                )
                            }
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                when (role) {
                    Role.ROLE_STUDENT -> {
                        RowStudent(nav = navController)
                    }
                    Role.ROLE_TEACHER -> {
                        RowTeacher(nav = navController)
                    }
                    else -> {}
                }


            }
        }
    }
}

@Composable
fun ToolBar(title: String, nav: NavController) {
    TopAppBar(backgroundColor = colorResource(id = R.color.text_color),
        title = {
            Text(
                text = title,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 21.sp,
            )
        },
        actions = {
            Icon(
                painter = painterResource(id = R.drawable.ic_notifications),
                contentDescription = "",
                tint = Color.White,
                modifier = Modifier
                    .padding(end = 10.dp)
                    .clickable { nav.navigate(R.id.action_mainFragment_to_newsFragment) }
            )
        })
}


@Composable
fun RowScheduleToday(role: Role = Role.ROLE_TEACHER, callback: () -> Unit) {
    val alpha = if (role == Role.ROLE_TEACHER) 1f else 0f
    Card(
        elevation = 4.dp,
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
            .padding(bottom = 12.dp, start = 12.dp, end = 12.dp, top = 20.dp)
            .fillMaxWidth()
            .shadow(
                elevation = 4.dp,
                spotColor = Button,
                shape = RectangleShape
            )

    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp), horizontalAlignment = Alignment.Start
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier.weight(3f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_calendar2),
                        contentDescription = ""
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(text = "Lịch hôm nay", fontWeight = FontWeight.Bold)
                }

                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier
                        .weight(3f)
                        .alpha(alpha)) {
                Icon(painter = painterResource(id = R.drawable.ic_add), contentDescription = "", modifier = Modifier.clickable { callback.invoke() })
            }

            }
            Divider(color = Color.Black, thickness = 1.dp, modifier = Modifier.padding(start = 5.dp))
            RowItemSchedule()
            RowItemSchedule()
            RowItemSchedule()
        }
    }


}


@Composable
fun RowTeacher(nav: NavController) {
    Column() {
        Row(title = "Đồ Án",
            sub = "Thông tin chi tiết đồ án",
            idIcon = R.drawable.ic_project,
            callback = { nav.navigate(R.id.action_mainFragment_to_projectGraduateFragment) })
        Row(
            title = "Thông báo đồ án",
            sub = "Thông báo chi tiết đồ án",
            idIcon = R.drawable.ic_class,
            callback = { nav.navigate(R.id.action_mainFragment_to_newsFragment) }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RowItemSchedule() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 24.dp, end = 24.dp, top = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "ROW", modifier = Modifier.weight(1f))
        Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.End) {
            Text(text = "9:00")
            Text(text = "9:00")
        }
    }
}


@Composable
fun RowStudent(nav: NavController) {
    Column() {
        Row(
            title = "Thời khoá biểu",
            sub = "Tra cứu thời khoá biểu",
            idIcon = R.drawable.ic_calendar2,
            callback = { nav.navigate(R.id.action_mainFragment_to_scheduleFragment) }

        )
        Row(
            title = "Đồ Án",
            sub = "Thông tin chi tiết đồ án",
            idIcon = R.drawable.ic_project,
            callback = { nav.navigate(R.id.action_mainFragment_to_projectGraduateFragment) }
        )
        Row(
            title = "Lớp sinh viên",
            sub = "Thông tin lớp sinh viên",
            idIcon = R.drawable.ic_class,
            callback = { nav.navigate(R.id.action_mainFragment_to_studentsFragment) }
        )
    }


}


@Composable
fun Row(
    title: String = "Đồ án",
    sub: String = "Thông tin chi tiết đo án",
    idIcon: Int = R.drawable.ic_project,
    callback: () -> Unit
) {
    Card(
        elevation = 4.dp,
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
            .padding(bottom = 12.dp, start = 12.dp, end = 12.dp)
            .fillMaxWidth()
            .shadow(
                elevation = 4.dp,
                spotColor = Button,
                shape = RectangleShape
            )
            .clickable { callback.invoke() }

    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(12.dp)) {
            Icon(
                painter = painterResource(id = idIcon),
                contentDescription = "",
                modifier = Modifier.size(width = 30.dp, height = 30.dp),
                tint = Button
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column() {
                Text(text = title, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Text(text = sub)
            }
        }
    }

}