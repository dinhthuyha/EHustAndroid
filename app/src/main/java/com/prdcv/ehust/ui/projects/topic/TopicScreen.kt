package com.prdcv.ehust.ui.projects.topic

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.prdcv.ehust.common.State
import com.prdcv.ehust.model.Topic
import com.prdcv.ehust.ui.compose.DefaultTheme
import com.prdcv.ehust.ui.compose.Chip
import com.prdcv.ehust.viewmodel.ProjectsViewModel

//@Preview(showBackground = true)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun DefaultPreview(
    viewModel: ProjectsViewModel = viewModel()
) {
    val state = viewModel.topicState.collectAsState()
    DefaultTheme {
        Scaffold {

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                LazyColumn(
                    Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                ) {
                    when(val topics = state.value){
                        is State.Loading -> {}
                        is State.Error -> {}
                        is State.Success -> {items(items = topics.data ) { t ->
                            TopicRow(topic = t)
                        }}
                        else -> {}
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
                Row(horizontalArrangement = Arrangement.End) {
                    FilterItem(text = "${topic.status?.name}")
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
        onClick = { state.value = !state.value
            if (state.value) {
                //send request update state
                Log.d("TAG", "FilterItem: ")
            }
                  },
        border = ChipDefaults.outlinedBorder,
        colors = ChipDefaults.filterChipColors(
            selectedBackgroundColor = MaterialTheme.colors.secondaryVariant
        ),
        selectedIcon = {
            Icon(
                imageVector = Icons.Filled.Done,
                contentDescription = null,
                modifier = Modifier.requiredSize(ChipDefaults.SelectedIconSize)
            )
        },
        modifier = Modifier.padding(end = 10.dp)

    ) {
        Text(text = text)
    }
}
