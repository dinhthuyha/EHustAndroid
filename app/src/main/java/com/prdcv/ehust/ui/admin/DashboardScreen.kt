package com.prdcv.ehust.ui.admin

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.prdcv.ehust.R
import com.prdcv.ehust.viewmodel.AssignViewModel

@Composable
fun DashboardScreen(viewModel: AssignViewModel) {
    val uiState = viewModel.uiState
    val TAG ="hadinh"

    val dashboardInfo by uiState.informationDashBoard
    Log.d(TAG, "DashboardScreen: ${dashboardInfo.semester}")
    Scaffold(topBar = {
        TopAppBar(backgroundColor = colorResource(id = R.color.text_color)) {
            Text(
                text = "Dashboard",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 21.sp,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(
                text = "Học kỳ ${dashboardInfo.semester}",
                Modifier.padding(start = 15.dp, bottom = 5.dp),
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
            Card(
                elevation = 4.dp,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    InfoRow("\uD83D\uDCDA Số môn học đồ án", dashboardInfo.numberProject.toString())
                    Divider(thickness = .5f.dp)
                    InfoRow("\uD83E\uDDD1\uD83C\uDFFB\u200D\uD83C\uDFEB Số giảng viên", dashboardInfo.numberTeacher.toString())
                    Divider(thickness = .5f.dp)
                    InfoRow("\uD83E\uDDD1\uD83C\uDFFB\u200D\uD83C\uDF93 Số sinh viên", dashboardInfo.numberStudent.toString())
                }
            }
        }
    }
}

@Composable
private fun InfoRow(title: String, content: String) {
    Row(modifier = Modifier.padding(10.dp)) {
        Text(title)
        Spacer(modifier = Modifier.weight(1f))
        Text(content)
    }
}

@Composable
fun GradientCard(title: String, trailingContent: String, modifier: Modifier = Modifier) {
    Card(
        modifier = Modifier
            .padding(bottom = 10.dp)
            .then(modifier),
        shape = RoundedCornerShape(30)
    ) {
        Column(
            modifier = Modifier
                .background(
                    Brush.linearGradient(
                        listOf(
                            Color(0xFFF44336),
                            Color(0xFF3F51B5),
                        )
                    )
                )
                .padding(12.dp)
                .background(Color.Transparent),
        ) {
            Text(text = title, fontWeight = FontWeight.W400, color = Color.White)
            Spacer(modifier = Modifier.height(5.dp))
            Text(text = trailingContent, color = Color.White, fontSize = 15.sp)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GradientCardPreview() {
    GradientCard(title = "title", trailingContent = "1234")
}