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
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.widget.ConstraintLayout
import com.hadt.ehust.model.TopicStatus
import com.prdcv.ehust.R
import com.prdcv.ehust.ui.compose.Button
import com.prdcv.ehust.ui.compose.DefaultTheme
import com.prdcv.ehust.ui.profile.ToolBar
import com.prdcv.ehust.ui.projects.topic.TopicsFragmentDirections
import org.bouncycastle.asn1.x500.style.RFC4519Style.title

@Preview(showBackground = true)
@Composable
fun HomeScreen() {
    DefaultTheme() {
        Scaffold(topBar = { ToolBar(title = "Trang chủ")}) {
            Column {
                RowScheduleToday()
                Spacer(modifier = Modifier.height(12.dp))
                RowStudent()
                RowTeacher()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RowScheduleToday() {
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

    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                , horizontalAlignment = Alignment.Start
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            ) {
                Row(horizontalArrangement = Arrangement.Start, modifier = Modifier.weight(2f)) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_calendar2),
                        contentDescription = ""
                    )
                    Text(text = "Lịch hôm nay", fontWeight = FontWeight.Bold)
                }
                Row(horizontalArrangement = Arrangement.End, modifier = Modifier.weight(3f)) {
                    Icon(painter = painterResource(id = R.drawable.ic_add), contentDescription = "")
                }

            }
            Divider(color = Color.Black, thickness = 1.dp)
            RowItemSchedule()
            RowItemSchedule()
            RowItemSchedule()
        }
    }


}

@Preview(showBackground = true)
@Composable
fun RowTeacher() {
    Column() {
        Row(title = "Đồ Án", sub = "Thông tin chi tiết đồ án", idIcon = R.drawable.ic_project)
        Row(
            title = "Thông báo đồ án",
            sub = "Thông báo chi tiết đồ án",
            idIcon = R.drawable.ic_class
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

@Preview(showBackground = true)
@Composable
fun RowStudent() {
    Column() {
        Row(
            title = "Thời khoá biểu",
            sub = "Tra cứu thời khoá biểu",
            idIcon = R.drawable.ic_calendar2
        )
        Row(title = "Đồ Án", sub = "Thông tin chi tiết đồ án", idIcon = R.drawable.ic_project)
        Row(title = "Lớp sinh viên", sub = "Thông tin lớp sinh viên", idIcon = R.drawable.ic_class)
    }


}

@Preview(showBackground = true)
@Composable
fun Row(
    title: String = "Đồ án",
    sub: String = "Thông tin chi tiết đo án",
    idIcon: Int = R.drawable.ic_project
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