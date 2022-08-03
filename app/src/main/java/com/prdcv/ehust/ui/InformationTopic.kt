package com.prdcv.ehust.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer
import com.hadt.ehust.model.TypeSubject
import com.prdcv.ehust.ui.compose.DefaultTheme
import com.prdcv.ehust.ui.profile.ToolBar


@Composable
fun InformationTopic(idTopic: Int = 0, viewModel: TopicsViewModel = hiltViewModel()) {
    val uiState = viewModel.uiState.moreInformationTopic
    val loading = viewModel.uiState.refreshState
    LaunchedEffect(key1 = Unit) {
        viewModel.findDetailInformationTopic(idTopic)
    }

    DefaultTheme() {
        Scaffold(topBar = { ToolBar(title = "Thông tin chi tiết đề tài") }) {

            LazyColumn(modifier = Modifier.padding(start = 10.dp, top = 10.dp)) {
                if (loading.isRefreshing) {
                    items(count = 7) {
                        RowHOlder()
                    }
                } else {
                    item {
                        Column {
                            Row(modifier = Modifier.padding(bottom = 10.dp)) {
                                Text(
                                    text = "Tên đề tài:",
                                    style = MaterialTheme.typography.subtitle1,
                                    modifier = Modifier
                                        .padding(start = 10.dp)

                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(
                                    text = uiState.value.title ?: "",
                                    modifier = Modifier.padding(start = 8.dp)
                                )
                            }

                            Text(
                                text = "Mô tả đề tài: ",
                                style = MaterialTheme.typography.subtitle1,
                                modifier = Modifier
                                    .padding(top = 8.dp, start = 10.dp)

                            )
                            Text(
                                text = uiState.value.description ?: "",
                                modifier = Modifier
                                    .padding(top = 8.dp, start = 10.dp)

                            )

                            Row(modifier = Modifier.padding(bottom = 8.dp)) {
                                Text(
                                    text = "Thời gian thực hiện: ",
                                    style = MaterialTheme.typography.subtitle1,
                                    modifier = Modifier
                                        .padding(start = 10.dp)

                                )
                                Text(
                                    text = "${uiState.value.startTimeProgress.toString()} - ${uiState.value.dueTime.toString()}",
                                    modifier = Modifier
                                        .padding(start = 10.dp)

                                )
                            }

                            Row(modifier = Modifier.padding(bottom = 8.dp)) {
                                Text(
                                    text = "Giảng viên hướng dẫn: ",
                                    style = MaterialTheme.typography.subtitle1,
                                    modifier = Modifier
                                        .padding(start = 10.dp)

                                )
                                Text(
                                    text = uiState.value.nameTeacher ?: "",
                                    modifier = Modifier
                                        .padding(start = 10.dp)

                                )
                            }
                            Row(modifier = Modifier.padding(bottom = 8.dp)) {
                                Text(
                                    text = "Email: ", style = MaterialTheme.typography.subtitle1,
                                    modifier = Modifier
                                        .padding(start = 10.dp)

                                )
                                Text(
                                    text = uiState.value.emailTeacher ?: "",
                                    modifier = Modifier
                                        .padding(start = 10.dp)

                                )
                            }

                            Row(modifier = Modifier.padding(bottom = 8.dp)) {
                                Text(
                                    text = "Sinh viên thực hiện: ",
                                    style = MaterialTheme.typography.subtitle1,
                                    modifier = Modifier
                                        .padding(start = 10.dp)

                                )
                                Text(
                                    text = uiState.value.nameStudent ?: "",
                                    modifier = Modifier
                                        .padding(start = 10.dp)

                                )
                            }
                            Row(modifier = Modifier.padding(bottom = 8.dp)) {
                                Text(
                                    text = "Email: ",
                                    style = MaterialTheme.typography.subtitle1,
                                    modifier = Modifier.padding(start = 10.dp)
                                )
                                Text(
                                    text = uiState.value.emailStudent ?: "",
                                    modifier = Modifier
                                        .padding(start = 10.dp)
                                )
                            }
                            Row(modifier = Modifier.padding(bottom = 8.dp)) {
                                when (uiState.value.type) {
                                    TypeSubject.PROJECT -> {}
                                    TypeSubject.THESIS -> {
                                        Text(
                                            text = "Trạng thái: ",
                                            style = MaterialTheme.typography.subtitle1,
                                            modifier = Modifier.padding(start = 10.dp)
                                        )

                                        Text(
                                            text = "Đang thực hiện",
                                            modifier = Modifier
                                                .padding(start = 10.dp)
                                        )
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
}

@Composable
fun RowHOlder() {
    Text(
        text = "Xây dựng ứng dụng quản lí đồ án trên nền tảng Android.\n" +
                "Hệ thống bao gồm:\n",
        modifier = Modifier
            .padding(16.dp)
            .placeholder(
                visible = true,
                highlight = PlaceholderHighlight.shimmer()
            )

    )
}
