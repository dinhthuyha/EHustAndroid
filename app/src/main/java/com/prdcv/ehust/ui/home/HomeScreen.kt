package com.prdcv.ehust.ui.home

import android.util.Log
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer
import com.hadt.ehust.model.TypeNotification
import com.prdcv.ehust.R
import com.prdcv.ehust.data.model.Meeting
import com.prdcv.ehust.data.model.Role
import com.prdcv.ehust.data.model.ScheduleEvent
import com.prdcv.ehust.ui.compose.Button
import com.prdcv.ehust.ui.compose.DefaultTheme
import com.prdcv.ehust.ui.main.MainFragmentDirections
import com.prdcv.ehust.ui.task.TaskRow
import com.prdcv.ehust.ui.task.detail.TaskDetailArgs
import com.prdcv.ehust.ui.ShareViewModel
import com.prdcv.ehust.ui.HomeScreenState
import java.time.LocalTime


@Composable
fun HomeScreen(
    role: Role = Role.ROLE_STUDENT,
    navController: NavController,
    shareViewModel: ShareViewModel,
    callback: () -> Unit
) {
    LaunchedEffect(key1 = Unit) {
        shareViewModel.fetchDataHomeScreen()
    }
    val uiScheduleState = shareViewModel.uiState
    DefaultTheme() {
        Scaffold(topBar = { ToolBar(title = "Trang chủ", nav = navController) }) {
            Log.d("TAG", "HomeScreen: ${uiScheduleState.refreshState.isRefreshing}")
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                Spacer(modifier = Modifier.height(10.dp))
                RowScheduleToday(
                    role = role,
                    callback = callback,
                    shareViewModel.getScheduleToday(uiScheduleState.schedulesState),
                    uiScheduleState.meetingsToday,
                    isLoading = uiScheduleState.refreshState.isRefreshing
                )
                Spacer(modifier = Modifier.height(12.dp))
                if (role == Role.ROLE_STUDENT) {
                    TasksWillExpiresSoon(uiScheduleState, navController)
                }
                Spacer(modifier = Modifier.height(12.dp))
                when (role) {
                    Role.ROLE_STUDENT -> {
                        RowStudent(
                            nav = navController,
                            isLoading = uiScheduleState.refreshState.isRefreshing
                        )
                    }
                    Role.ROLE_TEACHER -> {
                        RowTeacher(
                            nav = navController,
                            isLoading = uiScheduleState.refreshState.isRefreshing
                        )
                    }
                    else -> {}
                }


            }
        }

    }

}

@Composable
fun TasksWillExpiresSoon(uiScheduleState: HomeScreenState, navController: NavController) {
    uiScheduleState.filteredTaskList.forEach { item ->
        TaskRow(isLoading = uiScheduleState.refreshState.isRefreshing,
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
                    .clickable { nav.navigate(MainFragmentDirections.actionMainFragmentToNewsFragment(TypeNotification.TYPE_NORMAL)) }
            )
        })
}


@Composable
fun RowScheduleToday(
    role: Role = Role.ROLE_TEACHER,
    callback: () -> Unit,
    schedule: List<ScheduleEvent>,
    meetings: List<Meeting>,
    isLoading: Boolean = false
) {
    val alpha = if (role == Role.ROLE_TEACHER) 1f else 0f
    Card(
        elevation = 4.dp,
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
            .padding(bottom = 12.dp, start = 13.dp, end = 13.dp, top = 20.dp)
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
                    .placeholder(
                        visible = isLoading,
                        highlight = PlaceholderHighlight.shimmer()
                    )
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
                        .alpha(alpha)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_add),
                        contentDescription = "",
                        modifier = Modifier.clickable { callback.invoke() })
                }

            }
            Divider(
                color = Color.Black,
                thickness = 1.dp,
                modifier = Modifier
                    .padding(start = 5.dp)
                    .placeholder(
                        visible = isLoading,
                        highlight = PlaceholderHighlight.shimmer()
                    )
            )
            schedule.forEach { t ->
                RowItemSchedule(
                    title = t.subjectClass.name,
                    startTime = t.startTime,
                    endTime = t.finishTime,
                    isLoading = isLoading
                )
            }
            meetings.forEach {
                var title = ""
                when (role) {
                    Role.ROLE_TEACHER -> {
                        title = "${it.title} với sinh viên ${it.nameStudent}"
                    }
                    Role.ROLE_STUDENT -> {
                        title = it.title
                    }
                }
                RowItemSchedule(
                    title = title,
                    startTime = it.startTime,
                    endTime = it.endTime,
                    isLoading = isLoading
                )
            }
        }
    }


}


@Composable
fun RowTeacher(nav: NavController, isLoading: Boolean = false) {
    Column() {
        Row(
            title = "Đồ Án",
            sub = "Thông tin chi tiết đồ án",
            idIcon = R.drawable.ic_project,
            callback = { nav.navigate(R.id.action_mainFragment_to_projectGraduateFragment) },
            isLoading = isLoading
        )
        Row(
            title = "Thông báo đồ án",
            sub = "Thông báo chi tiết đồ án",
            idIcon = R.drawable.ic_news,
            callback = { nav.navigate(MainFragmentDirections.actionMainFragmentToNewsFragment( TypeNotification.TYPE_PROJECT)) },
            isLoading = isLoading
        )
    }
}

@Composable
fun RowItemSchedule(
    title: String = "Quanr tri du an",
    startTime: LocalTime?,
    endTime: LocalTime?,
    isLoading: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 2.dp, top = 8.dp)
            .placeholder(
                visible = isLoading,
                highlight = PlaceholderHighlight.shimmer()
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, modifier = Modifier.weight(2.5f))
        Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.End) {
            Text(text = startTime.toString())
            Text(text = endTime.toString())
        }
    }
}


@Composable
fun RowStudent(nav: NavController, isLoading: Boolean = false) {
    Column() {
        Row(
            title = "Thời khoá biểu",
            sub = "Tra cứu thời khoá biểu",
            idIcon = R.drawable.ic_calendar2,
            callback = { nav.navigate(R.id.action_mainFragment_to_scheduleFragment) },
            isLoading = isLoading
        )
        Row(
            title = "Đồ Án",
            sub = "Thông tin chi tiết đồ án",
            idIcon = R.drawable.ic_project,
            callback = { nav.navigate(R.id.action_mainFragment_to_projectGraduateFragment) },
            isLoading = isLoading
        )
        Row(
            title = "Lớp sinh viên",
            sub = "Thông tin lớp sinh viên",
            idIcon = R.drawable.ic_class,
            callback = { nav.navigate(R.id.action_mainFragment_to_studentsFragment) },
            isLoading = isLoading
        )
    }


}


@Composable
fun Row(
    title: String = "Đồ án",
    sub: String = "Thông tin chi tiết đo án",
    idIcon: Int = R.drawable.ic_project,
    callback: () -> Unit,
    isLoading: Boolean = false
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
                modifier = Modifier
                    .size(width = 40.dp, height = 40.dp)
                    .placeholder(
                        visible = isLoading,
                        highlight = PlaceholderHighlight.shimmer()
                    ),
                tint = Button
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column() {
                Text(
                    text = title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .placeholder(
                            visible = isLoading,
                            highlight = PlaceholderHighlight.shimmer()
                        )
                        .padding(bottom = 8.dp)
                )
                Text(
                    text = sub,
                    fontWeight = FontWeight.Light,
                    fontSize = 13.sp,
                    modifier = Modifier
                        .placeholder(
                            visible = isLoading,
                            highlight = PlaceholderHighlight.shimmer()
                        )
                )
            }
        }
    }

}