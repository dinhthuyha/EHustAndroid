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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.prdcv.ehust.ui.compose.DefaultTheme
import com.prdcv.ehust.ui.profile.ToolBar

@Preview(showBackground = true)
@Composable
fun InformationTopic() {
    DefaultTheme() {
        Scaffold(topBar = { ToolBar(title = "Thông tin chi tiết đề tài") }) {
            LazyColumn(modifier =  Modifier.padding(start = 15.dp, top = 15.dp)) {
                item {
                    Column {
                        Row(modifier = Modifier.padding(bottom = 15.dp)) {
                            Text(text = "Tên đề tài:", style = MaterialTheme.typography.subtitle1)
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(text = "Ứng dụng quản lí đồ án trên nền tàng Android")
                        }

                            Text(
                                text = "Mô tả đề tài: ",
                                style = MaterialTheme.typography.subtitle1
                            )
                            Text( modifier = Modifier.padding(start = 15.dp),
                                text = "Hệ thống bao gồm:\n" +
                                        "+ Ứng dụng di động\n" +
                                        "+ Ứng dụng quản lý trên web\n" +
                                        "Thông qua ứng dụng di động, người dùng có thể:\n" +
                                        "+ Báo cáo các sự cố giao thông tại vị trí hiện tại: tắc đường, tai nạn, mưa ngập, … kèm theo thông tin mô tả gồm ảnh chụp\n" +
                                        "+ Nhận các thông tin cập nhật từ hệ thống\n" +
                                        "+ Kiểm tra tuyến đường có gặp sự cố hay không\n" +
                                        "Hệ thống quản lý xử lý thông tin:\n" +
                                        "+ Nhận thông tin kèm theo vị trí GPS\n" +
                                        "+ Đánh giá mức độ tin cậy của thông tin???\n" +
                                        "+ Cập nhật thông tin cho các thiết bị di động\n" +
                                        "Tham khảo: https://widdy.vn/widdy-ung-dung-mang-xa-hoi-giao-thong-cho-tai-xe-viet.html"
                            )

                        Row(modifier = Modifier.padding(bottom = 8.dp)) {
                            Text(
                                text = "Thời gian thực hiện: ",
                                style = MaterialTheme.typography.subtitle1
                            )
                            Text(text = "20-4-2022 - 08-08-2022")
                        }

                        Row(modifier = Modifier.padding(bottom = 8.dp)) {
                            Text(
                                text = "Giảng viên hướng dẫn: ",
                                style = MaterialTheme.typography.subtitle1
                            )
                            Text(text = "Lê Bá Vui")
                        }
                        Row(modifier = Modifier.padding(bottom = 8.dp)) {
                            Text(text = "Email: ", style = MaterialTheme.typography.subtitle1)
                            Text(text = "vui.leba@hust.edu.vn")
                        }

                        Row(modifier = Modifier.padding(bottom = 8.dp)) {
                            Text(
                                text = "Sinh viên thực hiện: ",
                                style = MaterialTheme.typography.subtitle1
                            )
                            Text(text = "Đinh Thuý Hà")
                        }
                        Row(modifier = Modifier.padding(bottom = 8.dp)) {
                            Text(text = "Email: ", style = MaterialTheme.typography.subtitle1)
                            Text(text = "ha.dt173086@sis.hust.edu.vn")
                        }

                    }

                }
            }
        }
    }
}
