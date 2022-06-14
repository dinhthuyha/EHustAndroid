package com.prdcv.ehust.ui.projects.topic

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.ChipDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FilterChip
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.prdcv.ehust.model.Topic
import com.prdcv.ehust.ui.compose.DefaultTheme
import com.prdcv.ehust.ui.profile.ToolBar

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    DefaultTheme {
        Scaffold {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                ToolBar("My topics")

                LazyColumn(
                    Modifier
                        //                .background(color = Color.Red)
                        .fillMaxWidth()
                        .padding(10.dp)
                ) {
                    items(topics) { t ->
                        TopicRow(topic = t)
                    }
                }
            }

        }
    }
}

@Composable
fun TopicRow(topic: Topic) {
    Card(
        elevation = 2.dp,
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { }
    ) {
        Row {
            Column(
                modifier = Modifier
                    .width(IntrinsicSize.Max)
                    .padding(10.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Đề tài: ",
                        fontSize = 15.sp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.size(5.dp))
                    Text(
                        text = "${topic.name} ",
                        fontSize = 17.sp,
                        modifier = Modifier
                            .padding(2.dp)
                    )
                    Spacer(modifier = Modifier.size(3.dp))


                }
                Row {
                    FilterItem(text = "request")
                }
            }
        }

    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FilterItem(text: String) {
    val state = remember { mutableStateOf(false) }

    FilterChip(
        selected = state.value,
        onClick = { state.value = !state.value  },
        border = ChipDefaults.outlinedBorder,
        colors = ChipDefaults.outlinedFilterChipColors(),
        selectedIcon = {
            Icon(
                imageVector = Icons.Filled.Done,
                contentDescription = null,
                modifier = Modifier.requiredSize(ChipDefaults.SelectedIconSize),

            )
        },
        modifier = Modifier.padding(end = 10.dp)
    ) {
        Text(text = text)
    }
}

private val topics = listOf<Topic>(
    Topic("lập trình web bán hàng online"),
    Topic("Xây dụng app music")
)